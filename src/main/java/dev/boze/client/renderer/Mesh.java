package dev.boze.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.shaders.ShaderProgram;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.StaticColor;
import mapped.Class3094;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class Mesh implements IMinecraft {
    private final DrawMode field1596;
    private final int field1597;
    private final int field1598;
    private final int field1599;
    private final int field1600;
    public double field1593 = 1.0;
    public float field1594 = 1.0F;
    public boolean field1595 = true;
    private ByteBuffer field1601;
    private long field1602;
    private int field1603;
    private ByteBuffer field1604;
    private long field1605;
    private int field1606;
    private int field1607;
    private boolean field1608;
    private boolean field1609;
    private double field1610;
    private double field1611;
    private boolean field1612;

    public Mesh(final DrawMode drawMode, final Attrib... attributes) {
        super();
        this.field1593 = 1.0;
        this.field1594 = 1.0f;
        this.field1595 = true;
        int stride = 0;
        for (int length = attributes.length, i = 0; i < length; ++i) {
            stride += attributes[i].size * 4;
        }
        this.field1596 = drawMode;
        this.field1597 = stride * drawMode.indicesCount;
        this.field1601 = BufferUtils.createByteBuffer(this.field1597 * 256 * 4);
        this.field1602 = MemoryUtil.memAddress0(this.field1601);
        this.field1604 = BufferUtils.createByteBuffer(drawMode.indicesCount * 512 * 4);
        this.field1605 = MemoryUtil.memAddress0(this.field1604);
        GL.method1168(this.field1598 = GL.method1160());
        GL.method1169(this.field1599 = GL.method1161());
        GL.method1170(this.field1600 = GL.method1161());
        int n = 0;
        for (int j = 0; j < attributes.length; ++j) {
            final int size = attributes[j].size;
            GL.method1174(j);
            GL.method1175(j, size, 5126, false, stride, n);
            n += size * 4;
        }
        GL.method1168(0);
        GL.method1169(0);
        GL.method1170(0);
    }

    public void method2142() {
        if (!this.field1608) {
            this.field1603 = 0;
            this.field1606 = 0;
            this.field1607 = 0;
            this.field1608 = true;
            this.field1609 = Class3094.field220;
            if (this.field1609 && this.field1595) {
                Vec3d var4 = mc.gameRenderer.getCamera().getPos();
                this.field1610 = var4.x;
                this.field1611 = var4.z;
            } else {
                this.field1610 = 0.0;
                this.field1611 = 0.0;
            }
        }
    }

    public Mesh method706(float[] hsba) {
        if (hsba.length < 3) {
            return this.method707(0.0F, 0.0F, 0.0F, 0.0F);
        } else {
            return hsba.length < 4 ? this.method707(hsba[0], hsba[1], hsba[2], 1.0F) : this.method707(hsba[0], hsba[1], hsba[2], hsba[3]);
        }
    }

    public Mesh method707(float r, float g, float b, float a) {
        long var5 = this.field1602 + (long) this.field1603 * 4L;
        MemoryUtil.memPutFloat(var5, r);
        MemoryUtil.memPutFloat(var5 + 4L, g);
        MemoryUtil.memPutFloat(var5 + 8L, b);
        MemoryUtil.memPutFloat(var5 + 12L, a);
        this.field1603 += 4;
        return this;
    }

    public Mesh method708(StaticColor c, float a) {
        long var3 = this.field1602 + (long) this.field1603 * 4L;
        MemoryUtil.memPutFloat(var3, c.method1384());
        MemoryUtil.memPutFloat(var3 + 4L, c.method1385());
        MemoryUtil.memPutFloat(var3 + 8L, c.method215());
        MemoryUtil.memPutFloat(var3 + 12L, a * (float) this.field1593);
        this.field1603 += 4;
        return this;
    }

    public Mesh method709(Matrix4f matrix, float x, float y, float z) {
        Vector4f var5 = new Vector4f(x, y, z, 1.0F);
        var5 = matrix.transform(var5);
        return this.method710(var5.x, var5.y, var5.z);
    }

    public Mesh method710(double x, double y, double z) {
        long var7 = this.field1602 + (long) this.field1603 * 4L;
        MemoryUtil.memPutFloat(var7, (float) (x - this.field1610));
        MemoryUtil.memPutFloat(var7 + 4L, (float) y);
        MemoryUtil.memPutFloat(var7 + 8L, (float) (z - this.field1611));
        this.field1603 += 3;
        return this;
    }

    public Mesh method711(double x, double y) {
        long var5 = this.field1602 + (long) this.field1603 * 4L;
        MemoryUtil.memPutFloat(var5, (float) x);
        MemoryUtil.memPutFloat(var5 + 4L, (float) y);
        this.field1603 += 2;
        return this;
    }

    public Mesh method712(double[] v) {
        long var2 = this.field1602 + (long) this.field1603 * 4L;
        MemoryUtil.memPutFloat(var2, (float) v[0]);
        MemoryUtil.memPutFloat(var2 + 4L, (float) v[1]);
        this.field1603 += 2;
        return this;
    }

    public Mesh method713(int f) {
        long var2 = this.field1602 + (long) this.field1603 * 4L;
        MemoryUtil.memPutFloat(var2, (float) f);
        this.field1603++;
        return this;
    }

    public Mesh method714(double f) {
        long var3 = this.field1602 + (long) this.field1603 * 4L;
        MemoryUtil.memPutFloat(var3, (float) f);
        this.field1603++;
        return this;
    }

    public Mesh method715(RGBAColor c) {
        long var2 = this.field1602 + (long) this.field1603 * 4L;
        MemoryUtil.memPutFloat(var2, (float) c.field408 / 255.0F);
        MemoryUtil.memPutFloat(var2 + 4L, (float) c.field409 / 255.0F);
        MemoryUtil.memPutFloat(var2 + 8L, (float) c.field410 / 255.0F);
        MemoryUtil.memPutFloat(var2 + 12L, (float) c.field411 / 255.0F * Math.min((float) this.field1593, 1.0F));
        this.field1603 += 4;
        return this;
    }

    public int method2010() {
        return this.field1606++;
    }

    public void method1964(int i1, int i2) {
        long var3 = this.field1605 + (long) this.field1607 * 4L;
        MemoryUtil.memPutInt(var3, i1);
        MemoryUtil.memPutInt(var3 + 4L, i2);
        this.field1607 += 2;
        this.method1416();
    }

    public void method1313(int i1, int i2, int i3) {
        long var4 = this.field1605 + (long) this.field1607 * 4L;
        MemoryUtil.memPutInt(var4, i1);
        MemoryUtil.memPutInt(var4 + 4L, i2);
        MemoryUtil.memPutInt(var4 + 8L, i3);
        this.field1607 += 3;
        this.method1416();
    }

    public void method1214(int i1, int i2, int i3, int i4) {
        long var5 = this.field1605 + (long) this.field1607 * 4L;
        MemoryUtil.memPutInt(var5, i1);
        MemoryUtil.memPutInt(var5 + 4L, i2);
        MemoryUtil.memPutInt(var5 + 8L, i3);
        MemoryUtil.memPutInt(var5 + 12L, i3);
        MemoryUtil.memPutInt(var5 + 16L, i4);
        MemoryUtil.memPutInt(var5 + 20L, i1);
        this.field1607 += 6;
        this.method1416();
    }

    public void method1416() {
        if ((this.field1606 + 1) * this.field1597 >= this.field1601.capacity()) {
            int var4 = this.field1601.capacity() * 2;
            if (var4 % this.field1597 != 0) {
                var4 += var4 % this.field1597;
            }

            ByteBuffer var5 = BufferUtils.createByteBuffer(var4);
            MemoryUtil.memCopy(MemoryUtil.memAddress0(this.field1601), MemoryUtil.memAddress0(var5), (long) this.field1603 * 4L);
            this.field1601 = var5;
            this.field1602 = MemoryUtil.memAddress0(this.field1601);
        }

        if (this.field1607 * 4 >= this.field1604.capacity()) {
            int var6 = this.field1604.capacity() * 2;
            if (var6 % this.field1596.indicesCount != 0) {
                var6 += var6 % (this.field1596.indicesCount * 4);
            }

            ByteBuffer var7 = BufferUtils.createByteBuffer(var6);
            MemoryUtil.memCopy(MemoryUtil.memAddress0(this.field1604), MemoryUtil.memAddress0(var7), (long) this.field1607 * 4L);
            this.field1604 = var7;
            this.field1605 = MemoryUtil.memAddress0(this.field1604);
        }
    }

    public void method1198() {
        if (this.field1608) {
            if (this.field1607 > 0) {
                GL.method1169(this.field1599);
                GL.method1172(34962, this.field1601.limit(this.field1603 * 4), 35048);
                GL.method1169(0);
                GL.method1170(this.field1600);
                GL.method1172(34963, this.field1604.limit(this.field1607 * 4), 35048);
                GL.method1170(0);
            }

            this.field1608 = false;
        }
    }

    public void method718(MatrixStack matrices) {
        this.method719(matrices, false, false);
    }

    public void method719(MatrixStack matrices, boolean useDepth, boolean cull) {
        GL.method1197();
        if (useDepth) {
            GL.method1199();
        } else {
            GL.method1200();
        }

        GL.method1201();
        if (cull) {
            GL.method1203();
        } else {
            GL.method1204();
        }

        GL.method1207();
        if (!MinecraftClient.IS_SYSTEM_MAC) {
            GL30.glLineWidth(this.field1594);
        }

        if (this.field1609) {
            Matrix4fStack var7 = RenderSystem.getModelViewStack();
            var7.pushMatrix();
            if (matrices != null) {
                var7.mul(matrices.peek().getPositionMatrix());
            }

            Vec3d var8 = mc.gameRenderer.getCamera().getPos();
            var7.translate(0.0F, (float) (-var8.y), 0.0F);
        }

        this.field1612 = true;
    }

    public void method720(MatrixStack matrices) {
        this.method721(matrices, false, false);
    }

    public void method721(MatrixStack matrices, boolean useDepth, boolean cull) {
        if (this.field1608) {
            this.method1198();
        }

        if (this.field1607 > 0) {
            boolean var7 = this.field1612;
            if (!var7) {
                this.method719(matrices, useDepth, cull);
            }

            this.beforeRender();
            ShaderProgram.field1552.method1198();
            GL.method1168(this.field1598);
            GL.method1173(this.field1596.method2010(), this.field1607, 5125);
            GL.method1168(0);
            if (!var7) {
                this.method1904();
            }
        }
    }

    public void method1904() {
        if (this.field1609) {
            RenderSystem.getModelViewStack().popMatrix();
        }

        GL.method1198();
        this.field1612 = false;
    }

    protected void beforeRender() {
    }

    public enum Attrib {
        Int(1),
        Float(1),
        Vec2(2),
        Vec3(3),
        Color(4),
        Hsba(4);

        public final int size;

        Attrib(int var3) {
            this.size = var3;
        }
    }
}
