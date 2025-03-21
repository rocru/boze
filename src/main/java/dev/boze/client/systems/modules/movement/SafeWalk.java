package dev.boze.client.systems.modules.movement;

import dev.boze.client.events.ClipAtLedgeEvent;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class SafeWalk extends Module {
    public static final SafeWalk INSTANCE = new SafeWalk();

    public SafeWalk() {
        super("SafeWalk", "Prevents you from falling off edges", Category.Movement);
    }

    @EventHandler
    public void method1866(ClipAtLedgeEvent event) {
        event.method1020();
    }
}
