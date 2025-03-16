package dev.boze.client.gui.notification;

import dev.boze.client.utils.IMinecraft;

public interface INotification extends IMinecraft {
   void renderNotification(float var1, double var2);

   void sendToChat(boolean var1);

   double getHeight();
}
