package dev.boze.client.systems.modules.misc.AutoTool;

import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.systems.modules.misc.AutoTool;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.InventoryHelper;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class qG implements IMinecraft {
   public static boolean field1630;
   public static boolean field1631;

   @EventHandler(
      priority = 200
   )
   private static void method2072(PreTickEvent var0) {
      field1631 = false;
   }

   @EventHandler(
      priority = -200
   )
   private static void method1810(PostTickEvent var0) {
      if (!field1631 && field1630) {
         field1630 = false;
      }
   }

   public static Vec3d method735(BlockPos blockPos, double range, boolean throughWalls) {
      if (!method736(blockPos, mc.world.getBlockState(blockPos))) {
         return null;
      } else {
         BlockPos var7 = blockPos instanceof Mutable ? new BlockPos(blockPos) : blockPos;
         Direction var8 = method737(blockPos, throughWalls, range);
         if (var8 == null) {
            return null;
         } else {
            int var9 = InventoryHelper.method174(qG::lambda$breakBlock$0, SwapMode.Normal);
            int var10 = -1;
            if (var9 != -1) {
               var10 = mc.player.getInventory().selectedSlot;
               mc.player.getInventory().selectedSlot = var9;
               ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
            }

            field1631 = true;
            if (mc.interactionManager.isBreakingBlock()) {
               mc.interactionManager.updateBlockBreakingProgress(var7, var8);
            } else {
               mc.interactionManager.attackBlock(var7, var8);
            }

            mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            if (var10 != -1) {
               mc.player.getInventory().selectedSlot = var10;
            }

            field1630 = true;
            return new Vec3d((double)var7.getX() + 0.5, (double)var7.getY() + 0.5, (double)var7.getZ() + 0.5)
               .add(new Vec3d(var8.getUnitVector()).multiply(0.5));
         }
      }
   }

   public static boolean method736(BlockPos blockPos, BlockState state) {
      return !mc.player.isCreative() && state.getHardness(mc.world, blockPos) < 0.0F ? false : state.getOutlineShape(mc.world, blockPos) != VoxelShapes.empty();
   }

   public static boolean method2101(BlockPos blockPos) {
      return method736(blockPos, mc.world.getBlockState(blockPos));
   }

   public static Direction method737(BlockPos pos, boolean throughWalls, double range) {
      Vec3d var7 = EntityUtil.method2144(mc.player);
      Direction var8 = null;
      double var9 = 999.0;

      for (Direction var14 : Direction.values()) {
         Vec3d var15 = new Vec3d(
            (double)pos.getX() + 0.5 + (double)var14.getVector().getX() * 0.5,
            (double)pos.getY() + 0.5 + (double)var14.getVector().getY() * 0.5,
            (double)pos.getZ() + 0.5 + (double)var14.getVector().getZ() * 0.5
         );
         double var16 = var7.distanceTo(var15);
         if (!(var16 > range) && (var8 == null || !(var16 > var9))) {
            if (throughWalls) {
               var8 = var14;
               var9 = var16;
            } else {
               BlockHitResult var18 = mc.world.raycast(new RaycastContext(var7, var15, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
               if (var18 != null && var18.getType() == Type.BLOCK && var18.getBlockPos().equals(pos)) {
                  var8 = var14;
                  var9 = var16;
               }
            }
         }
      }

      return var8;
   }

   public static boolean method738(BlockPos blockPos, BlockState state) {
      return mc.player.isCreative() || state.calcBlockBreakingDelta(mc.player, mc.world, blockPos) >= 1.0F;
   }

   public static boolean method2102(BlockPos blockPos) {
      return method738(blockPos, mc.world.getBlockState(blockPos));
   }

   private static Float lambda$breakBlock$0(BlockPos var0, ItemStack var1) {
      return AutoTool.method1683(var1, mc.world.getBlockState(var0));
   }
}
