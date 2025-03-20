package dev.boze.client.utils;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.misc.FastLatency;
import dev.boze.client.utils.trackers.LatencyTracker;
import meteordevelopment.orbit.EventHandler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MovementHandler implements IMinecraft {
    public double field1519;

    public MovementHandler() throws IllegalAccessException, NoSuchFieldException, IOException, NoSuchAlgorithmException {
    }

    @EventHandler(
            priority = 51
    )
    public void method2041(MovementEvent event) {
        this.field1519 = FastLatency.INSTANCE.isEnabled()
                ? (float) LatencyTracker.INSTANCE.field1308 + 100.0F * AutoCrystal.INSTANCE.autoCrystalTracker.field1525
                : (float) LatencyTracker.INSTANCE.field1308 + 220.0F * AutoCrystal.INSTANCE.autoCrystalTracker.field1525;
        if (AutoCrystal.INSTANCE.isEnabled()) {
            this.field1519 = AutoCrystal.INSTANCE.autoCrystalTracker.method1385() + 50.0F;
        }
    }
}
