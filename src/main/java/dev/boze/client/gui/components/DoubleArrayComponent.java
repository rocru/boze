package dev.boze.client.gui.components;

import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public abstract class DoubleArrayComponent extends InputBaseComponent {
   public DoubleArrayComponent(String name, double x, double y, double width, double height) {
      super(name, x, y, width, height);
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (Theme.method1378()) {
         double var8 = field1131 * 2.0;
         double var10 = var8 * 4.0;
         double var12 = (this.field1135 - var10) / 5.0;
         double var14 = this.field1133 + (this.getOffset()[0] + 0.25) * 2.0 * this.field1135;
         double var16 = this.field1134 + (this.getOffset()[1] - 0.25) * -2.0 * this.field1136;

         for (int var18 = 0; var18 < 5; var18++) {
            for (int var19 = 0; var19 < 5; var19++) {
               if (isMouseWithinBounds(
                  var14,
                  var16,
                  this.field1133 + (double)var18 * (var12 + var8) - var8 * 0.5,
                  this.field1134 + (double)var19 * (var12 + var8) - var8 * 0.5,
                  var12 + var8,
                  var12 + var8
               )) {
                  RenderUtil.field3963
                     .method2252(
                        this.field1133 + (double)var18 * (var12 + var8), this.field1134 + (double)var19 * (var12 + var8), var12, var12, Theme.method1350()
                     );
               } else {
                  RenderUtil.field3963
                     .method2252(
                        this.field1133 + (double)var18 * (var12 + var8), this.field1134 + (double)var19 * (var12 + var8), var12, var12, Theme.method1348()
                     );
               }
            }
         }
      } else {
         RenderUtil.field3963.method2257(this.field1133, this.field1134, this.field1135, this.field1136, 15, 12, field1131, Theme.method1348());
         double[] var20 = this.getOffset();
         RenderUtil.field3963
            .method2257(
               this.field1133 + (var20[0] + 0.25) * 2.0 * this.field1135 - field1131,
               this.field1134 + (var20[1] - 0.25) * -2.0 * this.field1136 - field1131,
               field1131 * 2.0,
               field1131 * 2.0,
               15,
               12,
               field1131,
               Theme.method1350()
            );
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (!isMouseWithinBounds(mouseX, mouseY, this.field1133, this.field1134, this.field1135, this.field1136)) {
         return false;
      } else {
         if (button == 0) {
            if (Theme.method1378()) {
               double var9 = field1131 * 2.0;
               double var11 = var9 * 4.0;
               double var13 = (this.field1135 - var11) / 5.0;

               for (int var15 = 0; var15 < 5; var15++) {
                  for (int var16 = 0; var16 < 5; var16++) {
                     if (isMouseWithinBounds(
                        mouseX, mouseY, this.field1133 + (double)var15 * (var13 + var9), this.field1134 + (double)var16 * (var13 + var9), var13, var13
                     )) {
                        this.setPosition((double)var15 * 0.125 - 0.25, (double)var16 * -0.125 + 0.25);
                     }
                  }
               }
            } else {
               double var17 = (mouseX - this.field1133) / this.field1135;
               double var18 = (mouseY - this.field1134) / this.field1136;
               this.setPosition(var17 * 0.5 - 0.25, var18 * -0.5 + 0.25);
            }
         } else if (button == 1) {
            this.setPosition(0.0, 0.0);
         }

         return true;
      }
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field1133, this.field1134, this.field1135, this.field1136)) {
         if (button == 0 && !Theme.method1378()) {
            double var13 = (mouseX - this.field1133) / this.field1135;
            double var15 = (mouseY - this.field1134) / this.field1136;
            this.setPosition(var13 * 0.5 - 0.25, var15 * -0.5 + 0.25);
         }

         return true;
      } else {
         return false;
      }
   }

   protected abstract void setPosition(double var1, double var3);

   protected abstract double[] getOffset();
}
