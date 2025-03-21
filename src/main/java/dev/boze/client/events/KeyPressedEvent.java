package dev.boze.client.events;

import net.minecraft.client.option.KeyBinding;

public class KeyPressedEvent {
    private static final KeyPressedEvent field1924 = new KeyPressedEvent();
    private KeyBinding field1925;
    private boolean field1926;
    private boolean field1927;

    public static KeyPressedEvent method1066(KeyBinding keyBinding, boolean pressed) {
        field1924.field1925 = keyBinding;
        field1924.field1926 = pressed;
        field1924.field1927 = false;
        return field1924;
    }

    public KeyBinding method1067() {
        return this.field1925;
    }

    public boolean method1068() {
        return this.field1926;
    }

    public boolean method1069() {
        return this.field1927;
    }

    public void method1070(boolean pressed) {
        this.field1926 = pressed;
        this.field1927 = true;
    }
}
