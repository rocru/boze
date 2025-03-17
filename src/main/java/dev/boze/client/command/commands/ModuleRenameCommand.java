package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.ModuleArgument;
import dev.boze.client.systems.modules.Module;
import net.minecraft.command.CommandSource;

public class ModuleRenameCommand extends Command {
    public ModuleRenameCommand() {
        super("rename", "Rename", "Renames modules");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method402("module", ModuleArgument.method1003()).then(method402("title", StringArgumentType.string()).executes(ModuleRenameCommand::lambda$build$0))
        );
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        Module var3 = ModuleArgument.method1004(var0, "module");
        var3.setName((String) var0.getArgument("title", String.class));
        return 1;
    }
}
