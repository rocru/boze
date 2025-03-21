package dev.boze.client.enums;

public enum PistonPushStage {
    Initial,
    Piston,
    Redstone,
    Obsidian,
    Done;

    private static final PistonPushStage[] field38 = method33();

    private static PistonPushStage[] method33() {
        return new PistonPushStage[]{Initial, Piston, Redstone, Obsidian, Done};
    }
}
