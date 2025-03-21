package dev.boze.client.instances;

import dev.boze.api.internal.Instances;
import dev.boze.client.Boze;
import dev.boze.client.instances.impl.*;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Field;

public class BozeInstances {
    public static void method1127() {
        method1128("capes", new CapesInstance());
        method1128("chat", new ChatInstance());
        method1128("friends", new FriendsInstance());
        method1128("input", new InputInstance());
        method1128("interaction", new InteractionInstance());
        method1128("json", new JsonInstance());
        method1128("modules", new ModulesInstance());
        method1128("render", new RenderInstance());
    }

    private static void method1128(String var0, Object var1) {
        try {
            Field var4 = Instances.class.getDeclaredField(var0);
            var4.setAccessible(true);
            var4.set(null, var1);
        } catch (IllegalAccessException | NoSuchFieldException var5) {
            Boze.LOG.error("Failed to initialize Boze instances, exiting");
            MinecraftClient.getInstance().stop();
        }
    }
}
