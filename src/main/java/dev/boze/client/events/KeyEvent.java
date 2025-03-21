package dev.boze.client.events;

import dev.boze.client.enums.KeyAction;

public class KeyEvent extends CancelableEvent {
    private static final KeyEvent INSTANCE = new KeyEvent();
    public int key;
    public int modifiers;
    public KeyAction action;

    public static KeyEvent method1065(int var0, int var1, KeyAction var2) {
        INSTANCE.method1021(false);
        INSTANCE.key = var0;
        INSTANCE.modifiers = var1;
        INSTANCE.action = var2;
        return INSTANCE;
    }
}
