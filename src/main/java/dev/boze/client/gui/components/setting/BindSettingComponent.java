package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.KeyboardUtil;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class3077;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class BindSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
    private final BindSetting field269;
    private boolean field270 = false;

    public BindSettingComponent(BindSetting setting, BaseComponent parent, double x, double y, double width, double height) {
        super(setting, parent, x, y, width, height);
        this.field269 = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.field269.method2116()) {
            this.field321 = (double) Theme.method1376() * scaleFactor;
            super.render(context, mouseX, mouseY, delta);
            RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field272 = Theme.method1348());
            IFontRender.method499()
                    .drawShadowedText(
                            this.field316,
                            this.field318 + 6.0 * scaleFactor,
                            this.field319 + this.field321 / 2.0 - IFontRender.method499().method1390() / 2.0,
                            Theme.method1350()
                    );
            double var8 = 0.0;
            if (this.field269.hasChildren()) {
                double var10 = this.field321 / 9.0;
                double var12 = this.field318 + this.field320 - scaleFactor * 6.0 - var10;
                if (this.field269.isExpanded()) {
                    RenderUtil.field3963.method2257(var12, this.field319 + var10 * 2.0, var10, var10 * 5.0, 15, 12, var10 / 2.0, Theme.method1350());
                } else {
                    RenderUtil.field3963.method2261(var12, this.field319 + var10 * 2.0, var10, Theme.method1350());
                    RenderUtil.field3963.method2261(var12, this.field319 + var10 * 4.0, var10, Theme.method1350());
                    RenderUtil.field3963.method2261(var12, this.field319 + var10 * 6.0, var10, Theme.method1350());
                }

                var8 = scaleFactor * 6.0 + var10 / 2.0;
            }

            String var17;
            if (this.field270) {
                var17 = "...";
            } else if (this.field269.getValue().getBind() == -1) {
                var17 = " ";
            } else {
                var17 = this.field269.getValue().isKey()
                        ? KeyboardUtil.getKeyName(this.field269.getValue().getBind())
                        : KeyboardUtil.getButtonName(this.field269.getValue().getBind());
            }

            double var11 = this.field269.getValue().getBind() == -1
                    ? IFontRender.method499().method1390() - scaleFactor * 2.0
                    : IFontRender.method499().method501(var17);
            double var13 = IFontRender.method499().method1390();
            double var15 = Math.max(var11, var13);
            RenderUtil.field3963
                    .method2257(
                            this.field318 + this.field320 - var8 - (var15 + scaleFactor * (double) (var15 > var11 ? 2 : 4)) - scaleFactor * 6.0,
                            this.field319 + this.field321 * 0.5 - (var13 * 0.5 + scaleFactor),
                            var15 + scaleFactor * (double) (var15 > var11 ? 2 : 4),
                            var13 + scaleFactor * 2.0,
                            15,
                            24,
                            scaleFactor * 4.0,
                            Theme.method1348().method183(Theme.method1390())
                    );
            IFontRender.method499()
                    .drawShadowedText(
                            var17,
                            this.field318 + this.field320 - var8 - (var15 + scaleFactor * (double) (var15 > var11 ? 2 : 4)) * 0.5 - scaleFactor * 6.0 - var11 / 2.0,
                            this.field319 + this.field321 * 0.5 - var13 * 0.5,
                            Theme.method1350()
                    );
            if (isMouseWithinBounds(
                    mouseX,
                    mouseY,
                    this.field318 + this.field320 - var8 - (var15 + scaleFactor * (double) (var15 > var11 ? 2 : 4)) - scaleFactor * 6.0,
                    this.field319 + this.field321 * 0.5 - (var13 * 0.5 + scaleFactor),
                    var15 + scaleFactor * (double) (var15 > var11 ? 2 : 4),
                    var13 + scaleFactor * 2.0
            )) {
                ClickGUI.field1335.field1337 = CursorType.IBeam;
            }
        } else {
            this.field321 = 0.0;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.field269.method2116() && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
            if (this.field270) {
                this.field269.getValue().set(false, button);
                this.field270 = false;
                Class3077.field174 = false;
                return true;
            }

            if (button == 0 && !Class3077.field174) {
                this.field270 = true;
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }

            if (button == 1 && this.field269.hasChildren()) {
                this.field269.setExpanded(!this.field269.isExpanded());
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.field270) {
            if (keyCode == 256) {
                this.field269.setValue(Bind.create());
            } else {
                this.field269.getValue().set(true, keyCode);
            }

            this.field270 = false;
            Class3077.field174 = false;
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }
}
