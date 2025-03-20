package dev.boze.client.utils;

public class MinecraftUtils implements IMinecraft {
    public static boolean isClientActive() {
        return mc != null && mc.player != null && mc.world != null;
    }

    public static boolean isSinglePlayer() {
        return mc.isOnThread();
    }

    public static boolean isClientReadyForSinglePlayer() {
        return mc != null && mc.player != null && mc.world != null && mc.isOnThread();
    }
}
