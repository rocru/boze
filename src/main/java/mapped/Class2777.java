package mapped;

import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.systems.modules.client.Theme;

public class Class2777 {
   public static void method5433(Notifications icon, double x, double y) {
      double var7 = Class2778.method5436();
      var2830.render(var2831, var2832 - var7 * 0.5, Theme.method1350());
   }

   public static void method5434(double itemX, double itemWidth, double buttonY, Notifications... icons) {
      for (int var10 = 0; var10 < var2836.length; var10++) {
         method5433(var2836[var10], Class2778.method5439(var2833, var2834, var10), var2835);
      }
   }
}
