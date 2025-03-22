package dev.boze.client.renderer;

import dev.boze.client.renderer.Mesh.Attrib;
import net.minecraft.client.util.math.MatrixStack;

public class QuadRenderer {
    private static final MatrixStack field2165 = new MatrixStack();
    private static Mesh field2164;

    public static void initialize() {
        field2164 = new Mesh(DrawMode.Triangles, Attrib.Vec2);
        field2164.method2142();
        field2164.method1214(
                field2164.method711(-1.0, -1.0).method2010(),
                field2164.method711(-1.0, 1.0).method2010(),
                field2164.method711(1.0, 1.0).method2010(),
                field2164.method711(1.0, -1.0).method2010()
        );
        field2164.method1198();
    }

    public static void render() {
        field2164.method718(field2165);
    }

    public static void method1215() {
        field2164.method720(field2165);
    }

    public static void method1216() {
        field2164.method1904();
    }
}
