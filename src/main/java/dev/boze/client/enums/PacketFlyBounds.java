package dev.boze.client.enums;

enum PacketFlyBounds {
    Normal,
    World,
    Strict;

    private static final PacketFlyBounds[] field1648 = method767();

    private static PacketFlyBounds[] method767() {
        return new PacketFlyBounds[]{Normal, World, Strict};
    }
}
