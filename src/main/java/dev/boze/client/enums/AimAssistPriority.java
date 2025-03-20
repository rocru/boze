package dev.boze.client.enums;

public enum AimAssistPriority {
    Distance,
    FOV,
    Health;

    private static final AimAssistPriority[] field1735 = method852();

    private static AimAssistPriority[] method852() {
        return new AimAssistPriority[]{Distance, FOV, Health};
    }
}
