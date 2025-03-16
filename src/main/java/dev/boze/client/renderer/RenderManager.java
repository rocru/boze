package dev.boze.client.renderer;

import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.shaders.ShaderProgram;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.utils.IMinecraft;

public class RenderManager implements IMinecraft {
    public static final RenderManager field1618 = new RenderManager();
    private final Framebuffer[] field1619 = new Framebuffer[6];
    private boolean field1620;
    private long field1621;

    public void renderGUI() {
        boolean var4 = this.isClickGUIOpen();
        long var5 = System.currentTimeMillis();
        if (this.field1620) {
            if (!var4) {
                if (this.field1621 == -1L) {
                    this.field1621 = System.currentTimeMillis() + 150L;
                }

                if (var5 >= this.field1621) {
                    this.field1620 = false;
                    this.field1621 = -1L;
                }
            }
        } else if (var4) {
            this.field1620 = true;
            this.field1621 = System.currentTimeMillis() + 150L;
        }

        if (this.field1620) {
            for (int var7 = 0; var7 < this.field1619.length; var7++) {
                if (this.field1619[var7] == null) {
                    this.field1619[var7] = new Framebuffer(1.0 / Math.pow(2.0, var7));
                }
            }

            double var13 = 1.0;
            if (var5 < this.field1621) {
                if (var4) {
                    var13 = 1.0 - (double) (this.field1621 - var5) / 150.0;
                } else {
                    var13 = (double) (this.field1621 - var5) / 150.0;
                }
            } else {
                this.field1621 = -1L;
            }

            int var9 = Gui.INSTANCE.field2352.method434();
            double var10 = Gui.INSTANCE.field2353.getValue() * var13;
            QuadRenderer.render();
            this.applyShader(this.field1619[0], mc.getFramebuffer().getColorAttachment(), ShaderRegistry.field2272, var10);

            for (int var12 = 0; var12 < var9; var12++) {
                this.applyShader(this.field1619[var12 + 1], this.field1619[var12].textureID, ShaderRegistry.field2272, var10);
            }

            for (int var14 = var9; var14 >= 1; var14--) {
                this.applyShader(this.field1619[var14 - 1], this.field1619[var14].textureID, ShaderRegistry.field2271, var10);
            }

            mc.getFramebuffer().beginWrite(true);
            ShaderRegistry.field2270.method2142();
            GL.method1211(this.field1619[0].textureID);
            ShaderRegistry.field2270.method690("u_Texture", 0);
            QuadRenderer.method1215();
            QuadRenderer.method1216();
        }
    }

    private void applyShader(Framebuffer var1, int var2, ShaderProgram var3, double var4) {
        var1.bindFramebuffer();
        var1.clearFramebuffer();
        var3.method2142();
        GL.method1211(var2);
        var3.method690("u_Texture", 0);
        var3.method581("u_TexelSizeHalf", 0.5 / (double) var1.framebufferWidth, 0.5 / (double) var1.framebufferHeight);
        var3.method691("u_Offset", var4);
        QuadRenderer.method1215();
    }

    private boolean isClickGUIOpen() {
        return mc.currentScreen instanceof ClickGUI && Gui.INSTANCE.field2351.method419();
    }

    public void resizeFramebuffers(int width, int height) {
        for (int var6 = 0; var6 < this.field1619.length; var6++) {
            if (this.field1619[var6] != null) {
                this.field1619[var6].resizeFramebuffer();
            } else {
                this.field1619[var6] = new Framebuffer(1.0 / Math.pow(2.0, var6));
            }
        }
    }
}
