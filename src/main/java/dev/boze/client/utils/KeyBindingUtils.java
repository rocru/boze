package dev.boze.client.utils;

import dev.boze.client.mixin.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;

public class KeyBindingUtils {
    public static int getKeyCode(KeyBinding bind) {
        return ((KeyBindingAccessor) bind).getKey().getCode();
    }
}
