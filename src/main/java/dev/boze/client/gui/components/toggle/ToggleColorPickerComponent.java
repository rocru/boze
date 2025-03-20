package dev.boze.client.gui.components.toggle;

import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.gui.components.scaled.ColorPickerComponent;
import dev.boze.client.utils.render.color.ChangingColor;

public class ToggleColorPickerComponent extends ToggleComponent {
    final ChangingColor field2047;
    final ColorPickerComponent field2048;

    public ToggleColorPickerComponent(ColorPickerComponent var1, String var2, double var3, double var5, double var7, double var9, ChangingColor var11) {
        super(var2, var3, var5, var7, var9);
        this.field2048 = var1;
        this.field2047 = var11;
    }

    @Override
    protected void setToggled(boolean value) {
        this.field2047.field418 = value;
    }

    @Override
    protected boolean isToggled() {
        return this.field2047.field418;
    }
}
