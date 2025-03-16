package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.systems.modules.client.Options;
import net.minecraft.command.CommandSource;

public class WatermarkCommand extends Command {
   public WatermarkCommand() {
      super("watermark", "Watermark", "Sets your watermark");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(method402("watermark", StringArgumentType.greedyString()).executes(WatermarkCommand::lambda$build$0));
   }

   private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
      Options.INSTANCE.field988.method1341((String)var0.getArgument("watermark", String.class));
      return 1;
   }
}
