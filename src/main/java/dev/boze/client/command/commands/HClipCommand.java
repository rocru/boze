package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Vec3d;

public class HClipCommand extends Command {
    public HClipCommand() {
        super("hclip", "HClip", "Clips you forwards");
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        Vec3d var3 = Vec3d.fromPolar(0.0F, mc.player.getYaw(mc.getRenderTickCounter().getTickDelta(true)))
                .normalize()
                .multiply((Double) var0.getArgument("getDistance", Double.class));
        mc.player.updatePosition(mc.player.getX() + var3.x, mc.player.getY(), mc.player.getZ() + var3.z);
        return 1;
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method402("getDistance", DoubleArgumentType.doubleArg()).executes(HClipCommand::lambda$build$0));
    }
}
