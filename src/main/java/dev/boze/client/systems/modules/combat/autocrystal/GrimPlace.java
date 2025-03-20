package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.ac.Grim;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.IMinecraft;
import mapped.Class3062;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;

import java.util.HashSet;
import java.util.Set;

class GrimPlace implements IPlace, IMinecraft {
   @Override
   public BlockHitResult method113(BlockPos candidatePos) {
      Box var4 = new Box(candidatePos);
      ClientPlayerEntityAccessor var5 = (ClientPlayerEntityAccessor)mc.player;
      Vec3d var6 = new Vec3d(var5.getLastX(), var5.getLastY(), var5.getLastZ());
      Vec3d var7 = this.method134(var5.getLastPitch(), var5.getLastYaw());
      Pair var8 = this.method124(var4, var6, var7);
      return var8 != null ? new BlockHitResult((Vec3d)var8.getLeft(), (Direction)var8.getRight(), candidatePos, var4.contains(var6)) : null;
   }

   @Override
   public BlockHitResult method114(BlockPos candidatePos) {
      Set var5 = this.method121(candidatePos);
      if (!var5.isEmpty()) {
         BlockHitResult var6 = this.method122(candidatePos, var5);
         if (var6 != null) {
            return var6;
         }
      }

      boolean var12 = candidatePos.getY() == mc.world.getTopY() - 1;
      Box var7 = new Box(candidatePos);
      Vec3d var8 = mc.player.getPos();
      Vec3d var9 = var8.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
      Vec3d var10 = this.method123(var7, var9);
      if (var9.distanceTo(var10) > (double)AutoCrystal.INSTANCE.autoCrystalPlace.field138.getValue().floatValue()) {
         return null;
      } else {
         Direction var11 = Direction.getFacing(var9.x - var10.x, var9.y - var10.y, var9.z - var10.z);
         if (var12 && var11 == Direction.UP) {
            var11 = Direction.DOWN;
         }

         return new BlockHitResult(var10, var11, candidatePos, var7.contains(var9));
      }
   }

   Set<Direction> method121(BlockPos var1) {
      HashSet var5 = new HashSet();

      for (Direction var9 : Direction.values()) {
         if (Grim.field1831.method568(var1, var9)) {
            var5.add(var9);
         }
      }

      return var5;
   }

   BlockHitResult method122(BlockPos var1, Set<Direction> var2) {
      Vec3d var6 = new Vec3d((double)var1.getX() + 0.5, (double)var1.getY() + 1.0, (double)var1.getZ() + 0.5);
      Box var7 = EntityType.END_CRYSTAL.getDimensions().getBoxAt(var6);
      BlockHitResult var8 = null;
      double var9 = Double.MAX_VALUE;
      Box var11 = new Box(var1);
      Vec3d var12 = mc.player.getPos();
      Vec3d var13 = var12.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);

      for (Direction var15 : var2) {
         Vec3d[] var16 = BlockSelection.method1451(var1, var15);

         for (Vec3d var20 : var16) {
            if (!(var13.distanceTo(var20) > (double)AutoCrystal.INSTANCE.autoCrystalPlace.field138.getValue().floatValue())) {
               Pair var21 = this.method127(var7, var12, var20);
               if (var21 != null && var21.getLeft() != null && var21.getRight() != null) {
                  double var22 = var13.distanceTo((Vec3d)var21.getLeft());
                  if (var22 < var9 && var22 <= 3.0) {
                     var8 = new BlockHitResult(var20, var15, var1, var11.contains(var13));
                     var9 = var22;
                  }
               }
            }
         }
      }

