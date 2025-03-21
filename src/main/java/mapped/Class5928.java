package mapped;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.KeyBindingUtils;
import dev.boze.client.utils.misc.CursorType;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class Class5928 implements IMinecraft {
    private static final boolean[] field44;
    private static final boolean[] field45;
    private static CursorType field46;

    public Class5928() {
        super();
    }

    public static void method1738(final int key, final boolean pressed) {
        if (key >= 0 && key < Class5928.field44.length) {
            Class5928.field44[key] = pressed;
        }
    }

    public static void method107(final int button, final boolean pressed) {
        if (button >= 0 && button < Class5928.field45.length) {
            Class5928.field45[button] = pressed;
        }
    }

    public static void method288(final KeyBinding bind, final boolean pressed) {
        method1738(KeyBindingUtils.getKeyCode(bind), pressed);
    }

    public static boolean method109(final KeyBinding bind) {
        return method159(KeyBindingUtils.getKeyCode(bind));
    }

    public static boolean method159(final int key) {
        return !Class3077.field174 && key != -1 && key < Class5928.field44.length && Class5928.field44[key];
    }

    public static boolean method518(final int button) {
        return button != -1 && button < Class5928.field45.length && Class5928.field45[button];
    }

    public static void method112(final CursorType style) {
        if (Class5928.field46 != style) {
            GLFW.glfwSetCursor(Class5928.mc.getWindow().getHandle(), style.method826());
            Class5928.field46 = style;
        }
    }

    static {
        field44 = new boolean[512];
        field45 = new boolean[16];
        Class5928.field46 = CursorType.Normal;
    }
}
