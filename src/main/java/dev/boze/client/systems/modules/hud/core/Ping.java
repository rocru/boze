package dev.boze.client.systems.modules.hud.core;


import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.trackers.LatencyTracker;
import net.minecraft.client.gui.DrawContext;

public class Ping extends HUDModule {
    private final BooleanSetting field2638 = new BooleanSetting("ShowPrefix", false, "Show prefix (Ping)");
    private final BooleanSetting field2639 = new BooleanSetting("Custom", false, "Use custom theme settings");
    private final ColorSetting field2640 = new ColorSetting(
            "Prefix", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Prefix color", this.field2639
    );
    private final ColorSetting field2641 = new ColorSetting(
            "Number", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Number color", this.field2639
    );
    private final ColorSetting field2642 = new ColorSetting(
            "Suffix", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Suffix color", this.field2639
    );
    private final BooleanSetting field2643 = new BooleanSetting("Shadow", false, "Text shadow", this.field2639);
    public static final Ping INSTANCE = new Ping();

    public Ping() {
        super("Ping", "Shows your current ping", 40.0, 40.0);
    }

    @Override
    public void method295(DrawContext context) {
        if (this.field2638.method419()) {
            this.method298(
                    "Ping",
                    Integer.toString(LatencyTracker.INSTANCE.field1308),
                    "ms",
                    this.field2639.method419() ? this.field2640.method1362() : HUD.INSTANCE.field2383.method1362(),
                    this.field2639.method419() ? this.field2641.method1362() : HUD.INSTANCE.field2383.method1362(),
                    this.field2639.method419() ? this.field2642.method1362() : HUD.INSTANCE.field2383.method1362(),
                    this.field2639.method419() ? this.field2643.method419() : HUD.INSTANCE.field2384.method419()
            );
        } else {
            this.method297(
                    Integer.toString(LatencyTracker.INSTANCE.field1308),
                    "ms",
                    this.field2639.method419() ? this.field2641.method1362() : HUD.INSTANCE.field2383.method1362(),
                    this.field2639.method419() ? this.field2642.method1362() : HUD.INSTANCE.field2383.method1362(),
                    this.field2639.method419() ? this.field2643.method419() : HUD.INSTANCE.field2384.method419()
            );
        }
    }
}
