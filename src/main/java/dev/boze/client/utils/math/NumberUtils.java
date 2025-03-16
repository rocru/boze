package dev.boze.client.utils.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
   public static double method2195(double value, int places) {
      BigDecimal var3 = new BigDecimal(value);
      var3 = var3.setScale(places, RoundingMode.HALF_UP);
      return var3.doubleValue();
   }

   public static double method2196(double value, double step) {
      if (step < 0.5 && value + step >= Math.ceil(value)) {
         return Math.ceil(value);
      } else {
         return step < 0.5 && value - step <= Math.floor(value) ? Math.floor(value) : step * (double)Math.round(value * (1.0 / step));
      }
   }

   public static float method2197(float value, int places) {
      BigDecimal var2 = new BigDecimal((double)value);
      var2 = var2.setScale(places, RoundingMode.HALF_UP);
      return var2.floatValue();
   }

   public static float method2198(float value, float step) {
      if ((double)step < 0.5 && (double)(value + step) >= Math.ceil((double)value)) {
         return (float)Math.ceil((double)value);
      } else {
         return (double)step < 0.5 && (double)(value - step) <= Math.floor((double)value)
            ? (float)Math.floor((double)value)
            : step * (float)Math.round(value * (1.0F / step));
      }
   }
}
