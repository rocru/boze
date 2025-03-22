package dev.boze.client.font;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.renderer.Mesh;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.ByteTexture;
import dev.boze.client.utils.render.Filter;
import dev.boze.client.utils.render.Format;
import mapped.Class3059;
import mapped.Class5903;
import net.minecraft.client.texture.AbstractTexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.stb.STBTTPackedchar.Buffer;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GlyphBuffer {
    private final int field1975;
    private final float field1976;
    private final float field1977;
    private final Class3059[] field1978;
    public AbstractTexture field1974;

    public GlyphBuffer(ByteBuffer buffer, int height) {
        this.field1975 = height;
        STBTTFontinfo var6 = STBTTFontinfo.create();
        STBTruetype.stbtt_InitFont(var6, buffer);
        this.field1978 = new Class3059[128];
        Buffer var7 = STBTTPackedchar.create(this.field1978.length);
        ByteBuffer var8 = BufferUtils.createByteBuffer(4194304);
        STBTTPackContext var9 = STBTTPackContext.create();
        STBTruetype.stbtt_PackBegin(var9, var8, 2048, 2048, 0, 1);
        STBTruetype.stbtt_PackSetOversampling(var9, 2, 2);
        STBTruetype.stbtt_PackFontRange(var9, buffer, 0, (float) height, 32, var7);
        STBTruetype.stbtt_PackEnd(var9);
        this.field1974 = new ByteTexture(2048, 2048, var8, Format.field1680, Filter.Linear, Filter.Linear);
        this.field1976 = STBTruetype.stbtt_ScaleForPixelHeight(var6, (float) height);
        MemoryStack var10 = MemoryStack.stackPush();

        try {
            IntBuffer var11 = var10.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(var6, var11, null, null);
            this.field1977 = (float) var11.get(0);
        } catch (Throwable var14) {
            if (var10 != null) {
                try {
                    var10.close();
                } catch (Throwable var13) {
                    var14.addSuppressed(var13);
                }
            }

            throw var14;
        }

        if (var10 != null) {
            var10.close();
        }

        for (int var15 = 0; var15 < this.field1978.length; var15++) {
            STBTTPackedchar var16 = var7.get(var15);
            float var12 = 4.8828125E-4F;
            this.field1978[var15] = new Class3059(
                    var16.xoff(),
                    var16.yoff(),
                    var16.xoff2(),
                    var16.yoff2(),
                    (float) var16.x0() * var12,
                    (float) var16.y0() * 4.8828125E-4F,
                    (float) var16.x1() * var12,
                    (float) var16.y1() * 4.8828125E-4F,
                    var16.xadvance()
            );
        }
    }

    public double method1109(String string, int length) {
        double var6 = 0.0;

        for (int var8 = 0; var8 < length; var8++) {
            char var9 = string.charAt(var8);
            if (var9 < ' ' || var9 > 128) {
                var9 = ' ';
            }

            Class3059 var10 = this.field1978[var9 - ' '];
            var6 += var10.field144;
        }

        return var6;
    }

    public double method1110() {
        return this.field1975;
    }

    public double method1111(Mesh mesh, String string, double x, double y, Class5903<?> color, float opacity, double scale) {
        y += (double) (this.field1977 * this.field1976) * scale;

        for (int var14 = 0; var14 < string.length(); var14++) {
            char var15 = string.charAt(var14);
            if (var15 < ' ' || var15 > 128) {
                var15 = ' ';
            }

            Class3059 var16 = this.field1978[var15 - ' '];
            mesh.method1214(
                    mesh.method711(x + (double) var16.field136 * scale, y + (double) var16.field137 * scale)
                            .method711(var16.field140, var16.field141)
                            .method708(color.method208(), opacity)
                            .method2010(),
                    mesh.method711(x + (double) var16.field136 * scale, y + (double) var16.field139 * scale)
                            .method711(var16.field140, var16.field143)
                            .method708(color.method208(), opacity)
                            .method2010(),
                    mesh.method711(x + (double) var16.field138 * scale, y + (double) var16.field139 * scale)
                            .method711(var16.field142, var16.field143)
                            .method708(color.method208(), opacity)
                            .method2010(),
                    mesh.method711(x + (double) var16.field138 * scale, y + (double) var16.field137 * scale)
                            .method711(var16.field142, var16.field141)
                            .method708(color.method208(), opacity)
                            .method2010()
            );
            x += (double) var16.field144 * scale;
        }

        return x;
    }

    public void method1112(Mesh mesh, String string, double x, double y, float r, float g, float b, float opacity, double scale) {
        y += (double) (this.field1977 * this.field1976) * scale;

        for (int var16 = 0; var16 < string.length(); var16++) {
            char var17 = string.charAt(var16);
            if (var17 < ' ' || var17 > 128) {
                var17 = ' ';
            }

            Class3059 var18 = this.field1978[var17 - ' '];
            mesh.method1214(
                    mesh.method711(x + (double) var18.field136 * scale, y + (double) var18.field137 * scale)
                            .method711(var18.field140, var18.field141)
                            .method707(r, g, b, opacity)
                            .method2010(),
                    mesh.method711(x + (double) var18.field136 * scale, y + (double) var18.field139 * scale)
                            .method711(var18.field140, var18.field143)
                            .method707(r, g, b, opacity)
                            .method2010(),
                    mesh.method711(x + (double) var18.field138 * scale, y + (double) var18.field139 * scale)
                            .method711(var18.field142, var18.field143)
                            .method707(r, g, b, opacity)
                            .method2010(),
                    mesh.method711(x + (double) var18.field138 * scale, y + (double) var18.field137 * scale)
                            .method711(var18.field142, var18.field141)
                            .method707(r, g, b, opacity)
                            .method2010()
            );
            x += (double) var18.field144 * scale;
        }
    }

    public void method1113(Mesh mesh, String string, double x, double y, Class5903<?> color, float opacity, float saturation, double scale) {
        y += (double) (this.field1977 * this.field1976) * scale;

        for (int var15 = 0; var15 < string.length(); var15++) {
            char var16 = string.charAt(var15);
            if (var16 < ' ' || var16 > 128) {
                var16 = ' ';
            }

            Class3059 var17 = this.field1978[var16 - ' '];
            mesh.method1214(
                    mesh.method711(x + (double) var17.field136 * scale, y + (double) var17.field137 * scale)
                            .method711(var17.field140, var17.field141)
                            .method707(color.method208().method1384(), color.method208().method1385(), saturation, opacity)
                            .method2010(),
                    mesh.method711(x + (double) var17.field136 * scale, y + (double) var17.field139 * scale)
                            .method711(var17.field140, var17.field143)
                            .method707(color.method208().method1384(), color.method208().method1385(), saturation, opacity)
                            .method2010(),
                    mesh.method711(x + (double) var17.field138 * scale, y + (double) var17.field139 * scale)
                            .method711(var17.field142, var17.field143)
                            .method707(color.method208().method1384(), color.method208().method1385(), saturation, opacity)
                            .method2010(),
                    mesh.method711(x + (double) var17.field138 * scale, y + (double) var17.field137 * scale)
                            .method711(var17.field142, var17.field141)
                            .method707(color.method208().method1384(), color.method208().method1385(), saturation, opacity)
                            .method2010()
            );
            x += (double) var17.field144 * scale;
        }
    }

    public double method1114(Mesh mesh, String string, double x, double y, RGBAColor color, double scale) {
        y += (double) (this.field1977 * this.field1976) * scale;

        for (int var13 = 0; var13 < string.length(); var13++) {
            char var14 = string.charAt(var13);
            if (var14 < ' ' || var14 > 128) {
                var14 = ' ';
            }

            Class3059 var15 = this.field1978[var14 - ' '];
            if (color instanceof BozeDrawColor var16) {
                mesh.method1214(
                        mesh.method711(x + (double) var15.field136 * scale, y + (double) var15.field137 * scale)
                                .method711(var15.field140, var15.field141)
                                .method715(var16)
                                .method714(var16.method958())
                                .method712(var16.method959())
                                .method714(var16.method960())
                                .method711(var16.getMinHue(), var16.getMaxHue())
                                .method2010(),
                        mesh.method711(x + (double) var15.field136 * scale, y + (double) var15.field139 * scale)
                                .method711(var15.field140, var15.field143)
                                .method715(var16)
                                .method714(var16.method958())
                                .method712(var16.method959())
                                .method714(var16.method960())
                                .method711(var16.getMinHue(), var16.getMaxHue())
                                .method2010(),
                        mesh.method711(x + (double) var15.field138 * scale, y + (double) var15.field139 * scale)
                                .method711(var15.field142, var15.field143)
                                .method715(var16)
                                .method714(var16.method958())
                                .method712(var16.method959())
                                .method714(var16.method960())
                                .method711(var16.getMinHue(), var16.getMaxHue())
                                .method2010(),
                        mesh.method711(x + (double) var15.field138 * scale, y + (double) var15.field137 * scale)
                                .method711(var15.field142, var15.field141)
                                .method715(var16)
                                .method714(var16.method958())
                                .method712(var16.method959())
                                .method714(var16.method960())
                                .method711(var16.getMinHue(), var16.getMaxHue())
                                .method2010()
                );
            } else {
                mesh.method1214(
                        mesh.method711(x + (double) var15.field136 * scale, y + (double) var15.field137 * scale)
                                .method711(var15.field140, var15.field141)
                                .method715(color)
                                .method2010(),
                        mesh.method711(x + (double) var15.field136 * scale, y + (double) var15.field139 * scale)
                                .method711(var15.field140, var15.field143)
                                .method715(color)
                                .method2010(),
                        mesh.method711(x + (double) var15.field138 * scale, y + (double) var15.field139 * scale)
                                .method711(var15.field142, var15.field143)
                                .method715(color)
                                .method2010(),
                        mesh.method711(x + (double) var15.field138 * scale, y + (double) var15.field137 * scale)
                                .method711(var15.field142, var15.field141)
                                .method715(color)
                                .method2010()
                );
            }

            x += (double) var15.field144 * scale;
        }

        return x;
    }
}
