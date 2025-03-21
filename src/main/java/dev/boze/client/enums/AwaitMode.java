package dev.boze.client.enums;

public enum AwaitMode {
    Off,
    Once,
    Repeat;

    private static final AwaitMode[] field17 = method12();

    private static AwaitMode[] method12() {
        return new AwaitMode[]{Off, Once, Repeat};
    }
}
