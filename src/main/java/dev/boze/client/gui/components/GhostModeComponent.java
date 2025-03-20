package dev.boze.client.gui.components;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.jumptable.hR;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class GhostModeComponent extends BaseComponent implements IMinecraft {
    public GhostModeComponent(double x, double y, double width, double height) {
        super("Ghost Mode", null, x, y, width, height);
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        RenderUtil.field3963.method2233();
        IFontRender.method499().startBuilding(scaleFactor * 0.5);
        RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, Theme.method1348());
        IFontRender.method499()
                .drawShadowedText(
                        this.field316,
                        this.field318 + 6.0 * scaleFactor,
                        this.field319 + this.field321 / 2.0 - IFontRender.method499().method1390() / 2.0,
                        Theme.method1350()
                );
        if (Theme.method1382()) {
            ClickGUI.field1335.field1333.method2252(this.field318, this.field319, this.field320, this.field321, RGBAColor.field402);
        }

        switch (hR.field2102[Gui.INSTANCE.field2371.getValue().ordinal()]) {
            case 1:
                RenderUtil.field3963
                        .method2257(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field321 * 1.2,
                                this.field319 + this.field321 * 0.2,
                                this.field321 * 1.2,
                                this.field321 * 0.6,
                                15,
                                12,
                                this.field321 * 0.8,
                                Options.INSTANCE.method1971() ? Theme.method1352() : Theme.method1352().method2025(Theme.method1391())
                        );
                RenderUtil.field3963
                        .method2261(
                                this.field318 + this.field320 - 6.0 * scaleFactor - (Options.INSTANCE.method1971() ? this.field321 * 0.5 : this.field321 * 1.1),
                                this.field319 + this.field321 * 0.3,
                                this.field321 * 0.4,
                                Theme.method1350()
                        );
                break;
            case 2:
                RenderUtil.field3963
                        .method2261(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field321 * 0.6,
                                this.field319 + this.field321 * 0.2,
                                this.field321 * 0.6,
                                Theme.method1348().method183(Theme.method1390())
                        );
                if (Options.INSTANCE.method1971()) {
                    RenderUtil.field3963
                            .method2261(
                                    this.field318 + this.field320 - 6.0 * scaleFactor - this.field321 * 0.45,
                                    this.field319 + this.field321 * 0.35,
                                    this.field321 * 0.3,
                                    Theme.method1350()
                            );
                }
                break;
            case 3:
                RenderUtil.field3963
                        .method2242(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field321 * 0.3,
                                this.field319 + this.field321 * 0.5,
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field321 * 0.15,
                                this.field319 + this.field321 * 0.8,
                                !Options.INSTANCE.method1971() ? Theme.method1348().method183(Theme.method1390()) : Theme.method1352()
                        );
                RenderUtil.field3963
                        .method2242(
                                this.field318 + this.field320 - 6.0 * scaleFactor - this.field321 * 0.15,
                                this.field319 + this.field321 * 0.8,
                                this.field318 + this.field320 - 6.0 * scaleFactor,
                                this.field319 + this.field321 * 0.2,
                                !Options.INSTANCE.method1971() ? Theme.method1348().method183(Theme.method1390()) : Theme.method1352()
                        );
        }

        RenderUtil.field3963.method2235(context);
        IFontRender.method499().endBuilding(context);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321) && button == 0) {
            Options.INSTANCE.method478().setValue(!Options.INSTANCE.method478().getValue());
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