      return var8;
   }

   @Override
   public Vec3d method115(Vec3d endCrystalPos) {
      Box var5 = EntityType.END_CRYSTAL.getDimensions().getBoxAt(endCrystalPos);
      Vec3d var6 = mc.player.getPos();
      Vec3d var7 = var6.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
      if (var7.distanceTo(endCrystalPos) <= 3.0) {
         return endCrystalPos;
      } else {
         Vec3d var8 = this.method123(var5, var6.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0));
         return var7.distanceTo(var8) <= 3.0 ? var8 : null;
      }
   }

   @Override
   public boolean method116(Vec3d endCrystalPos) {
      ClientPlayerEntityAccessor var5 = (ClientPlayerEntityAccessor)mc.player;
      Vec3d var6 = new Vec3d(var5.getLastX(), var5.getLastY(), var5.getLastZ());
      Vec3d var7 = this.method134(var5.getLastPitch(), var5.getLastYaw());
      Box var8 = EntityType.END_CRYSTAL.getDimensions().getBoxAt(endCrystalPos);
      double var9 = this.method125(var8, var6, var7);
      return !(var9 > 3.0);
   }

   @Override
   public boolean method118(Vec3d endCrystalPos, float[] rotation) {
      Vec3d var5 = mc.player.getPos();
      return this.method119(endCrystalPos, var5, rotation);
   }

   @Override
   public boolean method119(Vec3d endCrystalPos, Vec3d playerPos, float[] rotation) {
      Vec3d var7 = this.method134(rotation[1], rotation[0]);
      Box var8 = EntityType.END_CRYSTAL.getDimensions().getBoxAt(endCrystalPos);
      double var9 = this.method125(var8, playerPos, var7);
      return !(var9 > 3.0);
   }

   @Override
   public boolean method117(Vec3d endCrystalPos, Vec3d playerPos) {
      Box var6 = EntityType.END_CRYSTAL.getDimensions().getBoxAt(endCrystalPos);
      Vec3d var7 = playerPos.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
      Vec3d var8 = this.method123(var6, var7);
      return var7.distanceTo(var8) <= 3.0;
   }

   private Vec3d method123(Box var1, Vec3d var2) {
      return Class3062.method5991(var2, var1);
   }

   private Pair<Vec3d, Direction> method124(Box var1, Vec3d var2, Vec3d var3) {
      Pair var7 = null;
      double var8 = Double.MAX_VALUE;
      Vec3d var10 = var2.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
      Vec3d var11 = var10.add(var3.getX() * 6.0, var3.getY() * 6.0, var3.getZ() * 6.0);
      Pair var12 = this.method127(var1, var10, var11);
      if (var12.getLeft() != null) {
         if (this.method126(var1, var10)) {
            var8 = 0.0;
            var7 = var12;
         }

         double var13 = var10.distanceTo((Vec3d)var12.getLeft());
         if (var13 < var8) {
            var7 = var12;
            var8 = var13;
         }
      }

      return var8 > (double)AutoCrystal.INSTANCE.autoCrystalPlace.field138.getValue().floatValue() ? null : var7;
   }

   private double method125(Box var1, Vec3d var2, Vec3d var3) {
      Vec3d var7 = var2.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
      Vec3d var8 = var7.add(var3.getX() * 6.0, var3.getY() * 6.0, var3.getZ() * 6.0);
      Vec3d var9 = (Vec3d)this.method127(var1, var7, var8).getLeft();
      if (this.method126(var1, var7)) {
         return 0.0;
      } else {
         return var9 != null ? var7.distanceTo(var9) : Double.MAX_VALUE;
      }
   }

   private boolean method126(Box var1, Vec3d var2) {
      return var2.getX() > var1.minX
         && var2.getX() < var1.maxX
         && var2.getY() > var1.minY
         && var2.getY() < var1.maxY
         && var2.getZ() > var1.minZ
         && var2.getZ() < var1.maxZ;
   }

   private Pair<Vec3d, Direction> method127(Box var1, Vec3d var2, Vec3d var3) {
      Vec3d var7 = this.method128(var2, var3, var1.minX);
      Vec3d var8 = this.method128(var2, var3, var1.maxX);
      Vec3d var9 = this.method129(var2, var3, var1.minY);
      Vec3d var10 = this.method129(var2, var3, var1.maxY);
      Vec3d var11 = this.method130(var2, var3, var1.minZ);
      Vec3d var12 = this.method130(var2, var3, var1.maxZ);
      Direction var13 = null;
      if (AutoCrystal.INSTANCE.field1041.field210.getValue()) {
         if (!this.method131(var1, var7)) {
            var7 = null;
         }

         if (!this.method131(var1, var8)) {
            var8 = null;
         }

         if (!this.method132(var1, var9)) {
            var9 = null;
         }

         if (!this.method132(var1, var10)) {
            var10 = null;
         }

         if (!this.method133(var1, var11)) {
            var11 = null;
         }

         if (!this.method133(var1, var12)) {
            var12 = null;
         }
      }

      Vec3d var14 = null;
      if (var7 != null) {
         var14 = var7;
         var13 = Direction.WEST;
      }

      if (var8 != null && (var14 == null || var2.distanceTo(var8) < var2.distanceTo(var14))) {
         var14 = var8;
         var13 = Direction.EAST;
      }

      if (var9 != null && (var14 == null || var2.distanceTo(var9) < var2.distanceTo(var14))) {
         var14 = var9;
         var13 = Direction.DOWN;
      }

      if (var10 != null && (var14 == null || var2.distanceTo(var10) < var2.distanceTo(var14))) {
         var14 = var10;
         var13 = Direction.UP;
      }

      if (var11 != null && (var14 == null || var2.distanceTo(var11) < var2.distanceTo(var14))) {
         var14 = var11;
         var13 = Direction.NORTH;
      }

      if (var12 != null && (var14 == null || var2.distanceTo(var12) < var2.distanceTo(var14))) {
         var14 = var12;
         var13 = Direction.SOUTH;
      }

      return new Pair(var14, var13);
   }

   private Vec3d method128(Vec3d var1, Vec3d var2, double var3) {
      double var8 = var2.getX() - var1.getX();
      double var10 = var2.getY() - var1.getY();
      double var12 = var2.getZ() - var1.getZ();
      if (var8 * var8 < 1.0E-7F) {
         return null;
      } else {
         double var14 = (var3 - var1.getX()) / var8;
         return var14 >= 0.0 && var14 <= 1.0 ? new Vec3d(var1.getX() + var8 * var14, var1.getY() + var10 * var14, var1.getZ() + var12 * var14) : null;
      }
   }

   private Vec3d method129(Vec3d var1, Vec3d var2, double var3) {
      double var8 = var2.getX() - var1.getX();
      double var10 = var2.getY() - var1.getY();
      double var12 = var2.getZ() - var1.getZ();
      if (var10 * var10 < 1.0E-7F) {
         return null;
      } else {
         double var14 = (var3 - var1.getY()) / var10;
         return var14 >= 0.0 && var14 <= 1.0 ? new Vec3d(var1.getX() + var8 * var14, var1.getY() + var10 * var14, var1.getZ() + var12 * var14) : null;
      }
   }

   private Vec3d method130(Vec3d var1, Vec3d var2, double var3) {
      double var8 = var2.getX() - var1.getX();
      double var10 = var2.getY() - var1.getY();
      double var12 = var2.getZ() - var1.getZ();
      if (var12 * var12 < 1.0E-7F) {
         return null;
      } else {
         double var14 = (var3 - var1.getZ()) / var12;
         return var14 >= 0.0 && var14 <= 1.0 ? new Vec3d(var1.getX() + var8 * var14, var1.getY() + var10 * var14, var1.getZ() + var12 * var14) : null;
      }
   }

   private boolean method131(Box var1, Vec3d var2) {
      return var2 != null && var2.getY() >= var1.minY && var2.getY() <= var1.maxY && var2.getZ() >= var1.minZ && var2.getZ() <= var1.maxZ;
   }

   private boolean method132(Box var1, Vec3d var2) {
      return var2 != null && var2.getX() >= var1.minX && var2.getX() <= var1.maxX && var2.getZ() >= var1.minZ && var2.getZ() <= var1.maxZ;
   }

   private boolean method133(Box var1, Vec3d var2) {
      return var2 != null && var2.getX() >= var1.minX && var2.getX() <= var1.maxX && var2.getY() >= var1.minY && var2.getY() <= var1.maxY;
   }

   private Vec3d method134(float var1, float var2) {
      float var3 = var1 * (float) (Math.PI / 180.0);
      float var4 = -var2 * (float) (Math.PI / 180.0);
      float var5 = MathHelper.cos(var4);
      float var6 = MathHelper.sin(var4);
      float var7 = MathHelper.cos(var3);
      float var8 = MathHelper.sin(var3);
      return new Vec3d((double)(var6 * var7), (double)(-var8), (double)(var5 * var7));
   }
}
