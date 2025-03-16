package dev.boze.client.gui.components;

import dev.boze.client.font.IFontRender;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.math.NumberUtils;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public abstract class RotationComponent extends InputBaseComponent {
   public RotationComponent(String name, double x, double y, double width, double height) {
      super(name, x, y, width, height);
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      IFontRender.method499()
         .drawShadowedText(
            this.field1132,
            this.field1133 + 6.0 * field1131,
            this.field1134 + this.field1136 * 0.33 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      String var8 = Integer.toString((int)(this.method2091() * 360.0));
      IFontRender.method499()
         .drawShadowedText(
            var8,
            this.field1133 + this.field1135 - 6.0 * field1131 - IFontRender.method499().method501(var8),
            this.field1134 + this.field1136 * 0.25 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      IFontRender.method499()
         .drawShadowedText(
            "-", this.field1133 + 6.0 * field1131, this.field1134 + this.field1136 * 0.7 - IFontRender.method499().method1390() / 2.0, Theme.method1350()
         );
      IFontRender.method499()
         .drawShadowedText(
            "+",
            this.field1133 + this.field1135 - 6.0 * field1131 - IFontRender.method499().method501("+"),
            this.field1134 + this.field1136 * 0.7 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      float[] var9 = new float[]{0.0F, 1.0F, 1.0F, 1.0F};
      float[] var10 = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
      RenderUtil.field3966
         .method2264(this.field1133 + 12.0 * field1131, this.field1134 + this.field1136 * 0.75 - field1131, 0.0, field1131, 180.0, 360.0, var9);
      RenderUtil.field3966
         .method2255(
            this.field1133 + 13.0 * field1131,
            this.field1134 + this.field1136 * 0.75 - field1131,
            this.field1135 - 26.0 * field1131,
            field1131 * 2.0,
            var9,
            var10,
            var10,
            var9
         );
      RenderUtil.field3966
         .method2264(
            this.field1133 + 12.0 * field1131 + (this.field1135 - 26.0 * field1131),
            this.field1134 + this.field1136 * 0.75 - field1131,
            0.0,
            field1131,
            0.0,
            180.0,
            var10
         );
      float[] var11 = new float[]{(float)this.method2091(), 1.0F, 1.0F, 1.0F};
      RenderUtil.field3966
         .method2263(
            this.field1133 + 10.0 * field1131 + (this.field1135 - 26.0 * field1131) * this.method2091(),
            this.field1134 + this.field1136 * 0.75 - field1131 * 2.0,
            field1131 * 4.0,
            var11
         );
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field1133, this.field1134, this.field1135, this.field1136)) {
         if (button == 0) {
            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field1133 + 12.0 * field1131,
               this.field1134 + this.field1136 * 0.75 - field1131 * 2.0,
               this.field1135 - 24.0 * field1131,
               field1131 * 4.0
            )) {
               double var9 = mouseX - (this.field1133 + 12.0 * field1131);
               double var11 = var9 / (this.field1135 - 24.0 * field1131);
               this.method938(MathHelper.clamp(NumberUtils.method2196(var11, this.method1614()), 0.0, 1.0));
               return true;
            }

            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field1133 + 5.0 * field1131,
               this.field1134 + this.field1136 * 0.7 - IFontRender.method499().method1390() / 2.0 - field1131,
               IFontRender.method499().method501("-") + 2.0 * field1131,
               IFontRender.method499().method1390() + 2.0 * field1131
            )) {
               this.method938(MathHelper.clamp(this.method2091() - this.method1614(), 0.0, 1.0));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               return true;
            }

            if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field1133 + this.field1135 - 7.0 * field1131 - IFontRender.method499().method501("+"),
               this.field1134 + this.field1136 * 0.7 - IFontRender.method499().method1390() / 2.0 - field1131,
               IFontRender.method499().method501("+") + 2.0 * field1131,
               IFontRender.method499().method1390() + 2.0 * field1131
            )) {
               this.method938(MathHelper.clamp(this.method2091() + this.method1614(), 0.0, 1.0));
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               return true;
            }
         } else if (button == 1
            && isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field1133 + 12.0 * field1131,
               this.field1134 + this.field1136 * 0.75 - field1131 * 2.0,
               this.field1135 - 24.0 * field1131,
               field1131 * 4.0
            )) {
            this.method2142();
            return true;
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
            this.field1133 + 12.0 * field1131,
            this.field1134 + this.field1136 * 0.75 - field1131 * 2.0,
            this.field1135 - 24.0 * field1131,
            field1131 * 4.0
         )) {
         double var13 = mouseX - (this.field1133 + 12.0 * field1131);
         double var15 = var13 / (this.field1135 - 24.0 * field1131);
         this.method938(MathHelper.clamp(NumberUtils.method2196(var15, this.method1614()), 0.0, 1.0));
         return true;
      } else {
         return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   protected abstract void method938(double var1);

   protected abstract double method2091();

   protected abstract double method1614();

   protected abstract void method2142();
}
