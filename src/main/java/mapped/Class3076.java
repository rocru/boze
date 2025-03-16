package mapped;

import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;

public class Class3076 {
   private static Module field170;
   private static int field171;
   private static float field172;
   private static boolean field173 = false;

   public static void method6024(Module module, int priority, float timerSpeed) {
      if (var3221 == field170) {
         field171 = var3222;
         field172 = var3223;
         field173 = true;
      } else if (var3222 > field171 || !field173) {
         field170 = var3221;
         field171 = var3222;
         field172 = var3223;
         field173 = true;
      }
   }

   public static void method6025(Module module) {
      if (field170 == var3224) {
         method6026();
      }
   }

   public static void method6026() {
      field170 = null;
      field171 = 0;
      field172 = 1.0F;
      field173 = false;
   }

   public static float method6027() {
      if (!MinecraftUtils.isClientReadyForSinglePlayer()) {
         field173 = false;
      }

      return field173 ? field172 : 1.0F;
   }
}
