package dev.boze.client.gui.colorpanel.action;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.manager.ColorManager;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class ResetComponent extends TextBaseComponent {
    final ColorManager field1172;
    final ColorPanel field1173;

    public ResetComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
        super(var2, var3, var5, var7, var9);
        this.field1173 = var1;
        this.field1172 = var11;
    }

    @Override
    protected void method1649(int button) {
        if (button == 0) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.field1172.method205(this.field1172.field414.copy());
        }
    }
}
