package dev.boze.client.enums;

public enum DisconnectMode {
    LogOut,
    Macro,
    Both;

    private static final DisconnectMode[] field46 = method39();

    private static DisconnectMode[] method39() {
        return new DisconnectMode[]{LogOut, Macro, Both};
    }
}
