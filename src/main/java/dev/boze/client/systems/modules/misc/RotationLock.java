package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.RotationLockYawLock;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.movement.GrimDisabler;
import meteordevelopment.orbit.EventHandler;

import java.util.Random;

public class RotationLock extends Module {
   public static final RotationLock INSTANCE = new RotationLock();
   private final EnumSetting<RotationLockYawLock> field3083 = new EnumSetting<RotationLockYawLock>(
      "LockYaw",
      RotationLockYawLock.Angle,
      "Yaw lock mode\n - Off: No yaw lock\n - Angle: Locks yaw to specified angle\n - Cardinal: Locks yaw to nearest cardinal (90) direction on activation\n - InterCardinal: Locks yaw to nearest inter-cardinal (45) direction on activation\n"
   );
   private final BooleanSetting field3084 = new BooleanSetting("LockPitch", false, "Lock pitch");
   private final FloatSetting field3085 = new FloatSetting("Yaw", 0.0F, -180.0F, 180.0F, 0.1F, "Yaw", this::lambda$new$0);
   private final FloatSetting field3086 = new FloatSetting("Pitch", 0.0F, -90.0F, 90.0F, 0.1F, "Pitch", this::lambda$new$1);
   private final Random field3087 = new Random();

   public RotationLock() {
      super("RotationLock", "Locks your rotation", Category.Misc);
   }

   @EventHandler
   private void method1762(PostPlayerTickEvent var1) {
      if (this.field3083.method461() == RotationLockYawLock.Angle) {
         mc.player.setYaw(this.field3085.method423());
      } else if (this.field3083.method461() == RotationLockYawLock.Cardinal) {
         mc.player
            .setYaw(
               (float)(Math.round(mc.player.getYaw() / 90.0F) * 90)
                  + (GrimDisabler.INSTANCE.isEnabled() ? (this.field3087.nextBoolean() ? 0.1F : -0.1F) : 0.0F)
            );
      } else if (this.field3083.method461() == RotationLockYawLock.InterCardinal) {
         mc.player
            .setYaw(
               (float)(Math.round(mc.player.getYaw() / 45.0F) * 45)
                  + (GrimDisabler.INSTANCE.isEnabled() ? (this.field3087.nextBoolean() ? 0.1F : -0.1F) : 0.0F)
            );
      }

      if (this.field3084.method419()) {
         mc.player.setPitch(this.field3086.method423());
      }
   }

   private boolean lambda$new$1() {
      return this.field3084.method419();
   }

   private boolean lambda$new$0() {
      return this.field3083.method461() == RotationLockYawLock.Angle;
   }
}
