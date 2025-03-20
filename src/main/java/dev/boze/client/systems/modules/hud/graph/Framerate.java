package dev.boze.client.systems.modules.hud.graph;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.systems.modules.GraphHUDModule;
import meteordevelopment.orbit.EventHandler;

public class Framerate extends GraphHUDModule {
    public static final Framerate INSTANCE = new Framerate();
    private double field2671;
    private int field2672;
    private long field2673;
    private long field2674;

    public Framerate() {
        super("Framerate", "Graphs your framerate");
    }

    public void method1568() {
        this.field2674 = System.nanoTime() - this.field2673;
        this.field2673 = System.nanoTime();
        this.field2671 = this.field2671 + (double) (1000000000L / this.field2674);
        this.field2672++;
    }

    @EventHandler
    public void method1569(MovementEvent event) {
        this.method1324(this.field2671 / (double) this.field2672);
        this.field2671 = 0.0;
        this.field2672 = 0;
    }
}
