package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import net.minecraft.command.CommandSource;

public class SoundReloadCommand extends Command {
   public SoundReloadCommand() {
      super("reloadsounds", "ReloadSounds", "Reloads sound system");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(SoundReloadCommand::lambda$build$0);
   }

   private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
      mc.getSoundManager().reloadSounds();
      return 1;
   }
}
