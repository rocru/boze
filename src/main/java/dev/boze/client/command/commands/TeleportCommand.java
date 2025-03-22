package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.PlayerArgument;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.CommandSource;

public class TeleportCommand extends Command {
    public TeleportCommand() {
        super("tp", "TP", "Teleports to specified location");
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        AbstractClientPlayerEntity var3 = PlayerArgument.method732(var0);
        if (var3 != null) {
            mc.player.setPosition(var3.getPos());
        }

        return 1;
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        mc.player
                .setPosition((Double) var0.getArgument("X", Double.class), (Double) var0.getArgument("Y", Double.class), (Double) var0.getArgument("Z", Double.class));
        return 1;
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method402("X", DoubleArgumentType.doubleArg())
                        .then(method402("Y", DoubleArgumentType.doubleArg()).then(method402("Z", DoubleArgumentType.doubleArg()).executes(TeleportCommand::lambda$build$0)))
        );
        builder.then(method402("player", PlayerArgument.method731()).executes(TeleportCommand::lambda$build$1));
    }
}
