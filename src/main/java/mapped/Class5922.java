package mapped;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.settings.generic.ScalingSetting;
import dev.boze.client.systems.modules.render.Zoom;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.*;

public class Class5922 implements IMinecraft {
    private static final Vector4f field22;
    private static final Vector4f field23;
    private static final Vector4f field24;
    private static final Vector3d field25;
    private static final Vector3d field26;
    private static final Matrix4f field27;
    private static final Matrix4f field28;
    private static int field29;
    public static double field30;

    public Class5922() {
        super();
    }

    public static void method58(final Matrix4f modelView) {
        Class5922.field27.set(modelView);
        Class5922.field28.set(RenderSystem.getProjectionMatrix());
        Class3062.method5989(Class5922.field25, Class5922.mc.gameRenderer.getCamera().getPos());
        Class5922.field26.set(Class5922.field25);
        Class5922.field26.negate();
        Class5922.field29 = Class5922.mc.getWindow().calculateScaleFactor(Class5922.mc.options.getGuiScale().getValue(), Class5922.mc.forcesUnicodeFont());
    }

    public static boolean method59(final Vector3d pos, final ScalingSetting mode) {
        final double distance = Class5922.field25.distance(pos);
        double s;
        if (mode.method1304() == ScalingSetting.ScalingMode.Const) {
            s = MathHelper.clamp(1.0 - distance * 0.01, 0.5, 2.147483647E9) * mode.method1303();
        } else {
            s = MathHelper.clamp(1.0 / distance, mode.getMinValue(), mode.getMaxValue()) * mode.method1303() * 3.0;
        }
        if (Zoom.INSTANCE.isEnabled()) {
            s *= Zoom.INSTANCE.method2091();
        }
        return method60(pos, s);
    }

    public static boolean method60(final Vector3d pos, final double s) {
        Class5922.field30 = s;
        Class5922.field22.set(Class5922.field26.x + pos.x, Class5922.field26.y + pos.y, Class5922.field26.z + pos.z, 1.0);
        Class5922.field22.mul(Class5922.field27, Class5922.field23);
        Class5922.field23.mul(Class5922.field28, Class5922.field24);
        if (Class5922.field24.w <= 0.0f) {
            return false;
        }
        method66(Class5922.field24);
        final double v = Class5922.field24.x * Class5922.mc.getWindow().getFramebufferWidth();
        final double v2 = Class5922.field24.y * Class5922.mc.getWindow().getFramebufferHeight();
        if (Double.isInfinite(v) || Double.isInfinite(v2)) {
            return false;
        }
        pos.set(v / Class5922.field29, (Class5922.mc.getWindow().getFramebufferHeight() - v2) / Class5922.field29, Class5922.field24.z);
        return true;
    }

    public static void method61(final Vector3d pos) {
        method63(RenderSystem.getModelViewStack(), pos);
    }

    public static void method62(final Vector3d pos, final DrawContext drawContext) {
        method61(pos);
        final MatrixStack matrices = drawContext.getMatrices();
        matrices.push();
        matrices.translate((float) pos.x, (float) pos.y, 0.0f);
        matrices.scale((float) Class5922.field30, (float) Class5922.field30, 1.0f);
    }

    private static void method63(final Matrix4fStack matrix4fStack, final Vector3d vector3d) {
        matrix4fStack.pushMatrix();
        matrix4fStack.translate((float) vector3d.x, (float) vector3d.y, 0.0f);
        matrix4fStack.scale((float) Class5922.field30, (float) Class5922.field30, 1.0f);
    }

    public static void method2142() {
        RenderSystem.getModelViewStack().popMatrix();
    }

    public static void method332(final DrawContext drawContext) {
        method2142();
        drawContext.getMatrices().pop();
    }

    private static void method66(final Vector4f vector4f) {
        final float w = 1.0f / vector4f.w * 0.5f;
        vector4f.x = vector4f.x * w + 0.5f;
        vector4f.y = vector4f.y * w + 0.5f;
        vector4f.z = vector4f.z * w + 0.5f;
        vector4f.w = w;
    }

    static {
        field22 = new Vector4f();
        field23 = new Vector4f();
        field24 = new Vector4f();
        field25 = new Vector3d();
        field26 = new Vector3d();
        field27 = new Matrix4f();
        field28 = new Matrix4f();
    }
}
