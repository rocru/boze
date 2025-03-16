package dev.boze.client.gui.components;

import dev.boze.client.font.IFontRender;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public abstract class IntSliderComponent extends InputBaseComponent {
   public IntSliderComponent(String name, double x, double y, double width, double height) {
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
      IFontRender.method499()
         .drawShadowedText(
            this.method2010() + "",
            this.field1133 + this.field1135 - 6.0 * field1131 - IFontRender.method499().method501(this.method2010() + ""),
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
      RGBAColor var7 = this.method1347();
      RenderUtil.field3963
         .method2257(
            this.field1133 + 12.0 * field1131,
            this.field1134 + this.field1136 * 0.75 - field1131,
            this.field1135 - 24.0 * field1131,
            field1131 * 2.0,
            15,
            12,
            field1131,
            var7.method2025(Theme.method1391())
         );
      RenderUtil.field3963
         .method2257(
            this.field1133 + 12.0 * field1131,
            this.field1134 + this.field1136 * 0.75 - field1131,
            (this.field1135 - 26.0 * field1131)
                  * MathHelper.clamp((double)(this.method2010() - this.method1547()) / (double)(this.method1365() - this.method1547()), 0.0, 1.0)
               + field1131 * 2.0,
            field1131 * 2.0,
            15,
            12,
            field1131,
            var7
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
               var11 *= (double)(this.method1365() - this.method1547());
               var11 += (double)this.method1547();
               this.method1649((int)var11);
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
               this.method1649(MathHelper.clamp(this.method2010() - 1, this.method1547(), this.method1365()));
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
               this.method1649(MathHelper.clamp(this.method2010() + 1, this.method1547(), this.method1365()));
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
         var15 *= (double)(this.method1365() - this.method1547());
         var15 += (double)this.method1547();
         this.method1649((int)var15);
         return true;
      } else {
         return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   protected abstract void method1649(int var1);

   protected abstract int method2010();

   protected abstract int method1547();

   protected abstract int method1365();

   protected abstract void method2142();

   protected RGBAColor method1347() {
      return Theme.method1352();
   }
}
