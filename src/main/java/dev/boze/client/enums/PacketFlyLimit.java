package dev.boze.client.enums;

enum PacketFlyLimit {
    Off,
    Tick,
    Speed,
    Both;

    private static final PacketFlyLimit[] field31 = method26();

    private static PacketFlyLimit[] method26() {
        return new PacketFlyLimit[]{Off, Tick, Speed, Both};
    }
}
