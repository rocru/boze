package dev.boze.client.utils.render;

public enum Format {
    field1680,
    RGB,
    RGBA;

    private static final Format[] field1681 = method800();

    public int method2010() {
        return switch (this) {
            case field1680 -> 6403;
            case RGB -> 6407;
            case RGBA -> 6408;
        };
    }

    private static Format[] method800() {
        return new Format[]{field1680, RGB, RGBA};
    }
}
