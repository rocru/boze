package dev.boze.client.systems.modules.hud.color;

import dev.boze.client.systems.modules.hud.ColorHUDModule;
import dev.boze.client.utils.trackers.TargetTracker;

public class Pops extends ColorHUDModule {
    public static final Pops INSTANCE = new Pops();

    public Pops() {
        super("Pops", "Shows your pop count");
    }

    @Override
    protected String method1562() {
        return "Pops: ";
    }

    @Override
    protected String method1563() {
        return TargetTracker.field1359.getOrDefault(mc.player.getNameForScoreboard(), 0).toString();
    }
}
