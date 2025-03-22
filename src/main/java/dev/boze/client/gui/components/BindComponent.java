package dev.boze.client.gui.components;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.KeyboardUtil;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class3077;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public abstract class BindComponent extends InputBaseComponent {
    private boolean field1137 = false;

    public BindComponent(String name, double x, double y, double width, double height) {
        super(name, x, y, width, height);
    }

    @Override
    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        IFontRender.method499()
                .drawShadowedText(
                        this.field1132,
                        this.field1133 + 6.0 * field1131,
                        this.field1134 + this.field1136 / 2.0 - IFontRender.method499().method1390() / 2.0,
                        Theme.method1350()
                );
        String var8;
        if (this.field1137) {
            var8 = "...";
        } else if (this.getBind().getBind() == -1) {
            var8 = " ";
        } else {
            var8 = this.getBind().isKey() ? KeyboardUtil.getKeyName(this.getBind().getBind()) : KeyboardUtil.getButtonName(this.getBind().getBind());
        }

        double var9 = this.getBind().getBind() == -1 ? IFontRender.method499().method1390() - field1131 * 2.0 : IFontRender.method499().method501(var8);
        double var11 = IFontRender.method499().method1390();
        double var13 = Math.max(var9, var11);
        RenderUtil.field3963
                .method2257(
                        this.field1133 + this.field1135 - (var13 + field1131 * (double) (var13 > var9 ? 2 : 4)) - field1131 * 6.0,
                        this.field1134 + this.field1136 * 0.5 - (var11 * 0.5 + field1131),
                        var13 + field1131 * (double) (var13 > var9 ? 2 : 4),
                        var11 + field1131 * 2.0,
                        15,
                        24,
                        field1131 * 4.0,
                        Theme.method1348().method183(Theme.method1390())
                );
        IFontRender.method499()
                .drawShadowedText(
                        var8,
                        this.field1133 + this.field1135 - (var13 + field1131 * (double) (var13 > var9 ? 2 : 4)) * 0.5 - field1131 * 6.0 - var9 / 2.0,
                        this.field1134 + this.field1136 * 0.5 - var11 * 0.5,
                        Theme.method1350()
                );
        if (isMouseWithinBounds(
                mouseX,
                mouseY,
                this.field1133 + this.field1135 - (var13 + field1131 * (double) (var13 > var9 ? 2 : 4)) - field1131 * 6.0,
                this.field1134 + this.field1136 * 0.5 - (var11 * 0.5 + field1131),
                var13 + field1131 * (double) (var13 > var9 ? 2 : 4),
                var11 + field1131 * 2.0
        )) {
            ClickGUI.field1335.field1337 = CursorType.IBeam;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseWithinBounds(mouseX, mouseY, this.field1133, this.field1134, this.field1135, this.field1136)) {
            if (this.field1137) {
                Bind var9 = this.getBind();
                var9.set(false, button);
                this.setBind(var9);
                this.field1137 = false;
                Class3077.field174 = false;
                return true;
            }

            if (button == 0 && !Class3077.field174) {
                this.field1137 = true;
                Class3077.field174 = true;
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.field1137) {
            if (keyCode == 256) {
                this.setBind(Bind.create());
            } else {
                Bind var7 = this.getBind();
                var7.set(true, keyCode);
                this.setBind(var7);
            }

            this.field1137 = false;
            Class3077.field174 = false;
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public boolean method2114() {
        return this.field1137;
    }

    public void method2142() {
        this.field1137 = false;
        Class3077.field174 = false;
    }

    protected abstract Bind getBind();

    protected abstract void setBind(Bind var1);
}
