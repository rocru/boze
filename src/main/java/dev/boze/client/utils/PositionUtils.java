package dev.boze.client.utils;

import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public class PositionUtils implements IMinecraft {
   public static void method146(BlockHitResult hitResult) {
      method393(hitResult, false);
   }

   public static void method393(BlockHitResult hitResult, boolean toVec) {
      BlockPos var5 = hitResult.getBlockPos();
      Direction var6 = hitResult.getSide();
      if (mc.world.getBlockState(var5).onUse(mc.world, mc.player, hitResult) == ActionResult.PASS) {
         BlockState var7 = mc.world.getBlockState(var5);
         VoxelShape var8 = var7.getCollisionShape(mc.world, var5);
         if (var8.isEmpty()) {
            var8 = var7.getOutlineShape(mc.world, var5);
         }

         double var9 = var8.isEmpty() ? 1.0 : var8.getMax(Axis.Y);
         Vec3d var11 = toVec
            ? hitResult.getPos()
            : new Vec3d(
               (double)var5.getX() + 0.5 + (double)var6.getOffsetX(), (double)var5.getY() + var9, (double)var5.getZ() + 0.5 + (double)var6.getOffsetZ()
            );
         int var12 = (int)Math.ceil(mc.player.getPos().distanceTo(var11) / 10.0) - 1;

         for (int var13 = 0; var13 < var12; var13++) {
            mc.player.networkHandler.sendPacket(new OnGroundOnly(true));
         }

         mc.player.networkHandler.sendPacket(new PositionAndOnGround(var11.x, var11.y, var11.z, true));
         mc.player.setPosition(var11);
      }
   }

   public static void method494(Vec3d vec) {
      int var4 = (int)Math.ceil(mc.player.getPos().distanceTo(vec) / 10.0) - 1;

      for (int var5 = 0; var5 < var4; var5++) {
         mc.player.networkHandler.sendPacket(new OnGroundOnly(true));
      }

      mc.player.networkHandler.sendPacket(new PositionAndOnGround(vec.x, vec.y, vec.z, true));
      mc.player.setPosition(vec);
   }
}
