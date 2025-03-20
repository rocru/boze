package dev.boze.client.utils.player;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MinecraftUtils;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

public class InventoryUtil implements IMinecraft {
    public static boolean isInventoryOpen() {
        return !MinecraftUtils.isClientActive() ? false : mc.currentScreen instanceof HandledScreen;
    }
}
