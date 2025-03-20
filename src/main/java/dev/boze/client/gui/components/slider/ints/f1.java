package dev.boze.client.gui.components.slider.ints;

import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.gui.components.scaled.RGBASettingComponent;
import dev.boze.client.settings.RGBASetting;

public class f1 extends IntSliderComponent {
    final RGBASetting field2013;
    final RGBASettingComponent field2014;

    public f1(RGBASettingComponent var1, String var2, double var3, double var5, double var7, double var9, RGBASetting var11) {
        super(var2, var3, var5, var7, var9);
        this.field2014 = var1;
        this.field2013 = var11;
    }

    @Override
    protected void method1649(int value) {
        this.field2013.method1649(value);
    }

    @Override
    protected int method2010() {
        return this.field2013.method2010();
    }

    @Override
    protected int method1547() {
        return 1;
    }

    @Override
    protected int method1365() {
        return 20;
    }

    @Override
    protected void method2142() {
        this.field2013.method1649(1);
    }
}
