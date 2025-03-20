package dev.boze.client.gui.components;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.gui.DrawContext;

public abstract class InputBaseComponent implements IMinecraft {
    public static double field1131 = 1.2;
    public String field1132;
    public double field1133;
    public double field1134;
    public double field1135;
    public double field1136;

    public InputBaseComponent(String var1, double var2, double var4, double var6, double var8) {
        this.field1132 = var1;
        this.field1133 = var2;
        this.field1134 = var4;
        this.field1135 = var6;
        this.field1136 = var8;
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
