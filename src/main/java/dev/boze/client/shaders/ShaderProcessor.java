package dev.boze.client.shaders;

import dev.boze.client.auth.ShaderCheck;
import dev.boze.client.settings.ShaderSetting;
import net.minecraft.client.gl.Framebuffer;

public abstract class ShaderProcessor {
    protected ShaderProgram field1368;
    private boolean[] field1369;

    public abstract void setShaderSetting(ShaderSetting var1);

    public abstract void applyShader(Runnable var1, Framebuffer var2, Framebuffer var3);

    protected void updateShaderFlags(boolean[] blocks) {
        boolean var5 = true;
        if (this.field1369 == null) {
            var5 = false;
        } else {
            for (int var6 = 0; var6 < blocks.length; var6++) {
                if (this.field1369[var6] != blocks[var6]) {
                    var5 = false;
                    break;
                }
            }
        }

        if (!var5) {
            if (this.field1369 == null) {
                this.field1369 = new boolean[blocks.length];
            }

            System.arraycopy(blocks, 0, this.field1369, 0, blocks.length);
            String var7 = ShaderCheck.method970(this.getShaderName(), blocks);
            this.field1368 = new ShaderProgram(this.getShaderName() + ".vert", var7, true);
        }
    }

    protected abstract String getShaderName();

    public ShaderProgram getShaderProgram() {
        return this.field1368;
    }
}
