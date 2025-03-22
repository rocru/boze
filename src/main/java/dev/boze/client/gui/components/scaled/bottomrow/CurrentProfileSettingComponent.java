package dev.boze.client.gui.components.scaled.bottomrow;

import dev.boze.client.core.Version;
import dev.boze.client.enums.BottomRow;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.CurrentProfileSetting;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.network.BozeExecutor;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class1201;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class CurrentProfileSettingComponent extends BottomRowScaledComponent {
    private final CurrentProfileSetting field1463;
    private final String field1464;
    private final ArrayList<String> field1465;

    public CurrentProfileSettingComponent(CurrentProfileSetting setting) {
        super(setting.name, BottomRow.TextAddClose, 0.1, 0.4);
        this.field1463 = setting;
        this.field1464 = "";
        this.field1465 = new ArrayList(setting.field968);
    }

    private static void lambda$deleteProfile$0(String var0) {
        ConfigManager.delete(var0, ConfigType.PROFILE);
    }

    @Override
    protected int method2010() {
        return this.field1465.size();
    }

    @Override
    protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
        String var13 = this.field1465.get(index);
        String var14 = var13.substring(var13.lastIndexOf(46) + 1);
        boolean var15 = var13.equals(this.field1463.getValue());
        RenderUtil.field3963
                .method2257(
                        itemX,
                        itemY,
                        itemWidth,
                        itemHeight,
                        15,
                        24,
                        Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
                        var15 ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
                );
        IFontRender.method499()
                .drawShadowedText(
                        var14, itemX + BaseComponent.scaleFactor * 6.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
                );
        double var16 = itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.DELETE.method2091();
        Notifications.DELETE.render(var16, itemY + itemHeight * 0.5 - Notifications.DELETE.method1614() * 0.5, Theme.method1350());
        double var18 = var16 - BaseComponent.scaleFactor * 6.0 - Notifications.SHARE.method2091();
        Notifications.SHARE.render(var18, itemY + itemHeight * 0.5 - Notifications.SHARE.method1614() * 0.5, Theme.method1350());
    }

    @Override
    protected void method640(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
    }

    @Override
    protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
        String var18 = this.field1465.get(index);
        double var19 = itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.DELETE.method2091();
        double var21 = var19 - BaseComponent.scaleFactor * 6.0 - Notifications.SHARE.method2091();
        if (isMouseWithinBounds(
                mouseX,
                mouseY,
                var19,
                itemY + itemHeight * 0.5 - Notifications.DELETE.method1614() * 0.5,
                Notifications.DELETE.method2091(),
                Notifications.DELETE.method1614()
        )) {
            return this.method1701(var18);
        } else if (isMouseWithinBounds(
                mouseX,
                mouseY,
                var21,
                itemY + itemHeight * 0.5 - Notifications.SHARE.method1614() * 0.5,
                Notifications.SHARE.method2091(),
                Notifications.SHARE.method1614()
        )) {
            this.method1800(var18);
            return true;
        } else if (!var18.equals(this.field1463.getValue())) {
            Class1201.method2381(this.field1463, var18);
            this.field1463.setValue(var18);
            return true;
        } else {
            return false;
        }
    }

    private boolean method1701(String string) {
        if (string.equals(this.field1463.getValue())) {
            return false;
        }
        this.field1465.remove(string);
        this.field1463.field968.remove(string);
        BozeExecutor.method2200(() -> CurrentProfileSettingComponent.lambda$deleteProfile$0(string));
        return true;
    }

    private void method1800(String string) {
        String string2 = string.substring(string.lastIndexOf(46) + 1);
        BozeExecutor.method2200(() -> this.lambda$shareProfile$1(string2, string));
    }

    private void method1337(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            String var5 = this.field1463.field969 + var1;
            if (!this.field1465.contains(var5)) {
                this.field1465.add(var5);
                this.field1463.field968.add(var5);
                this.field1463.setValue(var5);
                this.field1427 = "";
                BozeExecutor.method2200(() -> this.lambda$addNewProfile$2(var5));
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }
    }

    @Override
    protected void method1904() {
        this.method1337(this.field1427);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void method583(char c) {
        super.method583(c);
    }

    private void method646(String var1) {
        ChatInstance.method740("Profiles - " + this.field1387, var1);
    }

    private void method647(String var1) {
        ChatInstance.method743("Profiles - " + this.field1387, var1);
    }

    private void lambda$addNewProfile$2(String var1) {
        Class1201.method2382(true, this.field1463, var1);
    }

    private void lambda$shareProfile$1(String var1, String var2) {
        try {
            NbtCompound var6 = new NbtCompound();
            var6.putString("name", var1);
            var6.putString("prefix", this.field1463.field969);
            var6.putString("version", Version.tag);
            var6.putLong("timestamp", System.currentTimeMillis());
            NbtCompound var7 = ConfigManager.downloadConfig(var2, ConfigType.PROFILE);
            NbtCompound var8 = new NbtCompound();
            var8.put("v2.info", var6);
            var8.put("v2.profile", var7);
            String var9 = ConfigManager.publishConfig(var2, var8, "PR");
            if (var9 != null && !var9.isEmpty()) {
                this.method646("Profile shared: " + var1);
                this.method646("Use command to load: " + Options.method1563() + "load " + var9);
                GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), var9);
            } else {
                this.method647("Error sharing profile: " + var1);
            }
        } catch (Exception var10) {
            this.method647("Error sharing profile: " + var1);
            var10.printStackTrace();
        }
    }
}
