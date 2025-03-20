package dev.boze.client.gui.components.slider.ints;

import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.utils.RGBAColor;

public class f5 extends IntSliderComponent {
    final ColorSetting field2021;
    final ColorSettingComponent field2022;

    public f5(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
        super(var2, var3, var5, var7, var9);
        this.field2022 = var1;
        this.field2021 = var11;
    }

    @Override
    protected void method1649(int value) {
        this.field2021.method1374().field410 = value;
    }

    @Override
    protected int method2010() {
        return this.field2021.method1374().field410;
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
        this.field2021.method1374().field410 = this.field2021.field980.field410;
    }

    @Override
    protected RGBAColor method1347() {
        return RGBAColor.field406;
    }
}
