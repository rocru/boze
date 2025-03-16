package dev.boze.client.ac;

import dev.boze.client.jumptable.y;
import dev.boze.client.utils.BoundingBoxUtil;
import dev.boze.client.utils.BoxUtil;
import java.util.ArrayList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Grim implements Anticheat {
   public static final Grim field1831 = new Grim();
   private double field1832 = 4.5;
   private static final double field1833 = 0.4;
   private static final double field1834 = 1.62;
   private static final double field1835 = 2.0E-4;

   private Grim() {
   }

   public void method938(double var1) {
      this.field1832 = var1;
   }

   @Override
   public BlockHitResult method565(ArrayList<BlockHitResult> var1) {
      Box var5 = this.method940();
      BlockHitResult var6 = null;
      double var7 = Double.MAX_VALUE;

      for (BlockHitResult var10 : var1) {
         double var11 = BoxUtil.method2131(var10.getPos(), var5);
         if (var11 < var7) {
            var6 = var10;
            var7 = var11;
         }
      }

      return var6;
   }

   @Override
   public BlockHitResult method566(BlockPos var1, Direction var2) {
      Box var5 = this.method940();
      Vec3d var6 = BoxUtil.method2133(var5, BoundingBoxUtil.calculateBoundingBox(var1, var2));
      double var7 = BoxUtil.method2131(var6, var5);
      if (var7 <= this.field1832) {
         Box var9 = BoundingBoxUtil.getVoxelShapeBoundingBox(var1);
         return new BlockHitResult(var6, var2, var1, var9.intersects(var5));
      } else {
         return null;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public boolean method567(BlockPos var1, Direction var2, boolean var3, boolean var4) {
      Box var8 = BoundingBoxUtil.getVoxelShapeBoundingBox(var1);
      Box var9 = this.method940();
      if (this.method939(var9, var8)) {
         return true;
      } else {
         switch (y.field2131[var2.ordinal()]) {
            case 1:
               if (!(var9.minZ > var8.minZ)) {
                  return true;
               }
               break;
            case 2:
               if (!(var9.maxZ < var8.maxZ)) {
                  return true;
               }
               break;
            case 3:
               if (!(var9.maxX < var8.maxX)) {
                  return true;
               }
               break;
            case 4:
               if (!(var9.minX > var8.minX)) {
                  return true;
               }
               break;
            case 5:
               if (!((var4 ? var9.maxY : var9.maxY + 1.5) < var8.maxY)) {
                  return true;
               }
               break;
            case 6:
               if (!(var9.minY > var8.minY)) {
                  return true;
               }
               break;
            default:
               throw new IncompatibleClassChangeError();
         }

         return false;
      }
   }

   private boolean method939(Box var1, Box var2) {
      return var2.maxX - 1.0E-6 > var1.minX
         && var2.minX + 1.0E-6 < var1.maxX
         && var2.maxY - 1.0E-6 > var1.minY
         && var2.minY + 1.0E-6 < var1.maxY
         && var2.maxZ - 1.0E-6 > var1.minZ
         && var2.minZ + 1.0E-6 < var1.maxZ;
   }

   private Box method940() {
      return new Box(mc.player.getX(), mc.player.getY() + 0.4, mc.player.getZ(), mc.player.getX(), mc.player.getY() + 1.62, mc.player.getZ()).expand(2.0E-4);
   }
}
