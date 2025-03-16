package dev.boze.client.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.RenderMode;
import dev.boze.client.font.FontLoader;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.render.Scissor;
import dev.boze.client.Boze;
import mapped.Class5929;
import net.minecraft.client.gui.DrawContext;

public class HUDRenderer implements IMinecraft {
   public static void renderHud(DrawContext context) {
      if (mc.currentScreen instanceof ClickGUI && ClickGUI.field1335.field1336 || HUD.INSTANCE.field2373.method419()) {
         boolean var4 = false;

         for (Module var6 : Boze.getModules().modules) {
            if (var6 instanceof HUDModule && var6.isEnabled()) {
               var4 = true;
               break;
            }
         }

         if (var4) {
            double var14 = HUD.INSTANCE.field2376.getValue() * (double)Math.min(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
            RenderUtil.field3963.method2233();
            RGBAColor var7 = FontLoader.field1063.method1347();
            BozeDrawColor var8 = FontLoader.field1064.method964();
            FontLoader.field1063.method198(HUD.INSTANCE.field2395.method1347());
            FontLoader.field1064.method961(HUD.INSTANCE.field2396.method1362());
            IFontRender.method499().startBuilding(HUD.INSTANCE.field2375.getValue());
            boolean var9 = HUD.INSTANCE.field2394.method419();
            if (HUD.INSTANCE.field2397 == null) {
               HUD.INSTANCE.field2397 = new RenderUtil(RenderMode.COLOR);
            }

            if (HUD.INSTANCE.field2398 == null) {
               HUD.INSTANCE.field2398 = new BaseFramebuffer(false);
            }

            if (HUD.INSTANCE.field2399 == null) {
               HUD.INSTANCE.field2399 = new BaseFramebuffer(false);
            }

            if (var9) {
               HUD.INSTANCE.field2397.method2233();
            }

            for (Module var11 : Boze.getModules().modules) {
               if (var11 instanceof HUDModule && var11.isEnabled()) {
                  double var12 = IFontRender.method499().getFontScale();
                  IFontRender.method499().setFontScale(var12 * ((HUDModule)var11).field595.getValue());
                  ((HUDModule)var11).method295(context);
                  IFontRender.method499().setFontScale(var12);
               }
            }

            if (var9) {
               HUD.INSTANCE.shader.field963.applyShader(HUDRenderer::lambda$renderHud$0, HUD.INSTANCE.field2398, HUD.INSTANCE.field2399);
            }

            for (Module var16 : Boze.getModules().modules) {
               if (var16 instanceof Class5929) {
                  Class5929 var17 = (Class5929)var16;
                  if (var16.isEnabled()) {
                     var17.method332(context);
                  }
               }
            }

            if (var14 > 0.0) {
               Scissor.enableScissor(
                  var14, var14, (double)mc.getWindow().getScaledWidth() - var14 * 2.0, (double)mc.getWindow().getScaledHeight() - var14 * 2.0
               );
            }

            RenderUtil.field3963.method2235(null);
            IFontRender.method499().endBuilding();
            if (var14 > 0.0) {
               Scissor.disableScissor();
            }

            FontLoader.field1063.method198(var7);
            FontLoader.field1064.method961(var8);
         }
      }
   }

   private static void lambda$renderHud$0() {
      HUD.INSTANCE.field2397.method2235(null);
   }
}
