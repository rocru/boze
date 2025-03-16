package mapped;

import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.settings.BooleanSetting;

class Class2774 {
   Notifications field80;
   Runnable field81;
   BooleanSetting field82;
   double field83;
   double field84;
   double field85;

   Class2774(Notifications var1, Runnable var2, BooleanSetting var3) {
      this.field80 = var1;
      this.field81 = var2;
      this.field82 = var3;
   }

   boolean method5428() {
      return this.field82 != null && this.field82.method419();
   }
}
