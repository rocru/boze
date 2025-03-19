package dev.boze.client.enums;

public enum ShaderMode {
    Colored,
    Rainbow,
    Image;

    private static final ShaderMode[] field1760 = method874();

    private static ShaderMode[] method874() {
        return new ShaderMode[]{Colored, Rainbow, Image};
    }
}
