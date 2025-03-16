package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class DisconnectCommand extends Command {
   public DisconnectCommand() {
      super("disconnect", "Disconnect", "Disconnect from the game");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(DisconnectCommand::lambda$build$0);
      builder.then(method403("force").executes(DisconnectCommand::lambda$build$1));
   }

   private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
      mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(1000));
      return 1;
   }

   private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
      mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal("Disconnected")));
      return 1;
   }
}
