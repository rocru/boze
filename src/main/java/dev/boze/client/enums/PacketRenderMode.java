package dev.boze.client.enums;

public enum PacketRenderMode {
    Off,
    On,
    Auto;

    private static final PacketRenderMode[] field1759 = method873();

    private static PacketRenderMode[] method873() {
        return new PacketRenderMode[]{Off, On, Auto};
    }
}
