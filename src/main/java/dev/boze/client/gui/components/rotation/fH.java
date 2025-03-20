package dev.boze.client.gui.components.rotation;

import dev.boze.client.gui.components.RotationComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;

public class fH extends RotationComponent {
    final ColorSetting field1210;
    final ColorSettingComponent field1211;

    public fH(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
        super(var2, var3, var5, var7, var9);
        this.field1211 = var1;
        this.field1210 = var11;
    }

    @Override
    protected void method938(double value) {
        this.field1210.method1374().field1846[1] = value;
    }

    @Override
    protected double method2091() {
        return this.field1210.method1374().field1846[1];
    }

    @Override
    protected double method1614() {
        return 0.001;
    }

    @Override
    protected void method2142() {
        this.field1210.method1374().field1846[1] = 1.0;
    }
}
