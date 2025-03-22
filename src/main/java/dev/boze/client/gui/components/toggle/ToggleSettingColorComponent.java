package dev.boze.client.gui.components.toggle;

import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.gui.components.scaled.SettingColorComponent;

public class ToggleSettingColorComponent extends ToggleComponent {
    final SettingColorComponent field2057;

    public ToggleSettingColorComponent(SettingColorComponent var1, String var2, double var3, double var5, double var7, double var9) {
        super(var2, var3, var5, var7, var9);
        this.field2057 = var1;
    }

    @Override
    protected boolean isToggled() {
        return this.field2057.method1362().field1842;
    }

    @Override
    protected void setToggled(boolean value) {
        this.field2057.method1362().field1842 = value;
    }
}
