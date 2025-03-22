package mapped;

import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;

public class Class3076 {
    private static Module field170;
    private static int field171;
    private static float field172;
    private static boolean field173;

    static {
        Class3076.field173 = false;
    }

    public Class3076() {
        super();
    }

    public static void method6024(final Module module, final int priority, final float timerSpeed) {
        if (module == Class3076.field170) {
            Class3076.field171 = priority;
            Class3076.field172 = timerSpeed;
            Class3076.field173 = true;
        } else if (priority > Class3076.field171 || !Class3076.field173) {
            Class3076.field170 = module;
            Class3076.field171 = priority;
            Class3076.field172 = timerSpeed;
            Class3076.field173 = true;
        }
    }

    public static void method6025(final Module module) {
        if (Class3076.field170 == module) {
            method6026();
        }
    }

    public static void method6026() {
        Class3076.field170 = null;
        Class3076.field171 = 0;
        Class3076.field172 = 1.0f;
        Class3076.field173 = false;
    }

    public static float method6027() {
        if (!MinecraftUtils.isClientReadyForSinglePlayer()) {
            Class3076.field173 = false;
        }
        return Class3076.field173 ? Class3076.field172 : 1.0f;
    }
}
