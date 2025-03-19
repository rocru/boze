package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.SprintMode;
import dev.boze.client.events.*;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.player.RotationHandler;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.EntityPose;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Vec3d;

public class Sprint extends Module {
   public static final Sprint INSTANCE = new Sprint();
   private final EnumSetting<SprintMode> field3334 = new EnumSetting<SprintMode>("Mode", SprintMode.NCP, "Mode for sprinting", Sprint::lambda$new$0);
   private final IntSetting field3335 = new IntSetting(
      "JumpDelay", 0, 0, 10, 1, "Ticks to wait after jumping before sprinting again\n0 to disable", this::method1868
   );
   private final BooleanSetting field3336 = new BooleanSetting("Rage", true, "Sprint in every direction", this::lambda$new$1);
   private final BooleanSetting field3337 = new BooleanSetting("Instant", false, "Accelerate instantly", this::lambda$new$2);
   private final BooleanSetting field3338 = new BooleanSetting(
      "Pulsate",
      false,
      "Pulsate instant sprint\nThis turns on instant for x (Duration) ticks, then turns it off for y (Interval) ticks, then repeats",
      this::lambda$new$3
   );
   private final IntSetting field3339 = new IntSetting(
      "Duration", 10, 1, 80, 1, "Pulse duration\nTicks to instant sprint for", this::lambda$new$4, this.field3338
   );
   private final IntSetting field3340 = new IntSetting(
      "Interval", 10, 1, 80, 1, "Pulse interval\nThe ticks to wait in-between pulses of instant sprint", this::lambda$new$5, this.field3338
   );
   private final BindSetting field3341 = new BindSetting("NoSprint", Bind.create(), "Override sprint and walk normally when bind held");
   private float field3342;
   private float field3343;
   private float field3344;
   private int field3345 = 0;
   private int field3346 = 0;
   private int field3347 = 0;
   private boolean field3348 = false;

   private boolean method1867() {
      return Options.INSTANCE.method1971() || this.field3334.method461() == SprintMode.Ghost;
   }

   private boolean method1868() {
      return !this.method1867() && this.field3334.method461() == SprintMode.NCP;
   }

   public boolean method1869() {
      return !this.method1867() && this.field3334.method461() == SprintMode.Grim && this.isEnabled();
   }

   public Sprint() {
      super("Sprint", "Makes you sprint automatically", Category.Movement);
      this.field435 = true;
   }

   @Override
   public void onEnable() {
      this.field3345 = this.field3340.method434();
      this.field3346 = 0;
      this.field3347 = 0;
      this.field3348 = false;
      this.field3344 = -420.0F;
   }

   @EventHandler
   public void method1870(KeyPressedEvent event) {
      if (!this.method1868()) {
         if (event.method1067() == mc.options.sprintKey && !this.field3341.method476().isPressed()) {
            event.method1070(true);
         }
      }
   }

   @EventHandler(
      priority = 20
   )
   public void method1871(PlayerGrimV3BypassEvent event) {
      if (this.method1869() && !mc.player.isSneaking() && !this.field3341.method476().isPressed() && (this.field3342 != 0.0F || this.field3343 != 0.0F)) {
         if (event.method1022()) {
            this.field3344 = -420.0F;
         } else {
            this.field3344 = mc.player.getYaw();
            if (this.field3342 == 0.0F && this.field3343 == 0.0F) {
               this.field3344 = -420.0F;
            } else {
               if (this.field3342 != 0.0F) {
                  if (this.field3343 > 0.0F) {
                     this.field3344 = this.field3344 + (float)(this.field3342 > 0.0F ? -45 : 45);
                  } else if (this.field3343 < 0.0F) {
                     this.field3344 = this.field3344 + (float)(this.field3342 > 0.0F ? 45 : -45);
                  }

                  if (this.field3342 < 0.0F) {
                     this.field3344 += 180.0F;
                  }
               } else if (this.field3343 != 0.0F) {
                  if (this.field3343 > 0.0F) {
                     this.field3344 -= 90.0F;
                  } else if (this.field3343 < 0.0F) {
                     this.field3344 += 90.0F;
                  }
               }

               this.field3344 %= 360.0F;
               event.yaw = this.field3344;
               event.method1021(true);
            }
         }
      }
   }

