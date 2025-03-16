package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.CategoryArgument;
import dev.boze.client.command.arguments.ModuleArgument;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class27;
import net.minecraft.command.CommandSource;

public class ArraylistDrawCommand extends Command {
   public ArraylistDrawCommand() {
      super("drawn", "Drawn", "Draw and undraw modules from the arraylist");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(
         ((RequiredArgumentBuilder)((RequiredArgumentBuilder)method402("module", ModuleArgument.method1003()).executes(ArraylistDrawCommand::lambda$build$0))
               .then(method403("draw").executes(ArraylistDrawCommand::lambda$build$1)))
            .then(method403("undraw").executes(ArraylistDrawCommand::lambda$build$2))
      );
      builder.then(
         ((RequiredArgumentBuilder)method402("category", CategoryArgument.method981()).then(method403("draw").executes(ArraylistDrawCommand::lambda$build$3)))
            .then(method403("undraw").executes(ArraylistDrawCommand::lambda$build$4))
      );
   }

   private static int lambda$build$4(CommandContext var0) throws CommandSyntaxException {
      Category var4 = CategoryArgument.method982(var0, "category");

      for (Module var6 : Class27.getModules().modules) {
         if (var6.category == var4) {
            var6.setVisibility(false);
         }
      }

      return 1;
   }

   private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
      Category var4 = CategoryArgument.method982(var0, "category");

      for (Module var6 : Class27.getModules().modules) {
         if (var6.category == var4) {
            var6.setVisibility(true);
         }
      }

      return 1;
   }

   private static int lambda$build$2(CommandContext var0) throws CommandSyntaxException {
      Module var3 = ModuleArgument.method1004(var0, "module");
      var3.setVisibility(false);
      return 1;
   }

   private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
      Module var3 = ModuleArgument.method1004(var0, "module");
      var3.setVisibility(true);
      return 1;
   }

   private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
      Module var4 = ModuleArgument.method1004(var0, "module");
      var4.setVisibility(!var4.getVisibility());
      return 1;
   }
}
