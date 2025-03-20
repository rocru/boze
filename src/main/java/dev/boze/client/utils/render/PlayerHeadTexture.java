package dev.boze.client.utils.render;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.renderer.Texture;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.http.HttpUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class PlayerHeadTexture extends Texture implements IMinecraft {
    private boolean field1054;

    public PlayerHeadTexture(String url) {
        BufferedImage var5;
        try {
            var5 = ImageIO.read(HttpUtil.get(url).method2186());
        } catch (IOException var12) {
            return;
        }

        byte[] var6 = new byte[192];
        int[] var7 = new int[4];
        int var8 = 0;

        for (int var9 = 8; var9 < 16; var9++) {
            for (int var10 = 8; var10 < 16; var10++) {
                var5.getData().getPixel(var9, var10, var7);

                for (int var11 = 0; var11 < 3; var11++) {
                    var6[var8] = (byte) var7[var11];
                    var8++;
                }
            }
        }

        var8 = 0;

        for (int var14 = 40; var14 < 48; var14++) {
            for (int var15 = 8; var15 < 16; var15++) {
                var5.getData().getPixel(var14, var15, var7);
                if (var7[3] != 0) {
                    for (int var16 = 0; var16 < 3; var16++) {
                        var6[var8] = (byte) var7[var16];
                        var8++;
                    }
                } else {
                    var8 += 3;
                }
            }
        }

        this.method495(BufferUtils.createByteBuffer(var6.length).put(var6));
        this.field1054 = true;
    }

    public PlayerHeadTexture() {
        try {
            InputStream var4 = mc.getResourceManager().getResource(Identifier.of("boze", "textures/steve.png")).get().getInputStream();

            try {
                ByteBuffer var5 = TextureUtil.readResource(var4);
                var5.rewind();
                MemoryStack var6 = MemoryStack.stackPush();

                try {
                    IntBuffer var7 = var6.mallocInt(1);
                    IntBuffer var8 = var6.mallocInt(1);
                    IntBuffer var9 = var6.mallocInt(1);
                    ByteBuffer var10 = STBImage.stbi_load_from_memory(var5, var7, var8, var9, 3);
                    this.method495(var10);
                    STBImage.stbi_image_free(var10);
                } catch (Throwable var13) {
                    if (var6 != null) {
                        try {
                            var6.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }

                    throw var13;
                }

                if (var6 != null) {
                    var6.close();
                }

                MemoryUtil.memFree(var5);
            } catch (Throwable var14) {
                if (var4 != null) {
                    try {
                        var4.close();
                    } catch (Throwable var11) {
                        var14.addSuppressed(var11);
                    }
                }

                throw var14;
            }

            if (var4 != null) {
                var4.close();
            }
        } catch (IOException var15) {
        }
    }

    private void method495(ByteBuffer var1) {
        Runnable var5 = () -> lambda$upload$0(var1);
        if (RenderSystem.isOnRenderThread()) {
            var5.run();
        } else {
            RenderSystem.recordRenderCall(var5::run);
        }
    }

    public boolean method2115() {
        return this.field1054;
    }

    private void lambda$upload$0(ByteBuffer var1) {
        this.method497(
                8,
                8,
                var1,
                dev.boze.client.renderer.Texture.Format.RGB,
                dev.boze.client.renderer.Texture.Filter.Nearest,
                dev.boze.client.renderer.Texture.Filter.Nearest,
                false
        );
    }
}
