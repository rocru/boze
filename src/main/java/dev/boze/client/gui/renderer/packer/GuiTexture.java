package dev.boze.client.gui.renderer.packer;

import java.util.ArrayList;
import java.util.List;

public class GuiTexture {
    private final List<TextureRegion> field2068 = new ArrayList(2);

    void method1123(TextureRegion var1) {
        this.field2068.add(var1);
    }

    public TextureRegion method1124(double width, double height) {
        double var8 = Math.sqrt(width * width + height * height);
        double var10 = Double.MAX_VALUE;
        TextureRegion var12 = null;

        for (TextureRegion var14 : this.field2068) {
            double var15 = Math.abs(var8 - var14.field2080);
            if (var15 < var10) {
                var10 = var15;
                var12 = var14;
            }
        }

        return var12;
    }
}
