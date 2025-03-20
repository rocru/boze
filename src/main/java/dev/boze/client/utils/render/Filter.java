package dev.boze.client.utils.render;

public enum Filter {
    Nearest,
    Linear;

    private static final Filter[] field1764 = method876();

    public int glID() {
        return this == Nearest ? 9728 : 9729;
    }

    private static Filter[] method876() {
        return new Filter[]{Nearest, Linear};
    }
}
