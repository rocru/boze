package dev.boze.client.gui.components.slider.floats;

import dev.boze.client.gui.components.FloatSliderComponent;
import dev.boze.client.gui.components.scaled.ColorPickerComponent;
import dev.boze.client.utils.render.color.ChangingColor;

class fr extends FloatSliderComponent {
   final ChangingColor field1143;
   final ColorPickerComponent field1144;

   fr(ColorPickerComponent var1, String var2, double var3, double var5, double var7, double var9, ChangingColor var11) {
      super(var2, var3, var5, var7, var9);
      this.field1144 = var1;
      this.field1143 = var11;
   }

   @Override
   protected void method207(float value) {
      this.field1143.field419 = value;
   }

   @Override
   protected float method1384() {
      return this.field1143.field419;
   }

   @Override
   protected float method1385() {
      return 0.01F;
   }

   @Override
   protected float method215() {
      return 1.0F;
   }

   @Override
   protected float method520() {
      return 0.01F;
   }

   @Override
   protected void method2142() {
      this.field1143.field419 = 0.1F;
   }
}
