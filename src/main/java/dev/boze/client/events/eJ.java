package dev.boze.client.events;

import dev.boze.client.utils.RotationHelper;

public class eJ {
    public static final eJ field1960 = new eJ();
    public boolean field1963 = false;
    private RotationHelper field1961;
    private boolean field1962 = false;

    public static eJ method1098(RotationHelper rotation) {
        field1960.field1961 = rotation;
        field1960.field1962 = false;
        field1960.field1963 = false;
        return field1960;
    }

    public void method1099(RotationHelper rotation) {
        this.field1961 = rotation;
        this.field1962 = true;
    }

    public RotationHelper method1100() {
        return this.field1961;
    }

    public boolean method1101() {
        return this.field1962;
    }
}
