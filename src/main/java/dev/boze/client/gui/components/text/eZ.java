package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.gui.components.scaled.SettingColorComponent;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class eZ extends TextBaseComponent {
    final SettingColorComponent field1198;

    public eZ(SettingColorComponent var1, String var2, double var3, double var5, double var7, double var9) {
        super(var2, var3, var5, var7, var9);
        this.field1198 = var1;
    }

    @Override
    protected void method1649(int button) {
        if (button == 0) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ColorSettingComponent.field1396 = this.field1198.method1362().copy();
        }
    }
}
