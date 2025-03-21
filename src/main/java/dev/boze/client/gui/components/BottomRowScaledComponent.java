package dev.boze.client.gui.components;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.render.Scissor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public abstract class BottomRowScaledComponent extends ScaledBaseComponent {
    private double field1419;
    private double field1420;
    private double field1421;
    private int field1422;
    private final BottomRow field1423;
    private final int field1424;
    private Runnable field1425;
    private final boolean field1426;
    protected String field1427;

    public BottomRowScaledComponent(final String name, final BottomRow bottomRow, final double widthRatio, final double heightRatio) {
        this(name, bottomRow, widthRatio, heightRatio, 0);
    }

    public BottomRowScaledComponent(final String name, final BottomRow bottomRow, final double widthRatio, final double heightRatio, final int extraComponents) {
        super(name, 0.0, 0.0);
        this.field1422 = 0;
        this.field1425 = null;
        this.field1427 = "";
        this.field1423 = bottomRow;
        this.field1426 = (bottomRow == BottomRow.TextAddClose);
        this.field1424 = extraComponents;
        final double n = heightRatio * 720.0 * BaseComponent.scaleFactor;
        final double n2 = (extraComponents > 0) ? (extraComponents * (Theme.method1376() * BaseComponent.scaleFactor + BaseComponent.scaleFactor * 6.0) + BaseComponent.scaleFactor * 6.0 * extraComponents) : 0.0;
        this.field1390 = Math.min(widthRatio * 1280.0 * BaseComponent.scaleFactor, BottomRowScaledComponent.mc.getWindow().getScaledWidth());
        this.field1391 = Math.min(n + n2, BottomRowScaledComponent.mc.getWindow().getScaledHeight());
        this.field1388 = BottomRowScaledComponent.mc.getWindow().getScaledWidth() * 0.5 - this.field1390 * 0.5;
        this.field1389 = BottomRowScaledComponent.mc.getWindow().getScaledHeight() * 0.5 - this.field1391 * 0.5;
    }

    protected void method1416() {
        this.field1422 = MathHelper.clamp(this.field1422, 0, MathHelper.clamp(this.method2010() - 12, 0, this.method2010()));
    }

    @Override
    public void render(final DrawContext context, final int mouseX, final int mouseY, final float delta) {
        RenderUtil.field3963.method2233();
        IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5);
        IconManager.setScale(BaseComponent.scaleFactor * 0.4);
        RenderUtil.field3963.method2257(this.field1388, this.field1389, this.field1390, this.field1391, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 6.0) : 0.0, Theme.method1349());
        if (Theme.method1382()) {
            ClickGUI.field1335.field1333.method2257(this.field1388, this.field1389, this.field1390, this.field1391, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 6.0) : 0.0, RGBAColor.field402);
        }
        IFontRender.method499().drawShadowedText(this.field1387, this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501(this.field1387) * 0.5, this.field1389 + BaseComponent.scaleFactor * 6.0, Theme.method1350());
        this.field1419 = this.field1389 + BaseComponent.scaleFactor * 12.0 + IFontRender.method499().method1390();
        this.field1420 = this.field1391 - IFontRender.method499().method1390() * 2.0 - BaseComponent.scaleFactor * 30.0 - ((this.field1424 > 0) ? (this.field1424 * (Theme.method1376() * BaseComponent.scaleFactor + BaseComponent.scaleFactor * 2.0) + BaseComponent.scaleFactor * 6.0 * this.field1424) : 0.0);
        this.field1421 = (this.field1420 - BaseComponent.scaleFactor * 34.0) / 12.0;
        RenderUtil.field3963.method2257(this.field1388 + BaseComponent.scaleFactor * 12.0, this.field1419, this.field1390 - BaseComponent.scaleFactor * 24.0, this.field1420, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 6.0) : 0.0, Theme.method1348());
        this.method1416();
        final int method2010 = this.method2010();
        for (int i = 0; i < Math.min(method2010, 12); ++i) {
            this.method639(context, i + this.field1422, this.field1388 + BaseComponent.scaleFactor * 18.0, this.field1419 + i * (this.field1421 + BaseComponent.scaleFactor * 2.0) + BaseComponent.scaleFactor * 6.0, this.field1390 - BaseComponent.scaleFactor * 36.0, this.field1421);
        }
        if (this.field1424 > 0) {
            this.method641(context, this.field1419 + this.field1420 + BaseComponent.scaleFactor * 6.0, this.field1421);
        }
        this.method1964(mouseX, mouseY);
        RenderUtil.field3963.method2235(context);
        IFontRender.method499().endBuilding();
        IconManager.method1115();
        for (int j = 0; j < Math.min(method2010, 12); ++j) {
            this.method640(context, j + this.field1422, this.field1388 + BaseComponent.scaleFactor * 18.0, this.field1419 + j * (this.field1421 + BaseComponent.scaleFactor * 2.0) + BaseComponent.scaleFactor * 6.0, this.field1390 - BaseComponent.scaleFactor * 36.0, this.field1421);
        }
        this.method332(context);
        if (this.field1425 != null) {
            this.field1425.run();
            this.field1425 = null;
        }
    }

    public void method635(final Runnable callback) {
        this.field1425 = callback;
    }

    private void method1964(final int n, final int n2) {
        switch (this.field1423.ordinal()) {
            case 0:
                this.method1198();
                break;
            case 1:
                this.method1190(n, n2);
                break;
        }
    }

    private void method332(final DrawContext drawContext) {
        if (this.field1423.ordinal() == 1) {
            this.method295(drawContext);
        }
    }

    protected void method1198() {
        final double n = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
        final double n2 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
        final double n3 = this.field1389 + this.field1391 - n2 - BaseComponent.scaleFactor * 6.0;
        final double n4 = this.field1388 + this.field1390 * 0.25 - n * 0.5;
        RenderUtil.field3963.method2257(n4, n3, n, n2, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 6.0) : 0.0, Theme.method1347());
        IFontRender.method499().drawShadowedText("Add", n4 + n * 0.5 - IFontRender.method499().method501("Add") * 0.5, n3 + n2 * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
        final double n5 = this.field1388 + this.field1390 * 0.75 - n * 0.5;
        RenderUtil.field3963.method2257(n5, n3, n, n2, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 6.0) : 0.0, Theme.method1347());
        IFontRender.method499().drawShadowedText("Close", n5 + n * 0.5 - IFontRender.method499().method501("Close") * 0.5, n3 + n2 * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
    }

    protected void handleAddAndCloseClick(final double mouseX, final double mouseY) {
        final double n = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
        final double n2 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
        final double n3 = this.field1389 + this.field1391 - n2 - BaseComponent.scaleFactor * 6.0;
        if (ScaledBaseComponent.isMouseWithinBounds(mouseX, mouseY, this.field1388 + this.field1390 * 0.25 - n * 0.5, n3, n, n2)) {
            BottomRowScaledComponent.mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            this.method1904();
            return;
        }
        if (ScaledBaseComponent.isMouseWithinBounds(mouseX, mouseY, this.field1388 + this.field1390 * 0.75 - n * 0.5, n3, n, n2)) {
            BottomRowScaledComponent.mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            ClickGUI.field1335.method580(null);
        }
    }

    private void method295(final DrawContext drawContext) {
        RenderUtil.field3963.method2233();
        IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5);
        final double n = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
        final double height = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
        final double y = this.field1389 + this.field1391 - height - BaseComponent.scaleFactor * 6.0;
        final double x = this.field1388 + BaseComponent.scaleFactor * 12.0;
        final double width = this.field1390 - n * 2.0 - BaseComponent.scaleFactor * 36.0;
        Scissor.enableScissor(x, y, width, height);
        RenderUtil.field3963.method2257(x, y, width, height, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 6.0) : 0.0, Theme.method1348());
        String substring = this.field1427;
        double x2 = x + BaseComponent.scaleFactor * 4.0;
        final double method501 = IFontRender.method499().method501(substring);
        if (method501 > width - BaseComponent.scaleFactor * 4.0) {
            x2 -= method501 - (width - BaseComponent.scaleFactor * 4.0);
        }
        if (System.currentTimeMillis() % 1000L >= 500L) {
            substring = substring.substring(0, substring.length() - 1);
        }
        IFontRender.method499().drawShadowedText(substring, x2, y + height * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
        RenderUtil.field3963.method2235(drawContext);
        IFontRender.method499().endBuilding();
        Scissor.disableScissor();
    }

    private void method1190(final int n, final int n2) {
        final double n3 = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
        final double n4 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
        final double n5 = this.field1389 + this.field1391 - n4 - BaseComponent.scaleFactor * 6.0;
        final double n6 = this.field1388 + BaseComponent.scaleFactor * 12.0 + (this.field1390 - n3 * 2.0 - BaseComponent.scaleFactor * 36.0) + BaseComponent.scaleFactor * 6.0;
        RenderUtil.field3963.method2257(n6, n5, n3, n4, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 6.0) : 0.0, Theme.method1347());
        IFontRender.method499().drawShadowedText("Add", n6 + n3 * 0.5 - IFontRender.method499().method501("Add") * 0.5, n5 + n4 * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
        final double n7 = n6 + n3 + BaseComponent.scaleFactor * 6.0;
        RenderUtil.field3963.method2257(n7, n5, n3, n4, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 6.0) : 0.0, Theme.method1347());
        IFontRender.method499().drawShadowedText("Close", n7 + n3 * 0.5 - IFontRender.method499().method501("Close") * 0.5, n5 + n4 * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
    }

    private void method637(final double n, final double n2) {
        final double n3 = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
        final double n4 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
        final double n5 = this.field1389 + this.field1391 - n4 - BaseComponent.scaleFactor * 6.0;
        final double x = this.field1388 + BaseComponent.scaleFactor * 12.0 + (this.field1390 - n3 * 2.0 - BaseComponent.scaleFactor * 36.0) + BaseComponent.scaleFactor * 6.0;
        if (ScaledBaseComponent.isMouseWithinBounds(n, n2, x, n5, n3, n4)) {
            this.method1904();
            BottomRowScaledComponent.mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (ScaledBaseComponent.isMouseWithinBounds(n, n2, x + n3 + BaseComponent.scaleFactor * 6.0, n5, n3, n4)) {
            BottomRowScaledComponent.mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            ClickGUI.field1335.method580(null);
        }
    }

    @Override
    public boolean isMouseOver(final double mouseX, final double mouseY, final int button) {
        if (ScaledBaseComponent.isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
            final IFontRender method499 = IFontRender.method499();
            method499.startBuilding(BaseComponent.scaleFactor * 0.5, true);
            IconManager.applyScale(BaseComponent.scaleFactor * 0.4);
            this.method638(mouseX, mouseY, button);
            method499.endBuilding();
            IconManager.method1115();
            return true;
        }
        return super.isMouseOver(mouseX, mouseY, button);
    }

    protected void method638(final double mouseX, final double mouseY, final int button) {
        switch (this.field1423.ordinal()) {
            case 0:
                this.handleAddAndCloseClick(mouseX, mouseY);
                break;
            case 1:
                this.method637(mouseX, mouseY);
                break;
        }
        if (ScaledBaseComponent.isMouseWithinBounds(mouseX, mouseY, this.field1388 + BaseComponent.scaleFactor * 12.0, this.field1419, this.field1390 - BaseComponent.scaleFactor * 24.0, this.field1420)) {
            for (int i = 0; i < Math.min(this.method2010(), 12); ++i) {
                final double y = this.field1419 + i * (this.field1421 + BaseComponent.scaleFactor * 2.0) + BaseComponent.scaleFactor * 6.0;
                final double x = this.field1388 + BaseComponent.scaleFactor * 18.0;
                final double width = this.field1390 - BaseComponent.scaleFactor * 36.0;
                if (ScaledBaseComponent.isMouseWithinBounds(mouseX, mouseY, x, y, width, this.field1421)) {
                    if (this.handleItemClick(i + this.field1422, button, x, y, width, this.field1421, mouseX, mouseY)) {
                        BottomRowScaledComponent.mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                    }
                    return;
                }
            }
        }
    }

    @Override
    public boolean onMouseScroll(final double mouseX, final double mouseY, final double amount) {
        if (ScaledBaseComponent.isMouseWithinBounds(mouseX, mouseY, this.field1388 + BaseComponent.scaleFactor * 12.0, this.field1419, this.field1390 - BaseComponent.scaleFactor * 24.0, this.field1420)) {
            this.field1422 = (int) MathHelper.clamp(this.field1422 - amount, 0.0, MathHelper.clamp(this.method2010() - 12, 0, this.method2010()));
            return true;
        }
        return super.onMouseScroll(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (this.field1426) {
            return this.method2077(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean method2077(final int keyCode, final int scanCode, final int modifiers) {
        if (!this.field1426) {
            return false;
        }
        if (keyCode == 257) {
            this.method1904();
            this.field1427 = "";
            return true;
        }
        if (keyCode == 259 && this.field1427.length() > 0) {
            this.field1427 = this.field1427.substring(0, this.field1427.length() - 1);
            return true;
        }
        return false;
    }

    @Override
    public void method583(final char c) {
        if (!this.field1426) {
            return;
        }
        if ((this.field1427.length() < 18 && c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-') {
            this.field1427 += c;
        }
    }

    protected abstract int method2010();

    protected abstract void method639(final DrawContext p0, final int p1, final double p2, final double p3, final double p4, final double p5);

    protected void method640(final DrawContext context, final int index, final double itemX, final double itemY, final double itemWidth, final double itemHeight) {
    }

    protected abstract boolean handleItemClick(final int p0, final int p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7);

    protected abstract void method1904();

    protected void method641(final DrawContext context, final double startY, final double height) {
    }
}
