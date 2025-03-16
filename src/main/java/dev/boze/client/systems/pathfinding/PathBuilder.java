package dev.boze.client.systems.pathfinding;

import dev.boze.client.utils.IMinecraft;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;

public class PathBuilder implements IMinecraft {
   public static Path method616(ArrayList<PathPos> path, double maxAcceleration, double maxSpeed) {
      Path var8 = new Path(method617((PathPos)path.get(0)));
      Vec3d var9 = method617((PathPos)path.get(0)).subtract(mc.player.getPos());
      if (var9.length() > 0.01) {
         var8.method2092(var9);
      }

      if (path.size() < 2) {
         return var8;
      } else {
         Vec3d var10 = method617((PathPos)path.get(1)).subtract(method617((PathPos)path.get(0)));

         for (int var11 = 1; var11 < path.size() - 1; var11++) {
            Vec3d var12 = method617((PathPos)path.get(var11));
            Vec3d var13 = method617((PathPos)path.get(var11 + 1));
            Vec3d var14 = var13.subtract(var12);
            if (!method117(var10, var14)) {
               var8.method2092(var10);
               var10 = var14;
            } else {
               var10 = var10.add(var14);
            }
         }

         if (var10.length() > 0.0) {
            var8.method2092(var10);
         }

         var8.method2093(maxAcceleration, maxSpeed);
         return var8;
      }
   }

   private static Vec3d method617(PathPos var0) {
      return new Vec3d((double)var0.getX() + 0.5, (double)var0.getY(), (double)var0.getZ() + 0.5);
   }

   private static boolean method117(Vec3d var0, Vec3d var1) {
      Vec3d var5 = var0.normalize();
      Vec3d var6 = var1.normalize();
      return var5.dotProduct(var6) > 0.99;
   }
}
