package dev.boze.client.font;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.render.BaseFramebuffer;
import dev.boze.client.renderer.GL;
import dev.boze.client.renderer.QuadRenderer;
import dev.boze.client.shaders.ShaderProgram;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.client.Fonts;
import dev.boze.client.utils.ColorWrapper;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.GradientColor;
import mapped.Class3032;
import mapped.Class3064;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.BufferAllocator;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class FontRenderer implements IFontRender, IMinecraft {
    public static final FontRenderer field1075 = new FontRenderer();
    private final Class3064<FontRenderContext> field1076 = new Class3064<>(() -> new FontRenderContext(this));
    private final HashMap<Integer, FontRenderContext> field1077 = new HashMap<>();
    private final BufferAllocator field1078 = new BufferAllocator(2048);
    private final Immediate field1079 = VertexConsumerProvider.immediate(this.field1078);
    private final BufferAllocator field1080 = new BufferAllocator(2048);
    private final Immediate field1081 = VertexConsumerProvider.immediate(this.field1080);
    private BaseFramebuffer field1082;
    private final Matrix4f field1083 = new Matrix4f();
    private double field1084 = 1.7;
    private double field1085 = 1.0;
    private boolean field1086;
    private final double field1087 = 1.0;
    private final List<FontDescriptor> field1088 = new LinkedList<>();
    private final List<FontDescriptor> field1089 = new LinkedList<>();
    private final List<FontDescriptor> field1090 = new LinkedList<>();

    private FontRenderer() {
    }

    @Override
    public void updateScale(double a) {
    }

    @Override
    public double measureTextWidth(String text, int length, boolean shadow) {
        String var7 = text;
        if (length != text.length()) {
            var7 = text.substring(0, length);
        }

        return (double) (mc.textRenderer.getWidth(var7) + (shadow ? 1 : 0)) * this.field1084 * this.field1085;
    }

    @Override
    public double method502(boolean shadow) {
        return (double) (9 + (shadow ? 2 : 1)) * this.field1084 * this.field1085;
    }

    @Override
    public double drawText(String text, double x, double y, ColorWrapper choice) {
        boolean var10 = this.field1086;
        if (!var10) {
            this.startBuilding();
        }

        double var11 = this.field1084 * this.field1085;
        x += 0.5 * var11;
        y += 0.5 * var11;
        double var13 = mc.textRenderer.getWidth(text);
        if (choice.field3910 instanceof GradientColor var15) {
            int var19 = (int) (var15.method208().method1384() * 255.0F) << 16
                    | (int) (var15.method208().method1385() * 255.0F) << 8
                    | (int) (var15.method208().method215() * 255.0F)
                    | (int) (choice.field3911 * 255.0F) << 24;
            if (this.field1082 == null) {
                this.field1082 = new BaseFramebuffer(false);
            }

            this.field1088.add(new FontDescriptor(text, x / var11, y / var11 + 1.0, var19, false, null, this.field1084, this.field1085));
        } else {
            int var20 = (int) (choice.field3910.method208().method1384() * 255.0F) << 16
                    | (int) (choice.field3910.method208().method1385() * 255.0F) << 8
                    | (int) (choice.field3910.method208().method215() * 255.0F)
                    | (int) (choice.field3911 * 255.0F) << 24;
            this.field1089.add(new FontDescriptor(text, x / var11, y / var11 + 1.0, var20, false, null, this.field1084, this.field1085));
        }

        if (!var10) {
            this.endBuilding();
        }

        return var13;
    }

    @Override
    public double drawShadowedText(String text, double x, double y, RGBAColor color, boolean shadow) {
        boolean var11 = this.field1086;
        if (!var11) {
            this.startBuilding();
        }

        int var14;
        double var15;
        label23:
        {
            double var12 = this.field1084 * this.field1085;
            x += 0.5 * var12;
            y += 0.5 * var12;
            var14 = color.field411;
            color.field411 = (int) ((double) (color.field411 / 255) * this.field1087 * 255.0);
            var15 = (double) mc.textRenderer.getWidth(text) * var12;
            if (color instanceof BozeDrawColor var17 && (var17.field1842 || var17.field1843 != 0.0)) {
                this.field1090.add(new FontDescriptor(text, x / var12, y / var12 + 1.0, -1, shadow, var17, this.field1084, this.field1085));
                break label23;
            }

            this.field1089.add(new FontDescriptor(text, x / var12, y / var12 + 1.0, color.method2010(), shadow, null, this.field1084, this.field1085));
        }

        color.field411 = var14;
        if (!var11) {
            this.endBuilding();
        }

        return var15;
    }

    @Override
    public void startBuilding(double scale, boolean scaleOnly, boolean big) {
        if (this.field1086) {
            throw new RuntimeException("Tried to start building font while already building");
        } else {
            this.field1084 = scale * 1.7;
            this.field1085 = 1.0;
            this.field1086 = true;
        }
    }

    @Override
    public double getFontScale() {
        return this.field1085;
    }

    @Override
    public void setFontScale(double scaleMultiplier) {
        this.field1085 = scaleMultiplier;
    }

    @Override
    public boolean method2114() {
        return this.field1086;
    }

    @Override
    public void endBuilding(DrawContext context) {
        if (!this.field1086) {
            throw new RuntimeException("Tried to end text rendering without starting it first");
        } else if (this.field1089.isEmpty() && this.field1088.isEmpty() && this.field1090.isEmpty()) {
            this.field1084 = 1.7;
            this.field1085 = 1.0;
            this.field1086 = false;
        } else {
            Matrix4fStack var5 = RenderSystem.getModelViewStack();
            RenderSystem.disableDepthTest();
            var5.pushMatrix();
            if (context != null) {
                var5.mul(context.getMatrices().peek().getPositionMatrix());
            }

            var5.scale((float) this.field1084, (float) this.field1084, 1.0F);
            RenderSystem.applyModelViewMatrix();
            if (!this.field1089.isEmpty()) {
                for (FontDescriptor var7 : this.field1089) {
                    Matrix4f var8 = new Matrix4f(this.field1083);
                    var8 = var8.scale((float) var7.field154, (float) var7.field154, 1.0F);
                    mc.textRenderer
                            .draw(
                                    var7.field147, (float) var7.field148, (float) var7.field149, var7.field150, false, var8, this.field1079, TextLayerType.NORMAL, 0, 15728880
                            );
                }

                this.field1079.draw();
                this.field1089.clear();
            }

            if (!this.field1090.isEmpty()) {
                for (FontDescriptor var12 : this.field1090) {
//               FontRenderContext var15 = this.field1077.computeIfAbsent(var12.field152.hashCode(), this::lambda$end$1);
                    FontRenderContext var15 = this.field1077.computeIfAbsent(var12.field152.hashCode(), n -> this.field1076.method5993().method1108(var12.field152));
                    Matrix4f var9 = new Matrix4f(this.field1083);
                    var9 = var9.scale((float) var12.field154, (float) var12.field154, 1.0F);
                    mc.textRenderer
                            .draw(
                                    var12.field147,
                                    (float) var12.field148,
                                    (float) var12.field149,
                                    var12.field150,
                                    var12.field151,
                                    var9,
                                    var15.field1971,
                                    TextLayerType.NORMAL,
                                    0,
                                    15728880
                            );
                }

                if (this.field1082 == null) {
                    this.field1082 = new BaseFramebuffer(true);
                }

                Iterator<Entry<Integer, FontRenderContext>> var11 = this.field1077.entrySet().iterator();

                while (var11.hasNext()) {
                    Entry<Integer, FontRenderContext> var13 = var11.next();
                    BozeDrawColor var16 = var13.getValue().field1972;
                    this.field1082.method1156(false, true);
                    var13.getValue().field1971.draw();
                    mc.getFramebuffer().beginWrite(false);
                    ShaderProgram var18 = ShaderRegistry.field2269;
                    var18.method2142();
                    ShaderRegistry.field2269.method581("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
                    var18.method690("u_Src_Texture", 0);
                    var18.method693(
                            "u_Fill",
                            (float) var16.field408 / 255.0F,
                            (float) var16.field409 / 255.0F,
                            (float) var16.field410 / 255.0F,
                            (float) var16.field411 / 255.0F
                    );
                    var18.method691("u_Fill_Offset", var16.method958());
                    var18.method581("u_Fill_Strength", var16.method959()[0], var16.method959()[1]);
                    var18.method691("u_Fill_Mod", var16.method960());
                    var18.method581("u_Fill_Hues", var16.getMinHue(), var16.getMaxHue());
                    GL.method1211(this.field1082.getColorAttachment());
                    QuadRenderer.method1215();
                    var18.method1416();
                    this.field1076.method5994(var13.getValue());
                    var11.remove();
                }

                this.field1090.clear();
            }

            if (!this.field1088.isEmpty()) {
                this.method505(0.0F, 0.0F);
                this.field1088.clear();
            }

            var5.popMatrix();
            RenderSystem.enableDepthTest();
            RenderSystem.applyModelViewMatrix();
            this.field1084 = 1.7;
            this.field1085 = 1.0;
            this.field1086 = false;
        }
    }

    public static void method1964(int width, int height) {
        FontRenderer var5 = field1075;
        if (var5.field1082 != null) {
            var5.field1082.resize(width, height, false);
        }
    }

    private int method504(int var1, float var2, float var3) {
        float var7 = (float) (var1 >> 16 & 0xFF) / 255.0F;
        float var8 = (float) (var1 >> 8 & 0xFF) / 255.0F;
        float var9 = (float) (var1 & 0xFF) / 255.0F;
        float var10 = Math.max(Math.max(var7, var8), var9);
        float var11 = Math.min(Math.min(var7, var8), var9);
        float var12 = var10 - var11;
        float var13 = 0.0F;
        float var14 = var10 == 0.0F ? 0.0F : var12 / var10;
        if (var12 != 0.0F) {
            if (var10 == var7) {
                var13 = (var8 - var9) / var12 + (float) (var8 < var9 ? 6 : 0);
            } else if (var10 == var8) {
                var13 = (var9 - var7) / var12 + 2.0F;
            } else {
                var13 = (var7 - var8) / var12 + 4.0F;
            }

            var13 /= 6.0F;
        }

        var14 *= var2;
        float var16 = var10 * var14;
        float var17 = var16 * (1.0F - Math.abs(var13 * 6.0F % 2.0F - 1.0F));
        float var18 = var10 - var16;
        float[] var19;
        if (var13 < 0.16666667F) {
            var19 = new float[]{var16, var17, 0.0F};
        } else if (var13 < 0.33333334F) {
            var19 = new float[]{var17, var16, 0.0F};
        } else if (var13 < 0.5F) {
            var19 = new float[]{0.0F, var16, var17};
        } else if (var13 < 0.6666667F) {
            var19 = new float[]{0.0F, var17, var16};
        } else if (var13 < 0.8333333F) {
            var19 = new float[]{var17, 0.0F, var16};
        } else {
            var19 = new float[]{var16, 0.0F, var17};
        }

        return (int) (var3 * 255.0F) << 24
                | (int) ((var19[0] + var18) * 255.0F) << 16
                | (int) ((var19[1] + var18) * 255.0F) << 8
                | (int) ((var19[2] + var18) * 255.0F);
    }

    private void method505(float var1, float var2) {
        if (this.field1082 == null) {
            this.field1082 = new BaseFramebuffer(false);
        }

        this.field1082.method1156(false, true);

        for (FontDescriptor var7 : this.field1088) {
            Matrix4f var8 = new Matrix4f(this.field1083);
            var8 = var8.scale((float) var7.field154, (float) var7.field154, 1.0F);
            int var9 = var7.field150;
            if (var1 != 0.0F) {
                var9 = this.method504(var7.field150, Fonts.INSTANCE.field2348.getValue(), Fonts.INSTANCE.field2347.getValue());
            }

            mc.textRenderer
                    .draw(var7.field147, (float) var7.field148 + var1, (float) var7.field149 + var2, var9, false, var8, this.field1081, TextLayerType.NORMAL, 0, 15728880);
        }

        this.field1081.draw();
        mc.getFramebuffer().beginWrite(false);
        ShaderRegistry.field2260.method2142();
        ShaderRegistry.field2260.method581("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
        ShaderRegistry.field2260.method690("u_Src_Texture", 0);
        ShaderRegistry.field2260.method690("u_Texture", 1);
        GL.method1210(this.field1082.getColorAttachment(), 0);
        GL.method1210(Class3032.method2010(), 1);
        QuadRenderer.method1215();
        ShaderRegistry.field2260.method1416();
        this.field1082.endRead();
    }
}