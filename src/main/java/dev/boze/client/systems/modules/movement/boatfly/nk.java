package dev.boze.client.systems.modules.movement.boatfly;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixin.VehicleMoveC2SPacketAccessor;
import dev.boze.client.systems.modules.movement.BoatFly;
import dev.boze.client.systems.pathfinding.Path;
import dev.boze.client.systems.pathfinding.PathBuilder;
import dev.boze.client.systems.pathfinding.PathFinder;
import dev.boze.client.systems.pathfinding.PathRules;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.Timer;
import mapped.Class5924;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.BoatPaddleStateC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

class nk extends nj {
   private static final double field1557 = Math.sqrt(2.0);
   private static final double field1558 = -0.03125;
   private static final int field1559 = 60;
   private static final double field1560 = 0.04;
   private static final double field1561 = 1.5;
   private static final float field1562 = 90.0F;
   private static final float field1563 = 180.0F;
   private static final float field1564 = 45.0F;
   private static final float field1565 = 135.0F;
   private Entity field1566 = null;
   private boolean field1567 = false;
   private int field1568 = 0;
   private int field1569 = -1;
   private int field1570 = 0;
   private int field1571 = 0;
   private Vec3d field1572 = null;
   private Vec3d field1573 = null;
   private int field1574 = 0;
   private final Timer field1575 = new Timer();
   private Path field1576 = null;
   private Vec3d field1577 = null;
   private Vec3d field1578 = null;
   private Vec3d field1579 = null;
   private Vec3d field1580 = null;

   public nk(BoatFly module) {
      super(module);
   }

   private boolean method2055(Entity var1) {
      return var1 instanceof BoatEntity || var1 instanceof PigEntity || var1 instanceof HorseEntity;
   }

   private void method1198() {
      Entity var4 = mc.player.getVehicle();
      if (!this.method2055(var4)) {
         if (this.field1566 != null) {
            this.field1566.setNoGravity(false);
         }

         this.field1566 = null;
      } else {
         if (this.field1566 != var4) {
            if (this.field1566 != null) {
               this.field1566.setNoGravity(false);
            }

            var4.setNoGravity(true);
            this.field1566 = var4;
         }
      }
   }

   @Override
   void method2142() {
      if (MinecraftUtils.isClientActive()) {
         this.method1198();
      }

      this.method1904();
   }

   void method1904() {
      this.field1574 = 0;
      this.field1576 = null;
      this.field1577 = null;
      this.field1578 = null;
      if (MinecraftUtils.isClientActive() && this.field1556.field3170.method419() && this.field1556.field3169.method419()) {
         this.field1579 = mc.player.getPos();
         Vec3d var4 = mc.player.getRotationVec(1.0F);
         this.field1580 = new Vec3d(var4.x, 0.0, var4.z).normalize();
      } else {
         this.field1579 = null;
         this.field1580 = null;
      }
   }

   private Vec3d method697(Vec3d var1, Vec3d var2, Vec3d var3) {
      Vec3d var4 = var1.subtract(var2);
      double var5 = var4.dotProduct(var3);
      return var2.add(var3.multiply(var5));
   }

   @Override
   void method1416() {
      if (this.field1566 != null) {
         this.field1566.setNoGravity(false);
      }

      this.field1566 = null;
   }

   private void method494(Vec3d var1) {
      if (MinecraftUtils.isClientActive() && this.field1566 != null && !this.field1567) {
         this.field1566.setYaw(MathHelper.wrapDegrees(mc.player.getYaw()));
         this.field1566.setVelocity(Vec3d.ZERO);
         double var5 = var1.getX() - this.field1566.getX();
         double var7 = var1.getY() - this.field1566.getY();
         double var9 = var1.getZ() - this.field1566.getZ();
         if (this.field1571 > 60) {
            this.field1566.updatePosition(this.field1566.getX(), this.field1566.getY() - 0.04, this.field1566.getZ());
            this.method1854();
         }

         this.field1566.updatePosition(var1.getX(), var1.getY(), var1.getZ());
         if (var5 != 0.0 || var7 != 0.0 || var9 != 0.0) {
            this.method1854();
         }
      }
   }

   private void method1854() {
      if (MinecraftUtils.isClientActive() && this.field1566 != null) {
         VehicleMoveC2SPacket var4 = method698(this.field1566, 0.0);
         mc.player.networkHandler.sendPacket(var4);
         mc.player.networkHandler.sendPacket(method698(this.field1566, 1000.0));
         this.field1567 = true;
         this.field1568 = 0;
      }
   }

   private static VehicleMoveC2SPacket method698(@NotNull Entity var0, double var1) {
      VehicleMoveC2SPacket var3 = new VehicleMoveC2SPacket(var0);
      VehicleMoveC2SPacketAccessor var4 = (VehicleMoveC2SPacketAccessor)var3;
      var4.setY(var3.getY() + var1);
      return var3;
   }

