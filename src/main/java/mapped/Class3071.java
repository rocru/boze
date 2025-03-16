package mapped;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.StaticColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Class3071 {
   public static BozeDrawColor method6015(BozeDrawColor one, BozeDrawColor two, double partial) {
      float[] var7 = RGBAColor.method191(var3198.field408, var3198.field409, var3198.field410);
      float[] var8 = RGBAColor.method191(var3199.field408, var3199.field409, var3199.field410);
      if (var8[0] - var7[0] > 0.5F) {
         var7[0]++;
      } else if (var7[0] - var8[0] > 0.5F) {
         var8[0]++;
      }

      int[] var9 = RGBAColor.method190(
         method6022((double)var7[0], (double)var8[0], var3200) * 360.0,
         method6022((double)var7[1], (double)var8[1], var3200),
         method6022((double)var7[2], (double)var8[2], var3200)
      );
      return new BozeDrawColor(
         var9[0],
         var9[1],
         var9[2],
         (int)method6022((double)var3198.field411, (double)var3199.field411, var3200),
         var3200 < 0.5 ? var3198.field1842 : var3199.field1842,
         method6022(var3198.field1843, var3199.field1843, var3200),
         method6022(var3198.field1844, var3199.field1844, var3200),
         method6023(var3198.field1845, var3199.field1845, var3200),
         method6023(var3198.field1846, var3199.field1846, var3200)
      );
   }

   public static RGBAColor method6016(RGBAColor one, RGBAColor two, double partial) {
      float[] var7 = RGBAColor.method191(var3201.field408, var3201.field409, var3201.field410);
      float[] var8 = RGBAColor.method191(var3202.field408, var3202.field409, var3202.field410);
      if (var8[0] - var7[0] > 0.5F) {
         var7[0]++;
      } else if (var7[0] - var8[0] > 0.5F) {
         var8[0]++;
      }

      int[] var9 = RGBAColor.method190(
         method6022((double)var7[0], (double)var8[0], var3203) * 360.0,
         method6022((double)var7[1], (double)var8[1], var3203),
         method6022((double)var7[2], (double)var8[2], var3203)
      );
      return new RGBAColor(var9[0], var9[1], var9[2], (int)method6022((double)var3201.field411, (double)var3202.field411, var3203));
   }

   public static StaticColor method6017(StaticColor one, StaticColor two, double partial) {
      return new StaticColor(
         (int)method6022((double)var3204.field430, (double)var3205.field430, var3206),
         (int)method6022((double)var3204.field431, (double)var3205.field431, var3206),
         (int)method6022((double)var3204.field432, (double)var3205.field432, var3206)
      );
   }

   public static StaticColor method6018(StaticColor one, StaticColor two, double partial) {
      float[] var7 = RGBAColor.method191(var3207.field430, var3207.field431, var3207.field432);
      float[] var8 = RGBAColor.method191(var3208.field430, var3208.field431, var3208.field432);
      if (var8[0] - var7[0] > 0.5F) {
         var7[0]++;
      } else if (var7[0] - var8[0] > 0.5F) {
         var8[0]++;
      }

      int[] var9 = RGBAColor.method190(
         method6022((double)var7[0], (double)var8[0], var3209) * 360.0,
         method6022((double)var7[1], (double)var8[1], var3209),
         method6022((double)var7[2], (double)var8[2], var3209)
      );
      return new StaticColor(var9[0], var9[1], var9[2]);
   }

   public static Vec3d method6019(Entity entity) {
      double[] var1 = method6020(var3210);
      return new Vec3d(var1[0], var1[1], var1[2]);
   }

   public static double[] method6020(Entity entity) {
      if (var3211.prevX == 0.0 && var3211.prevY == 0.0 && var3211.prevZ == 0.0) {
         var3211.prevX = var3211.getX();
         var3211.prevY = var3211.getY();
         var3211.prevZ = var3211.getZ();
      }

      double var4 = method6022(var3211.lastRenderX, var3211.getX(), (double)MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
      double var6 = method6022(var3211.lastRenderY, var3211.getY(), (double)MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
      double var8 = method6022(var3211.lastRenderZ, var3211.getZ(), (double)MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
      return new double[]{var4, var6, var8};
   }

   public static Box method6021(Box a, Box b, double partial) {
      return new Box(
         method6022(var3212.minX, var3213.minX, var3214),
         method6022(var3212.minY, var3213.minY, var3214),
         method6022(var3212.minZ, var3213.minZ, var3214),
         method6022(var3212.maxX, var3213.maxX, var3214),
         method6022(var3212.maxY, var3213.maxY, var3214),
         method6022(var3212.maxZ, var3213.maxZ, var3214)
      );
   }

   public static double method6022(double a, double b, double partial) {
      return var3215 * (1.0 - var3217) + var3216 * var3217;
   }

   public static double[] method6023(double[] a, double[] b, double partial) {
      return new double[]{var3218[0] * (1.0 - var3220) + var3219[0] * var3220, var3218[1] * (1.0 - var3220) + var3219[1] * var3220};
   }
}
