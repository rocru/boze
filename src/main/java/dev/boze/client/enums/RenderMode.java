package dev.boze.client.enums;

public enum RenderMode {
    COLOR,
    GRADIENT,
    RAINBOW,
    HSB,
    TEXTURE;

    private static final RenderMode[] field1794 = method905();

    private static RenderMode[] method905() {
        return new RenderMode[]{COLOR, GRADIENT, RAINBOW, HSB, TEXTURE};
    }
}
