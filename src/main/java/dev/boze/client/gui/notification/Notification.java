package dev.boze.client.gui.notification;

import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;

public class Notification implements INotification {
   private final String field1244;
   private final String field1245;
   private final Notifications field1246;
   private final NotificationPriority field1247;
   private double field1248;

   public Notification(String title, String content, Notifications icon, NotificationPriority priority) {
      this.field1244 = title;
      this.field1245 = content;
      this.field1246 = icon;
      this.field1247 = priority;
   }

   @Override
   public void renderNotification(float progress, double y) {
      RenderUtil.field3963.method2233();
      IFontRender.method499().startBuilding((double)dev.boze.client.systems.modules.client.Notifications.INSTANCE.field844.getValue().floatValue());
      IconManager.setScale((double)dev.boze.client.systems.modules.client.Notifications.INSTANCE.field844.getValue().floatValue());
      String var7 = this.field1244 != null ? this.field1244 + (this.field1245.startsWith(" ") ? "" : " - ") + this.field1245 : this.field1245;
      double var8 = IFontRender.method499().method501(var7) + IFontRender.method499().method1390() + 25.0;
      this.field1248 = IFontRender.method499().method1390() + 10.0;
      if (dev.boze.client.systems.modules.client.Notifications.INSTANCE.field841.getValue() == 0) {
         RenderUtil.field3963
            .method2252(
               (double)mc.getWindow().getScaledWidth() - (var8 + 10.0) * (double)progress,
               y,
               var8,
               this.field1248,
               dev.boze.client.systems.modules.client.Notifications.INSTANCE.field839.getValue()
            );
      } else {
         RenderUtil.field3963
            .method2257(
               (double)mc.getWindow().getScaledWidth() - (var8 + 10.0) * (double)progress,
               y,
               var8,
               this.field1248,
               15,
               24,
               (double)dev.boze.client.systems.modules.client.Notifications.INSTANCE.field841.getValue().intValue(),
               dev.boze.client.systems.modules.client.Notifications.INSTANCE.field839.getValue()
            );
      }

      RenderUtil.field3963.method2235(null);
      IFontRender.method499()
         .drawShadowedText(
            var7,
            (double)mc.getWindow().getScaledWidth() - (var8 + 10.0) * (double)progress + 15.0 + this.field1246.method2091(),
            y + 5.0,
            dev.boze.client.systems.modules.client.Notifications.INSTANCE.field840.getValue()
         );
      this.field1246
         .render(
            (double)mc.getWindow().getScaledWidth() - (var8 + 10.0) * (double)progress + 10.0,
            y + 5.0,
            this.field1247 == NotificationPriority.Normal
               ? dev.boze.client.systems.modules.client.Notifications.INSTANCE.field840.getValue()
               : (this.field1247 == NotificationPriority.Yellow ? RGBAColor.field404 : RGBAColor.field403)
         );
      IFontRender.method499().endBuilding();
      IconManager.method1115();
   }

   @Override
   public void sendToChat(boolean keepInChat) {
      if (keepInChat) {
         ChatInstance.method740(this.field1244, this.field1245);
      } else {
         ChatInstance.method741(7773, this.field1244, this.field1245);
      }
   }

   @Override
   public double getHeight() {
      return this.field1248;
   }
}
