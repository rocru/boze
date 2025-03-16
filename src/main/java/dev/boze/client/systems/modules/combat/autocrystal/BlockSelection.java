package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.jumptable.A;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BlockSelection {
   public static Vec3d[] method1451(BlockPos pos, Direction direction) {
      return method1452(pos, direction, 0.0);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static Vec3d[] method1452(BlockPos pos, Direction direction, double offset) {
      Vec3d[] var7 = new Vec3d[5];
      double var8 = (double)pos.getX() + offset;
      double var10 = (double)pos.getY() + offset;
      double var12 = (double)pos.getZ() + offset;
      double var14 = var8 + 1.0 - offset;
      double var16 = var10 + 1.0 - offset;
      double var18 = var12 + 1.0 - offset;
      switch (A.field2093[direction.ordinal()]) {
         case 1:
            var7[0] = new Vec3d(var8, var10, var12);
            var7[1] = new Vec3d(var14, var10, var12);
            var7[2] = new Vec3d(var8, var16, var12);
            var7[3] = new Vec3d(var14, var16, var12);
            var7[4] = new Vec3d((var8 + var14) / 2.0, (var10 + var16) / 2.0, var12);
            break;
         case 2:
            var7[0] = new Vec3d(var8, var10, var18);
            var7[1] = new Vec3d(var14, var10, var18);
            var7[2] = new Vec3d(var8, var16, var18);
            var7[3] = new Vec3d(var14, var16, var18);
            var7[4] = new Vec3d((var8 + var14) / 2.0, (var10 + var16) / 2.0, var18);
            break;
         case 3:
            var7[0] = new Vec3d(var14, var10, var12);
            var7[1] = new Vec3d(var14, var10, var18);
            var7[2] = new Vec3d(var14, var16, var12);
            var7[3] = new Vec3d(var14, var16, var18);
            var7[4] = new Vec3d(var14, (var10 + var16) / 2.0, (var12 + var18) / 2.0);
            break;
         case 4:
            var7[0] = new Vec3d(var8, var10, var12);
            var7[1] = new Vec3d(var8, var10, var18);
            var7[2] = new Vec3d(var8, var16, var12);
            var7[3] = new Vec3d(var8, var16, var18);
            var7[4] = new Vec3d(var8, (var10 + var16) / 2.0, (var12 + var18) / 2.0);
            break;
         case 5:
            var7[0] = new Vec3d(var8, var16, var12);
            var7[1] = new Vec3d(var14, var16, var12);
            var7[2] = new Vec3d(var8, var16, var18);
            var7[3] = new Vec3d(var14, var16, var18);
            var7[4] = new Vec3d((var8 + var14) / 2.0, var16, (var12 + var18) / 2.0);
            break;
         case 6:
            var7[0] = new Vec3d(var8, var10, var12);
            var7[1] = new Vec3d(var14, var10, var12);
            var7[2] = new Vec3d(var8, var10, var18);
            var7[3] = new Vec3d(var14, var10, var18);
            var7[4] = new Vec3d((var8 + var14) / 2.0, var10, (var12 + var18) / 2.0);
      }

      return var7;
   }
}
