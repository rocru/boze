package dev.boze.client.utils;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BoxUtil {
   public static double method2131(Vec3d point, Box box) {
      double var5 = MathHelper.clamp(point.getX(), box.minX, box.maxX);
      double var7 = MathHelper.clamp(point.getY(), box.minY, box.maxY);
      double var9 = MathHelper.clamp(point.getZ(), box.minZ, box.maxZ);
      double var11 = var5 - point.getX();
      double var13 = var7 - point.getY();
      double var15 = var9 - point.getZ();
      return Math.sqrt(var11 * var11 + var13 * var13 + var15 * var15);
   }

   public static Vec3d method2132(Vec3d vec, Box clamp) {
      return new Vec3d(
         MathHelper.clamp(vec.getX(), clamp.minX, clamp.maxX),
         MathHelper.clamp(vec.getY(), clamp.minY, clamp.maxY),
         MathHelper.clamp(vec.getZ(), clamp.minZ, clamp.maxZ)
      );
   }

   public static Vec3d method2133(Box box, Box clamp) {
      double var2 = method2134(box.minX, box.maxX, clamp.minX, clamp.maxX);
      double var4 = method2134(box.minY, box.maxY, clamp.minY, clamp.maxY);
      double var6 = method2134(box.minZ, box.maxZ, clamp.minZ, clamp.maxZ);
      return new Vec3d(var2, var4, var6);
   }

   private static double method2134(double var0, double var2, double var4, double var6) {
      if (var2 < var4) {
         return var4;
      } else {
         return var0 > var6 ? var6 : MathHelper.clamp(var0, var4, var6);
      }
   }
}
