package dev.boze.client.enums;

public enum RotationLockYawLock {
    Off,
    Angle,
    Cardinal,
    InterCardinal;

    private static final RotationLockYawLock[] field1802 = method912();

    private static RotationLockYawLock[] method912() {
        return new RotationLockYawLock[]{Off, Angle, Cardinal, InterCardinal};
    }
}
