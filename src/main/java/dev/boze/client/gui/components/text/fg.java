package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.utils.render.color.StaticColor;
import mapped.Class2775;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class fg extends TextBaseComponent {
    final Class2775 field1185;
    final StaticColor field1186;
    final ScaledBaseComponent field1187;
    final NewColorComponent field1188;

    public fg(NewColorComponent var1, String var2, double var3, double var5, double var7, double var9, Class2775 var11, StaticColor var12, ScaledBaseComponent var13) {
        super(var2, var3, var5, var7, var9);
        this.field1188 = var1;
        this.field1185 = var11;
        this.field1186 = var12;
        this.field1187 = var13;
    }

    @Override
    protected void method1649(int button) {
        if (button == 0) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.field1185.method5429(this.field1186);
            ClickGUI.field1335.method580(this.field1187);
        }
    }
}
