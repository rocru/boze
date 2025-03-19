package dev.boze.client.enums;

public enum AuraSwapMode {
    OnlyWeapon,
    Off,
    Normal,
    Silent;

    private static final AuraSwapMode[] field1728 = method846();

    private static AuraSwapMode[] method846() {
        return new AuraSwapMode[]{OnlyWeapon, Off, Normal, Silent};
    }
}
