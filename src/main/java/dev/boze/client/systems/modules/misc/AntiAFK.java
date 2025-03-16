package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.AntiAFKMode;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class AntiAFK extends Module {
   public static final AntiAFK INSTANCE = new AntiAFK();
   private final EnumSetting<AntiAFKMode> mode = new EnumSetting<AntiAFKMode>(
      "Mode", AntiAFKMode.Jump, "Mode of AntiAFK\n - Jump: Jumps to prevent being kicked\n - Sneak: Sneaks to prevent being kicked\n"
   );
   private final MinMaxDoubleSetting delay = new MinMaxDoubleSetting("Delay", new double[]{5.0, 10.0}, 0.1, 100.0, 1.0, "Delay in minutes between actions");
   private final dev.boze.client.utils.Timer timer = new dev.boze.client.utils.Timer();

   private AntiAFK() {
      super("AntiAFK", "Prevents you from getting kicked for being AFK", Category.Misc);
      this.field435 = true;
      this.delay.method401(this::lambda$new$0);
   }

   @EventHandler
   public void method1629(PostTickEvent event) {
      if (mc.player == null) {
         this.timer.reset();
      }
   }

   @EventHandler
   public void method1630(MouseUpdateEvent event) {
      if (event.deltaX != 0.0 || event.deltaY != 0.0) {
         this.timer.reset();
      }
   }

   @EventHandler
   public void method1631(TickInputPostEvent event) {
      if ((double)event.field1954 == 0.0 && (double)event.field1953 == 0.0) {
         if (this.timer.hasElapsed(this.delay.method1295() * 60000.0)) {
            if (this.mode.method461() == AntiAFKMode.Jump) {
               event.field1955 = true;
            } else {
               event.field1956 = true;
            }

            this.timer.reset();
            this.delay.method1296();
         }
      } else {
         this.timer.reset();
      }
   }

   private void lambda$new$0(double[] var1) {
      this.delay.method1296();
   }
}
