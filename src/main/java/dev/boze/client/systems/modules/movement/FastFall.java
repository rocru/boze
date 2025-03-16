package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.FastFallHorizontal;
import dev.boze.client.enums.FastFallMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class3091;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FastFall extends Module {
   public static final FastFall INSTANCE = new FastFall();
   private final EnumSetting<FastFallMode> field3246 = new EnumSetting<FastFallMode>(
      "Mode", FastFallMode.Step, "Mode for FastFall\n- Normal: Normal acceleration/tick shift fast fall\n- Step: Reverse Step\n"
   );
   private final FloatSetting field3247 = new FloatSetting("Height", 2.0F, 1.0F, 5.0F, 0.5F, "Downwards step height", this::lambda$new$0);
   private final BooleanSetting field3248 = new BooleanSetting("Accelerate", true, "Accelerate downwards", this::lambda$new$1);
   private final EnumSetting<FastFallHorizontal> field3249 = new EnumSetting<FastFallHorizontal>(
      "Horizontal", FastFallHorizontal.Cancel, "How to handle horizontal movement", this::lambda$new$2
   );
   private final FloatSetting field3250 = new FloatSetting("Multiplier", 1.0F, 1.0F, 50.0F, 0.5F, "Base fall speed multiplier", this::lambda$new$3);
   private final IntSetting field3251 = new IntSetting("ShiftTicks", 0, 0, 10, 1, "Shift ticks", this::lambda$new$4);
   private final MinMaxSetting field3252 = new MinMaxSetting("ShiftHeight", 3.0, 0.5, 50.0, 0.5, "Max height to shift ticks at", this::lambda$new$5);
   private boolean field3253;
   private boolean field3254 = false;

   public FastFall() {
      super("FastFall", "Makes you fall faster", Category.Movement);
   }

   @Override
   public void onEnable() {
      this.field3253 = false;
   }

   @EventHandler
   public void method1830(MovementEvent event) {
      if (mc.player != null) {
         if (this.field3246.method461() == FastFallMode.Step) {
            if (this.field3254
               && !mc.player.isOnGround()
               && mc.player.getVelocity().y <= 0.0
               && mc.world
                  .getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0.0, -((double)this.field3247.method423().floatValue() + 0.01), 0.0))
                  .iterator()
                  .hasNext()
               && !mc.player.isSubmergedInWater()) {
               mc.player.setVelocity(mc.player.getVelocity().x, -3.0, mc.player.getVelocity().z);
            }

            this.field3254 = mc.player.isOnGround();
         }
      }
   }

   @EventHandler
   public void method1831(PrePlayerTickEvent event) {
      if (this.field3246.method461() == FastFallMode.Normal) {
         if (mc.world.getBlockState(BlockPos.ofFloored(mc.player.getPos())).getBlock() != Blocks.AIR) {
            this.field3253 = false;
         } else {
            if (mc.player.isOnGround()) {
               mc.player.setVelocity(mc.player.getVelocity().x, -0.0784 * (double)this.field3250.method423().floatValue(), mc.player.getVelocity().z);
               this.field3253 = true;
            }

            if (mc.player.getVelocity().y > 0.0) {
               this.field3253 = false;
            }
         }
      }
   }

   @EventHandler
   public void method1832(PlayerMoveEvent event) {
      if (this.field3246.method461() == FastFallMode.Normal) {
         if (this.field3253 && !mc.player.isOnGround()) {
            if (mc.world
               .isSpaceEmpty(mc.player.getBoundingBox().withMinY(Math.max((double)mc.world.getBottomY(), mc.player.getY() - this.field3252.getValue())))) {
               return;
            }

            if (event.vec3.y < 0.0) {
               for (int var5 = 0; var5 < this.field3251.method434(); var5++) {
                  Class3091.field217 = true;
                  mc.player
                     .move(
                        event.movementType,
                        new Vec3d(
                           this.field3249.method461() == FastFallHorizontal.Boost ? event.vec3.x : 0.0,
                           event.vec3.y,
                           this.field3249.method461() == FastFallHorizontal.Boost ? event.vec3.z : 0.0
                        )
                     );
                  Class3091.field217 = false;
                  ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(mc.player.isOnGround());
                  if (this.field3248.method419()) {
                     event.vec3 = event.vec3.subtract(0.0, 0.08, 0.0);
                     mc.player.setVelocity(mc.player.getVelocity().x, mc.player.getVelocity().y - 0.08, mc.player.getVelocity().z);
                  }
               }

               if (this.field3249.method461() == FastFallHorizontal.Cancel) {
                  event.vec3 = event.vec3.subtract(event.vec3.x, 0.0, event.vec3.z);
               }
            }
         }
      }
   }

   private boolean lambda$new$5() {
      return this.field3246.method461() == FastFallMode.Normal && this.field3251.method434() > 0;
   }

   private boolean lambda$new$4() {
      return this.field3246.method461() == FastFallMode.Normal;
   }

   private boolean lambda$new$3() {
      return this.field3246.method461() == FastFallMode.Normal;
   }

   private boolean lambda$new$2() {
      return this.field3246.method461() == FastFallMode.Normal;
   }

   private boolean lambda$new$1() {
      return this.field3246.method461() == FastFallMode.Normal;
   }

   private boolean lambda$new$0() {
      return this.field3246.method461() == FastFallMode.Step;
   }
}
