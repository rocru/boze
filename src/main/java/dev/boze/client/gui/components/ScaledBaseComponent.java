package dev.boze.client.gui.components;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.gui.DrawContext;

public abstract class ScaledBaseComponent implements IMinecraft {
    public String field1387;
    public double field1388;
    public double field1389;
    public double field1390;
    public double field1391;

    public ScaledBaseComponent(String name, double width, double height) {
        this.field1387 = name;
        this.field1390 = Math.min(width * 1280.0 * BaseComponent.scaleFactor, mc.getWindow().getScaledWidth());
        this.field1391 = Math.min(height * 720.0 * BaseComponent.scaleFactor, mc.getWindow().getScaledHeight());
        this.field1388 = (double) mc.getWindow().getScaledWidth() * 0.5 - this.field1390 * 0.5;
        this.field1389 = (double) mc.getWindow().getScaledHeight() * 0.5 - this.field1391 * 0.5;
    }

    public ScaledBaseComponent(String name, double width, double height, boolean absolute) {
        this.field1387 = name;
        this.field1390 = width;
        this.field1391 = height;
        this.field1388 = (double) mc.getWindow().getScaledWidth() * 0.5 - this.field1390 * 0.5;
        this.field1389 = (double) mc.getWindow().getScaledHeight() * 0.5 - this.field1391 * 0.5;
    }

    public static boolean isMouseWithinBounds(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public boolean isMouseOver(double mouseX, double mouseY, int button) {
        return false;
    }

    public void onMouseClicked(double mouseX, double mouseY, int button) {
    }

    public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public void method583(char c) {
    }

    public void method2142() {
    }
}
