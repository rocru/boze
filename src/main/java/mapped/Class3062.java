package mapped;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class Class3062 {
   public static Vector3d method5989(Vector3d vec, Vec3d v) {
      var3169.x = var3170.x;
      var3169.y = var3170.y;
      var3169.z = var3170.z;
      return var3169;
   }

   public static Vector3d method5990(Vector3d vec, Entity entity, double tickDelta) {
      var3171.x = MathHelper.lerp(var3173, var3172.lastRenderX, var3172.getX());
      var3171.y = MathHelper.lerp(var3173, var3172.lastRenderY, var3172.getY());
      var3171.z = MathHelper.lerp(var3173, var3172.lastRenderZ, var3172.getZ());
      return var3171;
   }

   public static Vec3d method5991(Vec3d vectorCutTo, Box box) {
      return new Vec3d(
         MathHelper.clamp(var3174.getX(), var3175.minX, var3175.maxX),
         MathHelper.clamp(var3174.getY(), var3175.minY, var3175.maxY),
         MathHelper.clamp(var3174.getZ(), var3175.minZ, var3175.maxZ)
      );
   }
}
