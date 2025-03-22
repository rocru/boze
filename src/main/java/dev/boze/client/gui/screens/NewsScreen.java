package dev.boze.client.gui.screens;

import com.mojang.blaze3d.platform.TextureUtil;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.font.IFontRender;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.renderer.Texture;
import dev.boze.client.renderer.Texture.Filter;
import dev.boze.client.renderer.Texture.Format;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.http.NewsUtil;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.NoSuchAlgorithmException;

public class NewsScreen extends Screen implements IMinecraft {
    private final String[] field1129;
    private final Texture[] field1130;
    private int field1128;

    public NewsScreen(String[] urls) {
        super(Text.literal("News"));
        this.field1129 = urls;
        this.field1128 = urls.length - 1;
        this.field1130 = new Texture[urls.length];

        for (int var5 = 0; var5 < urls.length; var5++) {
            try {
                String var6;
                try {
                    var6 = NewsUtil.method2190(urls[var5]);
                } catch (NoSuchAlgorithmException var17) {
                    var6 = urls[var5];
                }

                File var7 = new File(ConfigManager.news, var6 + ".png");
                FileInputStream var8 = new FileInputStream(var7);
                ByteBuffer var9 = TextureUtil.readResource(var8);
                var9.rewind();
                MemoryStack var10 = MemoryStack.stackPush();

                try {
                    IntBuffer var11 = var10.mallocInt(1);
                    IntBuffer var12 = var10.mallocInt(1);
                    IntBuffer var13 = var10.mallocInt(1);
                    ByteBuffer var14 = STBImage.stbi_load_from_memory(var9, var11, var12, var13, 3);
                    Texture var15 = new Texture();
                    var15.method497(var11.get(0), var12.get(0), var14, Format.RGB, Filter.Nearest, Filter.Nearest, false);
                    STBImage.stbi_image_free(var14);
                    this.field1130[var5] = var15;
                } catch (Throwable var18) {
                    if (var10 != null) {
                        try {
                            var10.close();
                        } catch (Throwable var16) {
                            var18.addSuppressed(var16);
                        }
                    }

                    throw var18;
                }

                if (var10 != null) {
                    var10.close();
                }
            } catch (IOException var19) {
                ErrorLogger.log(var19);
            }
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.field1130[this.field1128] == null) {
            ChatInstance.method626("Failed to load news image");
            this.close();
        } else {
            RenderUtil.field3967.method2233();
            int var8 = Math.min(mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2);
            int var9 = mc.getWindow().getScaledWidth() / 2 - var8 / 2;
            int var10 = mc.getWindow().getScaledHeight() / 2 - var8 / 2;
            RenderUtil.field3967.method2270(var9, var10, var8, var8, 0.0, 0.0, 0.0, 1.0, 1.0, RGBAColor.field402);
            this.field1130[this.field1128].method2142();
            RenderUtil.field3967.method2235(context);
            int var11 = var9 / 2;
            int var12 = mc.getWindow().getScaledWidth() - var11;
            IFontRender.method499().startBuilding(1.5);
            IFontRender.method499()
                    .drawShadowedText(
                            "<",
                            (double) var11 - IFontRender.method499().method501("<") / 2.0,
                            (double) mc.getWindow().getScaledHeight() / 2.0 - IFontRender.method499().method1390() / 2.0,
                            RGBAColor.field402
                    );
            IFontRender.method499()
                    .drawShadowedText(
                            ">",
                            (double) var12 - IFontRender.method499().method501(">") / 2.0,
                            (double) mc.getWindow().getScaledHeight() / 2.0 - IFontRender.method499().method1390() / 2.0,
                            RGBAColor.field402
                    );
            IFontRender.method499().endBuilding(context);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 262) {
            this.field1128++;
            if (this.field1128 >= this.field1129.length) {
                this.field1128 = 0;
            }

            return true;
        } else if (keyCode == 263) {
            this.field1128--;
            if (this.field1128 < 0) {
                this.field1128 = this.field1129.length - 1;
            }

            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int var9 = Math.min(mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2);
            if (mouseX < (double) mc.getWindow().getScaledWidth() * 0.5 - (double) var9 * 0.5) {
                this.field1128--;
                if (this.field1128 < 0) {
                    this.field1128 = this.field1129.length - 1;
                }
            } else if (mouseX > (double) mc.getWindow().getScaledWidth() * 0.5 + (double) var9 * 0.5) {
                this.field1128++;
                if (this.field1128 >= this.field1129.length) {
                    this.field1128 = 0;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }
}
