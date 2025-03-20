package mapped;

import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.settings.BooleanSetting;

public class Class2774 {
   public Notifications field80;
   public Runnable field81;
   public BooleanSetting field82;
   public double field83;
   public double field84;
   public double field85;

   public Class2774(Notifications var1, Runnable var2, BooleanSetting var3) {
      this.field80 = var1;
      this.field81 = var2;
      this.field82 = var3;
   }

   public boolean method5428() {
      return this.field82 != null && this.field82.getValue();
   }
}
