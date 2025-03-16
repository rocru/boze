package dev.boze.client.font;

import dev.boze.client.utils.ColorWrapper;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.client.gui.DrawContext;

public interface IFontRender {
    default IFontRender method499() {
        return FontManager.method1106() ? FontManager.method1105() : FontManager.method1102();
    }

    default IFontRender method500(boolean custom) {
        return custom ? FontManager.method1102() : FontRenderer.field1075;
    }

    void updateScale(double var1);

    void startBuilding(double var1, boolean var3, boolean var4);

    default void startBuilding(double scale, boolean scaleOnly) {
        this.startBuilding(scale, scaleOnly, false);
    }

    default void startBuilding(double scale) {
        this.startBuilding(scale, false, false);
    }

    default void startBuilding() {
        this.startBuilding(1.0, false, false);
    }

    default void startBuilding2() {
        this.startBuilding(1.0, false, true);
    }

    double getFontScale();

    void setFontScale(double var1);

    double measureTextWidth(String var1, int var2, boolean var3);

    default double measureTextHeight(String text, boolean shadow) {
        return this.measureTextWidth(text, text.length(), shadow);
    }

    default double method501(String text) {
        return this.measureTextWidth(text, text.length(), false);
    }

    double method502(boolean var1);

    default double method1390() {
        return this.method502(false);
    }

    double drawText(String var1, double var2, double var4, ColorWrapper var6);

    double drawShadowedText(String var1, double var2, double var4, RGBAColor var6, boolean var7);

    default double drawShadowedText(String text, double x, double y, RGBAColor color) {
        return this.drawShadowedText(text, x, y, color, FontLoader.field1062);
    }

    boolean method2114();

    default void endBuilding() {
        this.endBuilding(null);
    }

    void endBuilding(DrawContext var1);
}
