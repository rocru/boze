package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.gui.components.scaled.SetFloatValueComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.math.NumberUtils;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;

public class FloatSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
   private final FloatSetting field302;
   private double field303 = 0.0;

   public FloatSettingComponent(FloatSetting setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting, parent, x, y, width, height);
      this.field302 = setting;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field302.method2116()) {
         this.field321 = ((double)Theme.method1376() + 4.0) * scaleFactor;
         super.render(context, mouseX, mouseY, delta);
         RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field272 = Theme.method1348());
         IFontRender.method499()
            .drawShadowedText(
               this.field316,
               this.field318 + 6.0 * scaleFactor,
               this.field319 + this.field321 * 0.33 - IFontRender.method499().method1390() / 2.0,
               Theme.method1350()
            );
         this.field303 = 0.0;
         if (this.field302.hasChildren()) {
            double var8 = this.field321 / 18.0;
            double var10 = this.field318 + this.field320 - scaleFactor * 6.0 - var8;
            if (this.field302.isExpanded()) {
               RenderUtil.field3963.method2257(var10, this.field319 + var8 * 2.0, var8, var8 * 5.0, 15, 12, var8 / 2.0, Theme.method1350());
            } else {
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 2.0, var8, Theme.method1350());
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 4.0, var8, Theme.method1350());
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 6.0, var8, Theme.method1350());
            }

            this.field303 = scaleFactor * 6.0 + var8 / 2.0;
         }

         DecimalFormat var12 = new DecimalFormat("#.###");
         String var9 = var12.format(this.field302.getValue());
         if (this.field302.getValue() >= 0.0F && var9.startsWith("-")) {
            var9 = var9.substring(1);
         }

         IFontRender.method499()
            .drawShadowedText(
               var9,
               this.field318 + this.field320 - 6.0 * scaleFactor - this.field303 - IFontRender.method499().method501(var9),
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
               this.field318 + this.field320 - 6.0 * scaleFactor - this.field303 - IFontRender.method499().method501("+"),
               this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0,
               Theme.method1350()
            );
         RGBAColor var13 = Theme.method1352();
         RenderUtil.field3963
            .method2257(
               this.field318 + 12.0 * scaleFactor,
               this.field319 + this.field321 * 0.75 - scaleFactor,
               this.field320 - 24.0 * scaleFactor - this.field303,
               scaleFactor * 2.0,
               15,
               12,
               scaleFactor,
               var13.method2025(Theme.method1391())
            );
         RenderUtil.field3963
            .method2257(
               this.field318 + 12.0 * scaleFactor,
               this.field319 + this.field321 * 0.75 - scaleFactor,
               (this.field320 - 26.0 * scaleFactor)
                     * (double)MathHelper.clamp(
                        (this.field302.getValue() - this.field302.field930) / (this.field302.field931 - this.field302.field930), 0.0F, 1.0F
                     )
                  + scaleFactor * 2.0,
               scaleFactor * 2.0,
               15,
               12,
               scaleFactor,
               var13
            );
      } else {
         this.field321 = 0.0;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321) && this.field302.method2116()) {
         this.field273.reset();
         if (button == 0) {
            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + 12.0 * scaleFactor,
               this.field319 + this.field321 * 0.75 - scaleFactor * 2.0,
               this.field320 - 24.0 * scaleFactor - this.field303,
               scaleFactor * 4.0
            )) {
               double var9 = mouseX - (this.field318 + 12.0 * scaleFactor);
               double var11 = var9 / (this.field320 - 24.0 * scaleFactor - this.field303);
               var11 *= (double)(this.field302.field931 - this.field302.field930);
               var11 += (double)this.field302.field930;
               this.field302.setValue(NumberUtils.method2198((float)var11, this.field302.field932));
            } else if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + 5.0 * scaleFactor,
               this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0 - scaleFactor,
               IFontRender.method499().method501("-") + 2.0 * scaleFactor,
               IFontRender.method499().method1390() + 2.0 * scaleFactor
            )) {
               this.field302.setValue(MathHelper.clamp(this.field302.getValue() - this.field302.field932, this.field302.field930, this.field302.field931));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            } else if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + this.field320 - 7.0 * scaleFactor - this.field303 - IFontRender.method499().method501("+"),
               this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0 - scaleFactor,
               IFontRender.method499().method501("+") + 2.0 * scaleFactor,
               IFontRender.method499().method1390() + 2.0 * scaleFactor
            )) {
               this.field302.setValue(MathHelper.clamp(this.field302.getValue() + this.field302.field932, this.field302.field930, this.field302.field931));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }

            return true;
         }

         if (button == 1 && this.field302.hasChildren()) {
            this.field302.setExpanded(!this.field302.isExpanded());
            return true;
         }

         if (button == 2) {
            ClickGUI.field1335.method580(new SetFloatValueComponent(this.field302));
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (button == 0
         && this.field302.method2116()
         && isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field318 + 12.0 * scaleFactor,
            this.field319 + this.field321 * 0.75 - scaleFactor * 2.0,
            this.field320 - 24.0 * scaleFactor - this.field303,
            scaleFactor * 4.0
         )) {
         this.field273.reset();
         double var13 = mouseX - (this.field318 + 12.0 * scaleFactor);
         double var15 = var13 / (this.field320 - 24.0 * scaleFactor - this.field303);
         var15 *= (double)(this.field302.field931 - this.field302.field930);
         var15 += (double)this.field302.field930;
         this.field302.setValue(NumberUtils.method2198((float)var15, this.field302.field932));
         this.field273.reset();
         return true;
      } else {
         return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
      }
   }
}
