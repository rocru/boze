package dev.boze.client.enums;

public enum NoSlowItems {
    Off,
    On,
    GrimV3,
    NCP,
    NCPStrict,
    GrimV2,
    GrimV2Old,
    Blink;

    private static final NoSlowItems[] field22 = method17();

    private static NoSlowItems[] method17() {
        return new NoSlowItems[]{Off, On, GrimV3, NCP, NCPStrict, GrimV2, GrimV2Old, Blink};
    }
}
