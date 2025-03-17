package dev.boze.client.command.commands;

import com.google.common.io.Files;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.ConfigArgument;
import dev.boze.client.command.arguments.ModuleArgument;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.*;
import dev.boze.client.utils.ConfigNBTSerializer;
import dev.boze.client.utils.network.BozeExecutor;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Locale;

public class ConfigCommand extends Command {
    static final boolean field1386 = !ConfigCommand.class.desiredAssertionStatus();

    public ConfigCommand() {
        super("config", "Config", "Import or export modules' configs");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("list").executes(this::lambda$build$1));
        builder.then(method403("share").then(method402("config", ConfigArgument.method987()).executes(this::lambda$build$3)));
        builder.then(
                method403("group")
                        .then(method402("title", StringArgumentType.word()).then(method402("codes", StringArgumentType.greedyString()).executes(this::lambda$build$5)))
        );
        builder.then(method403("import").then(method402("title", StringArgumentType.string()).executes(this::lambda$build$7)));
        builder.then(method403("delete").then(method402("title", StringArgumentType.string()).executes(this::lambda$build$8)));
        builder.then(
                method403("export")
                        .then(method402("module", ModuleArgument.method1003()).then(method402("title", StringArgumentType.string()).executes(this::lambda$build$9)))
        );
        builder.then(method403("exportall").then(method402("title", StringArgumentType.string()).executes(this::lambda$build$10)));

