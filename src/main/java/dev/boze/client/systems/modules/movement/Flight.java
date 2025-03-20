package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import dev.boze.client.enums.AntiKickMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class Flight extends Module {
   public static final Flight INSTANCE = new Flight();
   private final MinMaxSetting field3271 = new MinMaxSetting("Speed", 1.0, 0.1, 50.0, 0.1, "Horizontal speed");
   private final MinMaxSetting field3272 = new MinMaxSetting("UpSpeed", 1.0, 0.1, 10.0, 0.1, "Up speed");
   private final MinMaxSetting field3273 = new MinMaxSetting("DownSpeed", 1.0, 0.1, 10.0, 0.1, "Down speed");
   private final MinMaxSetting field3274 = new MinMaxSetting("AccelSpeed", 0.5, 0.05, 1.0, 0.05, "Acceleration speed");
   private final EnumSetting<AntiKickMode> field3275 = new EnumSetting<AntiKickMode>("AntiKick", AntiKickMode.Strong, "Anti Kick Mode");
   private final BooleanSetting field3276 = new BooleanSetting("InLiquid", true, "Fly in liquids");
   private final BooleanSetting field3277 = new BooleanSetting("AvoidUnloaded", true, "Avoid flying into unloaded chunks");
   private final IntSetting field3278 = new IntSetting(
      "Ticks", 5, 1, 20, 1, "Amount of ticks to simulate\nHigher value = less chance of rubber-banding", this.field3277
   );
   public final BooleanSetting field3279 = new BooleanSetting("NoClip", false, "NoClip mode - lets you fly through blocks\nThis won't work on most servers\n");
   private Timer field3280 = new Timer();
   private Vec3d field3281 = null;
   private boolean field3282 = false;

   public Flight() {
      super("Flight", "Fly", Category.Movement);
   }

   @Override
   public void onEnable() {
      this.field3281 = null;
      this.field3282 = false;
      this.field3280.reset();
   }

   @Override
   public void onDisable() {
      if (MinecraftUtils.isClientActive()) {
         mc.player.setVelocity(Vec3d.ZERO);
      }
   }

   @EventHandler(
      priority = 50
   )
   public void method1836(PlayerMoveEvent event) {
      if (!event.field1892) {
         if (this.field3276.getValue() || !Class5924.method91(FluidBlock.class)) {
            if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() == null) {
               Vec3d var5 = Class5924.method93(Class5924.method2091() * this.field3271.getValue());
               double var6 = mc.options.jumpKey.isPressed()
                  ? 0.4 * this.field3272.getValue()
                  : (mc.options.sneakKey.isPressed() ? -0.4 * this.field3273.getValue() : 0.0);
               if (this.field3275.getValue() != AntiKickMode.Off
                  && this.field3280.hasElapsed(1000.0)
                  && !mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(var5.x, -0.1, var5.z)).iterator().hasNext()) {
                  this.field3280.reset();
                  if (this.field3275.getValue() == AntiKickMode.Strict) {
                     var6 -= 0.0313;
                  } else {
                     if (this.field3275.getValue() == AntiKickMode.Toggle) {
                        return;
                     }

                     this.field3282 = true;
                  }
               }

               Vec3d var8 = new Vec3d(mc.player.getVelocity().x, mc.player.getY() - mc.player.prevY, mc.player.getVelocity().z);
               Vector3d var9 = new Vector3d(var5.x, var6, var5.z);
               if (this.field3277.getValue()) {
                  int var10 = ChunkSectionPos.getSectionCoord(mc.player.getX() + var9.x * (double)this.field3278.method434().intValue());
                  int var11 = ChunkSectionPos.getSectionCoord(mc.player.getZ() + var9.z * (double)this.field3278.method434().intValue());
                  boolean var12 = mc.world.getChunkManager().isChunkLoaded(var10, var11);
                  if (!var12) {
                     var9.x = 0.0;
                     var9.z = 0.0;
                  }
               }

               if (!mc.player.isOnGround()
                  && mc.world
                     .isSpaceEmpty(mc.player.getBoundingBox().withMaxY(mc.player.getBoundingBox().minY).withMinY(mc.player.getBoundingBox().minY - 0.3))) {
                  if (this.field3274.getValue() < 1.0 && var8.subtract(var9.x, var9.y, var9.z).length() > 0.5) {
                     var9.set(
                        var8.x + (var9.x - var8.x) * this.field3274.getValue(),
                        var8.y + (var9.y - var8.y) * this.field3274.getValue(),
                        var8.z + (var9.z - var8.z) * this.field3274.getValue()
                     );
                  }
               } else if (this.field3274.getValue() < 1.0 && var8.subtract(var9.x, 0.0, var9.z).length() > 0.5) {
                  var9.set(var8.x + (var9.x - var8.x) * this.field3274.getValue(), var9.y, var8.z + (var9.z - var8.z) * this.field3274.getValue());
               }

               event.vec3 = new Vec3d(var9.x, var9.y, var9.z);
               event.field1892 = true;
               mc.player.setVelocity(event.vec3);
            }
         }
      }
   }

   @EventHandler
   public void method1837(MovementEvent event) {
      if (this.field3282) {
         event.field1931 -= 0.0313;
         this.field3282 = false;
      }

      this.field3281 = mc.player.getPos();
   }
}
