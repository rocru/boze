package dev.boze.client.gui.components.setting;

import dev.boze.api.setting.SettingSlider;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.SettingBaseComponent;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.math.NumberUtils;
import dev.boze.client.utils.render.RenderUtil;
import java.text.DecimalFormat;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class SettingSliderComponent extends SettingBaseComponent implements IMinecraft {
   private final SettingSlider field1302;
   private double field1303 = 0.0;

   public SettingSliderComponent(SettingSlider slider, BaseComponent parent, double x, double y, double width, double height) {
      super(slider, parent, x, y, width, height);
      this.field1302 = slider;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      this.field321 = ((double)Theme.method1376() + 4.0) * scaleFactor;
      super.render(context, mouseX, mouseY, delta);
      RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field395 = Theme.method1348());
      IFontRender.method499()
         .drawShadowedText(
            this.field316,
            this.field318 + 6.0 * scaleFactor,
            this.field319 + this.field321 * 0.33 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      this.field1303 = 0.0;
      DecimalFormat var8 = new DecimalFormat("#.###");
      String var9 = var8.format(this.field1302.getValue());
      if (this.field1302.getValue() >= 0.0 && var9.startsWith("-")) {
         var9 = var9.substring(1);
      }

      IFontRender.method499()
         .drawShadowedText(
            var9,
            this.field318 + this.field320 - 6.0 * scaleFactor - this.field1303 - IFontRender.method499().method501(var9),
            this.field319 + this.field321 * 0.25 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      IFontRender.method499()
         .drawShadowedText(
            "-", this.field318 + 6.0 * scaleFactor, this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0, Theme.method1350()
         );
      IFontRender.method499()
         .drawShadowedText(
            "+",
            this.field318 + this.field320 - 6.0 * scaleFactor - this.field1303 - IFontRender.method499().method501("+"),
            this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      RGBAColor var10 = Theme.method1352();
      RenderUtil.field3963
         .method2257(
            this.field318 + 12.0 * scaleFactor,
            this.field319 + this.field321 * 0.75 - scaleFactor,
            this.field320 - 24.0 * scaleFactor,
            scaleFactor * 2.0,
            15,
            12,
            scaleFactor,
            var10.method2025(Theme.method1391())
         );
      RenderUtil.field3963
         .method2257(
            this.field318 + 12.0 * scaleFactor,
            this.field319 + this.field321 * 0.75 - scaleFactor,
            (this.field320 - 26.0 * scaleFactor)
                  * MathHelper.clamp((this.field1302.getValue() - this.field1302.min) / (this.field1302.max - this.field1302.min), 0.0, 1.0)
               + scaleFactor * 2.0,
            scaleFactor * 2.0,
            15,
            12,
            scaleFactor,
            var10
         );
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
         this.field396.reset();
         if (button == 0) {
            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + 12.0 * scaleFactor,
               this.field319 + this.field321 * 0.75 - scaleFactor * 3.0,
               this.field320 - 24.0 * scaleFactor,
               scaleFactor * 6.0
            )) {
               double var9 = mouseX - (this.field318 + 12.0 * scaleFactor);
               double var11 = var9 / (this.field320 - 24.0 * scaleFactor);
               var11 *= this.field1302.max - this.field1302.min;
               var11 += this.field1302.min;
               this.field1302.setValue(NumberUtils.method2196(var11, this.field1302.step));
               return true;
            }

            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + 5.0 * scaleFactor,
               this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0 - scaleFactor,
               IFontRender.method499().method501("-") + 2.0 * scaleFactor,
               IFontRender.method499().method1390() + 2.0 * scaleFactor
            )) {
               this.field1302.setValue(MathHelper.clamp(this.field1302.getValue() - this.field1302.step, this.field1302.min, this.field1302.max));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            } else if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + this.field320 - 7.0 * scaleFactor - this.field1303 - IFontRender.method499().method501("+"),
               this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0 - scaleFactor,
               IFontRender.method499().method501("+") + 2.0 * scaleFactor,
               IFontRender.method499().method1390() + 2.0 * scaleFactor
            )) {
               this.field1302.setValue(MathHelper.clamp(this.field1302.getValue() + this.field1302.step, this.field1302.min, this.field1302.max));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (button == 0
         && isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field318 + 12.0 * scaleFactor,
            this.field319 + this.field321 * 0.75 - scaleFactor * 3.0,
            this.field320 - 24.0 * scaleFactor,
            scaleFactor * 6.0
         )) {
         this.field396.reset();
         double var13 = mouseX - (this.field318 + 12.0 * scaleFactor);
         double var15 = var13 / (this.field320 - 24.0 * scaleFactor);
         var15 *= this.field1302.max - this.field1302.min;
         var15 += this.field1302.min;
         this.field1302.setValue(NumberUtils.method2196(var15, this.field1302.step));
         this.field396.reset();
         return true;
      } else {
         return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
      }
   }
}
