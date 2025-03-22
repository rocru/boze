package dev.boze.client.gui.components.toggle;

import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.gui.components.scaled.ColorPickerComponent;
import dev.boze.client.utils.render.color.ChangingColor;

public class ToggleColorPickerChangingComponent extends ToggleComponent {
    final ChangingColor field2045;
    final ColorPickerComponent field2046;

    public ToggleColorPickerChangingComponent(ColorPickerComponent var1, String var2, double var3, double var5, double var7, double var9, ChangingColor var11) {
        super(var2, var3, var5, var7, var9);
        this.field2046 = var1;
        this.field2045 = var11;
    }

    @Override
    protected boolean isToggled() {
        return this.field2045.field417;
    }

    @Override
    protected void setToggled(boolean value) {
        this.field2045.field417 = value;
    }
}
