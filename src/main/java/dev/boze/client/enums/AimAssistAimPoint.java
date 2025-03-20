package dev.boze.client.enums;

public enum AimAssistAimPoint {
    MinAngle,
    Closest,
    Clamped,
    Center,
    Head;

    private static final AimAssistAimPoint[] field18 = method13();

    private static AimAssistAimPoint[] method13() {
        return new AimAssistAimPoint[]{MinAngle, Closest, Clamped, Center, Head};
    }
}
