package dev.boze.client.enums;

public enum MiddleClick {
    None(false),
    Friend(false),
    XP(true),
    EP(true),
    Rocket(true);

    private static final MiddleClick[] field1798 = method908();
    public final boolean field1797;

    MiddleClick(boolean var3) {
        this.field1797 = var3;
    }

    private static MiddleClick[] method908() {
        return new MiddleClick[]{None, Friend, XP, EP, Rocket};
    }
}
