package dev.boze.client.gui.components.setting.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.WeirdColorSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class WeirdColorSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
    private final WeirdColorSetting field299;
    private double field300 = 0.0;

    public WeirdColorSettingComponent(WeirdColorSetting setting, BaseComponent parent, double x, double y, double width, double height) {
        super(setting, parent, x, y, width, height);
        this.field299 = setting;
        this.field300 = height;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.field299.method2116()) {
            this.field321 = (double) Theme.method1376() * scaleFactor;
            this.field300 = this.field321;
            super.render(context, mouseX, mouseY, delta);
            RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field272 = Theme.method1348());
            IFontRender.method499()
                    .drawShadowedText(
                            this.field316,
                            this.field318 + 6.0 * scaleFactor,
                            this.field319 + this.field300 / 2.0 - IFontRender.method499().method1390() / 2.0,
                            Theme.method1350()
                    );
            double var8 = 0.0;
            if (this.field299.hasChildren()) {
                double var10 = this.field300 / 9.0;
                double var12 = this.field318 + this.field320 - scaleFactor * 6.0 - var10;
                if (this.field299.isExpanded()) {
                    RenderUtil.field3963.method2257(var12, this.field319 + var10 * 2.0, var10, var10 * 5.0, 15, 12, var10 / 2.0, Theme.method1350());
                } else {
                    RenderUtil.field3963.method2261(var12, this.field319 + var10 * 2.0, var10, Theme.method1350());
                    RenderUtil.field3963.method2261(var12, this.field319 + var10 * 4.0, var10, Theme.method1350());
                    RenderUtil.field3963.method2261(var12, this.field319 + var10 * 6.0, var10, Theme.method1350());
                }

                var8 = scaleFactor * 6.0 + var10 / 2.0;
            }

            RenderUtil.field3963
                    .method2241(
                            this.field318 + this.field320 - scaleFactor * 12.0 - var8,
                            this.field319 + this.field300 * 0.5 - scaleFactor * 3.0,
                            scaleFactor * 6.0,
                            scaleFactor * 6.0,
                            this.field299.getValue().field3910.method208(),
                            this.field299.getValue().field3911
                    );
        } else {
            this.field321 = 0.0;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.field299.method2116() && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field300)) {
            if (button == 0) {
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ClickGUI.field1335.method580(new dev.boze.client.gui.components.scaled.bottomrow.WeirdColorSettingComponent(this.field299));
                return true;
            }

            if (button == 1 && this.field299.hasChildren()) {
                this.field299.setExpanded(!this.field299.isExpanded());
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
