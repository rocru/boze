package dev.boze.client.enums;

public enum YPriority {
    EyeLevel(1.5),
    Up(6.0),
    Down(-6.0);

    private static final YPriority[] field1705 = method824();
    public final double field1704;

    YPriority(double var3) {
        this.field1704 = var3;
    }

    private static YPriority[] method824() {
        return new YPriority[]{EyeLevel, Up, Down};
    }
}
