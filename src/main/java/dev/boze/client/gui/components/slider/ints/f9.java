package dev.boze.client.gui.components.slider.ints;

import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.StaticColor;

class f9 extends IntSliderComponent {
   final StaticColor field2029;
   final NewColorComponent field2030;

   f9(NewColorComponent var1, String var2, double var3, double var5, double var7, double var9, StaticColor var11) {
      super(var2, var3, var5, var7, var9);
      this.field2030 = var1;
      this.field2029 = var11;
   }

   @Override
   protected void method1649(int value) {
      this.field2029.field432 = value;
   }

   @Override
   protected int method2010() {
      return this.field2029.field432;
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
      this.field2029.field432 = 255;
   }

   @Override
   protected RGBAColor method1347() {
      return RGBAColor.field406;
   }
}
