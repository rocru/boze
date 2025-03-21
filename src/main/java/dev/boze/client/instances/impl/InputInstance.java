package dev.boze.client.instances.impl;

import dev.boze.api.internal.interfaces.IInput;
import dev.boze.client.utils.KeyboardUtil;

public final class InputInstance implements IInput {
    public String getKeyName(int key) {
        return KeyboardUtil.getKeyName(key);
    }

    public String getButtonName(int button) {
        return KeyboardUtil.getButtonName(button);
    }
}
