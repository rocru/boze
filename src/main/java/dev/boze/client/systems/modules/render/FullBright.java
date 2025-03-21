package dev.boze.client.systems.modules.render;

import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class FullBright extends Module {
    public static final FullBright INSTANCE = new FullBright();
    public final BooleanSetting field3565 = new BooleanSetting("Gamma", true, "Change gamma (global fullbright)");
    public final IntSetting field3566 = new IntSetting("Brightness", 15, 1, 15, 1, "Brightness for light");
    public final FloatSetting field3567 = new FloatSetting("Ambient", 0.0F, 0.0F, 1.0F, 0.05F, "Ambient light");
    public final BooleanSetting field3568 = new BooleanSetting("HeldItems", false, "Render held items in full brightness");
    public static int field3569 = 0;

    public FullBright() {
        super("FullBright", "Makes the world brighter", Category.Render);
    }

    @EventHandler
    private void method1959(Render3DEvent var1) {
        if (field3569 != this.field3566.getValue()) {
            field3569 = this.field3566.getValue();
            if (mc.worldRenderer != null) {
                mc.worldRenderer.reload();
            }
        }
    }
}
