package dev.boze.client.utils;

import dev.boze.client.jumptable.ap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class BoundingBoxUtil implements IMinecraft {
   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static Box calculateScaledBoundingBox(BlockPos pos, Direction direction, double scale) {
      if (!(scale < 0.0) && !(scale > 1.0)) {
         double var7 = scale / 2.0;

         return switch (ap.field2095[direction.ordinal()]) {
            case 1 -> new Box(
            (double)pos.getX() + 0.5 - var7,
            (double)pos.getY() + 0.5 - var7,
            (double)pos.getZ(),
            (double)pos.getX() + 0.5 + var7,
            (double)pos.getY() + 0.5 + var7,
            (double)pos.getZ()
         );
            case 2 -> new Box(
            (double)pos.getX() + 0.5 - var7,
            (double)pos.getY() + 0.5 - var7,
            (double)(pos.getZ() + 1),
            (double)pos.getX() + 0.5 + var7,
            (double)pos.getY() + 0.5 + var7,
            (double)(pos.getZ() + 1)
         );
            case 3 -> new Box(
            (double)(pos.getX() + 1),
            (double)pos.getY() + 0.5 - var7,
            (double)pos.getZ() + 0.5 - var7,
            (double)(pos.getX() + 1),
            (double)pos.getY() + 0.5 + var7,
            (double)pos.getZ() + 0.5 + var7
         );
            case 4 -> new Box(
            (double)pos.getX(),
            (double)pos.getY() + 0.5 - var7,
            (double)pos.getZ() + 0.5 - var7,
            (double)pos.getX(),
            (double)pos.getY() + 0.5 + var7,
            (double)pos.getZ() + 0.5 + var7
         );
            case 5 -> new Box(
            (double)pos.getX() + 0.5 - var7,
            (double)(pos.getY() + 1),
            (double)pos.getZ() + 0.5 - var7,
            (double)pos.getX() + 0.5 + var7,
            (double)(pos.getY() + 1),
            (double)pos.getZ() + 0.5 + var7
         );
            case 6 -> new Box(
            (double)pos.getX() + 0.5 - var7,
            (double)pos.getY(),
            (double)pos.getZ() + 0.5 - var7,
            (double)pos.getX() + 0.5 + var7,
            (double)pos.getY(),
            (double)pos.getZ() + 0.5 + var7
         );
            default -> throw new IncompatibleClassChangeError();
         };
      } else {
         throw new IllegalArgumentException("Scale must be between 0.0 and 1.0");
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static Box calculateBoundingBox(BlockPos pos, Direction direction) {
      return switch (ap.field2095[direction.ordinal()]) {
         case 1 -> new Box((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)pos.getZ());
         case 2 -> new Box(
         (double)pos.getX(), (double)pos.getY(), (double)(pos.getZ() + 1), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1)
      );
         case 3 -> new Box(
         (double)(pos.getX() + 1), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1)
      );
         case 4 -> new Box((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX(), (double)(pos.getY() + 1), (double)(pos.getZ() + 1));
         case 5 -> new Box(
         (double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1)
      );
         case 6 -> new Box((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)pos.getY(), (double)(pos.getZ() + 1));
         default -> throw new IncompatibleClassChangeError();
      };
   }

   public static Box getVoxelShapeBoundingBox(BlockPos pos) {
      VoxelShape var4 = mc.world.getBlockState(pos).getCollisionShape(mc.world, pos).offset((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
      Box var5 = new Box(pos);

      for (Box var7 : var4.getBoundingBoxes()) {
         double var8 = Math.max(var7.minX, var5.minX);
         double var10 = Math.max(var7.minY, var5.minY);
         double var12 = Math.max(var7.minZ, var5.minZ);
         double var14 = Math.min(var7.maxX, var5.maxX);
         double var16 = Math.min(var7.maxY, var5.maxY);
         double var18 = Math.min(var7.maxZ, var5.maxZ);
         var5 = new Box(var8, var10, var12, var14, var16, var18);
      }

      return var5;
   }
}
