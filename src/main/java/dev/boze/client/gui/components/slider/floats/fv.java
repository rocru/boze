package dev.boze.client.gui.components.slider.floats;

import dev.boze.client.gui.components.FloatSliderComponent;
import dev.boze.client.gui.components.scaled.bottomrow.EditGradientColorComponent;
import dev.boze.client.utils.render.color.GradientColor;

public class fv extends FloatSliderComponent {
    final GradientColor field1151;
    final EditGradientColorComponent field1152;

    public fv(EditGradientColorComponent var1, String var2, double var3, double var5, double var7, double var9, GradientColor var11) {
        super(var2, var3, var5, var7, var9);
        this.field1152 = var1;
        this.field1151 = var11;
    }

    @Override
    protected void method207(float value) {
        this.field1151.field428 = value;
    }

    @Override
    protected float method1384() {
        return this.field1151.field428;
    }

    @Override
    protected float method1385() {
        return 0.1F;
    }

    @Override
    protected float method215() {
        return 10.0F;
    }

    @Override
    protected float method520() {
        return 0.1F;
    }

    @Override
    protected void method2142() {
        this.field1151.field428 = 1.0F;
    }
}
