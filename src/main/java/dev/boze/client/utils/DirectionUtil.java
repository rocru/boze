package dev.boze.client.utils;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DirectionUtil {
   public static Vec3d getLookVector(float pitch, float yaw) {
      float var5 = pitch * (float) (Math.PI / 180.0);
      float var6 = -yaw * (float) (Math.PI / 180.0);
      float var7 = MathHelper.cos(var6);
      float var8 = MathHelper.sin(var6);
      float var9 = MathHelper.cos(var5);
      float var10 = MathHelper.sin(var5);
      return new Vec3d((double)(var8 * var9), (double)(-var10), (double)(var7 * var9));
   }
}
