package dev.boze.client.gui.components;

import net.minecraft.client.gui.DrawContext;

public abstract class BaseComponent {
    public static double scaleFactor = -1.0;
    public String field316;
    public final BaseComponent field317;
    public double field318;
    public double field319;
    public double field320;
    public double field321;

    public BaseComponent(String var1, BaseComponent var2, double var3, double var5, double var7, double var9) {
        this.field316 = var1;
        this.field317 = var2;
        this.field318 = var3;
        this.field319 = var5;
        this.field320 = var7;
        this.field321 = var9;
    }

    public void render(DrawContext var1, int var2, int var3, float var4) {
    }

    public boolean mouseClicked(double var1, double var3, int var5) {
        return false;
    }

    public void onMouseClicked(double var1, double var3, int var5) {
    }

    public boolean onDrag(double var1, double var3, int var5, double var6, double var8) {
        return false;
    }

    public boolean onMouseScroll(double var1, double var3, double var5) {
        return false;
    }

    public boolean keyPressed(int var1, int var2, int var3) {
        return false;
    }

    public boolean keyReleased(int var1, int var2, int var3) {
        return false;
    }

    public static boolean isMouseWithinBounds(double var0, double var2, double var4, double var6, double var8, double var10) {
        return var0 >= var4 && var0 <= var4 + var8 && var2 >= var6 && var2 <= var6 + var10;
    }
}
