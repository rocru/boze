package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.settings.MinMaxDoubleSetting;
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

public class MinMaxDoubleSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
   private final MinMaxDoubleSetting field307;
   private int field308 = -1;
   private double field309 = 0.0;

   public MinMaxDoubleSettingComponent(MinMaxDoubleSetting setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting, parent, x, y, width, height);
      this.field307 = setting;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field307.method2116()) {
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
         this.field309 = 0.0;
         if (this.field307.hasChildren()) {
            double var8 = this.field321 / 18.0;
            double var10 = this.field318 + this.field320 - scaleFactor * 6.0 - var8;
            if (this.field307.isExpanded()) {
               RenderUtil.field3963.method2257(var10, this.field319 + var8 * 2.0, var8, var8 * 5.0, 15, 12, var8 / 2.0, Theme.method1350());
            } else {
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 2.0, var8, Theme.method1350());
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 4.0, var8, Theme.method1350());
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 6.0, var8, Theme.method1350());
            }

            this.field309 = scaleFactor * 6.0 + var8 / 2.0;
         }

         DecimalFormat var17 = new DecimalFormat("#.###");
         String var9 = var17.format(this.field307.method1287()[this.field307.method1291()]);
         if (this.field307.method1287()[this.field307.method1291()] >= 0.0 && var9.startsWith("-")) {
            var9 = var9.substring(1);
         }

         String var18 = var17.format(this.field307.method1287()[this.field307.method1292()]);
         if (this.field307.method1287()[this.field307.method1292()] >= 0.0 && var18.startsWith("-")) {
            var18 = var18.substring(1);
         }

         String var11 = var9 + "-" + var18;
         IFontRender.method499()
            .drawShadowedText(
               var11,
               this.field318 + this.field320 - 6.0 * scaleFactor - this.field309 - IFontRender.method499().method501(var11),
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
            (this.field307.method1287()[this.field307.method1291()] - this.field307.field2201) / (this.field307.field2202 - this.field307.field2201), 0.0, 1.0
         );
         double var15 = MathHelper.clamp(
            (this.field307.method1287()[this.field307.method1292()] - this.field307.field2201) / (this.field307.field2202 - this.field307.field2201), 0.0, 1.0
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
      if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321) && this.field307.method2116()) {
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
               double var13 = this.field307.field2202 - this.field307.field2201;
               double var15 = (this.field307.method1287()[this.field307.method1291()] - this.field307.field2201) / var13;
               double var17 = (this.field307.method1287()[this.field307.method1292()] - this.field307.field2201) / var13;
               double var19 = var17 - var15;
               if (var11 <= var15 + var19 * 0.15 && var11 < var17 - var19 * 0.15) {
                  this.field308 = this.field307.method1291();
                  this.field307
                     .method1290(this.field307.method1291(), NumberUtils.method2196(var11 * var13 + this.field307.field2201, this.field307.field2203));
               } else if (var11 >= var17 - var19 * 0.15 && var11 > var15 + var19 * 0.15) {
                  this.field308 = this.field307.method1292();
                  this.field307
                     .method1290(this.field307.method1292(), NumberUtils.method2196(var11 * var13 + this.field307.field2201, this.field307.field2203));
               } else {
                  this.field308 = -2;
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
               this.field307
                  .method1290(0, MathHelper.clamp(this.field307.method1287()[0] - this.field307.field2203, this.field307.field2201, this.field307.field2202));
               this.field307
                  .method1290(1, MathHelper.clamp(this.field307.method1287()[1] - this.field307.field2203, this.field307.field2201, this.field307.field2202));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            } else if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + this.field320 - 7.0 * scaleFactor - this.field309 - IFontRender.method499().method501("+"),
               this.field319 + this.field321 * 0.7 - IFontRender.method499().method1390() / 2.0 - scaleFactor,
               IFontRender.method499().method501("+") + 2.0 * scaleFactor,
               IFontRender.method499().method1390() + 2.0 * scaleFactor
            )) {
               this.field307
                  .method1290(0, MathHelper.clamp(this.field307.method1287()[0] + this.field307.field2203, this.field307.field2201, this.field307.field2202));
               this.field307
                  .method1290(1, MathHelper.clamp(this.field307.method1287()[1] + this.field307.field2203, this.field307.field2201, this.field307.field2202));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
         } else if (button == 1 && this.field307.hasChildren()) {
            this.field307.setExpanded(!this.field307.isExpanded());
            return true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (button == 0
         && this.field307.method2116()
         && this.field308 != -1
         && isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field318 + 12.0 * scaleFactor,
            this.field319 + this.field321 * 0.75 - scaleFactor * 3.0,
            this.field320 - 24.0 * scaleFactor,
            scaleFactor * 6.0
         )) {
         this.field273.reset();
         if (this.field308 == -2) {
            double var13 = deltaX / (this.field320 - 24.0 * scaleFactor);
            var13 *= this.field307.field2202 - this.field307.field2201;
            this.field307.method1290(0, NumberUtils.method2196(this.field307.method1287()[0] + var13, this.field307.field2203));
            this.field307.method1290(1, NumberUtils.method2196(this.field307.method1287()[1] + var13, this.field307.field2203));
         } else {
            double var18 = mouseX - (this.field318 + 12.0 * scaleFactor);
            double var15 = var18 / (this.field320 - 24.0 * scaleFactor);
            var15 *= this.field307.field2202 - this.field307.field2201;
            var15 += this.field307.field2201;
            this.field307.method1290(this.field308, NumberUtils.method2196(var15, this.field307.field2203));
         }

         this.field273.reset();
         return true;
      } else {
         return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
      }
   }
}
