package dev.boze.client.api;

import dev.boze.api.render.DrawColor;
import dev.boze.api.render.Drawer2D;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public class BozeDrawer2D implements Drawer2D {
    private static BozeDrawer2D field1848 = null;

    public static BozeDrawer2D method967() {
        if (field1848 == null) {
            field1848 = new BozeDrawer2D();
        }

        return field1848;
    }

    public void startDrawing() {
        RenderUtil.field3965.method2233();
    }

    public void stopDrawing(DrawContext context) {
        RenderUtil.field3965.method2235(context);
    }

    public void quad(double x, double y, double width, double height, DrawColor topLeft, DrawColor topRight, DrawColor bottomRight, DrawColor bottomLeft) {
        RenderUtil.field3965
                .method2249(x, y, width, height, (BozeDrawColor) topLeft, (BozeDrawColor) topRight, (BozeDrawColor) bottomRight, (BozeDrawColor) bottomLeft);
    }
}