        for (Category var8 : Category.values()) {
            builder.then(
                    method403("export" + var8.name().toLowerCase(Locale.ROOT)).then(method402("title", StringArgumentType.string()).executes(this::lambda$build$11))
            );
        }
    }

    private Category method634(String var1) {
        for (Category var8 : Category.values()) {
            if (var8.name().equalsIgnoreCase(var1)) {
                return var8;
            }
        }

        return null;
    }

    private int lambda$build$11(Category var1, CommandContext var2) throws CommandSyntaxException {
        String var6 = (String) var2.getArgument("title", String.class);
        NbtCompound var7 = ConfigNBTSerializer.method2140(var6, var1);
        NbtCompound var8 = new NbtCompound();

        for (Module var10 : Boze.getModules().modules) {
            if (var10.category == var1) {
                var8.put(var10.internalName, ConfigNBTSerializer.method2136(var10));
            }
        }

        NbtCompound var11 = new NbtCompound();
        var11.put("v2.info", var7);
        var11.put("v2.modules", var8);
        ConfigManager.uploadConfig(var6 + "." + var1.name(), var11, ConfigType.CONFIG);
        this.method624("Exported config for " + var1.name() + " modules to " + var6);
        return 1;
    }

    private int lambda$build$10(CommandContext var1) throws CommandSyntaxException {
        String var5 = (String) var1.getArgument("title", String.class);
        NbtCompound var6 = ConfigNBTSerializer.method2141(var5);
        NbtCompound var7 = new NbtCompound();

        for (Module var9 : Boze.getModules().modules) {
            if (var9 != Friends.INSTANCE && var9 != Waypoints.INSTANCE && var9 != Accounts.INSTANCE && var9 != Profiles.INSTANCE) {
                var7.put(var9.internalName, ConfigNBTSerializer.method2136(var9));
            }
        }

        NbtCompound var10 = new NbtCompound();
        var10.put("v2.info", var6);
        var10.put("v2.modules", var7);
        ConfigManager.uploadConfig(var5 + ".All", var10, ConfigType.CONFIG);
        this.method624("Exported config for all modules to " + var5);
        return 1;
    }

    private int lambda$build$9(CommandContext var1) throws CommandSyntaxException {
        Module var4 = ModuleArgument.method1004(var1, "module");
        String var5 = (String) var1.getArgument("title", String.class);
        ConfigNBTSerializer.method2138(var4, var5);
        this.method624("Exported config for " + var4.internalName + " to " + var5);
        return 1;
    }

    private int lambda$build$8(CommandContext var1) throws CommandSyntaxException {
        for (String var8 : ConfigManager.get(ConfigType.CONFIG)) {
            if (Files.getNameWithoutExtension(var8).equalsIgnoreCase((String) var1.getArgument("title", String.class))) {
                ConfigManager.delete(var8, ConfigType.CONFIG);
                this.method624("Deleted config " + var1.getArgument("title", String.class));
                return 1;
            }
        }

        this.method626("Config " + var1.getArgument("title", String.class) + " not found");
        return 1;
    }

    private int lambda$build$7(CommandContext var1) throws CommandSyntaxException {
        ArrayList var5 = new ArrayList();

        for (String var9 : ConfigManager.get(ConfigType.CONFIG)) {
            if (Files.getNameWithoutExtension(var9).equalsIgnoreCase((String) var1.getArgument("title", String.class))) {
                String var10 = Files.getFileExtension(var9);
                if (var10.equals("All")) {
                    NbtCompound var17 = ConfigManager.downloadConfig(var9, ConfigType.CONFIG);
                    if (var17 != null) {
                        var5.add("All modules");
                        if (var17.contains("v2.modules")) {
                            NbtCompound var21 = var17.getCompound("v2.modules");

                            for (Module var28 : Boze.getModules().modules) {
                                if (var21.contains(var28.internalName)) {
                                    ConfigNBTSerializer.method2137(var28, var21.getCompound(var28.internalName));
                                }
                            }
                        } else {
                            for (Module var24 : Boze.getModules().modules) {
                                if (var17.contains(var24.internalName)) {
                                    NbtCompound var27 = var17.getCompound(var24.internalName);
                                    if (var27 != null) {
                                        var24.method235(var27);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Category var11;
                    if ((var11 = this.method634(var10)) != null) {
                        NbtCompound var16 = ConfigManager.downloadConfig(var9, ConfigType.CONFIG);
                        if (var16 != null) {
                            var5.add(var11.name() + " modules");
                            if (var16.contains("v2.modules")) {
                                NbtCompound var19 = var16.getCompound("v2.modules");

                                for (Module var26 : Boze.getModules().modules) {
                                    if (var26.category == var11 && var19.contains(var26.internalName)) {
                                        ConfigNBTSerializer.method2137(var26, var19.getCompound(var26.internalName));
                                    }
                                }
                            } else {
                                for (Module var22 : Boze.getModules().modules) {
                                    if (var22.category == var11 && var16.contains(var22.internalName)) {
                                        NbtCompound var15 = var16.getCompound(var22.internalName);
                                        if (var15 != null) {
                                            var22.method235(var15);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Module var12 = Boze.getModules().method395(var10);
                        NbtCompound var13 = ConfigManager.downloadConfig(var9, ConfigType.CONFIG);
                        if (var13 != null) {
                            if (var13.contains("v2.data")) {
                                NbtCompound var14 = var13.getCompound("v2.data");
                                ConfigNBTSerializer.method2137(var12, var14);
                                var5.add(var12.internalName);
                            } else {
                                var12.method235(var13);
                                var5.add(var12.internalName);
                            }
                        }
                    }
                }
            }
        }

        if (var5.size() > 0) {
            this.method624("Loaded config " + var1.getArgument("title", String.class) + " for:");
            var5.forEach(this::lambda$build$6);
        }

        return 1;
    }

    private void lambda$build$6(String var1) {
        this.method624(" - (highlight)%s", var1);
    }

    private int lambda$build$5(CommandContext var1) throws CommandSyntaxException {
        String var4 = StringArgumentType.getString(var1, "title");
        String var5 = StringArgumentType.getString(var1, "codes");
        String[] var6 = var5.split(" ");
        BozeExecutor.method2200(this::lambda$build$4);
        return 1;
    }

    private void lambda$build$4(String[] var1, String var2) {
        NbtCompound var6 = new NbtCompound();
        var6.putBoolean("v2.group", true);
        NbtCompound var7 = new NbtCompound();

        for (String var11 : var1) {
            if (!var11.startsWith("CF")) {
                this.method626("Invalid code " + var11);
                return;
            }

            NbtCompound var12 = ConfigManager.method1147(var11);
            if (var12 != null) {
                var7.put(var11, var12);
            }
        }

        var6.put("v2.codes", var7);
        String var13 = ConfigManager.publishConfig(var2, var6, "CF");
        if (!field1386 && var13 == null) {
            throw new AssertionError();
        } else {
            this.method624("Grouped config " + var2 + " has been shared with code " + var13);
            this.method624("Load it using the command " + Options.method1563() + "load " + var13);
            GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), var13);
        }
    }

    private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
        String var4 = (String) var1.getArgument("config", String.class);
        BozeExecutor.method2200(this::lambda$build$2);
        return 1;
    }

    private void lambda$build$2(String var1) {
        for (String var8 : ConfigManager.get(ConfigType.CONFIG)) {
            if (var8.equalsIgnoreCase(var1)) {
                NbtCompound var9 = ConfigManager.downloadConfig(var8, ConfigType.CONFIG);
                var9.putString("ConfigName", var1);
                String var10 = ConfigManager.publishConfig(var1, var9, "CF");
                if (!field1386 && var10 == null) {
                    throw new AssertionError();
                }

                this.method624("Config " + var1 + " has been shared with code " + var10);
                this.method624("Load it using the command " + Options.method1563() + "load " + var10);
                GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), var10);
                return;
            }
        }
    }

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        ArrayList var5 = new ArrayList();

        for (String var9 : ConfigManager.get(ConfigType.CONFIG)) {
            var5.add(Files.getNameWithoutExtension(var9));
        }

        if (!var5.isEmpty()) {
            this.method624("Configs (%d):", var5.size());
            var5.forEach(this::lambda$build$0);
        } else {
            this.method624("No configs found");
        }

        return 1;
    }

    private void lambda$build$0(String var1) {
        this.method624(" - (highlight)%s", var1);
    }
}
