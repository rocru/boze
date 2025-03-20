package dev.boze.client.systems.modules.hud.graph;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.GraphHUDModule;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;

public class CrystalSpeed extends GraphHUDModule {
    public static final CrystalSpeed INSTANCE = new CrystalSpeed();
    private final BooleanSetting field2670 = new BooleanSetting("OnlyWhenAC", true, "Only graph when AutoCrystal is enabled");

    public CrystalSpeed() {
        super("CrystalSpeed", "Graphs your crystal place speed");
        this.field2300.setValue(true);
    }

    @EventHandler
    public void method1566(MovementEvent event) {
        if (AutoCrystal.INSTANCE.isEnabled()) {
            this.method1324(AutoCrystal.INSTANCE.method1384());
        }
    }

    @Override
    public void method295(DrawContext context) {
        if (!this.field2670.getValue() || AutoCrystal.INSTANCE.isEnabled() || mc.currentScreen instanceof ClickGUI && ClickGUI.field1335.field1336) {
            super.method295(context);
        }
    }
}
