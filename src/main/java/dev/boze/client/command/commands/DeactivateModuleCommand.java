package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.ModuleArgument;
import dev.boze.client.systems.modules.Module;
import net.minecraft.command.CommandSource;

public class DeactivateModuleCommand extends Command {
    public DeactivateModuleCommand() {
        super("toggle", "Toggle", "Toggles modules", "t");
    }

    private static int lambda$build$4(CommandContext var0) throws CommandSyntaxException {
        for (Module var5 : Boze.getModules().modules) {
            var5.setNotify(false);
        }

        return 1;
    }

    private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
        for (Module var5 : Boze.getModules().modules) {
            var5.setNotify(true);
        }

        return 1;
    }

    private static int lambda$build$2(CommandContext var0) throws CommandSyntaxException {
        Module var3 = ModuleArgument.method1004(var0, "module");
        var3.setEnabled(false);
        return 1;
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        Module var3 = ModuleArgument.method1004(var0, "module");
        var3.setEnabled(true);
        return 1;
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        Module var3 = ModuleArgument.method1004(var0, "module");
        var3.toggle();
        return 1;
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                ((RequiredArgumentBuilder) method402("module", ModuleArgument.method1003()).executes(DeactivateModuleCommand::lambda$build$0))
                        .then(method403("on").executes(DeactivateModuleCommand::lambda$build$1))
                        .then(method403("off").executes(DeactivateModuleCommand::lambda$build$2))
        );
        builder.then(
                ((LiteralArgumentBuilder) method403("allnotifications").then(method403("on").executes(DeactivateModuleCommand::lambda$build$3)))
                        .then(method403("off").executes(DeactivateModuleCommand::lambda$build$4))
        );
    }
}
