package mapped;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.KeyBindingUtils;
import dev.boze.client.utils.misc.CursorType;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class Class5928 implements IMinecraft {
   private static final boolean[] field44 = new boolean[512];
   private static final boolean[] field45 = new boolean[16];
   private static CursorType field46 = CursorType.Normal;

   public static void method1738(int key, boolean pressed) {
      if (var5949 >= 0 && var5949 < field44.length) {
         field44[var5949] = var5950;
      }
   }

   public static void method107(int button, boolean pressed) {
      if (var113 >= 0 && var113 < field45.length) {
         field45[var113] = var114;
      }
   }

   public static void method288(KeyBinding bind, boolean pressed) {
      method1738(KeyBindingUtils.getKeyCode(var5951), var5952);
   }

   public static boolean method109(KeyBinding bind) {
      return method159(KeyBindingUtils.getKeyCode(var117));
   }

   public static boolean method159(int key) {
      if (Class3077.field174) {
         return false;
      } else {
         return var5953 == -1 ? false : var5953 < field44.length && field44[var5953];
      }
   }

   public static boolean method518(int button) {
      return var5954 == -1 ? false : var5954 < field45.length && field45[var5954];
   }

   public static void method112(CursorType style) {
      if (field46 != var120) {
         GLFW.glfwSetCursor(mc.getWindow().getHandle(), var120.method826());
         field46 = var120;
      }
   }
}
