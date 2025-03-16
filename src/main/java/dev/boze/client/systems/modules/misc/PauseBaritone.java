package dev.boze.client.systems.modules.misc;

import baritone.api.BaritoneAPI;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;

public class PauseBaritone extends Module {
   public static final PauseBaritone INSTANCE = new PauseBaritone();
   private final BindSetting field3050 = new BindSetting("Bind", Bind.fromKey(340), "Bind to pause baritone");
   private boolean field3051 = false;

   public PauseBaritone() {
      super("PauseBaritone", "Pause baritone when holding a key", Category.Misc);
   }

   @Override
   public void onEnable() {
      if (MinecraftUtils.isClientActive()
         && this.field3050.method476().getBind() == -1
         && BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()
         && !this.field3051) {
         BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
         this.field3051 = true;
      }
   }

   @Override
   public void onDisable() {
      if (MinecraftUtils.isClientActive() && this.field3051) {
         try {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
         } catch (Exception var5) {
         }

         this.field3051 = false;
      }
   }

   @EventHandler
   private void method1744(KeyEvent var1) {
      if (this.field3050.method476().getBind() != -1) {
         if (this.field3050.method476().matches(true, var1.key)) {
            if (var1.action == KeyAction.Press && BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing() && !this.field3051) {
               BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
               this.field3051 = true;
            } else if (var1.action == KeyAction.Release && this.field3051) {
               try {
                  BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
               } catch (Exception var6) {
               }

               this.field3051 = false;
            }
         }
      }
   }
}
