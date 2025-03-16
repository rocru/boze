package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.api.BozeInstance;
import dev.boze.api.addon.Addon;
import dev.boze.client.command.Command;
import dev.boze.client.instances.impl.ChatInstance;
import java.util.ArrayList;
import net.minecraft.command.CommandSource;

public class AddonsCommand extends Command {
   public AddonsCommand() {
      super("addons", "Addons", "Lists all addons");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(AddonsCommand::lambda$build$1);
   }

   private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
      ArrayList var3 = BozeInstance.INSTANCE.getAddons();
      ChatInstance.method740("Addons", "Addons: " + var3.size());
      var3.forEach(AddonsCommand::lambda$build$0);
      return 1;
   }

   private static void lambda$build$0(Addon var0) {
      ChatInstance.method740("Addons", " - (highlight)%s(default) - %s", var0.name, var0.version);
   }
}
