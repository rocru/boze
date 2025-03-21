package dev.boze.client.gui.renderer.packer;

import com.mojang.blaze3d.platform.TextureUtil;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.ByteTexture;
import dev.boze.client.utils.render.Filter;
import dev.boze.client.utils.render.Format;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class TexturePacker implements IMinecraft {
    public class Image {
        public final ByteBuffer field2069;
        public final TextureRegion field2070;
        public final int field2071;
        public final int field2072;
        public int field2073;
        public int field2074;
        private final boolean field2075;

        public Image(ByteBuffer buffer, TextureRegion region, int width, int height, boolean stb) {
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

    private static final int field1306 = 2048;
    private final List<Image> field1307 = new ArrayList();

    public GuiTexture method562(Identifier id) {
        try {
            InputStream var5 = mc.getResourceManager().getResource(id).get().getInputStream();
            GuiTexture var6 = new GuiTexture();
            MemoryStack var7 = MemoryStack.stackPush();

            try {
                ByteBuffer var8 = null;

                try {
                    var8 = TextureUtil.readResource(var5);
                    var8.rewind();
                    IntBuffer var9 = var7.mallocInt(1);
                    IntBuffer var10 = var7.mallocInt(1);
                    IntBuffer var11 = var7.mallocInt(1);
                    ByteBuffer var12 = STBImage.stbi_load_from_memory(var8, var9, var10, var11, 4);
                    int var13 = var9.get(0);
                    int var14 = var10.get(0);
                    TextureRegion var15 = new TextureRegion(var13, var14);
                    var6.method1123(var15);
                    this.field1307.add(new Image(var12, var15, var13, var14, true));
                    if (var13 > 20) {
                        this.method563(var6, var12, var13, var14, 20);
                    }

                    if (var13 > 32) {
                        this.method563(var6, var12, var13, var14, 32);
                    }

                    if (var13 > 48) {
                        this.method563(var6, var12, var13, var14, 48);
                    }
                } catch (IOException var23) {
                    ErrorLogger.log(var23);
                } finally {
                    MemoryUtil.memFree(var8);
                }
            } catch (Throwable var25) {
                if (var7 != null) {
                    try {
                        var7.close();
                    } catch (Throwable var22) {
                        var25.addSuppressed(var22);
                    }
                }

                throw var25;
            }

            if (var7 != null) {
                var7.close();
            }

            return var6;
        } catch (IOException var26) {
            ErrorLogger.log(var26);
            return null;
        }
    }

    private void method563(GuiTexture var1, ByteBuffer var2, int var3, int var4, int var5) {
        double var6 = (double) var5 / (double) var3;
        int var8 = (int) ((double) var4 * var6);
        ByteBuffer var9 = BufferUtils.createByteBuffer(var5 * var8 * 4);
        STBImageResize.stbir_resize_uint8(var2, var3, var4, 0, var9, var5, var8, 0, 4);
        TextureRegion var10 = new TextureRegion(var5, var8);
        var1.method1123(var10);
        this.field1307.add(new Image(var9, var10, var5, var8, false));
    }

    public ByteTexture method564() {
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;

        for (Image var9 : this.field1307) {
            if (var6 + var9.field2071 > 2048) {
                var4 = Math.max(var4, var6);
                var5 += var7;
                var6 = 0;
                var7 = 0;
            }

            var9.field2073 = 1 + var6;
            var9.field2074 = 1 + var5;
            var6 += 1 + var9.field2071 + 1;
            var7 = Math.max(var7, 1 + var9.field2072 + 1);
        }

        var4 = Math.max(var4, var6);
        var5 += var7;
        ByteBuffer var15 = BufferUtils.createByteBuffer(var4 * var5 * 4);

        for (Image var10 : this.field1307) {
            byte[] var11 = new byte[var10.field2071 * 4];

            for (int var12 = 0; var12 < var10.field2072; var12++) {
                var10.field2069.position(var12 * var11.length);
                var10.field2069.get(var11);
                var15.position(((var10.field2074 + var12) * var4 + var10.field2073) * 4);
                var15.put(var11);
            }

            var10.field2069.rewind();
            var10.method1125();
            var10.field2070.field2076 = (double) var10.field2073 / (double) var4;
            var10.field2070.field2077 = (double) var10.field2074 / (double) var5;
            var10.field2070.field2078 = (double) (var10.field2073 + var10.field2071) / (double) var4;
            var10.field2070.field2079 = (double) (var10.field2074 + var10.field2072) / (double) var5;
        }

        var15.rewind();
        return new ByteTexture(var4, var5, var15, Format.RGBA, Filter.Linear, Filter.Linear);
    }
}
