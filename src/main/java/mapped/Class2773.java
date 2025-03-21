package mapped;

import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.settings.BooleanSetting;

public class Class2773 {
    public Notifications field74;
    public Runnable field75;
    public BooleanSetting field76;
    public double field77;
    public double field78;
    public double field79;

    public Class2773(Notifications var1, Runnable var2, BooleanSetting var3) {
        this.field74 = var1;
        this.field75 = var2;
        this.field76 = var3;
    }

    public boolean method5427() {
        return this.field76 != null && this.field76.getValue();
    }
}
