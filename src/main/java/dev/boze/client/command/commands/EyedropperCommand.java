package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.screens.EyedropperScreen;
import dev.boze.client.Boze;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.CommandSource;

public class EyedropperCommand extends Command {
   public EyedropperCommand() {
      super("eyedropper", "EyeDropper", "Pick a pixel's color by clicking on it", "ed");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(this::lambda$build$0);
   }

   @EventHandler
   public void method2071(Render3DEvent event) {
      if (mc.currentScreen == null) {
         mc.setScreen(new EyedropperScreen());
         Boze.EVENT_BUS.unsubscribe(this);
      }
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      Boze.EVENT_BUS.subscribe(this);
      return 1;
   }
}
