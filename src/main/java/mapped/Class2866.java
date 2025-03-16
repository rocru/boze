package mapped;

import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.systems.modules.misc.Timer;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.player.RotationHandler;
import net.minecraft.entity.EntityPose;

public class Class2866 implements IMinecraft {
   public static void method1872(TickInputPostEvent event) {
      if (var2984.field1954 != 0.0F || var2984.field1953 != 0.0F) {
         if (Timer.INSTANCE.field1034.field927) {
            float var4 = mc.player.getYaw();
            float var5 = RotationHandler.field1546.method1384();
            double var6 = (double)var2984.field1953 * Math.cos(Math.toRadians((double)var4))
               - (double)var2984.field1954 * Math.sin(Math.toRadians((double)var4));
            double var8 = (double)var2984.field1954 * Math.cos(Math.toRadians((double)var4))
               + (double)var2984.field1953 * Math.sin(Math.toRadians((double)var4));
            double[] var10 = null;
            double var11 = mc.player.getPose() == EntityPose.STANDING ? 1.0 : (double)Math.max(Math.abs(var2984.field1954), Math.abs(var2984.field1953));

            for (double var13 = -var11; var13 <= var11; var13 += var11) {
               for (double var15 = -var11; var15 <= var11; var15 += var11) {
                  double var17 = var15 * Math.cos(Math.toRadians((double)var5)) - var13 * Math.sin(Math.toRadians((double)var5));
                  double var19 = var13 * Math.cos(Math.toRadians((double)var5)) + var15 * Math.sin(Math.toRadians((double)var5));
                  double var21 = var17 - var6;
                  double var23 = var19 - var8;
                  double var25 = Math.sqrt(var21 * var21 + var23 * var23);
                  if (var10 == null || var10[0] > var25) {
                     var10 = new double[]{var25, var13, var15};
                  }
               }
            }

            if (var10 != null) {
               var2984.field1954 = (float)var10[1];
               var2984.field1953 = (float)var10[2];
            }
         }
      }
   }
}
