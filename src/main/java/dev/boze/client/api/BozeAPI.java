package dev.boze.client.api;

import dev.boze.api.Globals;
import dev.boze.api.event.EventGrim.Interact;
import dev.boze.client.Boze;
import dev.boze.client.manager.ConfigManager;
import mapped.Class28;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Field;

public class BozeAPI {
    public static void method951() {
        method952("addonDir", ConfigManager.addons);
    }

    public static void init() {
        method953(Interact.class, "INSTANCE", new Class28());
    }

    private static void method952(String var0, Object var1) {
        try {
            Field var4 = Globals.class.getDeclaredField(var0);
            var4.setAccessible(true);
            var4.set(null, var1);
        } catch (IllegalAccessException | NoSuchFieldException var5) {
            Boze.LOG.error("Failed to initialize Boze globals, exiting");
            MinecraftClient.getInstance().stop();
        }
    }

    private static void method953(Class<?> var0, String var1, Object var2) {
        try {
            Field var5 = var0.getDeclaredField(var1);
            var5.setAccessible(true);
            var5.set(null, var2);
        } catch (IllegalAccessException | NoSuchFieldException var6) {
            Boze.LOG.error("Failed to initialize Boze globals, exiting");
            MinecraftClient.getInstance().stop();
        }
    }
}
