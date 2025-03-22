package dev.boze.client.gui.screens;

import dev.boze.client.font.IFontRender;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class BindSelectorScreen extends Screen implements IMinecraft {
    private static final String field2082 = "Please press a key or mouse button to bind";
    private final Module field2081;

    public BindSelectorScreen(Module module) {
        super(Text.literal("Bind Selector"));
        this.field2081 = module;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        IFontRender.method499().startBuilding(1.5);
        IFontRender.method499()
                .drawShadowedText(
                        "Please press a key or mouse button to bind",
                        (double) mc.getWindow().getScaledWidth() * 0.5 - IFontRender.method499().method501("Please press a key or mouse button to bind") * 0.5,
                        (double) mc.getWindow().getScaledHeight() * 0.5 - IFontRender.method499().method1390() * 0.5,
                        Theme.method1350()
                );
        IFontRender.method499().endBuilding(context);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.field2081.bind.set(Bind.create());
        } else {
            this.field2081.bind.set(true, keyCode);
        }

        this.close();
        return true;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.field2081.bind.set(false, button);
        this.close();
        return true;
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }
}
