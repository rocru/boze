package dev.boze.client.gui.colorpanel.main;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.manager.ColorManager;

public class GradientComponent extends ToggleComponent {
    final ColorManager field1980;
    final ColorPanel field1981;

    public GradientComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
        super(var2, var3, var5, var7, var9);
        this.field1981 = var1;
        this.field1980 = var11;
    }

    @Override
    protected boolean isToggled() {
        return this.field1980.method1374().field1842;
    }

    @Override
    protected void setToggled(boolean value) {
        this.field1980.method1374().field1842 = value;
    }
}
