package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.utils.render.PeekScreen;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class PeekCommand extends Command {
   private static final ItemStack[] field1868 = new ItemStack[27];

   public PeekCommand() {
      super("peek", "Peek", "Peek into shulkers and ender chests");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(PeekCommand::lambda$build$0);
   }

   private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
      if (PeekScreen.method589(mc.player.getMainHandStack(), field1868, true)) {
         return 1;
      } else if (PeekScreen.method589(mc.player.getOffHandStack(), field1868, true)) {
         return 1;
      } else {
         PeekScreen.method589(Items.ENDER_CHEST.getDefaultStack(), new ItemStack[27], true);
         return 1;
      }
   }
}
