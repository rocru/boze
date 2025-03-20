package dev.boze.client.utils.render;

import dev.boze.client.utils.IMinecraft;
import org.lwjgl.opengl.GL11;

public class Scissor implements IMinecraft {
    public static void enableScissor(double x, double y, double width, double height) {
        GL11.glEnable(3089);
        GL11.glScissor(
                (int) Math.round(x * mc.getWindow().getScaleFactor()),
                (int) Math.round((double) mc.getWindow().getFramebufferHeight() - (y + height) * mc.getWindow().getScaleFactor()),
                (int) Math.round(width * mc.getWindow().getScaleFactor()),
                (int) Math.round(height * mc.getWindow().getScaleFactor())
        );
    }

    public static void disableScissor() {
        GL11.glDisable(3089);
    }
}
