package dev.boze.client.events;

import dev.boze.client.enums.KeyAction;

public class MouseButtonEvent extends CancelableEvent {
    private static final MouseButtonEvent INSTANCE = new MouseButtonEvent();
    public int button;
    public KeyAction action;

    public static MouseButtonEvent method1038(int button, KeyAction action) {
        INSTANCE.method1021(false);
        INSTANCE.button = button;
        INSTANCE.action = action;
        return INSTANCE;
    }
}
