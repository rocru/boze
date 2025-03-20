package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.AddColorComponent;
import dev.boze.client.gui.components.scaled.ColorPickerComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.utils.render.color.ChangingColor;
import dev.boze.client.utils.render.color.StaticColor;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class eX extends TextBaseComponent {
    final String field1192;
    final ScaledBaseComponent field1193;
    final AddColorComponent field1194;

    public eX(AddColorComponent var1, String var2, double var3, double var5, double var7, double var9, String var11, ScaledBaseComponent var12) {
        super(var2, var3, var5, var7, var9);
        this.field1194 = var1;
        this.field1192 = var11;
        this.field1193 = var12;
    }

    @Override
    protected void method1649(int button) {
        if (button == 0) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ChangingColor var5 = new ChangingColor();
            var5.field416.add(new StaticColor(148, 123, 211));
            var5.field416.add(new StaticColor(123, 148, 211));
            var5.field419 = 0.1F;
            ClickGUI.field1335.method580(new ColorPickerComponent(this.field1192, var5, this.field1193));
        }
    }
}
