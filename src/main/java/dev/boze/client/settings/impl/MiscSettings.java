package dev.boze.client.settings.impl;

import dev.boze.client.events.SoundPlayEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingBlock;
import dev.boze.client.settings.generic.SettingsGroup;

public class MiscSettings implements SettingsGroup {
    public final BooleanSetting field2205 = new BooleanSetting("HurtBobbing", true, "Don't render hurt bobbing");
    public final BooleanSetting field2206 = new BooleanSetting("InsecureChat", true, "Hide insecure chat warning");
    private final BooleanSetting field2207 = new BooleanSetting("PopSounds", false, "Don't play pop sounds");
    public final BooleanSetting field2208 = new BooleanSetting("PopEffect", false, "Don't show pop effect");
    public final BooleanSetting field2209 = new BooleanSetting("Maps", false, "Don't render maps");
    public final BooleanSetting field2210 = new BooleanSetting("Narrator", false, "Disable narrator");
    private final SettingBlock field2211 = new SettingBlock(
            "Misc", "Miscellaneous rendering settings", this.field2205, this.field2206, this.field2207, this.field2208, this.field2209, this.field2210
    );

    @Override
    public Setting<?>[] get() {
        return this.field2211.method472();
    }

    public void method1298(SoundPlayEvent var1) {
        if (var1.sound.getId().getPath().equals("item.totem.use") && this.field2207.getValue()) {
            var1.method1020();
        }
    }
}
