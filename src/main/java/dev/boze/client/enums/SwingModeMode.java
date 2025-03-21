package dev.boze.client.enums;

public enum SwingModeMode {
    Offhand,
    Mainhand,
    Opposite,
    Shuffle,
    None;

    private static final SwingModeMode[] field1807 = method917();

    private static SwingModeMode[] method917() {
        return new SwingModeMode[]{Offhand, Mainhand, Opposite, Shuffle, None};
    }
}
