package mapped;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.LivingEntity;
import org.joml.Vector3d;

public class Class5920 implements IMinecraft {
   public static Vector3d method52(LivingEntity entity, int extrapolation, Vector3d vec) {
      if (var63.getVelocity().length() == 0.0) {
         return var65.set(0.0, 0.0, 0.0);
      } else {
         double var6 = var63.getX() - var63.prevX;
         double var8 = var63.getY() - var63.prevY;
         double var10 = var63.getZ() - var63.prevZ;
         double var12 = 0.0;
         double var14 = 0.0;
         double var16 = 0.0;

         for (int var18 = 0; var18 < var64; var18++) {
            double var19 = var12 + var6;
            double var21 = var14 + var8;
            double var23 = var16 + var10;
            if (!mc.world.isSpaceEmpty(var63, var63.getBoundingBox().offset(var19, var21, var23))) {
               if (mc.world.isSpaceEmpty(var63, var63.getBoundingBox().offset(0.0, var21, 0.0))) {
                  var19 = var12;
                  var23 = var16;
               } else if (mc.world.isSpaceEmpty(var63, var63.getBoundingBox().offset(var19, 0.0, var21))) {
                  var21 = var14;
               } else {
                  var19 = var12;
                  var21 = var14;
                  var23 = var16;
               }
            }

            var12 = var19;
            var14 = var21;
            var16 = var23;
            var8 -= 0.0784;
         }

         return var65.set(var63.getX() + var12, var63.getY() + var14, var63.getZ() + var16);
      }
   }
}
