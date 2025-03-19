package dev.boze.client.enums;

public enum AimPoint {
    MinAngle,
    Closest,
    Clamped,
    Center;

    private static final AimPoint[] field1722 = method840();

    private static AimPoint[] method840() {
        return new AimPoint[]{MinAngle, Closest, Clamped, Center};
    }
}
