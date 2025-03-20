package dev.boze.client.gui.components.setting;

import dev.boze.api.setting.SettingToggle;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.SettingBaseComponent;
import dev.boze.client.jumptable.hO;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class SettingToggleComponent extends SettingBaseComponent implements IMinecraft {
    private final SettingToggle field1239;
    private double field1240 = 0.0;

    public SettingToggleComponent(SettingToggle setting, BaseComponent parent, double x, double y, double width, double height) {
        super(setting, parent, x, y, width, height);
        this.field1239 = setting;
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.field321 = (double) Theme.method1376() * scaleFactor;
        super.render(context, mouseX, mouseY, delta);
        RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field395 = Theme.method1348());
        IFontRender.method499()
                .drawShadowedText(
                        this.field316,
                        this.field318 + 6.0 * scaleFactor,
                        this.field319 + this.field321 / 2.0 - IFontRender.method499().method1390() / 2.0,
                        Theme.method1350()
                );
        this.field1240 = 0.0;
        switch (hO.field2099[Gui.INSTANCE.field2371.getValue().ordinal()]) {
            case 1:
                RenderUtil.field3963
                        .method2257(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field1240 - this.field321 * 1.2,
                                this.field319 + this.field321 * 0.2,
                                this.field321 * 1.2,
                                this.field321 * 0.6,
                                15,
                                12,
                                this.field321 * 0.8,
                                Theme.method1348().method183(Theme.method1390())
                        );
                RenderUtil.field3963
                        .method2261(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field1240 - (this.field1239.getValue() ? this.field321 * 0.5 : this.field321 * 1.1),
                                this.field319 + this.field321 * 0.3,
                                this.field321 * 0.4,
                                Theme.method1350()
                        );
                break;
            case 2:
                RenderUtil.field3963
                        .method2261(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field1240 - this.field321 * 0.6,
                                this.field319 + this.field321 * 0.2,
                                this.field321 * 0.6,
                                Theme.method1348().method183(Theme.method1390())
                        );
                if (this.field1239.getValue()) {
                    RenderUtil.field3963
                            .method2261(
                                    this.field318 + this.field320 - 6.0 * scaleFactor - this.field1240 - this.field321 * 0.45,
                                    this.field319 + this.field321 * 0.35,
                                    this.field321 * 0.3,
                                    Theme.method1350()
                            );
                }
                break;
            case 3:
                RenderUtil.field3963
                        .method2242(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field1240 - this.field321 * 0.3,
                                this.field319 + this.field321 * 0.5,
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field1240 - this.field321 * 0.15,
                                this.field319 + this.field321 * 0.8,
                                !this.field1239.getValue() ? Theme.method1348().method183(Theme.method1390()) : Theme.method1352()
                        );
                RenderUtil.field3963
                        .method2242(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field1240 - this.field321 * 0.15,
                                this.field319 + this.field321 * 0.8,
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field1240,
                                this.field319 + this.field321 * 0.2,
                                !this.field1239.getValue() ? Theme.method1348().method183(Theme.method1390()) : Theme.method1352()
                        );
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321) && button == 0) {
            this.field396.reset();
            this.field1239.setValue(!this.field1239.getValue());
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
