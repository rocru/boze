package dev.boze.client.systems.modules.render;

import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.render.BaseFramebuffer;
import dev.boze.client.renderer.GL;
import dev.boze.client.renderer.QuadRenderer;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.lwjgl.opengl.GL32;

public class Blur extends Module {
   public static final Blur field3407 = new Blur();
   private final IntSetting field3408 = new IntSetting("Blurriness", 2, 1, 5, 1, "Blurriness");
   private final FloatSetting field3409 = new FloatSetting("Factor", 2.0F, 1.0F, 10.0F, 1.0F, "Blur factor");
   private final FloatSetting field3410 = new FloatSetting("Fade", 2.0F, 0.0F, 10.0F, 0.1F, "Fade time in ticks");
   private final RGBASetting field3411 = new RGBASetting("Color", new RGBAColor(-1), "Color for blur");
   private final BooleanSetting field3412 = new BooleanSetting("Boze", true, "Blur world while in Boze GUIs");
   private final BooleanSetting field3413 = new BooleanSetting("Inventories", false, "Blur world while in inventories");
   private final BooleanSetting field3414 = new BooleanSetting("Chat", false, "Blur world while in CHAT GUI");
   private final BooleanSetting field3415 = new BooleanSetting("Other", false, "Blur world while in all other GUIs");
   private boolean field3416;
   private long field3417;
   private BaseFramebuffer field3418;

   public Blur() {
      super("Blur", "Blurs world when in GUIs", Category.Render);
   }

   @EventHandler
   public void method1904() {
      boolean var4 = this.method1905();
      long var5 = System.currentTimeMillis();
      if (this.field3416) {
         if (!var4) {
            if (this.field3417 == -1L) {
               this.field3417 = System.currentTimeMillis() + (long)((int)(this.field3410.method423() * 50.0F));
            }

            if (var5 >= this.field3417) {
               this.field3416 = false;
               this.field3417 = -1L;
            }
         }
      } else if (var4) {
         this.field3416 = true;
         this.field3417 = System.currentTimeMillis() + (long)((int)(this.field3410.method423() * 50.0F));
      }

      if (this.field3416) {
         if (this.field3418 == null) {
            this.field3418 = new BaseFramebuffer(false);
         }

         double var7 = 1.0;
         if (var5 < this.field3417) {
            if (var4) {
               var7 = 1.0 - (double)(this.field3417 - var5) / ((double)this.field3410.method423().floatValue() * 50.0);
            } else {
               var7 = (double)(this.field3417 - var5) / ((double)this.field3410.method423().floatValue() * 50.0);
            }
         } else {
            this.field3417 = -1L;
         }

         ShaderRegistry.field2267.method2142();
         ShaderRegistry.field2267.method581("u_Size", (double)mc.getWindow().getFramebufferWidth(), (double)mc.getWindow().getFramebufferHeight());
         ShaderRegistry.field2267.method690("u_Src_Texture", 0);
         ShaderRegistry.field2267
            .method693(
               "u_Color",
               (double)((float)this.field3411.method1347().field408 / 255.0F),
               (double)((float)this.field3411.method1347().field409 / 255.0F),
               (double)((float)this.field3411.method1347().field410 / 255.0F),
               (double)((float)this.field3411.method1347().field411 / 255.0F)
            );
         ShaderRegistry.field2267.method690("u_Passes", this.field3408.method434());
         ShaderRegistry.field2267.method691("u_Radius", (double)this.field3409.method423().floatValue() * var7);
         this.field3418.method1156(false, true);
         QuadRenderer.render();
         GL.method1211(mc.getFramebuffer().getColorAttachment());
         QuadRenderer.method1215();
         QuadRenderer.method1216();
         GL32.glBindFramebuffer(36008, this.field3418.fbo);
         GL32.glBindFramebuffer(36009, mc.getFramebuffer().fbo);
         GL32.glBlitFramebuffer(
            0,
            0,
            this.field3418.textureWidth,
            this.field3418.textureHeight,
            0,
            0,
            mc.getFramebuffer().textureWidth,
            mc.getFramebuffer().textureHeight,
            16384,
            9728
         );
         mc.getFramebuffer().beginWrite(false);
      }
   }

   private boolean method1905() {
      if (!this.isEnabled()) {
         return false;
      } else {
         Screen var4 = mc.currentScreen;
         if (var4 instanceof ClickGUI) {
            return this.field3412.method419();
         } else if (var4 instanceof HandledScreen) {
            return this.field3413.method419();
         } else if (var4 instanceof ChatScreen) {
            return this.field3414.method419();
         } else {
            return var4 != null ? this.field3415.method419() : false;
         }
      }
   }

   public void method1906(int width, int height) {
      if (this.field3418 != null) {
         this.field3418.resize(width, height, false);
      }
   }
}
