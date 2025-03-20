package dev.boze.client.systems.modules.movement.boatfly;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerTravelEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.PlayerPositionLookS2CPacketAccessor;
import dev.boze.client.systems.modules.movement.BoatFly;
import mapped.Class5924;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicBoolean;

class nl extends nj {
   private static final int field1581 = 20;
   private static final int field1582 = 4;
   private static final int field1583 = 2;
   private static final double field1584 = 0.099;
   private static final double field1585 = -0.033;
   private static final double field1586 = -0.04;
   private static final double field1587 = -0.04F;
   private static final double field1588 = 32.0;
   private int field1589;
   private int field1590;
   private boolean field1591;
   private final AtomicBoolean field1592 = new AtomicBoolean(false);

   public nl(BoatFly module) {
      super(module);
   }

   @Override
   void method2142() {
      this.field1589 = 0;
   }

   @Override
   void method1416() {
   }

   @Override
   void method1794(PlayerTravelEvent var1) {
      if (this.method2116()) {
         if (mc.player.getVehicle() instanceof BoatEntity) {
            if (mc.player.age % 20 == 0) {
               this.field1590 = 0;
            }

            BoatEntity var5 = (BoatEntity)mc.player.getVehicle();
            this.method703(var5);
            if (!this.field1556.field3158.getValue()) {
               this.method1198();
            }

            var1.method1020();
         }
      }
   }

   private boolean method2116() {
      if (mc.player != null && mc.world != null) {
         return true;
      } else {
         this.field1556.toggle();
         return false;
      }
   }

   private void method703(BoatEntity var1) {
      var1.setNoGravity(true);
      var1.setYaw(mc.player.getYaw());
      Vec3d var5 = this.method1954();
      if (!this.field1556.field3158.getValue() && (this.field1556.field3160.getValue() || mc.player.age % 2 != 0)) {
         if (this.field1592.get() && this.field1556.field3161.getValue()) {
            var1.setVelocity(0.0, 0.0, 0.0);
            this.field1592.set(false);
         } else {
            var1.setVelocity(var5);
         }
      }
   }

   private Vec3d method1954() {
      double var4 = 0.0;
      double var6 = 0.0;
      double var8 = 0.0;
      if (Class5924.method2116()) {
         Vec3d var10 = Class5924.method93((double)this.field1556.field3152.getValue().floatValue());
         var4 = var10.x;
         var8 = var10.z;
      }

      var6 = this.method2091();
      return new Vec3d(var4, var6, var8);
   }

   private double method2091() {
      BoatEntity var4 = (BoatEntity)mc.player.getVehicle();
      if (mc.options.jumpKey.isPressed()) {
         if (mc.player.age % 8 < 2) {
            var4.setVelocity(var4.getVelocity().x, -0.04, var4.getVelocity().z);
         }

         return (double)this.field1556.field3153.getValue().floatValue();
      } else if (this.field1556.field3155.method476().isPressed()) {
         return (double)(-this.field1556.field3154.getValue());
      } else if (this.field1556.field3157.getValue()) {
         if (mc.player.age % 4 == 0) {
            var4.setVelocity(var4.getVelocity().x, 0.099, var4.getVelocity().z);
         } else {
            var4.setVelocity(var4.getVelocity().x, -0.033, var4.getVelocity().z);
         }

         return 0.0;
      } else {
         return -0.04F;
      }
   }

   private void method1198() {
      this.field1589++;
      mc.player.networkHandler.sendPacket(new TeleportConfirmC2SPacket(this.field1589));
   }

   @Override
   void method2042(PacketBundleEvent var1) {
      if (this.method2116()) {
         if (!this.field1556.field3158.getValue()) {
            this.method704(var1);
         }

         this.method705(var1);
      }
   }

   private void method704(PacketBundleEvent var1) {
      if (var1.packet instanceof PlayerPositionLookS2CPacket && mc.player.isRiding()) {
         PlayerPositionLookS2CPacket var5 = (PlayerPositionLookS2CPacket)var1.packet;
         PlayerPositionLookS2CPacketAccessor var6 = (PlayerPositionLookS2CPacketAccessor)var5;
         var6.setYaw(mc.player.getYaw());
         var6.setPitch(mc.player.getPitch());
         var5.getFlags().remove(PositionFlag.X_ROT);
         var5.getFlags().remove(PositionFlag.Y_ROT);
         this.field1589 = var5.getTeleportId();
         if (!this.field1556.field3156.getValue()) {
            Vec3d var7 = new Vec3d(var5.getX(), var5.getY(), var5.getZ());
            if (this.field1590 < this.field1556.field3162.method434() && var7.distanceTo(mc.player.getPos()) < 32.0) {
               var1.method1020();
            }
         }
      }
   }

   private void method705(PacketBundleEvent var1) {
      if (var1.packet instanceof VehicleMoveS2CPacket && mc.player.isRiding()) {
         if (this.field1556.field3158.getValue()) {
            if (!this.field1556.field3156.getValue()) {
               var1.method1020();
            }
         } else if (this.field1556.field3161.getValue()) {
            this.field1592.set(true);
         } else {
            this.field1590++;
            VehicleMoveS2CPacket var5 = (VehicleMoveS2CPacket)var1.packet;
            Vec3d var6 = new Vec3d(var5.getX(), var5.getY(), var5.getZ());
            if (this.field1590 < this.field1556.field3162.method434() && var6.distanceTo(mc.player.getVehicle().getPos()) < 32.0) {
               var1.method1020();
            }
         }
      }
   }

   @Override
   void method1853(PrePacketSendEvent var1) {
      if (this.method2116() && mc.player.isRiding() && !this.field1591 && this.field1556.field3159.getValue()) {
         if (var1.packet instanceof VehicleMoveC2SPacket && !this.field1592.get()) {
            this.method1904();
         } else if (var1.packet instanceof LookAndOnGround && mc.player.isRiding()) {
            var1.method1020();
         } else if (var1.packet instanceof PlayerInputC2SPacket && !this.field1556.field3160.getValue() && mc.player.age % 2 == 0) {
            var1.method1020();
         }
      }
   }

   private void method1904() {
      if (this.field1556.field3160.getValue()) {
         this.field1591 = true;
         mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(mc.player.getVehicle()));
         this.field1591 = false;
         mc.interactionManager.interactEntity(mc.player, mc.player.getVehicle(), Hand.OFF_HAND);
      } else if (mc.player.age % 2 == 0) {
         mc.interactionManager.interactEntity(mc.player, mc.player.getVehicle(), Hand.OFF_HAND);
      }
   }
}
