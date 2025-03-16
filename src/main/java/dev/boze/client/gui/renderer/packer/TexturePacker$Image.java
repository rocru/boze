package dev.boze.client.gui.renderer.packer;

import java.nio.ByteBuffer;
import org.lwjgl.stb.STBImage;

class TexturePacker$Image {
   public final ByteBuffer field2069;
   public final TextureRegion field2070;
   public final int field2071;
   public final int field2072;
   public int field2073;
   public int field2074;
   private final boolean field2075;

   public TexturePacker$Image(ByteBuffer buffer, TextureRegion region, int width, int height, boolean stb) {
      this.field2069 = buffer;
      this.field2070 = region;
      this.field2071 = width;
      this.field2072 = height;
      this.field2075 = stb;
   }

   public void method1125() {
      if (this.field2075) {
         STBImage.stbi_image_free(this.field2069);
      }
   }
}
