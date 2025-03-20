package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.ModuleArgument;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.Setting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.*;
import dev.boze.client.systems.modules.hud.color.Watermark;
import dev.boze.client.systems.modules.hud.core.ArrayList;
import dev.boze.client.systems.modules.hud.core.Binds;
import dev.boze.client.systems.modules.hud.core.Coordinates;
import dev.boze.client.systems.modules.render.PlaceRender;
import dev.boze.client.utils.Bind;
import mapped.Class2782;
import net.minecraft.command.CommandSource;

import java.io.PrintStream;

public class ConfigResetCommand extends Command {
    public ConfigResetCommand() {
        super("reset", "Reset", "Resets your Boze config");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("titles").executes(ConfigResetCommand::lambda$build$0));
        builder.then(method403("binds").executes(ConfigResetCommand::lambda$build$1));
        builder.then(method403("seen").executes(ConfigResetCommand::lambda$build$2));
        builder.then(method403("visible").executes(ConfigResetCommand::lambda$build$3));
        builder.then(method403("gui").executes(ConfigResetCommand::lambda$build$4));
        builder.then(method403("searchbar").executes(ConfigResetCommand::lambda$build$5));
        builder.then(method403("module").then(method402("module", ModuleArgument.method1003()).executes(this::lambda$build$6)));
        builder.executes(this::lambda$build$7);
    }

    private void method629(HUDModule var1) {
        if (var1 instanceof ArrayList) {
            var1.setEnabled(true);
            var1.field595.setValue(0.75);
            var1.method308(0.0);
            var1.method310(0.0);
            var1.method1649(2);
        } else if (var1 instanceof Binds) {
            var1.setEnabled(true);
            var1.field595.setValue(0.75);
            var1.method308(0.0);
            var1.method310(0.0);
            var1.method1649(1);
            var1.method312(true);
        } else if (var1 instanceof Coordinates) {
            var1.setEnabled(true);
            var1.method308(0.0);
            var1.method310(0.0);
            var1.method1649(3);
        } else if (var1 instanceof Watermark) {
            var1.setEnabled(true);
            var1.method308(0.0);
            var1.method310(0.0);
            var1.method1649(1);
        } else {
            var1.setEnabled(false);
            var1.method308((double) Theme.method1363() * 1.5 * BaseComponent.scaleFactor);
            var1.method310((double) Theme.method1363() / 4.0 * BaseComponent.scaleFactor);
            var1.method1649(1);
        }
    }

    private void method396(Module var1) {
        try {
            for (Setting var6 : var1.method1144()) {
                try {
                    var6.resetValue();
                    PrintStream var10000 = System.out;
                    String var10001 = var6.name;
                    String var10002 = var1.getName();
                    String var8 = var6.getValue().toString();
                    String var9 = var10002;
                    String var10 = var10001;
                    var10000.println("Resetting setting " + var10 + " in " + var9 + " to " + var8);
                    var6.setExpanded(false);
                } catch (Exception var11) {
                    System.out.println("Failed to reset setting " + var6.name + " in " + var1.getName());
                }
            }

            var1.setEnabled(var1 == PlaceRender.INSTANCE || var1 == Capes.INSTANCE || var1 == Friends.INSTANCE || var1 == Fonts.INSTANCE);

            System.out.println("Resetting " + var1.getName() + " state set to " + var1.isEnabled());
            if (var1 == Gui.INSTANCE) {
                var1.bind.set(true, 344);
            } else if (var1 == HUD.INSTANCE) {
                var1.bind.set(true, 259);
            } else {
                var1.bind.set(Bind.create());
            }

            if (var1 instanceof HUDModule var13) {
                this.method629(var13);
            }
        } catch (Exception var12) {
            System.out.println("Failed to reset " + var1.getName());
        }
    }

    private int lambda$build$7(CommandContext var1) throws CommandSyntaxException {
        for (Module var6 : Boze.getModules().modules) {
            this.method396(var6);
        }

        for (Category var8 : Category.values()) {
            var8.extended = true;
            var8.field42 = -1.0;
            var8.field43 = -1.0;
            var8.locked = true;
        }

        ClickGUI.field1335.init();
        return 1;
    }

    private int lambda$build$6(CommandContext var1) throws CommandSyntaxException {
        Module var4 = ModuleArgument.method1004(var1, "module");
        this.method396(var4);
        return 1;
    }

    private static int lambda$build$5(CommandContext var0) throws CommandSyntaxException {
        Class2782.field95 = -1.0;
        Class2782.field96 = -1.0;
        Class2782.field94 = true;
        ClickGUI.field1335.init();
        return 1;
    }

    private static int lambda$build$4(CommandContext var0) throws CommandSyntaxException {
        for (Setting var5 : Gui.INSTANCE.method1144()) {
            var5.resetValue();
            var5.setExpanded(false);
        }

        for (Setting var10 : Theme.INSTANCE.method1144()) {
            var10.resetValue();
            var10.setExpanded(false);
        }

        for (Category var7 : Category.values()) {
            var7.extended = true;
            var7.field42 = -1.0;
            var7.field43 = -1.0;
            var7.locked = true;
        }

        Class2782.field95 = -1.0;
        Class2782.field96 = -1.0;
        Class2782.field94 = true;
        ClickGUI.field1335.init();
        return 1;
    }

    private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
        for (Module var5 : Boze.getModules().modules) {
            var5.setVisibility(true);
        }

        return 1;
    }

    private static int lambda$build$2(CommandContext var0) throws CommandSyntaxException {
        for (Module var5 : Boze.getModules().modules) {
            var5.field433 = false;

            for (Setting var7 : var5.method1144()) {
                var7.descriptionSeen = false;
            }
        }

        return 1;
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        for (Module var5 : Boze.getModules().modules) {
            if (var5 == Gui.INSTANCE) {
                var5.bind.set(true, 344);
            } else if (var5 == HUD.INSTANCE) {
                var5.bind.set(true, 259);
            } else {
                var5.bind.set(Bind.create());
            }
        }

        return 1;
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        for (Module var5 : Boze.getModules().modules) {
            var5.setName(var5.internalName);
        }

        return 1;
    }
}
