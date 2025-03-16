package dev.boze.client.systems.modules.render;

import dev.boze.client.events.Render3DEvent;
import dev.boze.client.render.BaseFramebuffer;
import dev.boze.client.renderer.GL;
import dev.boze.client.renderer.QuadRenderer;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL32;

public class MotionBlur extends Module {
   public static final MotionBlur INSTANCE = new MotionBlur();
   private final IntSetting field3594 = new IntSetting("Strength", 2, 2, 10, 1, "Blur strength");
   private BaseFramebuffer field3595 = null;
   private int field3596 = 0;

   private MotionBlur() {
      super("MotionBlur", "Makes your screen blurry when you move", Category.Render);
      this.field3594.method401(this::lambda$new$0);
   }

   private int method1962() {
      int var1 = mc.getWindow().getFramebufferHeight();
      return Math.min(this.field3594.method434(), 16384 / var1);
   }

   @Override
   public void onEnable() {
      this.field3595 = null;
      this.field3596 = 0;
   }

   @EventHandler(
      priority = -10000
   )
   public void method1963(Render3DEvent event) {
      if (this.field3595 == null) {
         this.field3595 = new BaseFramebuffer(
            mc.getWindow().getFramebufferWidth(), MathHelper.clamp(mc.getWindow().getFramebufferHeight() * this.method1962(), 0, 16384), true
         );
      }

      this.field3595.method1156(false, false);
      GL32.glBindFramebuffer(36008, mc.getFramebuffer().fbo);
      GL32.glBindFramebuffer(36009, this.field3595.fbo);
      int var5 = mc.getFramebuffer().textureHeight * this.field3596;
      int var6 = mc.getFramebuffer().textureHeight * (this.field3596 + 1);
      GL32.glBlitFramebuffer(
         0, 0, mc.getFramebuffer().textureWidth, mc.getFramebuffer().textureHeight, 0, var5, mc.getFramebuffer().textureWidth, var6, 16384, 9728
      );
      mc.getFramebuffer().beginWrite(false);
      this.field3596 = (this.field3596 + 1) % this.method1962();
      ShaderRegistry.field2268.method2142();
      ShaderRegistry.field2268.method581("u_Size", (double)mc.getWindow().getFramebufferWidth(), (double)mc.getWindow().getFramebufferHeight());
      ShaderRegistry.field2268.method690("u_Textures", 0);
      ShaderRegistry.field2268.method690("u_Strength", this.method1962());
      QuadRenderer.render();
      GL.method1210(this.field3595.getColorAttachment(), 0);
      QuadRenderer.method1215();
      QuadRenderer.method1216();
   }

   public void method1964(int width, int height) {
      if (this.field3595 != null) {
         this.field3595.resize(width, MathHelper.clamp(height * this.method1962(), 0, 16384), MinecraftClient.IS_SYSTEM_MAC);
      }
   }

   private void lambda$new$0(Integer var1) {
      this.onEnable();
   }
}
