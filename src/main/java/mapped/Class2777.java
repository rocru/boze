package mapped;

import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.systems.modules.client.Theme;

public class Class2777 {
    public Class2777() {
        super();
    }

    public static void method5433(final Notifications icon, final double x, final double y) {
        icon.render(x, y - Class2778.method5436() * 0.5, Theme.method1350());
    }

    public static void method5434(final double itemX, final double itemWidth, final double buttonY, final Notifications... icons) {
        for (int i = 0; i < icons.length; ++i) {
            method5433(icons[i], Class2778.method5439(itemX, itemWidth, i), buttonY);
        }
    }
}
