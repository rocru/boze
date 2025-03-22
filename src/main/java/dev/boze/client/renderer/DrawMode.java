package dev.boze.client.renderer;

public enum DrawMode {
    Lines(2),
    Triangles(3);

    public final int indicesCount;

    DrawMode(int var3) {
        this.indicesCount = var3;
    }

    // $VF: synthetic method
    private static DrawMode[] method791() {
        return new DrawMode[]{Lines, Triangles};
    }

    public int method2010() {
        return switch (this) {
            case Lines -> 1;
            case Triangles -> 4;
        };
    }
}
