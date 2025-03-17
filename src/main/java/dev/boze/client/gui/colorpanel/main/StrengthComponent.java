package dev.boze.client.gui.colorpanel.main;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.DoubleArrayComponent;
import dev.boze.client.manager.ColorManager;

public class StrengthComponent extends DoubleArrayComponent {
   final ColorManager field1988;
   final ColorPanel field1989;

   public StrengthComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1989 = var1;
      this.field1988 = var11;
   }

   @Override
   protected void setPosition(double x, double y) {
      this.field1988.method1374().field1845[0] = x;
      this.field1988.method1374().field1845[1] = y;
   }

   @Override
   protected double[] getOffset() {
      return this.field1988.method1374().field1845;
   }
}
