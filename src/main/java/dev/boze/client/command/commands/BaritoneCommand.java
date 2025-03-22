package dev.boze.client.command.commands;

import baritone.api.BaritoneAPI;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import net.minecraft.command.CommandSource;

public class BaritoneCommand extends Command {
    public BaritoneCommand() {
        super("baritone", "Baritone", "Run Baritone commands", "b");
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        String var4 = (String) var0.getArgument("command", String.class);
        if (BaritoneAPI.class.getProtectionDomain().getCodeSource().getLocation().toString().contains("libraries")) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(var4);
        }

        return 1;
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method402("command", StringArgumentType.greedyString()).executes(BaritoneCommand::lambda$build$0));
    }
}
