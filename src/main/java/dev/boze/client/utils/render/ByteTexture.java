package dev.boze.client.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.resource.ResourceManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30C;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteTexture extends AbstractTexture {
   public ByteTexture(int width, int height, byte[] data, Format format, Filter filterMin, Filter filterMag) {
      if (!RenderSystem.isOnRenderThread()) {
         RenderSystem.recordRenderCall(this::lambda$new$0);
      } else {
         this.method2227(width, height, data, format, filterMin, filterMag);
      }
   }

   public ByteTexture(int width, int height, ByteBuffer buffer, Format format, Filter filterMin, Filter filterMag) {
      if (!RenderSystem.isOnRenderThread()) {
         RenderSystem.recordRenderCall(this::lambda$new$1);
      } else {
         this.method2228(width, height, buffer, format, filterMin, filterMag);
      }
   }

   private void method2227(int var1, int var2, byte[] var3, Format var4, Filter var5, Filter var6) {
      ByteBuffer var7 = BufferUtils.createByteBuffer(var3.length).put(var3);
      this.method2228(var1, var2, var7, var4, var5, var6);
   }

   private void method2228(int var1, int var2, ByteBuffer var3, Format var4, Filter var5, Filter var6) {
      this.bindTexture();
      GL30C.glPixelStorei(3312, 0);
      GL30C.glPixelStorei(3313, 0);
      GL30C.glPixelStorei(3314, 0);
      GL30C.glPixelStorei(32878, 0);
      GL30C.glPixelStorei(3315, 0);
      GL30C.glPixelStorei(3316, 0);
      GL30C.glPixelStorei(32877, 0);
      GL30C.glPixelStorei(3317, 4);
      GL30C.glTexParameteri(3553, 10242, 10497);
      GL30C.glTexParameteri(3553, 10243, 10497);
      GL30C.glTexParameteri(3553, 10241, var5.glID());
      GL30C.glTexParameteri(3553, 10240, var6.glID());
      var3.rewind();
      GL30C.glTexImage2D(3553, 0, var4.method2010(), var1, var2, 0, var4.method2010(), 5121, var3);
   }

   public void load(ResourceManager manager) throws IOException {
   }

   private void lambda$new$1(int var1, int var2, ByteBuffer var3, Format var4, Filter var5, Filter var6) {
      this.method2228(var1, var2, var3, var4, var5, var6);
   }

   private void lambda$new$0(int var1, int var2, byte[] var3, Format var4, Filter var5, Filter var6) {
      this.method2227(var1, var2, var3, var4, var5, var6);
   }
}
