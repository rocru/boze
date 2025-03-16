package dev.boze.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.util.Util;

public class BaseFramebuffer extends SimpleFramebuffer {
   private final float[] field2149 = (float[])Util.make(BaseFramebuffer::lambda$new$0);

   public BaseFramebuffer(boolean useDepth) {
      super(
         MinecraftClient.getInstance().getWindow().getFramebufferWidth(),
         MinecraftClient.getInstance().getWindow().getFramebufferHeight(),
         useDepth,
         MinecraftClient.IS_SYSTEM_MAC
      );
   }

   public BaseFramebuffer(int width, int height, boolean useDepth) {
      super(width, height, useDepth, MinecraftClient.IS_SYSTEM_MAC);
   }

   public void method1156(boolean setViewport, boolean clear) {
      super.beginWrite(setViewport);
      if (clear) {
         GlStateManager._clearColor(this.field2149[0], this.field2149[1], this.field2149[2], this.field2149[3]);
         short var6 = 16384;
         if (this.useDepthAttachment) {
            GlStateManager._clearDepth(1.0);
            var6 |= 256;
         }

         GlStateManager._clear(var6, MinecraftClient.IS_SYSTEM_MAC);
      }
   }

   public void setClearColor(float r, float g, float b, float a) {
      super.setClearColor(r, g, b, a);
      this.field2149[0] = r;
      this.field2149[1] = g;
      this.field2149[2] = b;
      this.field2149[3] = a;
   }

   private static float[] lambda$new$0() {
      return new float[]{0.0F, 0.0F, 0.0F, 0.0F};
   }
}
