package dev.boze.client.gui.components.slider.ints;

import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.gui.components.scaled.RGBASettingComponent;
import dev.boze.client.settings.RGBASetting;

public class f0 extends IntSliderComponent {
    final RGBASetting field2011;
    final RGBASettingComponent field2012;

    public f0(RGBASettingComponent var1, String var2, double var3, double var5, double var7, double var9, RGBASetting var11) {
        super(var2, var3, var5, var7, var9);
        this.field2012 = var1;
        this.field2011 = var11;
    }

    @Override
    protected void method1649(int value) {
        this.field2011.method1348().field411 = value;
    }

    @Override
    protected int method2010() {
        return this.field2011.method1348().field411;
    }

    @Override
    protected int method1547() {
        return 0;
    }

    @Override
    protected int method1365() {
        return 255;
    }

    @Override
    protected void method2142() {
        this.field2011.method1348().field411 = this.field2011.field958.field411;
    }
}
