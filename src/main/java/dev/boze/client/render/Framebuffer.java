package dev.boze.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.systems.modules.client.Gui;
import org.lwjgl.opengl.GL32;

public class Framebuffer extends BaseFramebuffer {
   public int field2150;

   public Framebuffer() {
      super(false);
   }

   public void initFbo(int width, int height, boolean getError) {
      RenderSystem.assertOnRenderThreadOrInit();
      int var7 = RenderSystem.maxSupportedTextureSize();
      if (width > 0 && width <= var7 && height > 0 && height <= var7) {
         this.viewportWidth = width;
         this.viewportHeight = height;
         this.textureWidth = width;
         this.textureHeight = height;
         this.fbo = GlStateManager.glGenFramebuffers();
         this.colorAttachment = GL32.glGenTextures();
         GlStateManager._glBindFramebuffer(36160, this.fbo);
         GL32.glBindTexture(37120, this.colorAttachment);
         this.field2150 = Gui.INSTANCE.field2362.method461().samples;
         GL32.glTexImage2DMultisample(37120, this.field2150, 32856, this.textureWidth, this.textureHeight, true);
         GlStateManager._glFramebufferTexture2D(36160, 36064, 37120, this.colorAttachment, 0);
         this.checkFramebufferStatus();
         this.clear(getError);
         this.endRead();
      } else {
         throw new IllegalArgumentException("Window " + width + "x" + height + " size out of bounds (max. size: " + var7 + ")");
      }
   }
}
