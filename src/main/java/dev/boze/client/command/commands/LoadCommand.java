package dev.boze.client.command.commands;

import com.google.common.io.Files;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.CurrentProfileSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Profiles;
import dev.boze.client.utils.ConfigNBTSerializer;
import mapped.Class1201;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class LoadCommand extends Command {
    private static final DynamicCommandExceptionType field1385 = new DynamicCommandExceptionType(LoadCommand::lambda$static$0);

    public LoadCommand() {
        super("load", "Load", "Load shared configs/profiles");
    }

    private static Message lambda$static$0(Object var0) {
        return Text.literal("Code " + var0 + " is not 10 characters long.");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method402("code", StringArgumentType.string()).executes(this::lambda$build$1));
    }

    private void method632(String var1, NbtCompound var2) {
        String var6 = null;
        if (var2.contains("ConfigName")) {
            var6 = var2.getString("ConfigName");
        }

        if (var6 == null) {
            this.method626("Invalid config " + var1);
        } else {
            ArrayList<String> var7 = new ArrayList<>();
            String var8 = Files.getFileExtension(var6);
            if (var8.equals("All")) {
                var7.add("All modules");
                if (var2.contains("v2.modules")) {
                    NbtCompound var10 = var2.getCompound("v2.modules");

                    for (Module var12 : Boze.getModules().modules) {
                        if (var10.contains(var12.internalName)) {
                            ConfigNBTSerializer.method2137(var12, var10.getCompound(var12.internalName));
                        }
                    }
                } else {
                    for (Module var17 : Boze.getModules().modules) {
                        if (var2.contains(var17.internalName)) {
                            NbtCompound var21 = var2.getCompound(var17.internalName);
                            if (var21 != null) {
                                var17.fromTag(var21);
                            }
                        }
                    }
                }
            } else {
                Category var9;
                if ((var9 = this.method633(var8)) != null) {
                    var7.add(var9.name() + " modules");
                    if (var2.contains("v2.modules")) {
                        NbtCompound var14 = var2.getCompound("v2.modules");

                        for (Module var22 : Boze.getModules().modules) {
                            if (var22.category == var9 && var14.contains(var22.internalName)) {
                                ConfigNBTSerializer.method2137(var22, var14.getCompound(var22.internalName));
                            }
                        }
                    } else {
                        for (Module var19 : Boze.getModules().modules) {
                            if (var19.category == var9 && var2.contains(var19.internalName)) {
                                NbtCompound var23 = var2.getCompound(var19.internalName);
                                if (var23 != null) {
                                    var19.fromTag(var23);
                                }
                            }
                        }
                    }
                } else {
                    Module var16 = Boze.getModules().method395(var8);
                    if (var16 == null) {
                        this.method626("Could not find module " + var8);
                        return;
                    }

                    if (var2.contains("v2.data")) {
                        NbtCompound var20 = var2.getCompound("v2.data");
                        ConfigNBTSerializer.method2137(var16, var20);
                        var7.add(var16.internalName);
                    } else {
                        var16.fromTag(var2);
                        var7.add(var16.internalName);
                    }
                }
            }

            if (!var7.isEmpty()) {
                this.method624("Loaded config " + var1 + " for:");
                var7.forEach(this::lambda$loadCFTag$2);
            }
        }
    }

    private Category method633(String var1) {
        for (Category var8 : Category.values()) {
            if (var8.name().equalsIgnoreCase(var1)) {
                return var8;
            }
        }

        return null;
    }

    private void lambda$loadCFTag$2(String var1) {
        this.method624(" - (highlight)%s", var1);
    }

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        String var5 = (String) var1.getArgument("code", String.class);
        if (var5.length() != 10) {
            throw field1385.create(var5);
        } else {
            String var6 = var5.substring(0, 2);
            if (var6.equals("PR")) {
                NbtCompound var15 = ConfigManager.method1147(var5);
                if (var15 == null) {
                    this.method626("Invalid code " + var5);
                    return 1;
                } else if (var15.contains("ProfileName")) {
                    Class1201.method2383(true);
                    String var17 = var15.getString("ProfileName");
                    this.method625("(highlight)" + var17 + "(default) is a legacy profile, splitting into 4 profiles");
                    String var19 = "v2.main." + var17;
                    String var21 = "v2.visuals." + var17;
                    String var22 = "v2.binds." + var17;
                    String var23 = "v2.client." + var17;
                    Profiles.INSTANCE.field762.setValue(var19);
                    Profiles.INSTANCE.field763.setValue(var21);
                    Profiles.INSTANCE.field764.setValue(var22);
                    Profiles.INSTANCE.field765.setValue(var23);
                    Boze.getModules().method398(var15, true);
                    Class1201.method2384(true, var19, var21, var22, var23);
                    this.method624("Loaded profile (highlight)" + var17);
                    return 1;
                } else if (var15.contains("v2.info")) {
                    NbtCompound var16 = var15.getCompound("v2.info");
                    String var18 = var16.getString("name");
                    String var20 = var16.getString("prefix");
                    String var11 = var20 + var18;

                    for (Setting var13 : Profiles.INSTANCE.method1144()) {
                        if (var13 instanceof CurrentProfileSetting var14 && var14.field969.equals(var20)) {
                            if (var14.field968.contains(var11)) {
                                this.method625(
                                        "Loading profile as (highlight)" + var5 + "(default) since profile (highlight)" + var18 + "(default) already exists"
                                );
                                var18 = var5;
                                var11 = var20 + var5;
                            }

                            var14.setValue(var11);
                            var14.field968.add(var11);
                            Class1201.method2380(var20, var15.getCompound("v2.profile"));
                            Class1201.method2382(true, var14, var11);
                            this.method624("Loaded profile (highlight)" + var18);
                            return 1;
                        }
                    }

                    this.method626("Error loading profile (highlight)" + var18);
                    return 1;
                } else {
                    this.method626("Unrecognized profile version " + var5);
                    return 1;
                }
            } else {
                if (var6.equals("CF")) {
                    NbtCompound var7 = ConfigManager.method1147(var5);
                    if (var7 == null) {
                        this.method626("Invalid code " + var5);
                        return 1;
                    }

                    if (var7.contains("v2.codes")) {
                        NbtCompound var8 = var7.getCompound("v2.codes");

                        for (String var10 : var8.getKeys()) {
                            this.method632(var10, var8.getCompound(var10));
                        }
                    } else {
                        this.method632(var5, var7);
                    }
                }

                return 1;
            }
        }
    }
}
