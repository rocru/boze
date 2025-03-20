package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class AntiHunger extends Module {
   public static final AntiHunger INSTANCE = new AntiHunger();
   public final BooleanSetting sprint = new BooleanSetting("Sprint", true, "Prevent sprinting server-side");
   public final BooleanSetting ground = new BooleanSetting("Ground", true, "Avoid being on ground server-side");

   public AntiHunger() {
      super("AntiHunger", "Reduces hunger from actions", Category.Misc);
   }

   @EventHandler
   public void method1634(MovementEvent event) {
      if (this.sprint.getValue()) {
         event.isSprinting = false;
      }

      if (mc.player.prevY <= mc.player.getY() && this.ground.getValue()) {
         event.isOnGround = false;
      }
   }
}
