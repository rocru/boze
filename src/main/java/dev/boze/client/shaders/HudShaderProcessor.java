package dev.boze.client.shaders;

import dev.boze.client.renderer.GL;
import dev.boze.client.renderer.QuadRenderer;
import dev.boze.client.settings.ShaderSetting;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.gl.Framebuffer;

public class HudShaderProcessor extends ShaderProcessor implements IMinecraft {
    private static final boolean[] field1366 = new boolean[]{
            true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true
    };
    private ShaderSetting field1367;

    private static boolean[] method618(ShaderSetting var0) {
        field1366[1] = var0.method457().method1362().method958() != 0.0 || var0.method458().method1362().method958() != 0.0;
        if (var0.method457().method2115()) {
            field1366[2] = true;
            field1366[4] = true;
            if (var0.method457().method1362().method958() == 0.0) {
                field1366[5] = false;
                field1366[6] = true;
            } else {
                field1366[5] = true;
                field1366[6] = false;
            }

            field1366[7] = false;
            field1366[8] = false;
        } else {
            field1366[2] = false;
            field1366[4] = false;
            field1366[5] = false;
            field1366[6] = false;
            if (var0.method457().method1362().method958() == 0.0) {
                field1366[7] = false;
                field1366[8] = true;
            } else {
                field1366[7] = true;
                field1366[8] = false;
            }
        }

        if (var0.method458().method2010() > 0) {
            field1366[10] = true;
            field1366[11] = var0.method458().method2115();
            field1366[12] = true;
            if (var0.method458().method1362().method958() == 0.0) {
                field1366[13] = false;
                field1366[14] = false;
                if (var0.method458().method1384() > 0.0F) {
                    field1366[15] = true;
                    field1366[16] = false;
                } else {
                    field1366[15] = false;
                    field1366[16] = true;
                }
            } else {
                if (var0.method458().method1384() > 0.0F) {
                    field1366[13] = false;
                    field1366[14] = true;
                } else {
                    field1366[13] = true;
                    field1366[14] = false;
                }

                field1366[15] = false;
                field1366[16] = false;
            }

            field1366[17] = true;
            field1366[18] = false;
        } else {
            for (int var4 = 10; var4 < 18; var4++) {
                field1366[var4] = false;
            }

            field1366[18] = true;
        }

        return field1366;
    }

    @Override
    public void setShaderSetting(ShaderSetting setting) {
        this.field1367 = setting;
        boolean[] var5 = method618(setting);
        super.updateShaderFlags(var5);
    }

    @Override
    public void applyShader(Runnable draw, Framebuffer framebuffer, Framebuffer blurFramebuffer) {
        if (this.field1368 != null) {
            framebuffer.clear(false);
            framebuffer.beginWrite(false);
            draw.run();
            if (this.field1367.method457().method2115()) {
                blurFramebuffer.clear(false);
                blurFramebuffer.beginWrite(false);
            } else {
                mc.getFramebuffer().beginWrite(false);
            }

            this.field1368.method2142();
            this.field1368.method581("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
            this.field1368.method690("u_Src_Texture", 0);
            this.field1368.method690("u_Dst_Texture", 2);
            this.field1368
                    .method693(
                            "u_Fill",
                            (float) this.field1367.method457().method1362().field408 / 255.0F,
                            (float) this.field1367.method457().method1362().field409 / 255.0F,
                            (float) this.field1367.method457().method1362().field410 / 255.0F,
                            (float) this.field1367.method457().method1362().field411 / 255.0F
                    );
            this.field1368.method691("u_Fill_Offset", this.field1367.method457().method1362().method958());
            this.field1368
                    .method581("u_Fill_Strength", this.field1367.method457().method1362().method959()[0], this.field1367.method457().method1362().method959()[1]);
            this.field1368.method691("u_Fill_Mod", this.field1367.method457().method1362().method960());
            this.field1368.method581("u_Fill_Hues", this.field1367.method457().method1362().getMinHue(), this.field1367.method457().method1362().getMaxHue());
            this.field1368.method693("u_Hidden", 0.0, 0.0, 0.0, 0.0);
            this.field1368
                    .method693(
                            "u_Outline",
                            (float) this.field1367.method458().method1362().field408 / 255.0F,
                            (float) this.field1367.method458().method1362().field409 / 255.0F,
                            (float) this.field1367.method458().method1362().field410 / 255.0F,
                            (float) this.field1367.method458().method1362().field411 / 255.0F
                    );
            this.field1368.method691("u_Outline_Offset", this.field1367.method458().method1362().method958());
            this.field1368
                    .method581("u_Outline_Strength", this.field1367.method458().method1362().method959()[0], this.field1367.method458().method1362().method959()[1]);
            this.field1368.method691("u_Outline_Mod", this.field1367.method458().method1362().method960());
            this.field1368.method581("u_Outline_Hues", this.field1367.method458().method1362().getMinHue(), this.field1367.method458().method1362().getMaxHue());
            this.field1368.method690("u_Radius", Math.max(this.field1367.method458().method2010(), 1));
            this.field1368.method691("u_GlowSize", this.field1367.method458().method1384());
            this.field1368.method691("u_MaxGlow", this.field1367.method458().method1385());
            this.field1368.method690("u_Passes", this.field1367.method457().method2010());
            GL.method1211(framebuffer.getColorAttachment());
            if (this.field1367.method457().method2115()) {
                GL.method1210(mc.getFramebuffer().getColorAttachment(), 2);
            }

            QuadRenderer.method1215();
            this.field1368.method1416();
            if (this.field1367.method457().method2115()) {
                mc.getFramebuffer().beginWrite(false);
                ShaderRegistry.field2264.method2142();
                ShaderRegistry.field2264.method581("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
                ShaderRegistry.field2264.method690("u_Src_Texture", 0);
                GL.method1211(blurFramebuffer.getColorAttachment());
                QuadRenderer.method1215();
                ShaderRegistry.field2264.method1416();
            }
        }
    }

    @Override
    protected String getShaderName() {
        return "hud";
    }
}
