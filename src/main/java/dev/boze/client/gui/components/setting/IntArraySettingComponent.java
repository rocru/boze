package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import java.text.DecimalFormat;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class IntArraySettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
   private final IntArraySetting field287;
   private int field288 = -1;
   private double[] field289 = null;
   private double field290 = 0.0;

   public IntArraySettingComponent(IntArraySetting setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting, parent, x, y, width, height);
      this.field287 = setting;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field287.method2116()) {
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
         this.field290 = 0.0;
         if (this.field287.hasChildren()) {
            double var8 = this.field321 / 18.0;
            double var10 = this.field318 + this.field320 - scaleFactor * 6.0 - var8;
            if (this.field287.isExpanded()) {
               RenderUtil.field3963.method2257(var10, this.field319 + var8 * 2.0, var8, var8 * 5.0, 15, 12, var8 / 2.0, Theme.method1350());
            } else {
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 2.0, var8, Theme.method1350());
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 4.0, var8, Theme.method1350());
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 6.0, var8, Theme.method1350());
            }

            this.field290 = scaleFactor * 6.0 + var8 / 2.0;
         }

         DecimalFormat var17 = new DecimalFormat("#.###");
         String var9 = var17.format((long)this.field287.method410()[this.field287.method1365()]);
         if ((double)this.field287.method410()[this.field287.method1365()] >= 0.0 && var9.startsWith("-")) {
            var9 = var9.substring(1);
         }

         String var18 = var17.format((long)this.field287.method410()[this.field287.method1366()]);
         if ((double)this.field287.method410()[this.field287.method1366()] >= 0.0 && var18.startsWith("-")) {
            var18 = var18.substring(1);
         }

         String var11 = var9 + "-" + var18;
         IFontRender.method499()
            .drawShadowedText(
               var11,
               this.field318 + this.field320 - 6.0 * scaleFactor - this.field290 - IFontRender.method499().method501(var11),
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
               this.field318 + this.field320 - 6.0 * scaleFactor - IFontRender.method499().method501("+"),
               this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0,
               Theme.method1350()
            );
         RGBAColor var12 = Theme.method1352();
         RenderUtil.field3963
            .method2257(
               this.field318 + 12.0 * scaleFactor,
               this.field319 + this.field321 * 0.75 - scaleFactor,
               this.field320 - 24.0 * scaleFactor,
               scaleFactor * 2.0,
               15,
               12,
               scaleFactor,
               var12.method2025(Theme.method1391())
            );
         double var13 = MathHelper.clamp(
            (double)(this.field287.method410()[this.field287.method1365()] - this.field287.field920)
               / (double)(this.field287.field921 - this.field287.field920),
            0.0,
            1.0
         );
         double var15 = MathHelper.clamp(
            (double)(this.field287.method410()[this.field287.method1366()] - this.field287.field920)
               / (double)(this.field287.field921 - this.field287.field920),
            0.0,
            1.0
         );
         RenderUtil.field3963
            .method2257(
               this.field318 + 12.0 * scaleFactor + var13 * (this.field320 - 26.0 * scaleFactor),
               this.field319 + this.field321 * 0.75 - scaleFactor,
               (this.field320 - 26.0 * scaleFactor) * (var15 - var13) + scaleFactor * 2.0,
               scaleFactor * 2.0,
               15,
               12,
               scaleFactor,
               var12
            );
      } else {
         this.field321 = 0.0;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321) && this.field287.method2116()) {
         this.field273.reset();
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
               double var13 = (double)(this.field287.field921 - this.field287.field920);
               double var15 = (double)(this.field287.method410()[this.field287.method1365()] - this.field287.field920) / var13;
               double var17 = (double)(this.field287.method410()[this.field287.method1366()] - this.field287.field920) / var13;
               double var19 = var17 - var15;
               if (var11 <= var15 + var19 * 0.15 && var11 < var17 - var19 * 0.15) {
                  this.field288 = this.field287.method1365();
                  this.field287.method413(this.field287.method1365(), (int)(var11 * var13 + (double)this.field287.field920));
               } else if (var11 >= var17 - var19 * 0.15 && var11 > var15 + var19 * 0.15) {
                  this.field288 = this.field287.method1366();
                  this.field287.method413(this.field287.method1366(), (int)(var11 * var13 + (double)this.field287.field920));
               } else {
                  this.field288 = -2;
                  this.field289 = new double[]{(double)this.field287.method410()[0], (double)this.field287.method410()[1]};
               }

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
               this.field287
                  .method413(0, MathHelper.clamp(this.field287.method410()[0] - this.field287.field922, this.field287.field920, this.field287.field921));
               this.field287
                  .method413(1, MathHelper.clamp(this.field287.method410()[1] - this.field287.field922, this.field287.field920, this.field287.field921));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            } else if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + this.field320 - 7.0 * scaleFactor - this.field290 - IFontRender.method499().method501("+"),
               this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0 - scaleFactor,
               IFontRender.method499().method501("+") + 2.0 * scaleFactor,
               IFontRender.method499().method1390() + 2.0 * scaleFactor
            )) {
               this.field287
                  .method413(0, MathHelper.clamp(this.field287.method410()[0] + this.field287.field922, this.field287.field920, this.field287.field921));
               this.field287
                  .method413(1, MathHelper.clamp(this.field287.method410()[1] + this.field287.field922, this.field287.field920, this.field287.field921));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
         } else if (button == 1 && this.field287.hasChildren()) {
            this.field287.setExpanded(!this.field287.isExpanded());
            return true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (button == 0
         && this.field287.method2116()
         && this.field288 != -1
         && isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field318 + 12.0 * scaleFactor,
            this.field319 + this.field321 * 0.75 - scaleFactor * 3.0,
            this.field320 - 24.0 * scaleFactor,
            scaleFactor * 6.0
         )) {
         this.field273.reset();
         if (this.field288 == -2) {
            double var13 = deltaX / (this.field320 - 24.0 * scaleFactor);
            var13 *= (double)(this.field287.field921 - this.field287.field920);
            this.field289[0] = this.field289[0] + var13;
            this.field289[1] = this.field289[1] + var13;
            this.field287.method413(0, (int)this.field289[0]);
            this.field287.method413(1, (int)this.field289[1]);
         } else {
            double var18 = mouseX - (this.field318 + 12.0 * scaleFactor);
            double var15 = var18 / (this.field320 - 24.0 * scaleFactor);
            var15 *= (double)(this.field287.field921 - this.field287.field920);
            var15 += (double)this.field287.field920;
            this.field287.method413(this.field288, (int)var15);
         }

         this.field273.reset();
         return true;
      } else {
         return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
      }
   }
}
