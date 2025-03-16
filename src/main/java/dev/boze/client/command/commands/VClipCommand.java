package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import net.minecraft.command.CommandSource;

public class VClipCommand extends Command {
   public VClipCommand() {
      super("vclip", "VClip", "Clips you up or down");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(method402("getDistance", DoubleArgumentType.doubleArg()).executes(VClipCommand::lambda$build$0));
   }

   private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
      mc.player.updatePosition(mc.player.getX(), mc.player.getY() + (Double)var0.getArgument("getDistance", Double.class), mc.player.getZ());
      return 1;
   }
}
