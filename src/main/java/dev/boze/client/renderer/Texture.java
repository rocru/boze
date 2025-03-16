package dev.boze.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class Texture {
    public int field1055;
    public int field1056;
    private int field1057;
    private boolean field1058;

    public Texture(int width, int height, byte[] data, Format format, Filter filterMin, Filter filterMag) {
        if (RenderSystem.isOnRenderThread()) {
            this.method496(width, height, data, format, filterMin, filterMag);
        } else {
            RenderSystem.recordRenderCall(() -> this.lambda$new$0(width, height, data, format, filterMin, filterMag));
        }
    }

    public Texture() {
    }

    private void method496(int var1, int var2, byte[] var3, Format var4, Filter var5, Filter var6) {
        ByteBuffer var7 = BufferUtils.createByteBuffer(var3.length).put(var3);
        this.method497(var1, var2, var7, var4, var5, var6, false);
    }

    public void method497(int width, int height, ByteBuffer buffer, Format format, Filter filterMin, Filter filterMag, boolean wrapClamp) {
        this.field1055 = width;
        this.field1056 = height;
        if (!this.field1058) {
            this.field1057 = GL.method1162();
            this.field1058 = true;
        }

        this.method2142();
        GL.method1193();
        GL.method1191(3553, 10242, wrapClamp ? '脯' : 10497);
        GL.method1191(3553, 10243, wrapClamp ? '脯' : 10497);
        GL.method1191(3553, 10241, filterMin.glID());
        GL.method1191(3553, 10240, filterMag.glID());
        buffer.rewind();
        GL.method1192(3553, 0, format.glID(), width, height, 0, format.glID(), 5121, buffer);
        if (filterMin == Filter.LinearMipmapLinear || filterMag == Filter.LinearMipmapLinear) {
            GL.method1194(3553);
        }
    }

    public boolean method2114() {
        return this.field1058;
    }

    public void method1649(int slot) {
        GL.method1210(this.field1057, slot);
    }

    public void method2142() {
        this.method1649(0);
    }

    public void method1416() {
        GL.method1165(this.field1057);
        this.field1058 = false;
    }

    private void lambda$new$0(int var1, int var2, byte[] var3, Format var4, Filter var5, Filter var6) {
        this.method496(var1, var2, var3, var4, var5, var6);
    }

    public enum Filter {
        Nearest,
        Linear,
        LinearMipmapLinear;

        public int glID() {
            return switch (this) {
                case Nearest -> 9728;
                case Linear -> 9729;
                case LinearMipmapLinear -> 9987;
            };
        }

    }

    public enum Format {
        field1754,
        RGB,
        RGBA;

        public int glID() {
            return switch (this) {
                case field1754 -> 6403;
                case RGB -> 6407;
                case RGBA -> 6408;
            };
        }
    }
}