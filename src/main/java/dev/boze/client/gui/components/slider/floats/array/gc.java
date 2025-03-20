package dev.boze.client.gui.components.slider.floats.array;

import dev.boze.client.gui.components.FloatArraySliderComponent;
import dev.boze.client.gui.components.scaled.RGBASettingComponent;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.utils.RGBAColor;

import java.awt.*;

public class gc extends FloatArraySliderComponent {
    final RGBASetting field2005;
    final RGBASettingComponent field2006;

    public gc(RGBASettingComponent var1, String var2, double var3, double var5, double var7, double var9, RGBASetting var11) {
        super(var2, var3, var5, var7, var9);
        this.field2006 = var1;
        this.field2005 = var11;
    }

    @Override
    protected float[] method111() {
        return Color.RGBtoHSB(this.field2005.method1348().field408, this.field2005.method1348().field409, this.field2005.method1348().field410, null);
    }

    @Override
    protected void method521(float hue, float sat, float bri) {
        int[] var4 = RGBAColor.method190((double) hue * 360.0, sat, bri);
        this.field2005.method1348().method192(var4[0], var4[1], var4[2], this.field2005.method1348().field411);
    }
}
