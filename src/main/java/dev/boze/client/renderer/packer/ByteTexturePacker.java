package dev.boze.client.renderer.packer;

import com.mojang.blaze3d.platform.TextureUtil;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.ByteTexture;
import dev.boze.client.utils.render.Filter;
import dev.boze.client.utils.render.Format;
import net.minecraft.util.Identifier;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteTexturePacker implements IMinecraft {
    public static ByteTexture method492(Identifier id) {
        try {
            InputStream var1 = mc.getResourceManager().getResource(id).get().getInputStream();
            return method493(var1);
        } catch (IOException var2) {
            ErrorLogger.log(var2);
            return null;
        }
    }

    public static ByteTexture method493(InputStream in) {
        MemoryStack var4 = MemoryStack.stackPush();

        ByteTexture var12;
        label77:
        {
            try {
                ByteBuffer var5 = null;

                try {
                    var5 = TextureUtil.readResource(in);
                    var5.rewind();
                    IntBuffer var6 = var4.mallocInt(1);
                    IntBuffer var7 = var4.mallocInt(1);
                    IntBuffer var8 = var4.mallocInt(1);
                    ByteBuffer var9 = STBImage.stbi_load_from_memory(var5, var6, var7, var8, 4);
                    int var10 = var6.get(0);
                    int var11 = var7.get(0);
                    var12 = new ByteTexture(var10, var11, var9, Format.RGBA, Filter.Linear, Filter.Linear);
                    break label77;
                } catch (IOException var19) {
                    ErrorLogger.log(var19);
                } finally {
                    MemoryUtil.memFree(var5);
                }
            } catch (Throwable var21) {
                if (var4 != null) {
                    try {
                        var4.close();
                    } catch (Throwable var18) {
                        var21.addSuppressed(var18);
                    }
                }

                throw var21;
            }

            if (var4 != null) {
                var4.close();
            }

            return null;
        }

        if (var4 != null) {
            var4.close();
        }

        return var12;
    }
}
