package dev.boze.client.gui.components.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.slider.doubles.fm;
import dev.boze.client.gui.components.slider.doubles.fn;
import dev.boze.client.gui.components.slider.ints.fU;
import dev.boze.client.gui.components.slider.ints.fV;
import dev.boze.client.gui.components.text.e3;
import dev.boze.client.gui.components.text.e4;
import dev.boze.client.gui.components.toggle.ToggleShaderSettingComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.ShaderSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class ShaderSettingComponent extends ScaledBaseComponent {
    private static final double field1472 = 2.0;
    private static final double field1473 = 4.0;
    private static final double field1474 = 8.0;
    public final ShaderSetting field1479;
    private final ColorPanel field1475;
    private final ColorPanel field1476;
    private final ArrayList<InputBaseComponent> field1477 = new ArrayList();
    private final ShaderSetting field1478;

    public ShaderSettingComponent(ShaderSetting original) {
        super(original.name, 328.0 * BaseComponent.scaleFactor, method2091() * 3.0 + 396.0 * BaseComponent.scaleFactor, true);
        this.field1478 = original;
        this.field1479 = original.method460();
        double var4 = this.field1388 + 2.0 * BaseComponent.scaleFactor;
        double var6 = this.field1389 + 8.0 * BaseComponent.scaleFactor;
        double var8 = method2091();
        var6 += var8;
        this.field1475 = new ColorPanel("Fill", this.field1479.method457(), var4, var6, 324.0 * BaseComponent.scaleFactor, 148.0 * BaseComponent.scaleFactor);
        var6 += this.field1475.field1136 + 8.0 * BaseComponent.scaleFactor + var8;
        this.field1476 = new ColorPanel("Outline", this.field1479.method458(), var4, var6, 324.0 * BaseComponent.scaleFactor, 148.0 * BaseComponent.scaleFactor);
        var6 += this.field1476.field1136 + var8 + 12.0 * BaseComponent.scaleFactor;
        double var10 = (this.field1390 - 24.0 * BaseComponent.scaleFactor) / 3.0;
        var4 = this.field1388 + 8.0 * BaseComponent.scaleFactor;
        this.field1477.add(new ToggleShaderSettingComponent(this, "Fast Render", var4, var6, var10, 14.0 * BaseComponent.scaleFactor));
        var4 += var10 + 4.0 * BaseComponent.scaleFactor;
        this.field1477.add(new fU(this, "Radius", var4, var6, var10, 14.0 * BaseComponent.scaleFactor));
        var4 += var10 + 4.0 * BaseComponent.scaleFactor;
        this.field1477.add(new fV(this, "Blur", var4, var6, var10, 14.0 * BaseComponent.scaleFactor));
        var4 = this.field1388 + 8.0 * BaseComponent.scaleFactor;
        var6 += 14.0 * BaseComponent.scaleFactor + 4.0 * BaseComponent.scaleFactor;
        double var12 = (this.field1390 - 20.0 * BaseComponent.scaleFactor) / 2.0;
        this.field1477.add(new fm(this, "Glow Size", var4, var6, var12, 14.0 * BaseComponent.scaleFactor));
        var4 += var12 + 4.0 * BaseComponent.scaleFactor;
        this.field1477.add(new fn(this, "Max Glow", var4, var6, var12, 14.0 * BaseComponent.scaleFactor));
        var4 = this.field1388 + 8.0 * BaseComponent.scaleFactor;
        var6 += 14.0 * BaseComponent.scaleFactor + 8.0 * BaseComponent.scaleFactor;
        double var14 = (this.field1390 - 24.0 * BaseComponent.scaleFactor) / 2.0;
        this.field1477.add(new e3(this, "Cancel", var4, var6, var14, 24.0 * BaseComponent.scaleFactor));
        var4 += 8.0 * BaseComponent.scaleFactor + var14;
        this.field1477.add(new e4(this, "Apply", var4, var6, var14, 24.0 * BaseComponent.scaleFactor, original));
    }

    private static double method2091() {
        IFontRender.method499().startBuilding(BaseComponent.scaleFactor, true);
        double var0 = IFontRender.method499().method1390();
        IFontRender.method499().endBuilding();
        return var0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        RenderUtil.field3963.method2233();
        IFontRender.method499().startBuilding(BaseComponent.scaleFactor);
        RenderUtil.field3963
                .method2257(
                        this.field1388,
                        this.field1389,
                        this.field1390,
                        this.field1391,
                        15,
                        24,
                        Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
                        Theme.method1349()
                );
        if (Theme.method1382()) {
            ClickGUI.field1335
                    .field1333
                    .method2257(
                            this.field1388,
                            this.field1389,
                            this.field1390,
                            this.field1391,
                            15,
                            24,
                            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
                            RGBAColor.field402
                    );
        }

        IFontRender.method499()
                .drawShadowedText(
                        "Fill",
                        this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Fill") * 0.5,
                        this.field1389 + 8.0 * BaseComponent.scaleFactor,
                        Theme.method1350()
                );
        IFontRender.method499()
                .drawShadowedText(
                        "Outline",
                        this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Outline") * 0.5,
                        this.field1389 + this.field1475.field1136 + 8.0 * BaseComponent.scaleFactor * 2.0 + IFontRender.method499().method1390(),
                        Theme.method1350()
                );
        IFontRender.method499()
                .drawShadowedText(
                        "Shader",
                        this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Shader") * 0.5,
                        this.field1389
                                + this.field1475.field1136
                                + this.field1476.field1136
                                + 8.0 * BaseComponent.scaleFactor * 3.0
                                + IFontRender.method499().method1390() * 2.0,
                        Theme.method1350()
                );
        RenderUtil.field3963.method2235(context);
        IFontRender.method499().endBuilding();
        this.field1475.render(context, mouseX, mouseY, delta);
        this.field1476.render(context, mouseX, mouseY, delta);
        RenderUtil.field3963.method2233();
        IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5);

        for (InputBaseComponent var9 : this.field1477) {
            var9.render(context, mouseX, mouseY, delta);
        }

        RenderUtil.field3963.method2235(context);
        IFontRender.method499().endBuilding();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY, int button) {
        if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
            return false;
        } else if (this.field1475.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else if (this.field1476.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            for (InputBaseComponent var10 : this.field1477) {
                if (var10.mouseClicked(mouseX, mouseY, button)) {
                    break;
                }
            }

            return true;
        }
    }

    @Override
    public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
            return false;
        } else if (this.field1475.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        } else if (this.field1476.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        } else {
            for (InputBaseComponent var14 : this.field1477) {
                if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                    break;
                }
            }

            return true;
        }
    }

    @Override
    public void method2142() {
        super.method2142();
        this.field1478.method459(this.field1479);
    }
}