   private boolean method2116() {
      return this.field1566 != null && this.field1572 != null && this.field1573 != null
         ? nm.method1799(this.field1566, this.field1572, 1.5) && this.field1572.y - this.field1573.y > -0.03125
         : false;
   }

   private Path method699() {
      if (this.field1556.field3169.method419()
         && MinecraftUtils.isClientActive()
         && this.field1566 != null
         && this.field1575.hasElapsed(this.field1556.field3175.getValue() * 1000.0)) {
         boolean var4 = false;
         if (this.field1556.field3177.method419()
            && this.field1576 != null
            && this.field1576.method2096()
            && (double)this.field1576.field3878 / (double)this.field1576.field3877.size() < 0.5) {
            Box var5 = this.field1566.getBoundingBox().expand(this.field1556.field3176.getValue(), 0.0, this.field1556.field3176.getValue());

            for (int var6 = this.field1576.field3878; var6 < this.field1576.field3877.size(); var6++) {
               Vec3d var7 = (Vec3d)this.field1576.field3877.get(var6);
               var5 = var5.offset(var7);
               if (!mc.world.getOtherEntities(mc.player, var5, nk::lambda$pathFind$0).isEmpty()) {
                  var4 = true;
               }
            }

            if (!var4) {
               return this.field1576;
            }
         }

         Vec3d var10 = this.field1566.getPos();
         Vec3d var11;
         if (this.field1556.field3170.method419() && this.field1579 != null && this.field1580 != null) {
            Vec3d var14 = this.method697(var10, this.field1579, this.field1580);
            var11 = var14.add(this.field1580.multiply((double)this.field1556.field3173.method434().intValue() * this.field1556.field3163.getValue() * 19.9));
         } else {
            Vec3d var12 = mc.player.getRotationVec(1.0F);
            var12 = new Vec3d(var12.x, 0.0, var12.z).normalize();
            var11 = var10.add(
               var12.multiply(Math.min((double)this.field1556.field3173.method434().intValue() * this.field1556.field3163.getValue() * 19.9, 200.0))
            );
         }

         PathFinder var15 = new PathFinder(BlockPos.ofFloored(var10), BlockPos.ofFloored(var11.x, var10.y, var11.z), new PathRules(true, true, true, true));
         if (this.field1556.field3170.method419() && this.field1579 != null && this.field1580 != null) {
            var15.method2121(this.field1579, this.field1580, (double)BoatFly.INSTANCE.field3171.method434().intValue());
         }

         while (!var15.method2117() && !var15.method2118()) {
            var15.method2098();
         }

         if (var15.method2117()) {
            double var8 = this.field1556.field3163.getValue() * 19.99;
            this.field1574 = 0;
            return PathBuilder.method616(var15.method2120(), var8, var8);
         } else {
            if (var15.method2118()) {
               this.field1574++;
               if (this.field1574 >= this.field1556.field3174.method434()) {
                  this.field1556.field3169.method421(false);
                  ChatInstance.method625("Failed to find path, disabling auto-pilot");
                  return null;
               }

               this.field1575.reset();
            }

            return null;
         }
      } else {
         return null;
      }
   }

   private Vec3d method1954() {
      Path var4 = this.method699();
      if (var4 == null) {
         return Vec3d.ZERO;
      } else {
         Vec3d var5 = var4.method2094();
         if (var5 == null) {
            return Vec3d.ZERO;
         } else {
            while (var5.lengthSquared() < 0.25 && var4.method2096()) {
               var5 = var5.add(var4.method2094());
            }

            var5 = new Vec3d(var5.x, 0.0, var5.z);
            this.field1577 = this.field1566.getPos();
            this.field1578 = this.field1577.add(var5);
            this.field1576 = var4;
            return var5;
         }
      }
   }

   @Override
   void method2072(PreTickEvent var1) {
      this.field1570++;
      if (this.field1570 % this.field1556.field3167.method434() == 0) {
         this.method1854();
      }

      if (this.field1567) {
         this.field1568++;
         if (this.field1568 > this.field1556.field3167.method434()) {
            this.method1854();
         }
      }
   }

   @Override
   void method2042(PacketBundleEvent var1) {
   }

   @Override
   boolean method1798(VehicleMoveS2CPacket var1) {
      this.method700(var1);
      return true;
   }

