package mapped;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;

public class Class2776 {
    public Class2776() {
        super();
    }

    public static void method5430(final double itemX, final double itemY, final double itemHeight, final Class5903<?> color) {
        final double method2091 = Notifications.DELETE.method2091();
        RenderUtil.field3963.method2241(itemX + BaseComponent.scaleFactor * 4.0, itemY + itemHeight * 0.5 - method2091 * 0.5, method2091, method2091, color.method208(), 1.0f);
    }

    public static void method5431(final String name, final double itemX, final double itemY, final double itemHeight) {
        IFontRender.method499().drawShadowedText(name, itemX + BaseComponent.scaleFactor * 4.0 + Notifications.DELETE.method2091() + BaseComponent.scaleFactor * 4.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
    }

    public static void method5432(final double itemX, final double itemY, final double itemWidth, final double itemHeight, final boolean isSelected) {
        RenderUtil.field3963.method2257(itemX, itemY, itemWidth, itemHeight, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 2.0) : 0.0, isSelected ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347());
    }
}
