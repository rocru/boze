package mapped;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Class5917 implements IMinecraft {
   public static Vec2f method31(Vec2f line1Begin, Vec2f line1End, Vec2f line2Begin, Vec2f line2End) {
      float var4 = (var21.x - var22.x) * (var23.y - var24.y) - (var21.y - var22.y) * (var23.x - var24.x);
      float var5 = (var21.x * var22.y - var21.y * var22.x) * (var23.x - var24.x) - (var21.x - var22.x) * (var23.x * var24.y - var23.y * var24.x);
      float var6 = (var21.x * var22.y - var21.y * var22.x) * (var23.y - var24.y) - (var21.y - var22.y) * (var23.x * var24.y - var23.y * var24.x);
      float var7 = var5 / var4;
      float var8 = var6 / var4;
      return new Vec2f(var7, var8);
   }

   public static double method32(double time, double bias) {
      return var25 / ((1.0 / var26 - 2.0) * (1.0 - var25) + 1.0);
   }

   public static Vec3d method33(Vec3d start, Box box) {
      return new Vec3d(
         MathHelper.clamp(var27.getX(), var28.minX, var28.maxX),
         MathHelper.clamp(var27.getY(), var28.minY, var28.maxY),
         MathHelper.clamp(var27.getZ(), var28.minZ, var28.maxZ)
      );
   }

   public static Vec3d method34(Box box) {
      Vec3d var4 = mc.player.getEyePos();
      return var29.minX < var4.x && var4.x < var29.maxX && var29.minZ < var4.z && var4.z < var29.maxZ
         ? new Vec3d(
            var29.minX + (var29.maxX - var29.minX) / 2.0, Math.max(var29.minY, Math.min(var4.y, var29.maxY)), var29.minZ + (var29.maxZ - var29.minZ) / 2.0
         )
         : method33(var4, var29);
   }

   public static double method35(double value, double goal, double increment) {
      return var30 < var31 ? Math.min(var30 + var32, var31) : Math.max(var30 - var32, var31);
   }

   public static Vec3d method136(Box box, Vec3d look, Vec3d eyes) {
      double var6 = Double.MAX_VALUE;
      Vec3d var8 = var5943.getCenter();
      double var9 = (var5943.minX + var5943.maxX) / 2.0;
      double var11 = (var5943.minY + var5943.maxY) / 2.0;
      double var13 = (var5943.minZ + var5943.maxZ) / 2.0;
      Vec3d[] var15 = new Vec3d[]{
         new Vec3d(var5943.minX, var11, var13),
         new Vec3d(var5943.maxX, var11, var13),
         new Vec3d(var9, var5943.minY, var13),
         new Vec3d(var9, var5943.maxY, var13),
         new Vec3d(var9, var11, var5943.minZ),
         new Vec3d(var9, var11, var5943.maxZ)
      };

      for (Vec3d var19 : var15) {
         Vec3d var20 = new Vec3d(var19.x - var5945.x, var19.y - var5945.y, var19.z - var5945.z);
         double var21 = Math.acos(var5944.dotProduct(var20) / (var5944.length() * var20.length()));
         if (var21 < var6) {
            var6 = var21;
            var8 = var19;
         }
      }

      double[] var24 = new double[]{var5943.minX, var5943.maxX, var5943.minX, var5943.maxX};
      double[] var25 = new double[]{var11, var11, var5943.minY, var5943.maxY};
      double[] var26 = new double[]{var13, var13, var13, var13};

      for (int var27 = 0; var27 < 4; var27++) {
         Vec3d var28 = new Vec3d(var24[var27], var25[var27], var26[var27]);
         Vec3d var29 = new Vec3d(var28.x - var5945.x, var28.y - var5945.y, var28.z - var5945.z);
         double var22 = Math.acos(var5944.dotProduct(var29) / (var5944.length() * var29.length()));
         if (var22 < var6) {
            var6 = var22;
            var8 = var28;
         }
      }

      return var8;
   }

   public static Vec3d method123(Box box, Vec3d eyes) {
      double var5 = Double.MAX_VALUE;
      Vec3d var7 = var5946.getCenter();
      double var8 = (var5946.minX + var5946.maxX) / 2.0;
      double var10 = (var5946.minY + var5946.maxY) / 2.0;
      double var12 = (var5946.minZ + var5946.maxZ) / 2.0;
      Vec3d[] var14 = new Vec3d[]{
         new Vec3d(var5946.minX, var10, var12),
         new Vec3d(var5946.maxX, var10, var12),
         new Vec3d(var8, var5946.minY, var12),
         new Vec3d(var8, var5946.maxY, var12),
         new Vec3d(var8, var10, var5946.minZ),
         new Vec3d(var8, var10, var5946.maxZ)
      };

      for (Vec3d var18 : var14) {
         double var19 = Math.sqrt(Math.pow(var18.x - var5947.x, 2.0) + Math.pow(var18.y - var5947.y, 2.0) + Math.pow(var18.z - var5947.z, 2.0));
         if (var19 < var5) {
            var5 = var19;
            var7 = var18;
         }
      }

      double[] var22 = new double[]{var5946.minX, var5946.maxX, var5946.minX, var5946.maxX};
      double[] var23 = new double[]{var10, var10, var5946.minY, var5946.maxY};
      double[] var24 = new double[]{var12, var12, var12, var12};

      for (int var25 = 0; var25 < 4; var25++) {
         Vec3d var26 = new Vec3d(var22[var25], var23[var25], var24[var25]);
         double var20 = Math.sqrt(Math.pow(var26.x - var5947.x, 2.0) + Math.pow(var26.y - var5947.y, 2.0) + Math.pow(var26.z - var5947.z, 2.0));
         if (var20 < var5) {
            var5 = var20;
            var7 = var26;
         }
      }

      return var7;
   }
}
