package dev.boze.client.events;

import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;

public class AddMessageEvent extends CancelableEvent {
   private static final AddMessageEvent field1893 = new AddMessageEvent();
   private Text field1894;
   private MessageIndicator field1895;
   private boolean field1896;

   public static AddMessageEvent method1040(Text var0, MessageIndicator var1) {
      field1893.method1021(false);
      field1893.field1894 = var0;
      field1893.field1895 = var1;
      field1893.field1896 = false;
      return field1893;
   }

   public Text method1041() {
      return this.field1894;
   }

   public MessageIndicator method1042() {
      return this.field1895;
   }

   public void method1043(Text var1) {
      this.field1894 = var1;
      this.field1896 = true;
   }

   public void method1044(MessageIndicator var1) {
      this.field1895 = var1;
      this.field1896 = true;
   }

   public boolean method1045() {
      return this.field1896;
   }
}
