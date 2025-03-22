package dev.boze.client.enums;

public enum KeyClick {
    None(false),
    Friend(false),
    XP(true),
    EP(true),
    Rocket(true);

    private static final KeyClick[] field1731 = method848();
    public final boolean field1730;

    KeyClick(boolean var3) {
        this.field1730 = var3;
    }

    private static KeyClick[] method848() {
        return new KeyClick[]{None, Friend, XP, EP, Rocket};
    }
}
