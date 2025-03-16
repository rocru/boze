package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FastSwim extends Module {
   public static final FastSwim INSTANCE = new FastSwim();
   private final IntSetting field3255 = new IntSetting("Acceleration", 1, 0, 20, 1, "Acceleration ticks");
   private final MinMaxSetting field3256 = new MinMaxSetting("Speed", 1.0, 0.1, 20.0, 0.1, "Horizontal speed");
   private final MinMaxSetting field3257 = new MinMaxSetting("UpSpeed", 0.1, 0.0, 10.0, 0.1, "Up speed");
   private final MinMaxSetting field3258 = new MinMaxSetting("DownSpeed", 0.1, 0.0, 10.0, 0.1, "Down speed");
   private final MinMaxSetting field3259 = new MinMaxSetting("FallSpeed", 1.0, 0.0, 10.0, 0.1, "Down speed");
   private final MinMaxSetting field3260 = new MinMaxSetting("LavaSpeed", 1.0, 0.1, 20.0, 0.1, "Horizontal speed");
   private final MinMaxSetting field3261 = new MinMaxSetting("LavaUp", 0.1, 0.0, 10.0, 0.1, "Up speed");
   private final MinMaxSetting field3262 = new MinMaxSetting("LavaDown", 0.1, 0.0, 10.0, 0.1, "Down speed");
   private final MinMaxSetting field3263 = new MinMaxSetting("LavaFall", 1.0, 0.0, 10.0, 0.1, "Down speed");
   private final BooleanSetting field3264 = new BooleanSetting("AntiKick", true, "Anti Kick");
   private final IntSetting field3265 = new IntSetting("Interval", 2, 1, 20, 1, "Anti Kick Interval", this.field3264);
   private final IntSetting field3266 = new IntSetting("Ticks", 1, 1, 5, 1, "Anti Kick Ticks", this.field3264);
   private long field3267 = -1L;
   private Vec3d field3268 = null;
   private int field3269 = 0;
   private int field3270 = 0;

   public FastSwim() {
      super("FastSwim", "Swim Faster", Category.Movement);
   }

   @Override
   public void onEnable() {
      this.field3268 = null;
      this.field3267 = System.currentTimeMillis();
   }

   @EventHandler(
      priority = 52
   )
   public void method1833(PlayerMoveEvent event) {
      if (!event.field1892) {
         if (this.field3264.method419()) {
            if (this.field3269 >= this.field3265.method434()) {
               this.field3270++;
               if (this.field3270 >= this.field3266.method434()) {
                  this.field3270 = 0;
                  this.field3269 = 0;
               }

               return;
            }

            this.field3269++;
         }

         if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() == null) {
            double var5;
            double var7;
            double var9;
            double var11;
            if (mc.player.getFluidHeight(FluidTags.LAVA) > 0.0) {
               var5 = this.field3260.getValue();
               var7 = this.field3261.getValue();
               var9 = this.field3262.getValue();
               var11 = this.field3263.getValue();
            } else {
               if (!(mc.player.getFluidHeight(FluidTags.WATER) > 0.0)) {
                  return;
               }

               var5 = this.field3256.getValue();
               var7 = this.field3257.getValue();
               var9 = this.field3258.getValue();
               var11 = this.field3259.getValue();
            }

            double var13 = this.field3255.method434() == 0
               ? 1.0
               : MathHelper.clamp((double)(System.currentTimeMillis() - this.field3267) / (double)(this.field3255.method434() * 50), 0.0, 1.0);
            Vec3d var15 = Class5924.method93(Class5924.method2091() * var5 * var13);
            double var16 = mc.options.jumpKey.isPressed() ? 0.4 * var7 : (mc.options.sneakKey.isPressed() ? -0.4 * var9 : -var11 * 0.04);
            event.vec3 = new Vec3d(var15.x, var16, var15.z);
            event.field1892 = true;
         }
      }
   }

   @EventHandler
   public void method1834(MovementEvent event) {
      if (this.field3268 == null || mc.player.getPos().distanceTo(this.field3268) <= 9.0E-4) {
         this.field3267 = System.currentTimeMillis();
      }

      this.field3268 = mc.player.getPos();
   }

   @EventHandler
   public void method1835(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket && MinecraftUtils.isClientActive() && !(mc.currentScreen instanceof DownloadingTerrainScreen)) {
         this.field3267 = System.currentTimeMillis();
      }
   }
}
