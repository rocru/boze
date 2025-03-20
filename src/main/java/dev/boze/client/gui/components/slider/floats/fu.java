package dev.boze.client.gui.components.slider.floats;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.FloatSliderComponent;
import dev.boze.client.gui.components.scaled.bottomrow.EditGradientColorComponent;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.render.color.GradientColor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public class fu extends FloatSliderComponent {
    final GradientColor field1149;
    final EditGradientColorComponent field1150;

    public fu(EditGradientColorComponent var1, String var2, double var3, double var5, double var7, double var9, GradientColor var11) {
        super(var2, var3, var5, var7, var9);
        this.field1150 = var1;
        this.field1149 = var11;
    }

    @Override
    protected void method207(float value) {
        this.field1149.field426 = value;
    }

    @Override
    protected float method1384() {
        return this.field1149.field426;
    }

    @Override
    protected float method1385() {
        return 0.0F;
    }

    @Override
    protected float method215() {
        return 1.0F;
    }

    @Override
    protected float method520() {
        return 0.01F;
    }

    @Override
    protected void method2142() {
        this.field1149.field426 = 0.0F;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        IFontRender.method499()
                .drawShadowedText(
                        this.field1132,
                        this.field1133 + 6.0 * field1131,
                        this.field1134 + this.field1136 * 0.33 - IFontRender.method499().method1390() / 2.0,
                        Theme.method1350()
                );
        String var7 = String.format("%.0f", this.method1384() * 360.0F);
        IFontRender.method499()
                .drawShadowedText(
                        var7,
                        this.field1133 + this.field1135 - 6.0 * field1131 - IFontRender.method499().method501(var7),
                        this.field1134 + this.field1136 * 0.25 - IFontRender.method499().method1390() / 2.0,
                        Theme.method1350()
                );
        IFontRender.method499()
                .drawShadowedText(
                        "-", this.field1133 + 6.0 * field1131, this.field1134 + this.field1136 * 0.7 - IFontRender.method499().method1390() / 2.0, Theme.method1350()
                );
        IFontRender.method499()
                .drawShadowedText(
                        "+",
                        this.field1133 + this.field1135 - 6.0 * field1131 - IFontRender.method499().method501("+"),
                        this.field1134 + this.field1136 * 0.7 - IFontRender.method499().method1390() / 2.0,
                        Theme.method1350()
                );
        RGBAColor var8 = Theme.method1352();
        RenderUtil.field3963
                .method2257(
                        this.field1133 + 12.0 * field1131,
                        this.field1134 + this.field1136 * 0.75 - field1131,
                        this.field1135 - 24.0 * field1131,
                        field1131 * 2.0,
                        15,
                        12,
                        field1131,
                        var8.method2025(Theme.method1391())
                );
        RenderUtil.field3963
                .method2257(
                        this.field1133 + 12.0 * field1131,
                        this.field1134 + this.field1136 * 0.75 - field1131,
                        (this.field1135 - 26.0 * field1131)
                                * (double) MathHelper.clamp((this.method1384() - this.method1385()) / (this.method215() - this.method1385()), 0.0F, 1.0F)
                                + field1131 * 2.0,
                        field1131 * 2.0,
                        15,
                        12,
                        field1131,
                        var8
                );
    }
}
