package dev.boze.client.api;

import dev.boze.api.render.DrawColor;
import dev.boze.api.render.Drawer3D;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.renderer.Renderer3D;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

public class BozeDrawer3D implements Drawer3D {
    private static BozeDrawer3D field1849 = null;
    private final Renderer3D field1850 = new Renderer3D();

    public static BozeDrawer3D method968() {
        if (field1849 == null) {
            field1849 = new BozeDrawer3D();
        }

        return field1849;
    }

    public void startDrawing() {
        this.field1850.method1217();
    }

    public void stopDrawing(MatrixStack matrices) {
        this.field1850.method1219(matrices);
    }

    public void line(double x1, double y1, double z1, double x2, double y2, double z2, DrawColor color1, DrawColor color2) {
        this.field1850.method1240(x1, y1, z1, x2, y2, z2, (BozeDrawColor) color1, (BozeDrawColor) color2);
    }

    public void quad(
            double x1,
            double y1,
            double z1,
            double x2,
            double y2,
            double z2,
            double x3,
            double y3,
            double z3,
            double x4,
            double y4,
            double z4,
            DrawColor topLeft,
            DrawColor topRight,
            DrawColor bottomRight,
            DrawColor bottomLeft
    ) {
        this.field1850
                .method1250(
                        x1,
                        y1,
                        z1,
                        x2,
                        y2,
                        z2,
                        x3,
                        y3,
                        z3,
                        x4,
                        y4,
                        z4,
                        (BozeDrawColor) topLeft,
                        (BozeDrawColor) topRight,
                        (BozeDrawColor) bottomRight,
                        (BozeDrawColor) bottomLeft
                );
    }

    public void box(Box box, DrawColor color) {
        this.field1850.method1273(box, (BozeDrawColor) color, (BozeDrawColor) color, ShapeMode.Triangles, 0);
    }

    public void outlinedBox(Box box, DrawColor quadColor, DrawColor lineColor) {
        this.field1850.method1273(box, (BozeDrawColor) quadColor, (BozeDrawColor) lineColor, ShapeMode.Full, 0);
    }
}
