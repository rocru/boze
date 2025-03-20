package dev.boze.client.gui.components;

import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public abstract class FloatArraySliderComponent extends InputBaseComponent {
    public FloatArraySliderComponent(String name, double x, double y, double width, double height) {
        super(name, x, y, width, height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        float[] var7 = this.method111();
        RenderUtil.field3966
                .method2255(
                        this.field1133 + field1131,
                        this.field1134 + field1131,
                        this.field1135 - field1131 * 2.0,
                        this.field1136 - field1131 * 2.0,
                        new float[]{var7[0], 0.0F, 1.0F, 1.0F},
                        new float[]{var7[0], 1.0F, 1.0F, 1.0F},
                        new float[]{var7[0], 1.0F, 0.0F, 1.0F},
                        new float[]{var7[0], 0.0F, 0.0F, 1.0F}
                );
        RenderUtil.field3966
                .method2263(
                        this.field1133 + (double) var7[1] * (this.field1135 - field1131 * 2.0),
                        this.field1134 + (double) (1.0F - var7[2]) * (this.field1136 - field1131 * 2.0),
                        field1131 * 2.0,
                        Color.RGBtoHSB(Theme.method1347().field408, Theme.method1347().field409, Theme.method1347().field410, null)
                );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseWithinBounds(
                mouseX, mouseY, this.field1133 + field1131, this.field1134 + field1131, this.field1135 - field1131 * 2.0, this.field1136 - field1131 * 2.0
        )) {
            if (button == 0) {
                double var9 = mouseX - (this.field1133 + field1131);
                double var11 = mouseY - (this.field1134 + field1131);
                double var13 = var9 / (this.field1135 - field1131 * 2.0);
                double var15 = var11 / (this.field1136 - field1131 * 2.0);
                this.method521(this.method111()[0], (float) var13, 1.0F - (float) var15);
            } else if (button == 1) {
                this.method521(this.method111()[0], 1.0F, 1.0F);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isMouseWithinBounds(
                mouseX, mouseY, this.field1133 + field1131, this.field1134 + field1131, this.field1135 - field1131 * 2.0, this.field1136 - field1131 * 2.0
        )) {
            if (button == 0) {
                double var13 = mouseX - (this.field1133 + field1131);
                double var15 = mouseY - (this.field1134 + field1131);
                double var17 = var13 / (this.field1135 - field1131 * 2.0);
                double var19 = var15 / (this.field1136 - field1131 * 2.0);
                this.method521(this.method111()[0], (float) var17, 1.0F - (float) var19);
            } else if (button == 1) {
                this.method521(this.method111()[0], 1.0F, 1.0F);
            }

            return true;
        } else {
            return false;
        }
    }

    protected abstract float[] method111();

    protected abstract void method521(float var1, float var2, float var3);
}
