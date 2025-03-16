package mapped;

import dev.boze.client.utils.RotationHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Class1202 {
   public static RotationHelper method2391(Vec3d from, Vec3d to) {
      Vec3d var2 = var1274.subtract(var1273);
      return method2392(var2);
   }

   public static RotationHelper method2392(Vec3d delta) {
      float var1 = method2394(var1275.getX(), var1275.getZ());
      float var2 = method2397(var1275.getY(), var1275.horizontalLength());
      return new RotationHelper(var1, var2);
   }

   public static float method2393(double fromX, double fromZ, double toX, double toZ) {
      return method2394(var1278 - var1276, var1279 - var1277);
   }

   public static float method2394(double deltaX, double deltaZ) {
      return (float)Math.toDegrees(Math.atan2(var1281, var1280)) - 90.0F;
   }

   public static float method2395(Vec3d delta) {
      return method2394(var1282.getX(), var1282.getZ());
   }

   public static float method2396(Vec2f delta) {
      return (float)Math.toDegrees(Math.atan2((double)var1283.y, (double)var1283.x)) - 90.0F;
   }

   private static float method2397(double var0, double var2) {
      return (float)(-Math.toDegrees(Math.atan2(var0, var2)));
   }
}
