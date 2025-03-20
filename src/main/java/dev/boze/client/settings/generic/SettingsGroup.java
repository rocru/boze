package dev.boze.client.settings.generic;

import dev.boze.client.settings.Setting;

public interface SettingsGroup {
    Setting<?>[] get();
}
