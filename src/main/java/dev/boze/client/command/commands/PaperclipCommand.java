package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.PlayerArgument;
import dev.boze.client.utils.PositionUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Vec3d;

public class PaperclipCommand extends Command {
    public PaperclipCommand() {
        super("paperclip", "PaperClip", "Teleports to specified location using paper clip exploit");
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        AbstractClientPlayerEntity var4 = PlayerArgument.method732(var0);
        if (var4 != null) {
            PositionUtils.method494(var4.getPos());
        }

        return 1;
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        PositionUtils.method494(
                new Vec3d((Double) var0.getArgument("X", Double.class), (Double) var0.getArgument("Y", Double.class), (Double) var0.getArgument("Z", Double.class))
        );
        return 1;
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method402("X", DoubleArgumentType.doubleArg())
                        .then(
                                method402("Y", DoubleArgumentType.doubleArg()).then(method402("Z", DoubleArgumentType.doubleArg()).executes(PaperclipCommand::lambda$build$0))
                        )
        );
        builder.then(method402("player", PlayerArgument.method731()).executes(PaperclipCommand::lambda$build$1));
    }
}
