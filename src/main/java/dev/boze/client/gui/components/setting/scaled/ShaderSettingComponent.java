package dev.boze.client.gui.components.setting.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.ShaderSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class ShaderSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
    private final ShaderSetting field310;

    public ShaderSettingComponent(ShaderSetting setting, BaseComponent parent, double x, double y, double width, double height) {
        super(setting, parent, x, y, width, height);
        this.field310 = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.field310.method2116()) {
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
            RenderUtil.field3965
                    .method2253(
                            this.field318 + this.field320 - scaleFactor * 24.0,
                            this.field319 + this.field321 * 0.5 - scaleFactor * 3.0,
                            scaleFactor * 6.0,
                            scaleFactor * 6.0,
                            this.field310.method457().method1362()
                    );
            RenderUtil.field3965
                    .method2253(
                            this.field318 + this.field320 - scaleFactor * 12.0,
                            this.field319 + this.field321 * 0.5 - scaleFactor * 3.0,
                            scaleFactor * 6.0,
                            scaleFactor * 6.0,
                            this.field310.method458().method1362()
                    );
        } else {
            this.field321 = 0.0;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.field310.method2116() && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321) && button == 0) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new dev.boze.client.gui.components.scaled.ShaderSettingComponent(this.field310));
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
