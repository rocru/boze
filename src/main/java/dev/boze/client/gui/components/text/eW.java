package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.AddColorComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.utils.render.color.StaticColor;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class eW extends TextBaseComponent {
    final String field1189;
    final ScaledBaseComponent field1190;
    final AddColorComponent field1191;

    public eW(AddColorComponent var1, String var2, double var3, double var5, double var7, double var9, String var11, ScaledBaseComponent var12) {
        super(var2, var3, var5, var7, var9);
        this.field1191 = var1;
        this.field1189 = var11;
        this.field1190 = var12;
    }

    @Override
    protected void method1649(int button) {
        if (button == 0) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            StaticColor var5 = new StaticColor(148, 123, 211);
            ClickGUI.field1335.method580(new NewColorComponent(this.field1189, var5, this.field1190));
        }
    }
}
