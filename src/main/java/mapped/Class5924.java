package mapped;

import dev.boze.client.enums.AutoWalkMode;
import dev.boze.client.mixin.BlockHitResultAccessor;
import dev.boze.client.mixin.MinecraftClientAccessor;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.systems.modules.movement.AutoWalk;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RotationHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class Class5924 implements IMinecraft {
   private static final double field42 = 1.0 / Math.sqrt(2.0);

   public static void method72(Entity var0, Vec3d var1) {
      HitResult var5 = mc.crosshairTarget;
      if (var0 != null) {
         mc.crosshairTarget = var1 == null ? new EntityHitResult(var0) : new EntityHitResult(var0, var1);
      } else {
         mc.crosshairTarget = Class3089.field215;
      }

      ((MinecraftClientAccessor)mc).callDoAttack();
      mc.crosshairTarget = var5;
   }

   public static HitResult method73(double var0, RotationHelper var2, boolean var3) {
      Entity var7 = mc.getCameraEntity();
      if (var7 == null) {
         return null;
      } else if (mc.world == null) {
         return null;
      } else {
         double var8 = Reach.method1614();
         Object var10 = var3 ? null : var7.raycast(var0, 1.0F, false);
         Vec3d var11 = var7.getCameraPosVec(1.0F);
         boolean var12 = false;
         if (var8 > 3.0) {
            var12 = true;
         }

         double var14 = var8 * var8;
         if (var10 != null) {
            var14 = var10.getPos().squaredDistanceTo(var11);
         }

         Vec3d var16 = var2.method1954();
         Vec3d var17 = var11.add(var16.x * var8, var16.y * var8, var16.z * var8);
         Box var19 = var7.getBoundingBox().stretch(var16.multiply(var8)).expand(1.0, 1.0, 1.0);
         EntityHitResult var20 = ProjectileUtil.raycast(var7, var11, var17, var19, Class5924::lambda$getTargetedEntity$0, var14);
         if (var20 != null) {
            Vec3d var21 = var20.getPos();
            double var22;
            if (Reach.INSTANCE.isEnabled()) {
               var22 = Math.pow(var11.distanceTo(var21) / Reach.method1613() * 3.0, 2.0);
            } else {
               var22 = var11.squaredDistanceTo(var21);
            }

            if (var12 && var22 > 9.0) {
               var10 = BlockHitResult.createMissed(var21, Direction.getFacing(var16.x, var16.y, var16.z), BlockPos.ofFloored(var21));
            } else if (var22 < var14 || mc.crosshairTarget == null) {
               var10 = var20;
            }
         }

         return (HitResult)var10;
      }
   }

   public static Hand method74() {
      for (Hand var6 : Hand.values()) {
         ItemStack var7 = mc.player.getStackInHand(var6);
         if (!var7.isEmpty() && var7.getUseAction() != UseAction.NONE) {
            return var6;
         }
      }

      return null;
   }

   public static void method2142() {
      double var0 = (double)MathHelper.floor(mc.player.getX()) + 0.5;
      double var2 = (double)MathHelper.floor(mc.player.getZ()) + 0.5;
      mc.player.updatePosition(var0, mc.player.getY(), var2);
      ((IClientPlayerEntity)mc.player).sendMovementPackets(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.isOnGround());
   }

   public static boolean method76(boolean var0) {
      return method77(var0, mc.player);
   }

   public static boolean method77(boolean var0, Entity var1) {
      if (MinecraftUtils.isClientActive() && var1 != null) {
         BlockPos var5 = BlockPos.ofFloored(var1.getX(), var1.getY(), var1.getZ());
         int var6 = 0;

         for (Direction var10 : Direction.values()) {
            if (var10 != Direction.UP) {
               BlockState var11 = mc.world.getBlockState(var5.offset(var10));
               if (var11.getBlock().getBlastResistance() < 600.0F) {
                  if (!var0 || var10 == Direction.DOWN) {
                     return false;
                  }

                  var6++;

                  for (Direction var15 : Direction.values()) {
                     if (var15 != var10.getOpposite() && var15 != Direction.UP) {
                        BlockState var16 = mc.world.getBlockState(var5.offset(var10).offset(var15));
                        if (var16.getBlock().getBlastResistance() < 600.0F) {
                           return false;
                        }
                     }
                  }
               }
            }
         }

         return var6 < 2;
      } else {
         return false;
      }
   }

   public static boolean method2055(Entity var0) {
      if (MinecraftUtils.isClientActive() && var0 != null) {
         BlockPos var4 = BlockPos.ofFloored(var0.getX(), var0.getY(), var0.getZ());
         BlockState var5 = mc.world.getBlockState(var4);
         return var5.getBlock() == Blocks.OBSIDIAN || var5.getBlock() == Blocks.ENDER_CHEST || var5.getBlock() == Blocks.BEDROCK;
      } else {
         return false;
      }
   }

   public static List<BlockPos> method348(Vec3d var0) {
      BlockPos var4 = BlockPos.ofFloored(var0);
      ArrayList var5 = new ArrayList();
      double var6 = Math.abs(var0.getX()) - Math.floor(Math.abs(var0.getX()));
      double var8 = Math.abs(var0.getZ()) - Math.floor(Math.abs(var0.getZ()));
      int var10 = method80(var6, false);
      int var11 = method80(var6, true);
      int var12 = method80(var8, false);
      int var13 = method80(var8, true);

      for (int var14 = 1; var14 < var10 + 1; var14++) {
         var5.add(method81(var4, (double)var14, 0.0, (double)(1 + var12)));
         var5.add(method81(var4, (double)var14, 0.0, (double)(-(1 + var13))));
      }

      for (int var15 = 0; var15 <= var11; var15++) {
         var5.add(method81(var4, (double)(-var15), 0.0, (double)(1 + var12)));
         var5.add(method81(var4, (double)(-var15), 0.0, (double)(-(1 + var13))));
      }

      for (int var16 = 1; var16 < var12 + 1; var16++) {
         var5.add(method81(var4, (double)(1 + var10), 0.0, (double)var16));
         var5.add(method81(var4, (double)(-(1 + var11)), 0.0, (double)var16));
      }

      for (int var17 = 0; var17 <= var13; var17++) {
         var5.add(method81(var4, (double)(1 + var10), 0.0, (double)(-var17)));
         var5.add(method81(var4, (double)(-(1 + var11)), 0.0, (double)(-var17)));
      }

      return var5;
   }

   public static int method80(double var0, boolean var2) {
      if (var2) {
         return var0 <= 0.3 ? 1 : 0;
      } else {
         return var0 >= 0.7 ? 1 : 0;
      }
   }

   public static BlockPos method81(BlockPos var0, double var1, double var3, double var5) {
      if (var0.getX() < 0) {
         var1 = -var1;
      }

      if (var0.getY() < 0) {
         var3 = -var3;
      }

      if (var0.getZ() < 0) {
         var5 = -var5;
      }

      return var0.add(BlockPos.ofFloored(var1, var3, var5));
   }

   public static boolean method2101(BlockPos var0) {
      return method2088(mc.world.getBlockState(var0).getBlock());
   }

   public static boolean method2088(Block var0) {
      return var0 instanceof EnderChestBlock
         || var0 instanceof RespawnAnchorBlock
         || var0 instanceof BedBlock
         || var0 instanceof AnvilBlock
         || var0 instanceof ButtonBlock
         || var0 instanceof AbstractPressurePlateBlock
         || var0 instanceof BlockWithEntity
         || var0 instanceof CraftingTableBlock
         || var0 instanceof DoorBlock
         || var0 instanceof FenceGateBlock
         || var0 instanceof NoteBlock
         || var0 instanceof TrapdoorBlock;
   }

   public static boolean method1934(Entity var0) {
      return var0 == null ? true : var0.getPos().equals(new Vec3d(var0.prevX, var0.prevY, var0.prevZ));
   }

   public static boolean method2114() {
      return mc.player.getPos().equals(new Vec3d(mc.player.prevX, mc.player.prevY, mc.player.prevZ));
   }

   public static boolean method2115() {
      return !method2114();
   }

   public static boolean method87(Block var0) {
      if (!MinecraftUtils.isClientActive()) {
         return false;
      } else {
         Box var4 = mc.player.getBoundingBox();
         if (var4 == null) {
            return false;
         } else {
            BlockPos var5 = BlockPos.ofFloored(var4.minX + 0.001, var4.minY + 0.001, var4.minZ + 0.001);
            BlockPos var6 = BlockPos.ofFloored(var4.maxX - 0.001, var4.maxY - 0.001, var4.maxZ - 0.001);
            if (mc.world.isRegionLoaded(var5, var6)) {
               Mutable var7 = new Mutable();

               for (int var8 = var5.getX(); var8 <= var6.getX(); var8++) {
                  for (int var9 = var5.getY(); var9 <= var6.getY(); var9++) {
                     for (int var10 = var5.getZ(); var10 <= var6.getZ(); var10++) {
                        var7.set(var8, var9, var10);
                        BlockState var11 = mc.world.getBlockState(var7);
                        if (var11.getBlock() == var0) {
                           return true;
                        }
                     }
                  }
               }
            }

            return false;
         }
      }
   }

   public static boolean method2116() {
      return mc.player.input.getMovementInput().lengthSquared() != 0.0F;
   }

   public static double method2091() {
      double var3 = 0.2873;
      if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
         int var5 = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
         var3 *= 1.0 + 0.2 * (double)(var5 + 1);
      }

      if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
         int var6 = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
         var3 /= 1.0 + 0.2 * (double)(var6 + 1);
      }

      return var3;
   }

   public static boolean method117(Vec3d var0, Vec3d var1) {
      BlockHitResult var5 = method98(var0, var1);
      return var5 == null || !(var5 instanceof BlockHitResult) || ((BlockHitResultAccessor)var5).isMissed();
   }

   public static boolean method91(Class<? extends Block> var0) {
      return method92(var0, (int)Math.floor(mc.player.getBoundingBox(mc.player.getPose()).minY));
   }

   public static boolean method92(Class<? extends Block> var0, int var1) {
      for (int var5 = MathHelper.floor(mc.player.getBoundingBox(mc.player.getPose()).minX);
         var5 < MathHelper.ceil(mc.player.getBoundingBox(mc.player.getPose()).maxX);
         var5++
      ) {
         for (int var6 = MathHelper.floor(mc.player.getBoundingBox(mc.player.getPose()).minZ);
            var6 < MathHelper.ceil(mc.player.getBoundingBox(mc.player.getPose()).maxZ);
            var6++
         ) {
            BlockState var7 = mc.world.getBlockState(new BlockPos(var5, var1, var6));
            if (var7 != null && var0.isInstance(var7.getBlock())) {
               return true;
            }
         }
      }

      return false;
   }

   public static Vec3d method93(double var0) {
      return method94(var0, false);
   }

   public static Vec3d method94(double var0, boolean var2) {
      double var10 = (double)mc.player.input.movementForward;
      if (var10 <= 0.0 && var2) {
         var10 = 1.0;
      }

      if (AutoWalk.INSTANCE.isEnabled() && AutoWalk.INSTANCE.field3148.method461() != AutoWalkMode.Baritone) {
         var10 = AutoWalk.INSTANCE.field3147.method419() ? -1.0 : 1.0;
      }

      double var12 = (double)mc.player.input.movementSideways;
      float var14 = mc.player.getYaw();
      double var6;
      double var8;
      if (var10 == 0.0 && var12 == 0.0) {
         var6 = 0.0;
         var8 = 0.0;
      } else {
         if (var10 != 0.0) {
            if (var12 > 0.0) {
               var14 += (float)(var10 > 0.0 ? -45 : 45);
            } else if (var12 < 0.0) {
               var14 += (float)(var10 > 0.0 ? 45 : -45);
            }

            var12 = 0.0;
            if (var10 > 0.0) {
               var10 = 1.0;
            } else if (var10 < 0.0) {
               var10 = -1.0;
            }
         }

         var6 = var10 * var0 * Math.cos(Math.toRadians((double)(var14 + 90.0F))) + var12 * var0 * Math.sin(Math.toRadians((double)(var14 + 90.0F)));
         var8 = var10 * var0 * Math.sin(Math.toRadians((double)(var14 + 90.0F))) - var12 * var0 * Math.cos(Math.toRadians((double)(var14 + 90.0F)));
      }

      return new Vec3d(var6, 0.0, var8);
   }

   public static Vec3d method95(double var0) {
      float var5 = mc.player.getYaw();
      Vec3d var6 = Vec3d.fromPolar(0.0F, var5);
      Vec3d var7 = Vec3d.fromPolar(0.0F, var5 + 90.0F);
      double var8 = 0.0;
      double var10 = 0.0;
      boolean var12 = false;
      if (mc.player.input.pressingForward) {
         var8 += var6.x / 20.0 * var0;
         var10 += var6.z / 20.0 * var0;
         var12 = true;
      }

      if (mc.player.input.pressingBack) {
         var8 -= var6.x / 20.0 * var0;
         var10 -= var6.z / 20.0 * var0;
         var12 = true;
      }

      boolean var13 = false;
      if (mc.player.input.pressingRight) {
         var8 += var7.x / 20.0 * var0;
         var10 += var7.z / 20.0 * var0;
         var13 = true;
      }

      if (mc.player.input.pressingLeft) {
         var8 -= var7.x / 20.0 * var0;
         var10 -= var7.z / 20.0 * var0;
         var13 = true;
      }

      if (var12 && var13) {
         var8 *= field42;
         var10 *= field42;
      }

      return new Vec3d(var8, 0.0, var10);
   }

   public static double method1614() {
      return method97(mc.player.input.getMovementInput()) + (method2116() ? 0.0 : 90.0) + (double)mc.player.getYaw();
   }

   public static double method97(Vec2f var0) {
      return Math.toDegrees(Math.atan2((double)var0.y, (double)var0.x)) - 90.0;
   }

   public static BlockHitResult method98(Vec3d var0, Vec3d var1) {
      return mc.world.raycast(new RaycastContext(var0, var1, ShapeType.OUTLINE, FluidHandling.NONE, mc.player));
   }

   private static boolean lambda$getTargetedEntity$0(Entity var0) {
      return !var0.isSpectator() && var0.canHit();
   }
}
