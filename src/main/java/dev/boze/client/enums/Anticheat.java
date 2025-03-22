package dev.boze.client.enums;

public enum Anticheat {
    NCP(dev.boze.client.ac.NCP.field1836, AnticheatMode.NCP, RotationMode.Sequential),
    Grim(dev.boze.client.ac.Grim.field1831, AnticheatMode.Grim, RotationMode.Sequential),
    Ghost(dev.boze.client.ac.Ghost.field1313, AnticheatMode.Grim, RotationMode.Vanilla);

    private static final Anticheat[] field1771 = method882();
    public final dev.boze.client.ac.Anticheat ac;
    public final AnticheatMode interactMode;
    public final RotationMode type;

    Anticheat(dev.boze.client.ac.Anticheat var3, AnticheatMode var4, RotationMode var5) {
        this.ac = var3;
        this.interactMode = var4;
        this.type = var5;
    }

    private static Anticheat[] method882() {
        return new Anticheat[]{NCP, Grim, Ghost};
    }
}
