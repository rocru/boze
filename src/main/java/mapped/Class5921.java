package mapped;

import dev.boze.client.utils.IMinecraft;
import java.util.HashMap;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class Class5921 implements IMinecraft {
   private static final Vector3d field18 = new Vector3d(0.0, 0.0, 0.0);
   private static final Class3064<Vector3d> field19 = new Class3064<Vector3d>(Vector3d::new);
   private static final HashMap<Integer, Vector3d> field20 = new HashMap();
   public static double field21 = 0.0784;

   public static void method2142() {
      for (Vector3d var4 : field20.values()) {
         field19.method5994(var4);
      }

      field20.clear();
   }

   public static HashMap<Integer, Vector3d> method1282() {
      return field20;
   }

   public static Vector3d method55(LivingEntity entity) {
      return (Vector3d)field20.getOrDefault(var66.getId(), field18);
   }

   public static Vector3d method56(LivingEntity entity, int extrapolation, boolean simulate) {
      if (var67 == null) {
         return field18;
      } else if (field20.containsKey(var67.getId())) {
         return (Vector3d)field20.get(var67.getId());
      } else {
         Vector3d var6 = method57(var67, var68, var69, field19.method5993());
         field20.put(var67.getId(), var6);
         return var6;
      }
   }

   public static Vector3d method57(LivingEntity entity, int extrapolation, boolean simulate, Vector3d vec) {
      if (var70 instanceof PlayerEntity var7 && var72) {
         Pair var8 = Class5918.method38(var71, var7);
         if (var8 != null) {
            Vec3d var27 = ((ClientPlayerEntity)var8.getLeft()).getPos();
            return var73.set(var27.x - var70.getX(), var27.y - var70.getY(), var27.z - var70.getZ());
         }
      }

      if (var70.getVelocity().length() == 0.0) {
         return var73.set(0.0, 0.0, 0.0);
      } else {
         double var26 = var70.getX() - var70.prevX;
         double var9 = var70.getY() - var70.prevY;
         double var11 = var70.getZ() - var70.prevZ;
         double var13 = 0.0;
         double var15 = 0.0;
         double var17 = 0.0;

         for (int var19 = 0; var19 < var71; var19++) {
            double var20 = var13 + var26;
            double var22 = var15 + var9;
            double var24 = var17 + var11;
            if (!mc.world.isSpaceEmpty(var70, var70.getBoundingBox().offset(var20, var22, var24))) {
               if (mc.world.isSpaceEmpty(var70, var70.getBoundingBox().offset(0.0, var22, 0.0))) {
                  var20 = var13;
                  var24 = var17;
               } else if (mc.world.isSpaceEmpty(var70, var70.getBoundingBox().offset(var20, 0.0, var24))) {
                  var22 = var15;
               } else {
                  var20 = var13;
                  var22 = var15;
                  var24 = var17;
               }
            }

            var13 = var20;
            var15 = var22;
            var17 = var24;
            var9 -= field21;
         }

         return var73.set(var13, var15, var17);
      }
   }
}
