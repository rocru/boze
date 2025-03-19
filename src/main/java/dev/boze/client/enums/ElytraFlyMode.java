package dev.boze.client.enums;

public enum ElytraFlyMode {
    Control,
    ControlStrict,
    Packet,
    Infinite,
    Creative;

    private static final ElytraFlyMode[] field1639 = method758();

    private static ElytraFlyMode[] method758() {
        return new ElytraFlyMode[]{Control, ControlStrict, Packet, Infinite, Creative};
    }
}
