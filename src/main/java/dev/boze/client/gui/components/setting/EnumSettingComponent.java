package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class EnumSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
    private final EnumSetting field311;

    public EnumSettingComponent(EnumSetting setting, BaseComponent parent, double x, double y, double width, double height) {
        super(setting, parent, x, y, width, height);
        this.field311 = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.field311.method2116()) {
            this.field321 = (double) Theme.method1376() * scaleFactor;
            super.render(context, mouseX, mouseY, delta);
            boolean var8 = this.field321 <= 22.0 * scaleFactor;
            RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field272 = Theme.method1348());
            IFontRender.method499()
                    .drawShadowedText(
                            this.field316,
                            this.field318 + 6.0 * scaleFactor,
                            this.field319 + this.field321 * (var8 ? 0.5 : 0.25) - IFontRender.method499().method1390() / 2.0,
                            Theme.method1350()
                    );
            double var9 = 0.0;
            if (this.field311.hasChildren()) {
                double var11 = this.field321 / (var8 ? 9.0 : 18.0);
                double var13 = this.field318 + this.field320 - scaleFactor * 6.0 - var11;
                if (this.field311.isExpanded()) {
                    RenderUtil.field3963.method2257(var13, this.field319 + var11 * 2.0, var11, var11 * 5.0, 15, 12, var11 / 2.0, Theme.method1350());
                } else {
                    RenderUtil.field3963.method2261(var13, this.field319 + var11 * 2.0, var11, Theme.method1350());
                    RenderUtil.field3963.method2261(var13, this.field319 + var11 * 4.0, var11, Theme.method1350());
                    RenderUtil.field3963.method2261(var13, this.field319 + var11 * 6.0, var11, Theme.method1350());
                }

                var9 = scaleFactor * 6.0 + var11 / 2.0;
            }

            byte var16 = 6;
            double var12 = var8 ? this.field318 + 6.0 * scaleFactor + IFontRender.method499().method501(this.field316) : this.field318;
            double var14 = var8 ? this.field320 - (6.0 * scaleFactor + IFontRender.method499().method501(this.field316) + var9) : this.field320;
            if (Gui.INSTANCE.field2372.getValue()) {
                RenderUtil.field3963
                        .method2257(
                                var12 + 6.0 * scaleFactor,
                                this.field319 + this.field321 * (var8 ? 0.0 : 0.5) + scaleFactor,
                                var14 - 12.0 * scaleFactor,
                                this.field321 * (var8 ? 1.0 : 0.5) - scaleFactor * 2.0,
                                15,
                                24,
                                scaleFactor * 4.0,
                                Theme.method1348().method183(Theme.method1390())
                        );
                var16 = 10;
            }

            IFontRender.method499()
                    .drawShadowedText(
                            "<",
                            var12 + (double) var16 * scaleFactor,
                            this.field319 + this.field321 * (var8 ? 0.5 : 0.75) - IFontRender.method499().method1390() / 2.0,
                            Theme.method1352()
                    );
            IFontRender.method499()
                    .drawShadowedText(
                            this.field311.getValue().name(),
                            var12 + var14 * 0.5 - IFontRender.method499().method501(this.field311.getValue().name()) * 0.5,
                            this.field319 + this.field321 * (var8 ? 0.5 : 0.75) - IFontRender.method499().method1390() / 2.0,
                            Theme.method1350()
                    );
            IFontRender.method499()
                    .drawShadowedText(
                            ">",
                            var12 + var14 - (double) var16 * scaleFactor - IFontRender.method499().method501(">"),
                            this.field319 + this.field321 * (var8 ? 0.5 : 0.75) - IFontRender.method499().method1390() / 2.0,
                            Theme.method1352()
                    );
        } else {
            this.field321 = 0.0;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.field311.method2116() && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
            if (button == 0) {
                this.field273.reset();
                int var9 = this.field311.method464(this.field311.getValue().toString());
                if (mouseX >= this.field318 + this.field320 * 0.5) {
                    if (this.method159(var9 + 1)) {
                        this.field311.setValue(((Enum[]) this.field311.getValue().getClass().getEnumConstants())[var9 + 1]);
                    } else {
                        this.field311.setValue(((Enum[]) this.field311.getValue().getClass().getEnumConstants())[0]);
                    }
                } else if (this.method159(var9 - 1)) {
                    this.field311.setValue(((Enum[]) this.field311.getValue().getClass().getEnumConstants())[var9 - 1]);
                } else {
                    this.field311
                            .setValue(
                                    ((Enum[]) this.field311.getValue().getClass().getEnumConstants())[this.field311.getValue().getClass().getEnumConstants().length
                                            - 1]
                            );
                }

                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }

            if (button == 1 && this.field311.hasChildren()) {
                this.field273.reset();
                this.field311.setExpanded(!this.field311.isExpanded());
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean method159(int var1) {
        return var1 <= this.field311.getValue().getClass().getEnumConstants().length - 1 && var1 >= 0;
    }
}
