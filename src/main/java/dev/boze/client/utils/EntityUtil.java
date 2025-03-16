package dev.boze.client.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class EntityUtil implements IMinecraft {
   public static Vec3d method2144(Entity entity) {
      return entity.getPos().add(0.0, (double)entity.getEyeHeight(entity.getPose()), 0.0);
   }

   public static Vector3d method2145(Entity entity) {
      Vec3d var1 = method2144(entity);
      return new Vector3d(var1.x, var1.y, var1.z);
   }

   public static float[] method2146(Vec3d to) {
      return mc.player == null ? new float[]{0.0F, 0.0F} : method2147(method2144(mc.player), to);
   }

   public static float[] method2147(Vec3d from, Vec3d to) {
      double var5 = to.x - from.x;
      double var7 = (to.y - from.y) * -1.0;
      double var9 = to.z - from.z;
      double var11 = (double)MathHelper.sqrt((float)(var5 * var5 + var9 * var9));
      float var13 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var9, var5)) - 90.0);
      float var14 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var7, var11)));
      if (var14 > 90.0F) {
         var14 = 90.0F;
      } else if (var14 < -90.0F) {
         var14 = -90.0F;
      }

      return new float[]{var13, var14};
   }

   public static Vec3d method2148(float[] rotations) {
      double var1 = Math.toRadians((double)rotations[0]);
      double var3 = Math.toRadians((double)rotations[1]);
      return new Vec3d(Math.sin(-var1) * Math.cos(var3), -Math.sin(var3), Math.cos(-var1) * Math.cos(var3));
   }

   public static float method2149(float[] rotation, float[] other) {
      return method2151(method2150(rotation, other));
   }

   public static float[] method2150(float[] rotation, float[] other) {
      return new float[]{MathHelper.wrapDegrees(other[0] - rotation[0]), other[1] - rotation[1]};
   }

   private static float method2151(float[] var0) {
      return (float)Math.sqrt((double)(var0[0] * var0[0] + var0[1] * var0[1]));
   }
}
