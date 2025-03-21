package dev.boze.client.enums;

public enum PacketFlyPhase {
    Off,
    Slow,
    Fast;

    private static final PacketFlyPhase[] field1757 = method871();

    private static PacketFlyPhase[] method871() {
        return new PacketFlyPhase[]{Off, Slow, Fast};
    }
}
