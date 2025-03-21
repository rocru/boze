package dev.boze.client.events;

import net.minecraft.client.gui.DrawContext;

public class Render2DEvent {
    private static final Render2DEvent field1946 = new Render2DEvent();
    public DrawContext field1947;
    public int scaledWidth;
    public int scaledHeight;
    public float tickDelta;

    public static Render2DEvent method1093(DrawContext drawContext, int screenWidth, int screenHeight, float tickDelta) {
        field1946.field1947 = drawContext;
        field1946.scaledWidth = screenWidth;
        field1946.scaledHeight = screenHeight;
        field1946.tickDelta = tickDelta;
        return field1946;
    }
}
