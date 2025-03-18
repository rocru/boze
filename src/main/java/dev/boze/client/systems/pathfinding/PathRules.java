package dev.boze.client.systems.pathfinding;

public class PathRules {
    private final boolean flying;
    private final boolean jesus;
    private final boolean noFall;
    private final boolean boat;

    public PathRules(boolean flying, boolean jesus, boolean noFall, boolean boat) {
        this.flying = flying;
        this.jesus = jesus;
        this.noFall = noFall;
        this.boat = boat;
    }

    public boolean isFlying() {
        return this.flying;
    }

    public boolean hasJesus() {
        return this.jesus;
    }

    public boolean hasNoFall() {
        return this.noFall;
    }

    public boolean shouldBoat() {
        return this.boat;
    }
}
