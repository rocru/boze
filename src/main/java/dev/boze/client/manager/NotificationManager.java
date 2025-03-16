package dev.boze.client.manager;

import dev.boze.client.gui.notification.INotification;
import dev.boze.client.gui.notification.NotificationType;
import dev.boze.client.systems.modules.client.Notifications;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationManager {
   private static final LinkedList<INotification> field2141 = new LinkedList();
   private static ConcurrentHashMap<INotification, Long> field2142 = new ConcurrentHashMap();

   public static void method1151(INotification var0) {
      if (Notifications.INSTANCE.isEnabled()) {
         if (Notifications.INSTANCE.field837.method461() == NotificationType.Chat) {
            try {
               var0.sendToChat(Notifications.INSTANCE.field838.method419());
            } catch (Exception var5) {
            }
         } else {
            field2141.add(var0);
         }
      }
   }

   public static void method1152() {
      if (field2142.size() < 5 && !field2141.isEmpty()) {
         field2142.put((INotification)field2141.pop(), System.currentTimeMillis());
      }

      double var3 = 10.0;

      for (INotification var6 : field2142.keySet()) {
         long var7 = System.currentTimeMillis() - (Long)field2142.get(var6);
         if (var7 >= (long)method1155()) {
            field2142.remove(var6);
         } else {
            float var9 = 1.0F;
            if (var7 < (long)method1154()) {
               var9 = (float)var7 / (float)method1154();
            } else if (var7 >= (long)(method1153() + method1154())) {
               var9 = 1.0F - (float)(var7 - (long)method1153() - (long)method1154()) / (float)method1154();
            }

            var6.renderNotification(var9, var3);
            var3 += var6.getHeight() + 5.0;
         }
      }
   }

   private static int method1153() {
      return (int)(Notifications.INSTANCE.field842.method423() * 1000.0F);
   }

   private static int method1154() {
      return (int)(Notifications.INSTANCE.field843.method423() * 1000.0F);
   }

   private static int method1155() {
      return method1153() + method1154() * 2;
   }
}
