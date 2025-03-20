package dev.boze.client.enums;

public enum AutoMineItemResetMode {
    Off,
    On,
    Delay;

    private static final AutoMineItemResetMode[] field1804 = method914();

    private static AutoMineItemResetMode[] method914() {
        return new AutoMineItemResetMode[]{Off, On, Delay};
    }
}
