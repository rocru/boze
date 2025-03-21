package dev.boze.client.events;

import dev.boze.client.enums.PlayerOverlay;

public class PlayerOverlayEvent extends CancelableEvent {
    private static final PlayerOverlayEvent INSTANCE = new PlayerOverlayEvent();
    public PlayerOverlay overlay;

    public static PlayerOverlayEvent method1080(PlayerOverlay type) {
        INSTANCE.overlay = type;
        INSTANCE.method1021(false);
        return INSTANCE;
    }
}