   @EventHandler(
      priority = 20
   )
   public void method1872(TickInputPostEvent event) {
      if (this.method1869() && !RotationHandler.field1546.method2114() && !mc.player.isSneaking() && !this.field3341.method476().isPressed()) {
         this.field3342 = event.field1954;
         this.field3343 = event.field1953;
         if (event.field1954 != 0.0F || event.field1953 != 0.0F) {
            event.field1954 = Math.max(Math.abs(event.field1954), Math.abs(event.field1953));
         }

         event.field1953 = 0.0F;
      }
   }

   @EventHandler(
      priority = 35
   )
   public void method1873(PlayerMoveEvent event) {
      if (this.method1868() && !event.field1892) {
         if (this.field3345 < this.field3340.method434()) {
            this.field3345++;
            this.field3346 = 0;
         } else {
            if (this.field3337.method419()
               && this.field3335.method434() == 0
               && !Speed.INSTANCE.isEnabled()
               && mc.player.getHungerManager().getFoodLevel() > 6
               && !mc.player.isFallFlying()
               && mc.player.getFluidHeight(FluidTags.LAVA) == 0.0
               && mc.player.getFluidHeight(FluidTags.WATER) == 0.0
               && mc.player.getPose() == EntityPose.STANDING
               && BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() == null) {
               Vec3d var5 = Class5924.method93(Class5924.method2091());
               event.vec3 = new Vec3d(var5.x, event.vec3.y, var5.z);
               event.field1892 = true;
               if (this.field3338.method419()) {
                  this.field3346++;
                  if (this.field3346 >= this.field3339.method434()) {
                     this.field3346 = 0;
                     this.field3345 = 0;
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 20
   )
   public void method1874(ACRotationEvent event) {
      if (!event.method1018(AnticheatMode.NCP, true)) {
         if (this.method1869() && this.field3344 != -420.0F) {
            event.method1021(true);
            event.yaw = this.field3344;
            event.pitch = mc.player.getPitch();
            this.field3344 = -420.0F;
         }
      }
   }

   @EventHandler
   public void method1875(PostPlayerTickEvent event) {
      if (this.method1868()) {
         if (!this.field3341.method476().isPressed() && !mc.player.horizontalCollision) {
            if (this.field3335.method434() > 0) {
               if (!mc.player.isOnGround()) {
                  this.field3347 = this.field3335.method434();
                  return;
               }

               if (this.field3347 > 0) {
                  this.field3347--;
                  return;
               }
            }

            if (!(event.field1941.forwardSpeed > 0.0F) && (mc.currentScreen == null || !this.field3348)) {
               if (this.field3336.method419() && (event.field1941.forwardSpeed != 0.0F || event.field1941.sidewaysSpeed != 0.0F)) {
                  event.field1941.setSprinting(true);
               }
            } else {
               event.field1941.setSprinting(true);
               this.field3348 = false;
            }

            this.field3348 = mc.currentScreen == null && mc.player.isSprinting();
         } else {
            event.field1941.setSprinting(false);
         }
      }
   }

   private boolean lambda$new$5() {
      return this.field3337.method419() && this.method1868();
   }

   private boolean lambda$new$4() {
      return this.field3337.method419() && this.method1868();
   }

   private boolean lambda$new$3() {
      return this.field3337.method419() && this.method1868() && this.field3335.method434() == 0;
   }

   private boolean lambda$new$2() {
      return this.field3335.method434() == 0 && this.method1868();
   }

   private boolean lambda$new$1() {
      return this.method1868();
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
