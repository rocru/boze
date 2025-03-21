package dev.boze.client.gui.notification;

import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.client.OldColors;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.text.Style;

public class NotificationRenderer implements INotification {
    private final String field1241;
    private final boolean field1242;
    private double field1243;

    public NotificationRenderer(String title, boolean state) {
        this.field1241 = title;
        this.field1242 = state;
    }

    @Override
    public void renderNotification(float progress, double y) {
        RenderUtil.field3963.method2233();
        RenderUtil.field3967.method2233();
        IFontRender.method499().startBuilding(dev.boze.client.systems.modules.client.Notifications.INSTANCE.field844.getValue().floatValue());
        IconManager.setScale(dev.boze.client.systems.modules.client.Notifications.INSTANCE.field844.getValue().floatValue());
        String var7 = this.field1241;
        double var8 = IFontRender.method499().method501(var7) + IFontRender.method499().method1390() + 25.0;
        this.field1243 = IFontRender.method499().method1390() + 10.0;
        RenderUtil.field3963
                .method2257(
                        (double) mc.getWindow().getScaledWidth() - (var8 + 10.0) * (double) progress,
                        y,
                        var8,
                        this.field1243,
                        15,
                        24,
                        5.0,
                        dev.boze.client.systems.modules.client.Notifications.INSTANCE.field839.getValue()
                );
        RenderUtil.field3963.method2235(null);
        IFontRender.method499()
                .drawShadowedText(
                        var7,
                        (double) mc.getWindow().getScaledWidth() - (var8 + 10.0) * (double) progress + 10.0,
                        y + 5.0,
                        dev.boze.client.systems.modules.client.Notifications.INSTANCE.field840.getValue()
                );
        (this.field1242 ? Notifications.TOGGLE_ON : Notifications.TOGGLE_OFF)
                .render(
                        (double) mc.getWindow().getScaledWidth() - (var8 + 10.0) * (double) progress + 10.0 + IFontRender.method499().method501(var7) + 5.0,
                        y + 5.0,
                        dev.boze.client.systems.modules.client.Notifications.INSTANCE.field840.getValue()
                );
        IFontRender.method499().endBuilding();
        IconManager.method1115();
    }

    @Override
    public void sendToChat(boolean keepInChat) {
        if (keepInChat) {
            ChatInstance.method751(
                    0, this.field1241, Style.EMPTY.withColor(OldColors.method1342().method2010()), " has been " + (this.field1242 ? "(green)enabled" : "(red)disabled")
            );
        } else {
            ChatInstance.method751(
                    7773,
                    this.field1241,
                    Style.EMPTY.withColor(OldColors.method1342().method2010()),
                    " has been " + (this.field1242 ? "(green)enabled" : "(red)disabled")
            );
        }
    }

    @Override
    public double getHeight() {
        return this.field1243;
    }
}
