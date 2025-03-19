package dev.boze.client.enums;

public enum DelayMode {
    Dynamic,
    Tick;

    private static final DelayMode[] field53 = method46();

    private static DelayMode[] method46() {
        return new DelayMode[]{Dynamic, Tick};
    }
}
