package dev.boze.client.utils.trackers;

import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class TickRateTracker {
   private static final float[] field3974 = new float[20];
   private static int field3975 = 0;
   private static long field3976;

   public static void reset() {
      field3975 = 0;
      field3976 = -1L;
      Arrays.fill(field3974, 0.0F);
   }

   public static float getAverageTickRate() {
      float var3 = 0.0F;
      float var4 = 0.0F;

      for (float var8 : field3974) {
         if (var8 > 0.0F) {
            var4 += var8;
            var3++;
         }
      }

      return MathHelper.clamp(var4 / var3, 0.0F, 20.0F);
   }

   public static float getMinTickRate() {
      float var3 = 20.0F;

      for (float var7 : field3974) {
         if (var7 > 0.0F && var7 < var3) {
            var3 = var7;
         }
      }

      return MathHelper.clamp(var3, 0.0F, 20.0F);
   }

   public static float getLastTickRate() {
      try {
         return MathHelper.clamp(field3974[field3974.length - 1], 0.0F, 20.0F);
      } catch (Exception var1) {
         return 20.0F;
      }
   }

   public static void update() {
      if (field3976 != -1L) {
         float var3 = (float)(System.currentTimeMillis() - field3976) / 1000.0F;
         field3974[field3975 % field3974.length] = MathHelper.clamp(20.0F / var3, 0.0F, 20.0F);
         field3975++;
      }

      field3976 = System.currentTimeMillis();
   }
}
