package dev.boze.client.systems.modules.movement;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.mixin.FireworkRocketEntityAccessor;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.LinkedList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.math.Vec3d;

public class RocketTweaks extends Module {
   public static RocketTweaks INSTANCE = new RocketTweaks();
   private final MinMaxSetting field3327 = new MinMaxSetting("Boost", 0.0, 0.0, 10.0, 0.1, "Additional boost when using firework rockets");
   private final IntSetting field3328 = new IntSetting("ExtendDuration", 0, 0, 60, 1, "Extend rocket duration by this amount");
   private final IntSetting field3329 = new IntSetting(
      "ChunkLimit",
      8,
      1,
      15,
      1,
      "The max amount of chunks away from the starting chunk to extend for\nThis must not be greater than the server's view distance",
      this.field3328
   );
   private final LinkedList<EntityStatusS2CPacket> field3330 = new LinkedList();
   private final LinkedList<EntitiesDestroyS2CPacket> field3331 = new LinkedList();
   private final Timer field3332 = new Timer();
   private Vec3d field3333 = null;

   private boolean method1861() {
      return this.field3328.method434() > 0;
   }

   private RocketTweaks() {
      super("RocketTweaks", "Tweaks for firework rockets", Category.Movement);
   }

   @Override
   public String method1322() {
      return this.field3333 != null ? "Extending" : super.method1322();
   }

   @Override
   public void onDisable() {
      if (MinecraftUtils.isClientActive()) {
         this.method1863();
      }
   }

   @EventHandler
   public void method1862(PostPlayerTickEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (mc.player.isFallFlying() && this.field3327.getValue() > 0.0) {
            if (mc.world.getEntitiesByType(EntityType.FIREWORK_ROCKET, mc.player.getBoundingBox().expand(30.0), RocketTweaks::lambda$onPlayerTick$0).isEmpty()) {
               return;
            }

            Vec3d var5 = mc.player.getRotationVector();
            Vec3d var6 = mc.player.getVelocity();
            mc.player
               .addVelocity(
                  (var5.x * 0.1 + (var5.x * 1.5 - var6.x) * 0.5) * this.field3327.getValue(),
                  (var5.y * 0.1 + (var5.y * 1.5 - var6.y) * 0.5) * this.field3327.getValue(),
                  (var5.z * 0.1 + (var5.z * 1.5 - var6.z) * 0.5) * this.field3327.getValue()
               );
         }
      }
   }

   private void method1863() {
      for (EntityStatusS2CPacket var5 : this.field3330) {
         mc.getNetworkHandler().onEntityStatus(var5);
      }

      this.field3330.clear();

      for (EntitiesDestroyS2CPacket var7 : this.field3331) {
         mc.getNetworkHandler().onEntitiesDestroy(var7);
      }

      this.field3331.clear();
      this.field3333 = null;
   }

   @EventHandler
   public void method1864(MovementEvent event) {
      if (this.method1861()) {
         if (this.field3333 != null
            && (
               this.field3333.squaredDistanceTo(mc.player.getX(), 0.0, mc.player.getZ()) > Math.pow((double)(this.field3329.method434() * 16), 2.0)
                  || !mc.player.isFallFlying()
                  || this.field3332.hasElapsed(55000.0)
            )) {
            this.method1863();
         }
      }
   }

   @EventHandler
   public void method1865(PacketBundleEvent event) {
      if (MinecraftUtils.isClientActive() && this.method1861()) {
         if (event.packet instanceof EntityStatusS2CPacket var5) {
            if (var5.getEntity(mc.world) instanceof FireworkRocketEntity var11
               && ((FireworkRocketEntityAccessor)var11).getShooter() == mc.player
               && mc.player.isFallFlying()) {
               if (this.field3333 == null) {
                  this.field3333 = new Vec3d(mc.player.getX(), 0.0, mc.player.getZ());
                  this.field3332.reset();
               }

               this.field3330.add(var5);
               event.method1020();
            }
         } else if (this.field3333 != null && this.method1861()) {
            if (event.packet instanceof CommonPingS2CPacket) {
               event.method1020();
            } else if (event.packet instanceof EntitiesDestroyS2CPacket var12) {
               IntListIterator var14 = var12.getEntityIds().iterator();

               while (var14.hasNext()) {
                  int var8 = (Integer)var14.next();
                  Entity var10 = mc.world.getEntityById(var8);
                  if (var10 instanceof FireworkRocketEntity) {
                     FireworkRocketEntity var9 = (FireworkRocketEntity)var10;
                     if (((FireworkRocketEntityAccessor)var9).getShooter() == mc.player) {
                        this.field3331.add(var12);
                        event.method1020();
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean lambda$onPlayerTick$0(FireworkRocketEntity var0) {
      return ((FireworkRocketEntityAccessor)var0).getShooter() == mc.player;
   }
}
