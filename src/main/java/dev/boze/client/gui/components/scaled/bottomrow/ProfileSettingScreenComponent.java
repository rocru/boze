package dev.boze.client.gui.components.scaled.bottomrow;

import dev.boze.client.Boze;
import dev.boze.client.enums.BottomRow;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.ProfileSetting;
import dev.boze.client.systems.modules.client.Profiles;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class1201;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;

public class ProfileSettingScreenComponent extends BottomRowScaledComponent {
    private final ProfileSetting field1456;
    private final ArrayList<String> field1457;

    public ProfileSettingScreenComponent(ProfileSetting setting) {
        super(setting.name, BottomRow.AddClose, 0.1, 0.4);
        this.field1456 = setting;
        this.field1457 = new ArrayList(Class1201.field57);
        this.field1457.remove("MAIN_PROFILE");
    }

    @Override
    protected int method2010() {
        return this.field1457.size();
    }

    @Override
    protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
        String var13 = this.field1457.get(index);
        boolean var14 = var13.equals(this.field1456.getValue());
        RenderUtil.field3963
                .method2257(
                        itemX,
                        itemY,
                        itemWidth,
                        itemHeight,
                        15,
                        24,
                        Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
                        var14 ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
                );
        IFontRender.method499()
                .drawShadowedText(
                        var13, itemX + BaseComponent.scaleFactor * 6.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
                );
        double var15 = itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.DELETE.method2091();
        Notifications.DELETE.render(var15, itemY + itemHeight * 0.5 - Notifications.DELETE.method1614() * 0.5, Theme.method1350());
    }

    @Override
    protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
        String var18 = this.field1457.get(index);
        double var19 = itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.DELETE.method2091();
        if (isMouseWithinBounds(
                mouseX,
                mouseY,
                var19,
                itemY + itemHeight * 0.5 - Notifications.DELETE.method1614() * 0.5,
                Notifications.DELETE.method2091(),
                Notifications.DELETE.method1614()
        )) {
            return this.method1701(var18);
        } else if (!var18.equals(this.field1456.getValue())) {
            String var21 = "v2.main." + var18;
            String var22 = "v2.visuals." + var18;
            String var23 = "v2.binds." + var18;
            String var24 = "v2.client." + var18;
            Profiles.INSTANCE.field762.setValue(var21);
            Profiles.INSTANCE.field763.setValue(var22);
            Profiles.INSTANCE.field764.setValue(var23);
            Profiles.INSTANCE.field765.setValue(var24);
            Boze.getModules().method398(ConfigManager.downloadConfig(var18, ConfigType.PROFILE), true);
            Class1201.method2384(true, var21, var22, var23, var24);
            return true;
        } else {
            return false;
        }
    }

    private boolean method1701(String var1) {
        if (var1.equals(this.field1456.getValue())) {
            return false;
        } else {
            this.field1457.remove(var1);
            Class1201.field57.remove(var1);
            ConfigManager.delete(var1, ConfigType.PROFILE);
            return true;
        }
    }

    @Override
    protected void method1904() {
    }

    @Override
    protected void method1198() {
        double var3 = IFontRender.method499().method501("Close") + BaseComponent.scaleFactor * 6.0;
        double var5 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
        double var7 = this.field1389 + this.field1391 - var5 - BaseComponent.scaleFactor * 6.0;
        double var9 = this.field1388 + this.field1390 * 0.5 - var3 * 0.5;
        RenderUtil.field3963.method2257(var9, var7, var3, var5, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1347());
        IFontRender.method499()
                .drawShadowedText(
                        "Close",
                        var9 + var3 * 0.5 - IFontRender.method499().method501("Close") * 0.5,
                        var7 + var5 * 0.5 - IFontRender.method499().method1390() * 0.5,
                        Theme.method1350()
                );
    }

    @Override
    protected void handleAddAndCloseClick(double mouseX, double mouseY) {
        double var8 = IFontRender.method499().method501("Close") + BaseComponent.scaleFactor * 6.0;
        double var10 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
        double var12 = this.field1389 + this.field1391 - var10 - BaseComponent.scaleFactor * 6.0;
        double var14 = this.field1388 + this.field1390 * 0.5 - var8 * 0.5;
        if (isMouseWithinBounds(mouseX, mouseY, var14, var12, var8, var10)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(null);
        }
    }
}
