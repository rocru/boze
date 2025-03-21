package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class NoMissDelay extends Module {
    public static final NoMissDelay INSTANCE = new NoMissDelay();
    private final MinMaxSetting field2817 = new MinMaxSetting(
            "Chance", 1.0, 0.01, 1.0, 0.01, "Chance of removing miss delay\nAt 1.0, some servers may detect 100% hit accuracy and ban\n"
    );
    private boolean field2818 = true;

    private NoMissDelay() {
        super(
                "NoMissDelay",
                "Removes miss delay and swinging\nWarning: this is semi-blatant with AutoClicker, use Trigger instead\nThis is due to hit accuracy checks on some servers\n",
                Category.Legit
        );
    }

    public boolean method1611() {
        return this.field2818;
    }

    @EventHandler(
            priority = 1000
    )
    public void method1612(RotationEvent event) {
        if (!event.method554(RotationMode.Vanilla)) {
            this.field2818 = !(Math.random() > this.field2817.getValue());
        }
    }
}
