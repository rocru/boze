package dev.boze.client.systems.modules.movement;

import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class Parkour extends Module {
    public static final Parkour INSTANCE = new Parkour();
    private final MinMaxDoubleSetting field3326 = new MinMaxDoubleSetting("TicksAhead", new double[]{1.0, 3.0}, 0.0, 5.0, 0.1, "Ticks ahead before edge to jump");

    public Parkour() {
        super("Parkour", "Automatically jumps for you on edges", Category.Movement);
        this.field435 = true;
    }

    @EventHandler
    public void method1860(TickInputPostEvent event) {
        if (!mc.player.isSneaking() && mc.player.isOnGround()) {
            if (mc.world
                    .isSpaceEmpty(
                            mc.player,
                            mc.player
                                    .getBoundingBox()
                                    .offset(
                                            mc.player.getVelocity().x * this.field3326.method1295(),
                                            -mc.player.getStepHeight(),
                                            mc.player.getVelocity().z * this.field3326.method1295()
                                    )
                    )) {
                event.field1955 = true;
                this.field3326.method1296();
            }
        }
    }
}
