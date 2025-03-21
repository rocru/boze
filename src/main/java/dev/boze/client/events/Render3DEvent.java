package dev.boze.client.events;

import dev.boze.client.renderer.Renderer3D;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent {
    private static final Render3DEvent field1948 = new Render3DEvent();
    private static Render3DEvent field1949 = null;
    public MatrixStack matrix;
    public Camera camera;
    public Renderer3D field1950;
    public float field1951;
    public double cameraX;
    public double cameraY;
    public double cameraZ;

    public static Render3DEvent method1094(
            MatrixStack matrices, Camera camera, Renderer3D renderer, float tickDelta, double offsetX, double offsetY, double offsetZ
    ) {
        if (field1949 == null) {
            try {
                Render3DEvent.class.getClassLoader().loadClass("dev.boze.client.MixinPlugin");
                field1949 = new Render3DEvent();
            } catch (Exception var14) {
                field1949 = field1948;
            }
        }

        field1948.matrix = matrices;
        field1948.camera = camera;
        field1948.field1950 = renderer;
        field1949.field1951 = tickDelta;
        field1948.cameraX = offsetX;
        field1948.cameraY = offsetY;
        field1948.cameraZ = offsetZ;
        return field1948;
    }
}
