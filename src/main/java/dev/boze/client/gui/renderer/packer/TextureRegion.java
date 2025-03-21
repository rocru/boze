package dev.boze.client.gui.renderer.packer;

public class TextureRegion {
    public double field2076;
    public double field2077;
    public double field2078;
    public double field2079;
    public double field2080;

    public TextureRegion(double width, double height) {
        this.field2080 = Math.sqrt(width * width + height * height);
    }
}
