package dev.boze.client.systems.modules.legit;

import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.hit.HitResult.Type;

public class JumpReset extends Module {
   public static final JumpReset INSTANCE = new JumpReset();
   private final BooleanSetting field2807 = new BooleanSetting("OnlyFacing", false, "Only jump if you are facing a player");
   private final MinMaxSetting field2808 = new MinMaxSetting("Chance", 1.0, 0.01, 1.0, 0.01, "Chance to jump");
   private final IntArraySetting field2809 = new IntArraySetting("Delay", new int[]{1, 4}, 0, 10, 1, "Tick delay for jump");

   private JumpReset() {
      super("JumpReset", "Jumps when hit to minimize velocity", Category.Legit);
   }

   @EventHandler
   public void method1607(TickInputPostEvent event) {
      if (mc.player.isOnGround() && !event.field1955 && mc.player.hurtTime < 11 && mc.player.hurtTime > this.field2809.method1367()) {
         if (mc.player.getRecentDamageSource() != null && mc.player.getRecentDamageSource().getAttacker() == null) {
            return;
         }

         if (Math.random() <= this.field2808.getValue()
            && (!this.field2807.method419() || mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.ENTITY)) {
            event.field1955 = true;
         }

         this.field2809.method1376();
      }
   }
}
