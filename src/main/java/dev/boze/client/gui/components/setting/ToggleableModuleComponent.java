package dev.boze.client.gui.components.setting;

import dev.boze.api.addon.module.ToggleableModule;
import dev.boze.api.setting.SettingBind;
import dev.boze.client.api.BozeBind;
import dev.boze.client.enums.ModuleDisplayMode;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.SettingBaseComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.KeyboardUtil;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class3077;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class ToggleableModuleComponent extends SettingBaseComponent implements IMinecraft {
   private final ToggleableModule field398;
   private boolean field399 = false;

   public ToggleableModuleComponent(ToggleableModule module, BaseComponent parent, double x, double y, double width, double height) {
      super((SettingBind)module.getBind(), parent, x, y, width, height);
      this.field398 = module;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (Theme.method1370() == ModuleDisplayMode.Bind) {
         this.field321 = 0.0;
      } else {
         this.field321 = (double)Theme.method1376() * scaleFactor;
         super.render(context, mouseX, mouseY, delta);
         RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field395 = Theme.method1348());
         IFontRender.method499()
            .drawShadowedText(
               this.field316,
               this.field318 + 6.0 * scaleFactor,
               this.field319 + this.field321 / 2.0 - IFontRender.method499().method1390() / 2.0,
               Theme.method1350()
            );
         String var10;
         if (this.field399) {
            var10 = "...";
         } else if (this.field398.getBind().getBind() == -1) {
            var10 = " ";
         } else {
            var10 = this.field398.getBind().isButton()
               ? KeyboardUtil.getButtonName(this.field398.getBind().getBind())
               : KeyboardUtil.getKeyName(this.field398.getBind().getBind());
         }

         double var11 = this.field398.getBind().getBind() == -1
            ? IFontRender.method499().method1390() - scaleFactor * 2.0
            : IFontRender.method499().method501(var10);
         double var13 = IFontRender.method499().method1390();
         double var15 = Math.max(var11, var13);
         RenderUtil.field3963
            .method2257(
               this.field318 + this.field320 - 0.0 - (var15 + scaleFactor * (double)(var15 > var11 ? 2 : 4)) - scaleFactor * 6.0,
               this.field319 + this.field321 * 0.5 - (var13 * 0.5 + scaleFactor),
               var15 + scaleFactor * (double)(var15 > var11 ? 2 : 4),
               var13 + scaleFactor * 2.0,
               15,
               24,
               scaleFactor * 4.0,
               Theme.method1348().method183(Theme.method1390())
            );
         IFontRender.method499()
            .drawShadowedText(
               var10,
               this.field318 + this.field320 - 0.0 - (var15 + scaleFactor * (double)(var15 > var11 ? 2 : 4)) * 0.5 - scaleFactor * 6.0 - var11 / 2.0,
               this.field319 + this.field321 * 0.5 - var13 * 0.5,
               Theme.method1350()
            );
         if (isMouseWithinBounds(
            (double)mouseX,
            (double)mouseY,
            this.field318 + this.field320 - 0.0 - (var15 + scaleFactor * (double)(var15 > var11 ? 2 : 4)) - scaleFactor * 6.0,
            this.field319 + this.field321 * 0.5 - (var13 * 0.5 + scaleFactor),
            var15 + scaleFactor * (double)(var15 > var11 ? 2 : 4),
            var13 + scaleFactor * 2.0
         )) {
            ClickGUI.field1335.field1337 = CursorType.IBeam;
         }
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (Theme.method1370() == ModuleDisplayMode.Bind) {
         return false;
      } else {
         if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
            if (this.field399) {
               this.field398.setBind(new BozeBind(true, button));
               this.field399 = false;
               Class3077.field174 = false;
               return true;
            }

            if (button == 0 && !Class3077.field174) {
               this.field399 = true;
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               return true;
            }
         }

         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (Theme.method1370() == ModuleDisplayMode.Bind) {
         return false;
      } else if (this.field399) {
         if (keyCode == 256) {
            this.field398.setBind(new BozeBind(false, -1));
         } else {
            this.field398.setBind(new BozeBind(false, keyCode));
         }

         this.field399 = false;
         Class3077.field174 = false;
         return true;
      } else {
         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }
}
