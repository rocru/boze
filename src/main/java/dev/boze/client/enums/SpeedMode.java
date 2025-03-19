package dev.boze.client.enums;

enum SpeedMode {
    Vanilla,
    Strafe,
    Grim,
    BHop,
    OnGround;

    private static final SpeedMode[] field1703 = method823();

    private static SpeedMode[] method823() {
        return new SpeedMode[]{Vanilla, Strafe, Grim, BHop, OnGround};
    }
}
