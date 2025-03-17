package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.MacroArgument;
import dev.boze.client.utils.Macro;
import net.minecraft.command.CommandSource;

public class MacroToggleCommand extends Command {
    public MacroToggleCommand() {
        super("deactivate", "Deactivate", "Deactivates macros");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method402("macro", MacroArgument.method1000()).executes(MacroToggleCommand::lambda$build$0));
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        Macro var3 = MacroArgument.method1001(var0, "macro");
        var3.field1050 = false;
        return 1;
    }
}
