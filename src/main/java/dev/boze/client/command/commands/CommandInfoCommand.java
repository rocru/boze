package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.instances.impl.ChatInstance;
import mapped.Class27;
import net.minecraft.command.CommandSource;

public class CommandInfoCommand extends Command {
   public CommandInfoCommand() {
      super("commands", "Commands", "Shows all commands", "help");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(CommandInfoCommand::lambda$build$1);
   }

   private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
      ChatInstance.method624("Commands:");
      Class27.getCommands().method1144().forEach(CommandInfoCommand::lambda$build$0);
      return 1;
   }

   private static void lambda$build$0(Command var0) {
      ChatInstance.method624(" - (highlight)" + var0.method210());
   }
}
