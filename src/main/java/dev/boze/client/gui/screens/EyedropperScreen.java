package dev.boze.client.gui.screens;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.gui.components.scaled.RGBASettingComponent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.renderer.GL;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class EyedropperScreen extends Screen implements IMinecraft {
    private boolean field1052;
    private int field1053;

    public EyedropperScreen() {
        super(Text.literal("Eyedropper"));
    }

    public void init() {
        this.field1052 = mc.options.hudHidden;
        mc.options.hudHidden = true;
        this.field1053 = mc.options.getGuiScale().getValue();
        mc.options.getGuiScale().setValue(1);
        mc.getWindow().setScaleFactor(mc.getWindow().calculateScaleFactor(1, mc.forcesUnicodeFont()));
    }

    public void close() {
        mc.options.hudHidden = this.field1052;
        mc.options.getGuiScale().setValue(this.field1053);
        mc.getWindow().setScaleFactor(mc.getWindow().calculateScaleFactor(this.field1053, mc.forcesUnicodeFont()));
        super.close();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Color var9 = GL.method1213((int) mouseX, (int) mouseY);
        ChatInstance.method624(String.format("Copied color {r: %d, g: %d, b: %d}", var9.getRed(), var9.getGreen(), var9.getBlue()));
        GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), String.format("{r: %d, g: %d, b: %d}", var9.getRed(), var9.getGreen(), var9.getBlue()));
        ColorSettingComponent.field1396 = new BozeDrawColor(var9.getRed(), var9.getGreen(), var9.getBlue(), 255);
        RGBASettingComponent.field1482 = new RGBAColor(var9.getRed(), var9.getGreen(), var9.getBlue(), 255);
        this.close();
        return true;
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }
}
