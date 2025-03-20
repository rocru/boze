package dev.boze.client.gui.components.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.MacroManagerComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.settings.MacroManagerSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.Macro;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class5928;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

public class ChooseMacroComponent extends ScaledBaseComponent {
   private final MacroManagerSetting field1409;
   private String field1410;

   public ChooseMacroComponent(MacroManagerSetting setting) {
      super(setting.name, 0.2, 0.2);
      this.field1409 = setting;
      this.field1410 = "";
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      RenderUtil.field3963.method2233();
      IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.834);
      RenderUtil.field3963
         .method2257(
            this.field1388,
            this.field1389,
            this.field1390,
            this.field1391,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1349()
         );
      if (Theme.method1382()) {
         ClickGUI.field1335
            .field1333
            .method2257(
               this.field1388,
               this.field1389,
               this.field1390,
               this.field1391,
               15,
               24,
               Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
               RGBAColor.field402
            );
      }

      IFontRender.method499()
         .drawShadowedText(
            "Choose Macro Name",
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Choose Macro Name") * 0.5,
            this.field1389 + this.field1391 * 0.15 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      double var8 = this.field1388 + this.field1390 * 0.25 - IFontRender.method499().method501("Continue") * 0.5;
      String var10 = this.field1410 + (System.currentTimeMillis() % 1500L < 750L ? "|" : "");
      RenderUtil.field3963
         .method2257(
            var8 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 * 0.5 - IFontRender.method499().method1390() - BaseComponent.scaleFactor * 3.0,
            this.field1390 - (this.field1390 * 0.25 - IFontRender.method499().method501("Cancel") * 0.5 - BaseComponent.scaleFactor * 3.0) * 2.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1348()
         );
      IFontRender.method499().drawShadowedText(var10, var8, this.field1389 + this.field1391 * 0.5 - IFontRender.method499().method1390(), Theme.method1350());
      if (isMouseWithinBounds(
         (double)mouseX,
         (double)mouseY,
         var8 - BaseComponent.scaleFactor * 3.0,
         this.field1389 + this.field1391 * 0.5 - IFontRender.method499().method1390() - BaseComponent.scaleFactor * 3.0,
         this.field1390 - (this.field1390 * 0.25 - IFontRender.method499().method501("Cancel") * 0.5 - BaseComponent.scaleFactor * 3.0) * 2.0,
         IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0
      )) {
         ClickGUI.field1335.field1337 = CursorType.IBeam;
      }

      RenderUtil.field3963
         .method2257(
            var8 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0 - BaseComponent.scaleFactor * 3.0,
            IFontRender.method499().method501("Continue") + BaseComponent.scaleFactor * 6.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      RenderUtil.field3963
         .method2257(
            this.field1388 + this.field1390 * 0.75 - IFontRender.method499().method501("Continue") * 0.5 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0 - BaseComponent.scaleFactor * 3.0,
            IFontRender.method499().method501("Continue") + BaseComponent.scaleFactor * 6.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Cancel",
            this.field1388 + this.field1390 * 0.25 - IFontRender.method499().method501("Cancel") * 0.5,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Continue",
            this.field1388 + this.field1390 * 0.75 - IFontRender.method499().method501("Continue") * 0.5,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      RenderUtil.field3963.method2235(context);
      IFontRender.method499().endBuilding();
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.834, true);
         double var9 = this.field1388 + this.field1390 * 0.25 - IFontRender.method499().method501("Continue") * 0.5;
         if (isMouseWithinBounds(
            mouseX,
            mouseY,
            var9 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0 - BaseComponent.scaleFactor * 3.0,
            IFontRender.method499().method501("Continue") + BaseComponent.scaleFactor * 6.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0
         )) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new MacroManagerComponent(this.field1409));
         } else if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field1388 + this.field1390 * 0.75 - IFontRender.method499().method501("Continue") * 0.5 - BaseComponent.scaleFactor * 3.0,
               this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0 - BaseComponent.scaleFactor * 3.0,
               IFontRender.method499().method501("Continue") + BaseComponent.scaleFactor * 6.0,
               IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0
            )
            && this.field1410.length() > 0) {
            try {
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               Macro var11 = new Macro();
               var11.field1048 = this.field1410;
               this.field1409.getValue().field2140.add(var11);
               ClickGUI.field1335.method580(new BindComponent(var11, this.field1409));
            } catch (Exception var12) {
               ChatInstance.method626("Error creating macro");
               ClickGUI.field1335.method580(new MacroManagerComponent(this.field1409));
            }
         }

         IFontRender.method499().endBuilding();
         return true;
      } else {
         return super.isMouseOver(mouseX, mouseY, button);
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      try {
         if (keyCode == 259) {
            this.field1410 = this.field1410.substring(0, this.field1410.length() - 1);
         } else if (keyCode == 257 && this.field1410.length() > 0) {
            try {
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               Macro var10 = new Macro();
               var10.field1048 = this.field1410;
               this.field1409.getValue().field2140.add(var10);
               ClickGUI.field1335.method580(new BindComponent(var10, this.field1409));
            } catch (Exception var8) {
               ChatInstance.method626("Error creating macro");
               ClickGUI.field1335.method580(new MacroManagerComponent(this.field1409));
            }
         } else if (keyCode == 86 && Class5928.method159(341)) {
            String var7 = GLFW.glfwGetClipboardString(mc.getWindow().getHandle());
            if (var7 != null && !var7.isEmpty()) {
               var7.chars().forEach(this::lambda$keyPressed$0);
            }
         }
      } catch (Exception var9) {
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public void method583(char c) {
      this.field1410 = this.field1410 + c;
   }

   private void lambda$keyPressed$0(int var1) {
      this.method583((char)var1);
   }
}
