package dev.boze.client.font;

import dev.boze.client.Boze;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.renderer.*;
import dev.boze.client.renderer.Mesh.Attrib;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.utils.ColorWrapper;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.GradientColor;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.BufferUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;

public class FontLoader implements IFontRender, IMinecraft {
    public static boolean field1062 = false;
    public static final RGBAColor field1063 = new RGBAColor(60, 60, 60, 180);
    public static final BozeDrawColor field1064 = new BozeDrawColor(60, 60, 60, 180);
    private final Mesh field1065 = new ShaderMesh(ShaderRegistry.field2258, DrawMode.Triangles, Attrib.Vec2, Attrib.Vec2, Attrib.Color);
    private final Mesh field1066 = new TextureShaderMesh(ShaderRegistry.field2259, DrawMode.Triangles, Attrib.Vec2, Attrib.Vec2, Attrib.Color);
    private final Mesh field1067 = new ShaderMesh(
            ShaderRegistry.field2261, DrawMode.Triangles, Attrib.Vec2, Attrib.Vec2, Attrib.Color, Attrib.Float, Attrib.Vec2, Attrib.Float, Attrib.Vec2
    );
    private final GlyphBuffer[] field1068;
    private GlyphBuffer field1069;
    private boolean field1070;
    private boolean field1071;
    private double field1072 = 1.0;
    private final int field1073;
    private final double field1074;

    public FontLoader(File file) {
        if (!file.exists()) throw new RuntimeException(file.getAbsolutePath());
        byte[] var5 = FontReader.read(file);
        if (var5.length == 0) {
            try {
                var5 = Files.readAllBytes(file.toPath());
            } catch (Throwable _t) {
                _t.printStackTrace(System.err);
            }
            if (var5.length == 0) throw new RuntimeException(file.getAbsolutePath());
        }
        ByteBuffer var6 = BufferUtils.createByteBuffer(var5.length).put(var5);
        this.field1068 = new GlyphBuffer[9];

        for (int var7 = 0; var7 < this.field1068.length; var7++) {
            var6.flip();
            this.field1068[var7] = new GlyphBuffer(var6, (int) Math.round(18.0 * ((double) var7 * 0.5 + 1.0)));
        }

        this.field1073 = 18;
        this.field1074 = 1.0;
    }

    public FontLoader(InputStream is) {
        byte[] var5 = readInputStream(is);
        ByteBuffer var6 = BufferUtils.createByteBuffer(var5.length).put(var5).flip();
        this.field1068 = new GlyphBuffer[9];

        for (int var7 = 0; var7 < this.field1068.length; var7++) {
            var6.flip();
            this.field1068[var7] = new GlyphBuffer(var6, (int) Math.round(18.0 * ((double) var7 * 0.5 + 1.0)));
        }

        this.field1073 = 18;
        this.field1074 = 1.0;
    }

    @Override
    public void updateScale(double a) {
        this.field1065.field1593 = a;
        this.field1066.field1593 = a;
        this.field1067.field1593 = a;
    }

    @Override
    public void startBuilding(double scale, boolean scaleOnly, boolean big) {
        if (this.field1070) {
            throw new RuntimeException("Tried to start building font while already building");
        } else {
            if (!scaleOnly) {
                this.field1065.method2142();
                this.field1066.method2142();
                this.field1067.method2142();
            }

            if (big) {
                this.field1069 = this.field1068[this.field1068.length - 1];
            } else {
                double var8 = Math.floor(scale * 10.0) / 10.0;
                byte var10;
                if (var8 >= 3.0) {
                    var10 = 5;
                } else if (var8 >= 2.5) {
                    var10 = 4;
                } else if (var8 >= 2.0) {
                    var10 = 3;
                } else if (var8 >= 1.5) {
                    var10 = 2;
                } else {
                    var10 = 1;
                }

                this.field1069 = this.field1068[var10 - 1 + mc.options.getGuiScale().getValue()];
            }

            this.field1070 = true;
            this.field1071 = scaleOnly;
            double var11 = this.field1069.method1110() / (double) this.field1073;
            this.field1072 = 1.0 + (scale - var11) / var11;
        }
    }

    @Override
    public double getFontScale() {
        return this.field1072;
    }

    @Override
    public void setFontScale(double scale) {
        this.field1072 = scale;
    }

    @Override
    public double measureTextWidth(String text, int length, boolean shadow) {
        GlyphBuffer var7 = this.field1070 ? this.field1069 : this.field1068[0];
        return (var7.method1109(text, length) + (double) (shadow ? 1 : 0)) * this.field1072;
    }