   private void method700(VehicleMoveS2CPacket var1) {
      this.field1567 = false;
      this.field1568 = 0;
      this.field1570 = 0;
      this.method1198();
      if (MinecraftUtils.isClientActive() && this.field1566 != null) {
         this.field1566.updatePosition(var1.getX(), var1.getY(), var1.getZ());
         mc.player.networkHandler.sendPacket(method698(this.field1566, 0.0));
         this.field1573 = this.field1572;
         this.field1572 = this.field1566.getPos();
         if (nm.method1799(this.field1566, this.field1572, 1.5)) {
            this.field1569 *= -1;
            this.field1566.updatePosition(this.field1566.getX(), this.field1566.getY() + (double)this.field1569 * 0.04, this.field1566.getZ());
         }
      }
   }

   @Override
   boolean method2114() {
      if (MinecraftUtils.isClientActive() && this.field1566 != null) {
         if (this.method2116()) {
            this.field1571++;
         } else {
            this.field1571 = 0;
         }

         Vec3d var4;
         if (this.field1556.field3169.method419() && !Class5924.method2116()) {
            var4 = this.method1954();
         } else {
            float var5 = MathHelper.wrapDegrees(mc.player.getYaw());
            var4 = this.method1952(var5);
         }

         this.method494(this.field1566.getPos().add(var4));
         return true;
      } else {
         return false;
      }
   }

   @Override
   boolean method2115() {
      return true;
   }

   @Override
   void method2071(Render3DEvent var1) {
      if (this.field1576 != null && this.field1556.field3178.method419() && this.field1556.field3169.method419()) {
         this.field1576.method2097(var1, this.field1556.field3179.method1362());
      }

      if (this.field1577 != null && this.field1556.field3178.method419() && this.field1556.field3169.method419()) {
         var1.field1950
            .method1236(
               this.field1577.x, this.field1577.y + 0.1, this.field1577.z, this.field1578.x, this.field1578.y + 0.1, this.field1578.z, RGBAColor.field403
            );
      }
   }

   @Override
   void method1853(PrePacketSendEvent var1) {
      if (var1.packet instanceof PlayerMoveC2SPacket || var1.packet instanceof BoatPaddleStateC2SPacket) {
         var1.method1020();
      }
   }

   private Vec3d method1952(float var1) {
      boolean var5 = mc.player.input.movementForward > 0.0F;
      boolean var6 = mc.player.input.movementForward < 0.0F;
      boolean var7 = mc.player.input.movementSideways < 0.0F;
      boolean var8 = mc.player.input.movementSideways > 0.0F;
      boolean var9 = this.field1556.field3155.method476().isPressed();
      boolean var10 = mc.player.input.jumping;
      boolean var11 = var5 || var6 || var8 || var7;
      boolean var12 = var10 || var9;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 0.0;
      double var19 = this.field1556.field3163.getValue() * 19.99;
      double var21 = this.field1556.field3164.getValue() * 19.99;
      if (this.field1556.field3165.method476().isPressed()) {
         var19 *= 1.0 - this.field1556.field3166.getValue();
         var21 *= 1.0 - this.field1556.field3166.getValue();
      }

      if (var12 && var11) {
         var19 /= field1557;
         var21 /= field1557;
      }

      if (var11) {
         float var23 = this.method702(var1, var5, var6, var7, var8);
         float var24 = -((float)Math.toRadians((double)MathHelper.wrapDegrees(var23)));
         var13 = var19 * (double)MathHelper.sin(var24);
         var15 = var19 * (double)MathHelper.cos(var24);
      }

      if (var10) {
         var17 += var21;
      }

      if (var9) {
         var17 -= var21;
      }

      Vec3d var28 = new Vec3d(var13, var17, var15);
      if (this.field1556.field3168.method419()) {
         if (mc.world.getRegistryKey().getValue().getPath().equals("the_nether") && mc.player.getY() + var28.y > 125.0) {
            var28 = new Vec3d(var28.x, 125.0 - mc.player.getY(), var28.z);
            if (mc.player.getY() + var28.y > 125.0) {
               throw new RuntimeException("Cannot move in nether roof");
            }
         }

         Box var29 = this.field1566.getBoundingBox();
         Box var25 = var29.offset(var28);
         if (mc.world.isSpaceEmpty(var25)) {
            return var28;
         }

         for (double var26 = 0.9; var26 > 0.1; var26 -= 0.1) {
            var25 = var29.offset(var28.multiply(var26));
            if (mc.world.isSpaceEmpty(var25)) {
               return var28.multiply(var26);
            }
         }
      }

      return var28;
   }

   private float method702(float var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      float var9 = var1;
      int var10 = var5 ? -1 : (var4 ? 1 : 0);
      if (var3 && var10 == 0) {
         var9 = var1 + 180.0F;
      }

      if (var4 || var5) {
         float var11 = var2 ? 45.0F : (var3 ? 135.0F : 90.0F);
         var9 += (float)var10 * var11;
      }

      return var9;
   }

   private static boolean lambda$pathFind$0(Entity var0) {
      return !(var0 instanceof BoatEntity) && !(var0 instanceof EndCrystalEntity);
   }
}
