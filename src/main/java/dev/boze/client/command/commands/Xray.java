package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.BlockArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.movement.ElytraAutoPilot.pi;
import dev.boze.client.systems.modules.render.XRay;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;

public class Xray extends Command {
   public Xray() {
      super("xray", "XRay", "Manage XRay blocks");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(method403("add").then(method402("block", BlockArgument.method975()).executes(this::lambda$build$0)));
      builder.then(method403("del").then(method402("block", BlockArgument.method975()).executes(this::lambda$build$1)));
      builder.then(method403("list").executes(this::lambda$build$3));
      builder.then(method403("clear").executes(this::lambda$build$4));
   }

   private int lambda$build$4(CommandContext var1) throws CommandSyntaxException {
      this.method624("Clearing all blocks...", new Object[0]);
      pi.method2142();
      mc.worldRenderer.reload();
      return 1;
   }

   private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
      this.method624("Blocks: " + XRay.INSTANCE.field3867.method2032().size(), new Object[0]);
      XRay.INSTANCE.field3867.method2032().forEach(Xray::lambda$build$2);
      return 1;
   }

   private static void lambda$build$2(Block var0) {
      ChatInstance.method624(" - (highlight)" + var0.getName().getString());
   }

   private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
      try {
         Block var4 = BlockArgument.method977(var1, "block");
         this.method624("Removed " + var4.getName().getString(), new Object[0]);
         XRay.INSTANCE.field3867.method346(var4.getName().getString());
         XRay.INSTANCE.field3867.method1416();
         mc.worldRenderer.reload();
         return 1;
      } catch (Exception var5) {
         this.method626("Block not found!", new Object[0]);
         return 0;
      }
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      try {
         Block var4 = BlockArgument.method977(var1, "block");
         this.method624("Added " + var4.getName().getString(), new Object[0]);
         XRay.INSTANCE.field3867.method1701(var4.getName().getString());
         XRay.INSTANCE.field3867.method1416();
         mc.worldRenderer.reload();
         return 1;
      } catch (Exception var5) {
         ChatInstance.method626("Block not found!");
         return 0;
      }
   }
}
