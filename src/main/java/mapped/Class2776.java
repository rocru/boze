package mapped;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;

public class Class2776 {
   public static void method5430(double itemX, double itemY, double itemHeight, Class5903<?> color) {
      double var10 = Notifications.DELETE.method2091();
      double var12 = var2817 + BaseComponent.scaleFactor * 4.0;
      double var14 = var2818 + var2819 * 0.5 - var10 * 0.5;
      RenderUtil.field3963.method2241(var12, var14, var10, var10, var2820.method208(), 1.0F);
   }

   public static void method5431(String name, double itemX, double itemY, double itemHeight) {
      double var10 = Notifications.DELETE.method2091();
      double var12 = var2822 + BaseComponent.scaleFactor * 4.0 + var10 + BaseComponent.scaleFactor * 4.0;
      IFontRender.method499().drawShadowedText(var2821, var12, var2823 + var2824 * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
   }

   public static void method5432(double itemX, double itemY, double itemWidth, double itemHeight, boolean isSelected) {
      RenderUtil.field3963
         .method2257(
            var2825,
            var2826,
            var2827,
            var2828,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
            var2829 ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
         );
   }
}
