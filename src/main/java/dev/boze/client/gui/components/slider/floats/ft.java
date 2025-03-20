package dev.boze.client.gui.components.slider.floats;

import dev.boze.client.gui.components.FloatSliderComponent;
import dev.boze.client.gui.components.scaled.bottomrow.WeirdColorSettingComponent;
import dev.boze.client.settings.WeirdColorSetting;

public class ft extends FloatSliderComponent {
    final WeirdColorSetting field1147;
    final WeirdColorSettingComponent field1148;

    public ft(WeirdColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, WeirdColorSetting var11) {
        super(var2, var3, var5, var7, var9);
        this.field1148 = var1;
        this.field1147 = var11;
    }

    @Override
    protected void method207(float value) {
        this.field1147.getValue().field3912 = value;
    }

    @Override
    protected float method1384() {
        return this.field1147.getValue().field3912;
    }

    @Override
    protected float method1385() {
        return 0.0F;
    }

    @Override
    protected float method215() {
        return 1.0F;
    }

    @Override
    protected float method520() {
        return 0.01F;
    }

    @Override
    protected void method2142() {
        this.field1147.getValue().field3912 = 1.0F;
    }
}
