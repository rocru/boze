package dev.boze.client.gui.colorpanel.gradient;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.manager.ColorManager;

public class OpacityComponent extends IntSliderComponent {
   final ColorManager field1226;
   final ColorPanel field1227;

   public OpacityComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1227 = var1;
      this.field1226 = var11;
   }

   @Override
   protected void method1649(int value) {
      this.field1226.method1374().field411 = value;
   }

   @Override
   protected int method2010() {
      return this.field1226.method1374().field411;
   }

   @Override
   protected int method1547() {
      return 0;
   }

   @Override
   protected int method1365() {
      return 255;
   }

   @Override
   protected void method2142() {
      this.field1226.method1374().field411 = this.field1226.field414.field411;
   }
}
