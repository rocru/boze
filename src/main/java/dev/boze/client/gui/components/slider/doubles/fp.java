package dev.boze.client.gui.components.slider.doubles;

import dev.boze.client.gui.components.DoubleSliderComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;

public class fp extends DoubleSliderComponent {
    final ColorSetting field2002;
    final ColorSettingComponent field2003;

    public fp(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
        super(var2, var3, var5, var7, var9);
        this.field2003 = var1;
        this.field2002 = var11;
    }

    @Override
    protected void method938(double value) {
        this.field2002.method1374().field1844 = value;
    }

    @Override
    protected double method2091() {
        return this.field2002.method1374().field1844;
    }

    @Override
    protected double method1614() {
        return 0.0;
    }

    @Override
    protected double method1390() {
        return 2.0;
    }

    @Override
    protected double method1391() {
        return 0.01;
    }

    @Override
    protected void method2142() {
        this.field2002.method1374().field1844 = 0.0;
    }
}
