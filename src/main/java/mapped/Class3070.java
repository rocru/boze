package mapped;

public class Class3070 {
   private static double method6012(double var0) {
      var0 %= Math.PI * 2;
      if (Math.abs(var0) > Math.PI) {
         var0 -= Math.PI * 2;
      }

      if (Math.abs(var0) > Math.PI / 2) {
         var0 = Math.PI - var0;
      }

      return var0;
   }

   public static double method6013(double radians) {
      var3196 = method6012(var3196);
      return Math.abs(var3196) <= Math.PI / 4 ? Math.sin(var3196) : Math.cos((Math.PI / 2) - var3196);
   }

   public static double method6014(double radians) {
      return method6013(var3197 + (Math.PI / 2));
   }
}
