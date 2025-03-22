package dev.boze.client.settings.generic;

import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.Setting;

public class ScalingSetting implements SettingsGroup {
    public final EnumSetting<ScalingMode> field2234 = new EnumSetting<>(
            "Scaling", ScalingMode.Dist, "Scaling Mode\n - Dist: Scale based on distance\n - Const: Constant scale\n"
    );
    public final MinMaxSetting field2235 = new MinMaxSetting("Factor", 1.0, 0.1, 2.0, 0.05, "Scale factor", this::lambda$new$0, this.field2234);
    public final MinMaxSetting field2236 = new MinMaxSetting("Min", 0.03, 0.01, 0.1, 0.01, "Min scale", this::lambda$new$1, this.field2234);
    public final MinMaxSetting field2237 = new MinMaxSetting("Max", 0.25, 0.1, 1.0, 0.01, "Max scale", this::lambda$new$2, this.field2234);
    public final MinMaxSetting field2238 = new MinMaxSetting("Scale", 1.0, 0.1, 2.0, 0.05, "Scale factor", this::lambda$new$3, this.field2234);
    private final Setting<?>[] settings = new Setting[]{this.field2234, this.field2235, this.field2236, this.field2237, this.field2238};

    @Override
    public Setting<?>[] get() {
        return this.settings;
    }

    public double method1303() {
        return this.field2234.getValue() == ScalingMode.Const ? this.field2238.getValue() * 0.5 : this.field2235.getValue();
    }

    public double getMinValue() {
        return this.field2236.getValue();
    }

    public double getMaxValue() {
        return this.field2237.getValue();
    }

    public ScalingMode method1304() {
        return this.field2234.getValue();
    }

    private boolean lambda$new$3() {
        return this.field2234.getValue() == ScalingMode.Const;
    }

    private boolean lambda$new$2() {
        return this.field2234.getValue() == ScalingMode.Dist;
    }

    private boolean lambda$new$1() {
        return this.field2234.getValue() == ScalingMode.Dist;
    }

    private boolean lambda$new$0() {
        return this.field2234.getValue() == ScalingMode.Dist;
    }

    public enum ScalingMode {
        Dist,
        Const
    }
}
