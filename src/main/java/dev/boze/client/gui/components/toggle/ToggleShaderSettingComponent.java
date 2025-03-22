package dev.boze.client.gui.components.toggle;

import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.gui.components.scaled.ShaderSettingComponent;

public class ToggleShaderSettingComponent extends ToggleComponent {
    final ShaderSettingComponent field1222;

    public ToggleShaderSettingComponent(ShaderSettingComponent var1, String var2, double var3, double var5, double var7, double var9) {
        super(var2, var3, var5, var7, var9);
        this.field1222 = var1;
    }

    @Override
    protected boolean isToggled() {
        return this.field1222.field1479.method458().method2115();
    }

    @Override
    protected void setToggled(boolean value) {
        this.field1222.field1479.method458().method206(value);
    }
}