    @Override
    public double method502(boolean shadow) {
        GlyphBuffer var5 = this.field1070 ? this.field1069 : this.field1068[0];
        return (var5.method1110() + (double) (shadow ? 1 : 0)) * this.field1072 * this.field1074;
    }

    @Override
    public double drawText(String text, double x, double y, ColorWrapper choice) {
        if (!this.field1070) {
            throw new RuntimeException("Tried to render text without starting text rendering");
        } else {
            Mesh var10 = choice.field3910 instanceof GradientColor ? this.field1066 : this.field1065;
            double var11 = 0.0;
            return var11 + this.field1069.method1111(var10, text, x, y, choice.field3910, choice.field3911, this.field1072);
        }
    }

    @Override
    public double drawShadowedText(String text, double x, double y, RGBAColor color, boolean shadow) {
        boolean var11 = this.field1070;
        if (!var11) {
            this.startBuilding();
        }

        Mesh var12 = this.field1065;
        if (color instanceof BozeDrawColor) {
            var12 = this.field1067;
        }

        double var13;
        if (shadow) {
            var13 = this.field1069.method1114(var12, text, x + 1.0, y + 1.0, color instanceof BozeDrawColor ? field1064 : field1063, this.field1072);
            this.field1069.method1114(var12, text, x, y, color, this.field1072);
        } else {
            var13 = this.field1069.method1114(var12, text, x, y, color, this.field1072);
        }

        if (!var11) {
            this.endBuilding();
        }

        return var13;
    }

    @Override
    public boolean method2114() {
        return this.field1070;
    }

    @Override
    public void endBuilding(DrawContext context) {
        if (!this.field1070) {
            throw new RuntimeException("Tried to end text rendering without starting it first");
        } else {
            if (!this.field1071) {
                this.field1065.method1198();
                this.field1066.method1198();
                this.field1067.method1198();
                GL.method1211(this.field1069.field1974.getGlId());
                this.field1065.method720(context == null ? null : context.getMatrices());
                this.field1066.method720(context == null ? null : context.getMatrices());
                this.field1067.method720(context == null ? null : context.getMatrices());
            }

            this.field1070 = false;
            this.field1072 = 1.0;
        }
    }

    private static byte[] readInputStream(InputStream var0) {
        try {
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            byte[] var5 = new byte[256];

            int var6;
            while ((var6 = var0.read(var5)) > 0) {
                var4.write(var5, 0, var6);
            }

            var0.close();
            return var4.toByteArray();
        } catch (IOException var7) {
            Boze.LOG.error("Failed to read bytes from input stream for font");
            return new byte[0];
        }
    }

    public void renderText(Mesh mesh, String text, double x, double y, float r, float g, float b, float opacity, float saturationMod, double scale) {
        float var17 = Math.max(Math.max(r, g), b);
        float var18 = Math.min(Math.min(r, g), b);
        float var19 = var17 - var18;
        float var20 = 0.0F;
        float var21 = var17 == 0.0F ? 0.0F : var19 / var17;
        if (var19 != 0.0F) {
            if (var17 == r) {
                var20 = (g - b) / var19 + (float) (g < b ? 6 : 0);
            } else if (var17 == g) {
                var20 = (b - r) / var19 + 2.0F;
            } else {
                var20 = (r - g) / var19 + 4.0F;
            }

            var20 /= 6.0F;
        }

        var21 *= saturationMod;
        float var23 = var17 * var21;
        float var24 = var23 * (1.0F - Math.abs(var20 * 6.0F % 2.0F - 1.0F));
        float var25 = var17 - var23;
        float[] var26;
        if (var20 < 0.16666667F) {
            var26 = new float[]{var23, var24, 0.0F};
        } else if (var20 < 0.33333334F) {
            var26 = new float[]{var24, var23, 0.0F};
        } else if (var20 < 0.5F) {
            var26 = new float[]{0.0F, var23, var24};
        } else if (var20 < 0.6666667F) {
            var26 = new float[]{0.0F, var24, var23};
        } else if (var20 < 0.8333333F) {
            var26 = new float[]{var24, 0.0F, var23};
        } else {
            var26 = new float[]{var23, 0.0F, var24};
        }

        float var27 = var26[0] + var25;
        float var28 = var26[1] + var25;
        float var29 = var26[2] + var25;
        this.field1069.method1112(mesh, text, x, y, var27, var28, var29, opacity, scale);
    }
}
