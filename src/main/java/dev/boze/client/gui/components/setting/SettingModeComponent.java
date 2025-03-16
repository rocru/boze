package dev.boze.client.gui.components.setting;

import dev.boze.api.setting.SettingMode;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.SettingBaseComponent;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class SettingModeComponent extends SettingBaseComponent implements IMinecraft {
   private final SettingMode field1350;

   public SettingModeComponent(SettingMode modePicker, BaseComponent parent, double x, double y, double width, double height) {
      super(modePicker, parent, x, y, width, height);
      this.field1350 = modePicker;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      this.field321 = (double)Theme.method1376() * scaleFactor;
      super.render(context, mouseX, mouseY, delta);
      boolean var8 = this.field321 < 18.0;
      RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field395 = Theme.method1348());
      IFontRender.method499()
         .drawShadowedText(
            this.field316,
            this.field318 + 6.0 * scaleFactor,
            this.field319 + this.field321 * (var8 ? 0.5 : 0.25) - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      byte var11 = 6;
      double var12 = var8 ? this.field318 + 6.0 * scaleFactor + IFontRender.method499().method501(this.field316) : this.field318;
      double var14 = var8 ? this.field320 - (6.0 * scaleFactor + IFontRender.method499().method501(this.field316) + 0.0) : this.field320;
      if (Gui.INSTANCE.field2372.method419()) {
         RenderUtil.field3963
            .method2257(
               var12 + 6.0 * scaleFactor,
               this.field319 + this.field321 * (var8 ? 0.0 : 0.5) + scaleFactor,
               var14 - 12.0 * scaleFactor,
               this.field321 * (var8 ? 1.0 : 0.5) - scaleFactor * 2.0,
               15,
               24,
               scaleFactor * 4.0,
               Theme.method1348().method183(Theme.method1390())
            );
         var11 = 10;
      }

      IFontRender.method499()
         .drawShadowedText(
            "<",
            var12 + (double)var11 * scaleFactor,
            this.field319 + this.field321 * (var8 ? 0.5 : 0.75) - IFontRender.method499().method1390() / 2.0,
            Theme.method1352()
         );
      IFontRender.method499()
         .drawShadowedText(
            this.field1350.getModeName(),
            var12 + var14 * 0.5 - IFontRender.method499().method501(this.field1350.getModeName()) * 0.5,
            this.field319 + this.field321 * (var8 ? 0.5 : 0.75) - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      IFontRender.method499()
         .drawShadowedText(
            ">",
            var12 + var14 - (double)var11 * scaleFactor - IFontRender.method499().method501(">"),
            this.field319 + this.field321 * (var8 ? 0.5 : 0.75) - IFontRender.method499().method1390() / 2.0,
            Theme.method1352()
         );
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321) && button == 0) {
         this.field396.reset();
         int var9 = this.field1350.getValue();
         if (mouseX >= this.field318 + this.field320 * 0.5) {
            if (var9 + 1 < this.field1350.getModes().size()) {
               this.field1350.setValue(var9 + 1);
            } else {
               this.field1350.setValue(0);
            }
         } else if (var9 - 1 >= 0) {
            this.field1350.setValue(var9 - 1);
         } else {
            this.field1350.setValue(this.field1350.getModes().size() - 1);
         }

         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      } else {
         return super.mouseClicked(mouseX, mouseY, button);
      }
   }
}
