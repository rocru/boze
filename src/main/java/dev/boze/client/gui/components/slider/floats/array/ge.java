package dev.boze.client.gui.components.slider.floats.array;

import dev.boze.client.gui.components.FloatArraySliderComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.StaticColor;

import java.awt.*;

public class ge extends FloatArraySliderComponent {
    final StaticColor field2009;
    final NewColorComponent field2010;

    public ge(NewColorComponent var1, String var2, double var3, double var5, double var7, double var9, StaticColor var11) {
        super(var2, var3, var5, var7, var9);
        this.field2010 = var1;
        this.field2009 = var11;
    }

    @Override
    protected float[] method111() {
        return Color.RGBtoHSB(this.field2009.field430, this.field2009.field431, this.field2009.field432, null);
    }

    @Override
    protected void method521(float hue, float sat, float bri) {
        int[] var4 = RGBAColor.method190((double) hue * 360.0, sat, bri);
        this.field2009.field430 = var4[0];
        this.field2009.field431 = var4[1];
        this.field2009.field432 = var4[2];
    }
}
