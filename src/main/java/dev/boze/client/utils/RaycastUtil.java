package dev.boze.client.utils;

import dev.boze.client.mixininterfaces.IRaycastContext;
import dev.boze.client.mixininterfaces.IVec3d;
import dev.boze.client.utils.world.PositionUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class RaycastUtil implements IMinecraft {
   public static ShapeType field1324 = ShapeType.COLLIDER;
   public static BlockPos field1325 = null;
   public static BlockPos field1326 = null;

   public static boolean method2055(Entity var0) {
      Vec3d var1 = PositionUtil.getPosition(var0);
      return method116(var1);
   }

   public static boolean method116(Vec3d var0) {
      Vec3d var1 = PositionUtil.getPlayerPosition();
      return method117(var1, var0);
   }

   public static boolean method117(Vec3d var0, Vec3d var1) {
      BlockHitResult var5 = mc.world.raycast(new RaycastContext(var0, var1, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
      return var5.getType() == Type.MISS ? true : var5.getBlockPos().equals(BlockPos.ofFloored(mc.player.getEyePos()));
   }

   public static BlockHitResult method574(double var0, RotationHelper var2) {
      return method575(var0, var2, false);
   }

   public static BlockHitResult method575(double var0, RotationHelper var2, boolean var3) {
      Vec3d var7 = mc.player.getCameraPosVec(1.0F);
      Vec3d var8 = var2.method1954().multiply(var0);
      Vec3d var9 = var7.add(var8);
      BlockHitResult var10 = mc.world.raycast(new RaycastContext(var7, var9, field1324, FluidHandling.NONE, mc.player));
      if (var3 && var10.getType() != Type.MISS) {
         Box var11 = mc.player.getBoundingBox().stretch(var8).expand(1.0, 1.0, 1.0);
         EntityHitResult var12 = ProjectileUtil.raycast(mc.player, var7, var9, var11, RaycastUtil::lambda$rayCast$0, var0);
         return var12 == null ? var10 : BlockHitResult.createMissed(var10.getPos(), var10.getSide(), var10.getBlockPos());
      } else {
         return var10;
      }
   }

   public static double method576(Vec3d var0, Entity var1, Vec3d var2, Box var3, RaycastContext var4, BlockPos var5, boolean var6) {
      double var10 = 1.0 / ((var3.maxX - var3.minX) * 2.0 + 1.0);
      double var12 = 1.0 / ((var3.maxY - var3.minY) * 2.0 + 1.0);
      double var14 = 1.0 / ((var3.maxZ - var3.minZ) * 2.0 + 1.0);
      double var16 = (1.0 - Math.floor(1.0 / var10) * var10) / 2.0;
      double var18 = (1.0 - Math.floor(1.0 / var14) * var14) / 2.0;
      if (!(var10 < 0.0) && !(var12 < 0.0) && !(var14 < 0.0)) {
         var2 = new Vec3d(var2.x, var2.y, var2.z);
         int var20 = 0;
         int var21 = 0;

         for (double var22 = 0.0; var22 <= 1.0; var22 += var10) {
            for (double var24 = 0.0; var24 <= 1.0; var24 += var12) {
               for (double var26 = 0.0; var26 <= 1.0; var26 += var14) {
                  double var28 = MathHelper.lerp(var22, var3.minX, var3.maxX);
                  double var30 = MathHelper.lerp(var24, var3.minY, var3.maxY);
                  double var32 = MathHelper.lerp(var26, var3.minZ, var3.maxZ);
                  ((IVec3d)var2).boze$set(var28 + var16, var30, var32 + var18);
                  ((IRaycastContext)var4).boze$set(var2, var0, ShapeType.COLLIDER, FluidHandling.NONE, var1);
                  if (method577(var4, var5, var6).getType() == Type.MISS) {
                     var20++;
                  }

                  var21++;
               }
            }
         }

         return (double)var20 / (double)var21;
      } else {
         return 0.0;
      }
   }

   private static BlockHitResult method577(RaycastContext var0, BlockPos var1, boolean var2) {
      return (BlockHitResult)BlockView.raycast(var0.getStart(), var0.getEnd(), var0, RaycastUtil::lambda$raycast$1, RaycastUtil::lambda$raycast$2);
   }

   private static BlockHitResult lambda$raycast$2(RaycastContext var0) {
      Vec3d var1 = var0.getStart().subtract(var0.getEnd());
      return BlockHitResult.createMissed(var0.getEnd(), Direction.getFacing(var1.x, var1.y, var1.z), BlockPos.ofFloored(var0.getEnd()));
   }

   private static BlockHitResult lambda$raycast$1(BlockPos var0, boolean var1, RaycastContext var2, BlockPos var3) {
      BlockState var7;
      if (var3.equals(var0)) {
         var7 = Blocks.OBSIDIAN.getDefaultState();
      } else {
         var7 = mc.world.getBlockState(var3);
         if (var7.getBlock().getBlastResistance() < 600.0F && var1) {
            var7 = Blocks.AIR.getDefaultState();
         } else if (var3.equals(field1325) || var3.equals(field1326)) {
            var7 = Blocks.AIR.getDefaultState();
         }
      }

      Vec3d var8 = var2.getStart();
      Vec3d var9 = var2.getEnd();
      VoxelShape var10 = var2.getBlockShape(var7, mc.world, var3);
      BlockHitResult var11 = mc.world.raycastBlock(var8, var9, var3, var10, var7);
      VoxelShape var12 = VoxelShapes.empty();
      BlockHitResult var13 = var12.raycast(var8, var9, var3);
      double var14 = var11 == null ? Double.MAX_VALUE : var2.getStart().squaredDistanceTo(var11.getPos());
      double var16 = var13 == null ? Double.MAX_VALUE : var2.getStart().squaredDistanceTo(var13.getPos());
      return var14 <= var16 ? var11 : var13;
   }

   private static boolean lambda$rayCast$0(Entity var0) {
      return !var0.isSpectator() && var0.canHit();
   }
}
