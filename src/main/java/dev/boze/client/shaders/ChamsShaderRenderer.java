package dev.boze.client.shaders;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ChamsMode;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.mixin.WorldRendererAccessor;
import dev.boze.client.mixininterfaces.IWorldRenderer;
import dev.boze.client.renderer.GL;
import dev.boze.client.renderer.QuadRenderer;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.render.Chams;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.ByteTexture;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL32;

import java.io.File;
import java.io.FileInputStream;

public class ChamsShaderRenderer implements IMinecraft {
   private static ShaderProgram field2239;
   private static ShaderProgram field2240;
   private static ShaderProgram field2241;
   private static ShaderProgram field2242;
   public static Framebuffer field2243;
   public static Framebuffer field2244;
   public static Framebuffer field2245;
   public static OutlineVertexConsumerProvider field2246;
   public static boolean field2247;
   public static boolean field2248;

   public static void method1305() {
      field2239 = new ShaderProgram("chams.vert", "chams_colored.frag");
      field2240 = new ShaderProgram("chams.vert", "chams_rainbow.frag");
      field2241 = new ShaderProgram("chams.vert", "chams_image.frag");
      field2242 = new ShaderProgram("chams.vert", "chams_outline.frag");
      field2243 = new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), true, false);
      field2244 = new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false, false);
      field2245 = new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false, false);
      field2246 = new OutlineVertexConsumerProvider(mc.getBufferBuilders().getEntityVertexConsumers());
   }

   public static void method1306() {
      if (Chams.INSTANCE.isEnabled()) {
         if (Chams.INSTANCE.field3463.getValue() != ChamsMode.Normal) {
            field2244.clear(false);
            field2245.clear(false);
         }

         if (Chams.INSTANCE.field3463.getValue() == ChamsMode.Normal || Chams.INSTANCE.field3463.getValue() == ChamsMode.Both) {
            field2243.clear(false);
         }
      }

      mc.getFramebuffer().beginWrite(false);
   }

   public static void method1307(float tickDelta, MatrixStack matrices, Camera camera) {
      if (Chams.INSTANCE.isEnabled()) {
         if (Chams.INSTANCE.field3463.getValue() == ChamsMode.Normal || Chams.INSTANCE.field3463.getValue() == ChamsMode.Both) {
            field2243.beginWrite(false);
            WorldRenderer var6 = mc.worldRenderer;
            field2248 = true;
            ((IWorldRenderer)var6).boze$renderEntitiesForChams(tickDelta, matrices, camera);
            field2248 = false;
            mc.getFramebuffer().beginWrite(false);
            ShaderRegistry.field2265.method2142();
            ShaderRegistry.field2265.method581("u_Size", (double)mc.getWindow().getFramebufferWidth(), (double)mc.getWindow().getFramebufferHeight());
            ShaderRegistry.field2265.method690("u_Src_Texture", 0);
            ShaderRegistry.field2265.method691("u_Opacity", (double)Chams.INSTANCE.field3464.getValue().floatValue());
            GL.method1211(field2243.getColorAttachment());
            QuadRenderer.method1215();
            ShaderRegistry.field2265.method1416();
            if (Chams.INSTANCE.field3463.getValue() == ChamsMode.Normal) {
               return;
            }
         }

         WorldRenderer var10 = mc.worldRenderer;
         WorldRendererAccessor var7 = (WorldRendererAccessor)var10;
         Framebuffer var8 = var10.getEntityOutlinesFramebuffer();
         var7.setEntityOutlinesFramebuffer(field2244);
         field2246.draw();
         var7.setEntityOutlinesFramebuffer(var8);
         mc.getFramebuffer().beginWrite(false);
         GL.method1211(field2244.getColorAttachment());
         ShaderProgram var9 = method1309();
         var9.method2142();
         var9.method581("u_Size", (double)mc.getWindow().getFramebufferWidth(), (double)mc.getWindow().getFramebufferHeight());
         var9.method690("u_Src_Texture", 0);
         var9.method690("u_Img_Texture", 1);
         var9.method690("u_Dst_Texture", 2);
         var9.method689("u_FastRender", Chams.INSTANCE.field3466.getValue());
         var9.method693(
            "u_Fill",
            (double)((float)Chams.INSTANCE.field3473.getValue().field408 / 255.0F),
            (double)((float)Chams.INSTANCE.field3473.getValue().field409 / 255.0F),
            (double)((float)Chams.INSTANCE.field3473.getValue().field410 / 255.0F),
            (double)((float)Chams.INSTANCE.field3473.getValue().field411 / 255.0F)
         );
         var9.method691("u_Fill_Offset", Chams.INSTANCE.field3473.getValue().method958());
         var9.method581("u_Fill_Strength", Chams.INSTANCE.field3473.getValue().method959()[0], Chams.INSTANCE.field3473.getValue().method959()[1]);
         var9.method691("u_Fill_Mod", Chams.INSTANCE.field3473.getValue().method960());
         var9.method581("u_Fill_Hues", Chams.INSTANCE.field3473.getValue().getMinHue(), Chams.INSTANCE.field3473.getValue().getMaxHue());
         var9.method693(
            "u_Hidden",
            (double)((float)Chams.INSTANCE.field3474.getValue().field408 / 255.0F),
            (double)((float)Chams.INSTANCE.field3474.getValue().field409 / 255.0F),
            (double)((float)Chams.INSTANCE.field3474.getValue().field410 / 255.0F),
            0.0
         );
         var9.method691("u_Hidden_Offset", Chams.INSTANCE.field3474.getValue().method958());
         var9.method581("u_Hidden_Strength", Chams.INSTANCE.field3474.getValue().method959()[0], Chams.INSTANCE.field3474.getValue().method959()[1]);
         var9.method691("u_Hidden_Mod", Chams.INSTANCE.field3474.getValue().method960());
         var9.method581("u_Hidden_Hues", Chams.INSTANCE.field3474.getValue().getMinHue(), Chams.INSTANCE.field3474.getValue().getMaxHue());
         var9.method693(
            "u_Outline",
            (double)((float)Chams.INSTANCE.field3475.getValue().field408 / 255.0F),
            (double)((float)Chams.INSTANCE.field3475.getValue().field409 / 255.0F),
            (double)((float)Chams.INSTANCE.field3475.getValue().field410 / 255.0F),
            (double)((float)Chams.INSTANCE.field3475.getValue().field411 / 255.0F)
         );
         var9.method691("u_Outline_Offset", Chams.INSTANCE.field3475.getValue().method958());
         var9.method581("u_Outline_Strength", Chams.INSTANCE.field3475.getValue().method959()[0], Chams.INSTANCE.field3475.getValue().method959()[1]);
         var9.method691("u_Outline_Mod", Chams.INSTANCE.field3475.getValue().method960());
         var9.method581("u_Outline_Hues", Chams.INSTANCE.field3475.getValue().getMinHue(), Chams.INSTANCE.field3475.getValue().getMaxHue());
         var9.method690("u_Radius", Chams.INSTANCE.field3468.getValue());
         var9.method691("u_Opacity", (double)((float)Chams.INSTANCE.field3471.getValue().intValue() / 255.0F));
         var9.method691("u_Glow", (double)Chams.INSTANCE.field3469.getValue().floatValue());
         var9.method691("u_Glow_Strength", (double)Chams.INSTANCE.field3470.getValue().floatValue());
         var9.method690("u_Passes", Chams.INSTANCE.field3467.getValue());
         if (Chams.INSTANCE.method1923() == ShaderMode.Image && Chams.INSTANCE.aJ != null) {
            GL.method1210(Chams.INSTANCE.aJ.getGlId(), 1);
         }

         GL.method1210(mc.getFramebuffer().getColorAttachment(), 2);
         QuadRenderer.method1215();
         var9.method1416();
      }
   }

   private static ShaderProgram method1308(ShaderMode var0) {
      if (var0 == ShaderMode.Image) {
         return field2241;
      } else {
         return var0 == ShaderMode.Rainbow ? field2240 : field2239;
      }
   }

   private static ShaderProgram method1309() {
      if (Chams.INSTANCE.method1923() == ShaderMode.Image) {
         if (!Chams.INSTANCE.field3472.getValue().isEmpty()
            && (!Chams.INSTANCE.field3472.getValue().equals(Chams.INSTANCE.aK) || Chams.INSTANCE.aJ == null)) {
            File var3 = new File(ConfigManager.images, Chams.INSTANCE.field3472.getValue() + ".png");

            try {
               FileInputStream var4 = new FileInputStream(var3);
               Chams.INSTANCE.aJ = ByteTexturePacker.method493(var4);
               if (Chams.INSTANCE.aJ != null) {
                  Chams.INSTANCE.aK = Chams.INSTANCE.field3472.getValue();
               } else {
                  Chams.INSTANCE.aK = "";
               }
            } catch (Exception var5) {
               NotificationManager.method1151(new Notification("Chams", "Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               Chams.INSTANCE.field3472.setValue("");
               Chams.INSTANCE.aK = "";
            }
         }

         if (Chams.INSTANCE.aJ != null) {
            return field2241;
         }
      }

      return Chams.INSTANCE.method1923() == ShaderMode.Rainbow ? field2240 : field2239;
   }

   public static void method1310(
      Runnable draw,
      ShaderMode shaderMode,
      boolean fastRender,
      ColorSetting fill,
      ColorSetting outline,
      int radius,
      float opacity,
      float glow,
      float glowStrength,
      int blur,
      ByteTexture image
   ) {
      method1311(draw, shaderMode, fastRender, fill.getValue(), outline.getValue(), radius, opacity, glow, glowStrength, blur, image);
   }

   public static void method1311(
      Runnable draw,
      ShaderMode shaderMode,
      boolean fastRender,
      BozeDrawColor fill,
      BozeDrawColor outline,
      int radius,
      float opacity,
      float glow,
      float glowStrength,
      int blur,
      ByteTexture image
   ) {
      field2244.clear(false);
      field2244.beginWrite(false);
      draw.run();
      if (blur > 0) {
         field2245.clear(false);
         field2245.beginWrite(false);
      } else {
         mc.getFramebuffer().beginWrite(false);
      }

      ShaderProgram var14 = method1308(shaderMode);
      var14.method2142();
      var14.method581("u_Size", (double)mc.getWindow().getFramebufferWidth(), (double)mc.getWindow().getFramebufferHeight());
      var14.method690("u_Src_Texture", 0);
      var14.method690("u_Img_Texture", 1);
      var14.method690("u_Dst_Texture", 2);
      var14.method689("u_FastRender", fastRender);
      var14.method693(
         "u_Fill",
         (double)((float)fill.field408 / 255.0F),
         (double)((float)fill.field409 / 255.0F),
         (double)((float)fill.field410 / 255.0F),
         (double)((float)fill.field411 / 255.0F)
      );
      var14.method691("u_Fill_Offset", fill.method958());
      var14.method581("u_Fill_Strength", fill.method959()[0], fill.method959()[1]);
      var14.method691("u_Fill_Mod", fill.method960());
      var14.method581("u_Fill_Hues", fill.getMinHue(), fill.getMaxHue());
      var14.method693("u_Hidden", 0.0, 0.0, 0.0, 0.0);
      var14.method693(
         "u_Outline",
         (double)((float)outline.field408 / 255.0F),
         (double)((float)outline.field409 / 255.0F),
         (double)((float)outline.field410 / 255.0F),
         (double)((float)outline.field411 / 255.0F)
      );
      var14.method691("u_Outline_Offset", outline.method958());
      var14.method581("u_Outline_Strength", outline.method959()[0], outline.method959()[1]);
      var14.method691("u_Outline_Mod", outline.method960());
      var14.method581("u_Outline_Hues", outline.getMinHue(), outline.getMaxHue());
      var14.method690("u_Radius", radius);
      var14.method691("u_Opacity", (double)opacity);
      var14.method691("u_Glow", (double)glow);
      var14.method691("u_Glow_Strength", (double)glowStrength);
      var14.method690("u_Passes", blur);
      GL.method1211(field2244.getColorAttachment());
      if (image != null && shaderMode == ShaderMode.Image) {
         GL.method1210(image.getGlId(), 1);
      }

      if (blur > 0) {
         GL.method1210(mc.getFramebuffer().getColorAttachment(), 2);
      }

      QuadRenderer.method1215();
      var14.method1416();
      if (blur > 0) {
         mc.getFramebuffer().beginWrite(false);
         ShaderRegistry.field2264.method2142();
         ShaderRegistry.field2264.method581("u_Size", (double)mc.getWindow().getFramebufferWidth(), (double)mc.getWindow().getFramebufferHeight());
         ShaderRegistry.field2264.method690("u_Src_Texture", 0);
         GL.method1211(field2245.getColorAttachment());
         QuadRenderer.method1215();
         ShaderRegistry.field2264.method1416();
      }
   }

   public static void method1312(Runnable draw, boolean fastRender, BozeDrawColor outline, int radius, float glow, float glowStrength) {
      field2244.clear(false);
      field2244.beginWrite(false);
      draw.run();
      mc.getFramebuffer().beginWrite(false);
      ShaderProgram var9 = field2242;
      var9.method2142();
      var9.method581("u_Size", (double)mc.getWindow().getFramebufferWidth(), (double)mc.getWindow().getFramebufferHeight());
      var9.method690("u_Src_Texture", 0);
      var9.method690("u_Img_Texture", 1);
      var9.method690("u_Dst_Texture", 2);
      var9.method689("u_FastRender", fastRender);
      var9.method693(
         "u_Outline",
         (double)((float)outline.field408 / 255.0F),
         (double)((float)outline.field409 / 255.0F),
         (double)((float)outline.field410 / 255.0F),
         (double)((float)outline.field411 / 255.0F)
      );
      var9.method691("u_Outline_Offset", outline.method958());
      var9.method581("u_Outline_Strength", outline.method959()[0], outline.method959()[1]);
      var9.method691("u_Outline_Mod", outline.method960());
      var9.method581("u_Outline_Hues", outline.getMinHue(), outline.getMaxHue());
      var9.method690("u_Radius", radius);
      var9.method691("u_Glow", (double)glow);
      var9.method691("u_Glow_Strength", (double)glowStrength);
      GL.method1211(field2244.getColorAttachment());
      QuadRenderer.method1215();
      var9.method1416();
   }

   public static void method1313(int readFbo, int textureWidth, int textureHeight) {
      GL32.glBindFramebuffer(36008, readFbo);
      GL32.glBindFramebuffer(36009, field2244.fbo);
      GL32.glBlitFramebuffer(0, 0, textureWidth, textureHeight, 0, 0, field2244.textureWidth, field2244.textureHeight, 16384, 9728);
   }

   public static void method1314(int width, int height) {
      if (field2244 != null) {
         field2244.resize(width, height, false);
      }

      if (field2245 != null) {
         field2245.resize(width, height, false);
      }

      if (field2243 != null) {
         field2243.resize(width, height, false);
      }
   }
}
