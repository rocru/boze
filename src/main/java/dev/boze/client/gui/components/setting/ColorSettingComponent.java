package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.client.OldColors;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ColorSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
   private final ColorSetting field292;
   private boolean field293;
   private double field294 = 0.0;
   private double field295;
   private double field296;
   private double field297;
   private double field298;

   public ColorSettingComponent(ColorSetting setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting, parent, x, y, width, height);
      this.field292 = setting;
      this.field294 = height;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field292.method2116()) {
         this.field321 = (double)Theme.method1376() * scaleFactor;
         this.field294 = this.field321;
         super.render(context, mouseX, mouseY, delta);
         if (this.field293) {
            this.field321 = this.field321 + this.field320;
         }

         RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field272 = Theme.method1348());
         IFontRender.method499()
            .drawShadowedText(
               this.field316,
               this.field318 + 6.0 * scaleFactor,
               this.field319 + this.field294 / 2.0 - IFontRender.method499().method1390() / 2.0,
               Theme.method1350()
            );
         double var8 = 0.0;
         if (this.field292.hasChildren()) {
            double var10 = this.field294 / 9.0;
            double var12 = this.field318 + this.field320 - scaleFactor * 6.0 - var10;
            if (this.field292.isExpanded()) {
               RenderUtil.field3963.method2257(var12, this.field319 + var10 * 2.0, var10, var10 * 5.0, 15, 12, var10 / 2.0, Theme.method1350());
            } else {
               RenderUtil.field3963.method2261(var12, this.field319 + var10 * 2.0, var10, Theme.method1350());
               RenderUtil.field3963.method2261(var12, this.field319 + var10 * 4.0, var10, Theme.method1350());
               RenderUtil.field3963.method2261(var12, this.field319 + var10 * 6.0, var10, Theme.method1350());
            }

            var8 = scaleFactor * 6.0 + var10 / 2.0;
         }

         RenderUtil.field3965
            .method2253(
               this.field318 + this.field320 - scaleFactor * 12.0 - var8,
               this.field319 + this.field294 * 0.5 - scaleFactor * 3.0,
               scaleFactor * 6.0,
               scaleFactor * 6.0,
               this.field292.getValue()
            );
         if (!this.field292.equals(OldColors.INSTANCE.clientGradient)) {
            Notifications.SYNC
               .render(
                  this.field295 = this.field318 + this.field320 - scaleFactor * 16.0 - var8 - Notifications.SYNC.method2091(),
                  this.field296 = this.field319 + this.field294 * 0.5 - IconManager.method1116() * 0.5,
                  this.field292.getSync() ? Theme.method1350() : this.field272.method183(Theme.method1390())
               );
            this.field297 = Notifications.SYNC.method2091();
            this.field298 = IconManager.method1116();
         }

         if (this.field293) {
            float[] var16 = Color.RGBtoHSB(Theme.method1347().field408, Theme.method1347().field409, Theme.method1347().field410, null);
            float[] var11 = Color.RGBtoHSB(this.field292.method1374().field408, this.field292.method1374().field409, this.field292.method1374().field410, null);
            if (Theme.method1382()) {
               RenderUtil.field3965
                  .method2253(
                     this.field318 + scaleFactor * 5.0,
                     this.field319 + this.field294,
                     this.field320 - scaleFactor * 24.0,
                     this.field320 - scaleFactor * 18.0,
                     Theme.method1383()
                  );
            }

            RenderUtil.field3966
               .method2255(
                  this.field318 + scaleFactor * 6.0,
                  this.field319 + this.field294 + scaleFactor,
                  this.field320 - scaleFactor * 26.0,
                  this.field320 - scaleFactor * 20.0,
                  new float[]{var11[0], 0.0F, 1.0F, 1.0F},
                  new float[]{var11[0], 1.0F, 1.0F, 1.0F},
                  new float[]{var11[0], 1.0F, 0.0F, 1.0F},
                  new float[]{var11[0], 0.0F, 0.0F, 1.0F}
               );
            RenderUtil.field3966
               .method2263(
                  this.field318 + scaleFactor * 6.0 + (double)var11[1] * (this.field320 - scaleFactor * 26.0),
                  this.field319 + this.field294 + (double)(1.0F - var11[2]) * (this.field320 - scaleFactor * 20.0),
                  scaleFactor * 2.0,
                  var16
               );
            float[] var17 = new float[]{0.0F, 1.0F, 1.0F, 1.0F};
            float[] var13 = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
            if (Theme.method1382()) {
               RenderUtil.field3965
                  .method2253(
                     this.field318 + this.field320 - scaleFactor * 15.0,
                     this.field319 + this.field294,
                     scaleFactor * 10.0,
                     this.field320 - scaleFactor * 18.0,
                     Theme.method1383()
                  );
            }

            RenderUtil.field3966
               .method2255(
                  this.field318 + this.field320 - scaleFactor * 14.0,
                  this.field319 + this.field294 + scaleFactor,
                  scaleFactor * 8.0,
                  this.field320 - scaleFactor * 20.0,
                  var17,
                  var17,
                  var13,
                  var13
               );
            RenderUtil.field3966
               .method2256(
                  this.field318 + this.field320 - scaleFactor * 14.0,
                  Math.min(
                     this.field319 + this.field294 + scaleFactor + (this.field320 - scaleFactor * 20.0) * (double)var11[0],
                     this.field319 + this.field294 + this.field320 - scaleFactor * 21.0
                  ),
                  scaleFactor * 8.0,
                  scaleFactor * 2.0,
                  var16
               );
            float[] var14 = new float[]{var11[0], var11[1], var11[2], 0.0F};
            float[] var15 = new float[]{var11[0], var11[1], var11[2], 1.0F};
            if (Theme.method1382()) {
               RenderUtil.field3965
                  .method2253(
                     this.field318 + scaleFactor * 5.0,
                     this.field319 + this.field321 - scaleFactor * 15.0,
                     this.field320 - scaleFactor * 10.0,
                     scaleFactor,
                     Theme.method1383()
                  );
               RenderUtil.field3965
                  .method2253(
                     this.field318 + scaleFactor * 5.0,
                     this.field319 + this.field321 - scaleFactor * 6.0,
                     this.field320 - scaleFactor * 10.0,
                     scaleFactor,
                     Theme.method1383()
                  );
               RenderUtil.field3965
                  .method2253(
                     this.field318 + scaleFactor * 5.0, this.field319 + this.field321 - scaleFactor * 14.0, scaleFactor, scaleFactor * 8.0, Theme.method1383()
                  );
               RenderUtil.field3965
                  .method2253(
                     this.field318 - scaleFactor * 6.0 + this.field320,
                     this.field319 + this.field321 - scaleFactor * 14.0,
                     scaleFactor,
                     scaleFactor * 8.0,
                     Theme.method1383()
                  );
            }

            RenderUtil.field3966
               .method2255(
                  this.field318 + 6.0 * scaleFactor,
                  this.field319 + this.field321 - scaleFactor * 14.0,
                  this.field320 - scaleFactor * 12.0,
                  scaleFactor * 8.0,
                  var14,
                  var15,
                  var15,
                  var14
               );
            RenderUtil.field3966
               .method2256(
                  Math.min(
                     this.field318 + 6.0 * scaleFactor + (this.field320 - scaleFactor * 12.0) * (double)((float)this.field292.method1374().field411 / 255.0F),
                     this.field318 - 8.0 * scaleFactor + this.field320
                  ),
                  this.field319 + this.field321 - scaleFactor * 14.0,
                  scaleFactor * 2.0,
                  scaleFactor * 8.0,
                  var16
               );
         }
      } else {
         this.field321 = 0.0;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.field292.method2116()) {
         if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field294)) {
            if (button == 0) {
               if (isMouseWithinBounds(mouseX, mouseY, this.field295, this.field296, this.field297, this.field298)
                  && !this.field292.equals(OldColors.INSTANCE.clientGradient)) {
                  this.field292.setSync(!this.field292.getSync());
                  mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                  return true;
               }

               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               ClickGUI.field1335.method580(new dev.boze.client.gui.components.scaled.ColorSettingComponent(this.field292));
               return true;
            }

            if (button == 1) {
               if (this.field292.hasChildren()) {
                  this.field292.setExpanded(!this.field292.isExpanded());
               } else if (!this.field292.getSync()) {
                  this.field293 = !this.field293;
               }

               return true;
            }
         } else if (this.field293) {
            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + scaleFactor * 6.0,
               this.field319 + this.field294 + scaleFactor,
               this.field320 - scaleFactor * 26.0,
               this.field320 - scaleFactor * 20.0
            )) {
               double var20 = mouseX - (this.field318 + scaleFactor * 6.0);
               double var22 = mouseY - (this.field319 + this.field294 + scaleFactor);
               double var23 = var20 / (this.field320 - scaleFactor * 26.0);
               double var15 = var22 / (this.field320 - scaleFactor * 20.0);
               float[] var17 = Color.RGBtoHSB(
                  this.field292.method1374().field408, this.field292.method1374().field409, this.field292.method1374().field410, null
               );
               int[] var18 = RGBAColor.method190((double)var17[0] * 360.0, (double)((float)var23), (double)(1.0F - (float)var15));
               this.field292.method1374().method192(var18[0], var18[1], var18[2], this.field292.method1374().field411);
               return true;
            }

            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + this.field320 - scaleFactor * 14.0,
               this.field319 + this.field294 + scaleFactor,
               scaleFactor * 8.0,
               this.field320 - scaleFactor * 20.0
            )) {
               double var19 = mouseY - (this.field319 + this.field294 + scaleFactor);
               double var21 = var19 / (this.field320 - scaleFactor * 20.0);
               float[] var13 = Color.RGBtoHSB(
                  this.field292.method1374().field408, this.field292.method1374().field409, this.field292.method1374().field410, null
               );
               int[] var14 = RGBAColor.method190(var21 * 360.0, (double)var13[1], (double)var13[2]);
               this.field292.method1374().method192(var14[0], var14[1], var14[2], this.field292.method1374().field411);
               return true;
            }

            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field318 + 6.0 * scaleFactor,
               this.field319 + this.field321 - scaleFactor * 14.0,
               this.field320 - scaleFactor * 12.0,
               scaleFactor * 8.0
            )) {
               double var9 = mouseX - (this.field318 + 6.0 * scaleFactor);
               double var11 = var9 / (this.field320 - scaleFactor * 12.0);
               this.field292.method1374().field411 = MathHelper.clamp((int)(var11 * 255.0), 0, 255);
               return true;
            }
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.field292.method2116() && this.field293) {
         if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field318 + scaleFactor * 6.0,
            this.field319 + this.field294 + scaleFactor,
            this.field320 - scaleFactor * 26.0,
            this.field320 - scaleFactor * 20.0
         )) {
            double var24 = mouseX - (this.field318 + scaleFactor * 6.0);
            double var26 = mouseY - (this.field319 + this.field294 + scaleFactor);
            double var27 = var24 / (this.field320 - scaleFactor * 26.0);
            double var19 = var26 / (this.field320 - scaleFactor * 20.0);
            float[] var21 = Color.RGBtoHSB(this.field292.method1374().field408, this.field292.method1374().field409, this.field292.method1374().field410, null);
            int[] var22 = RGBAColor.method190((double)var21[0] * 360.0, (double)((float)var27), (double)(1.0F - (float)var19));
            this.field292.method1374().method192(var22[0], var22[1], var22[2], this.field292.method1374().field411);
            return true;
         }

         if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field318 + this.field320 - scaleFactor * 14.0,
            this.field319 + this.field294 + scaleFactor,
            scaleFactor * 8.0,
            this.field320 - scaleFactor * 20.0
         )) {
            double var23 = mouseY - (this.field319 + this.field294 + scaleFactor);
            double var25 = var23 / (this.field320 - scaleFactor * 20.0);
            float[] var17 = Color.RGBtoHSB(this.field292.method1374().field408, this.field292.method1374().field409, this.field292.method1374().field410, null);
            int[] var18 = RGBAColor.method190(var25 * 360.0, (double)var17[1], (double)var17[2]);
            this.field292.method1374().method192(var18[0], var18[1], var18[2], this.field292.method1374().field411);
            return true;
         }

         if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field318 + 6.0 * scaleFactor,
            this.field319 + this.field321 - scaleFactor * 14.0,
            this.field320 - scaleFactor * 12.0,
            scaleFactor * 8.0
         )) {
            double var13 = mouseX - (this.field318 + 6.0 * scaleFactor);
            double var15 = var13 / (this.field320 - scaleFactor * 12.0);
            this.field292.method1374().field411 = MathHelper.clamp((int)(var15 * 255.0), 0, 255);
            return true;
         }
      }

      return false;
   }
}
