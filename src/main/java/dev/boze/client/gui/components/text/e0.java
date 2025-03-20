package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.gui.components.scaled.SettingColorComponent;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class e0 extends TextBaseComponent {
    final SettingColorComponent field1159;

    public e0(SettingColorComponent var1, String var2, double var3, double var5, double var7, double var9) {
        super(var2, var3, var5, var7, var9);
        this.field1159 = var1;
    }

    @Override
    protected void method1649(int button) {
        if (button == 0 && ColorSettingComponent.field1396 != null) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.field1159.method1362().set(ColorSettingComponent.field1396.copy());
        }
    }
}
