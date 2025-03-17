package dev.boze.client.gui.colorpanel.gradient;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.RotationComponent;
import dev.boze.client.manager.ColorManager;

public class MaxHueComponent extends RotationComponent {
   final ColorManager field1206;
   final ColorPanel field1207;

   public MaxHueComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1207 = var1;
      this.field1206 = var11;
   }

   @Override
   protected void method938(double value) {
      this.field1206.method1374().field1846[1] = value;
   }

   @Override
   protected double method2091() {
      return this.field1206.method1374().field1846[1];
   }

   @Override
   protected double method1614() {
      return 0.001;
   }

   @Override
   protected void method2142() {
      this.field1206.method1374().field1846[1] = 1.0;
   }
}
