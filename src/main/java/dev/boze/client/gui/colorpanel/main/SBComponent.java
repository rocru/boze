package dev.boze.client.gui.colorpanel.main;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.FloatArraySliderComponent;
import dev.boze.client.manager.ColorManager;
import dev.boze.client.utils.RGBAColor;
import java.awt.Color;

public class SBComponent extends FloatArraySliderComponent {
   final ColorManager field1984;
   final ColorPanel field1985;

   public SBComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1985 = var1;
      this.field1984 = var11;
   }

   @Override
   protected float[] method111() {
      return Color.RGBtoHSB(this.field1984.method1374().field408, this.field1984.method1374().field409, this.field1984.method1374().field410, null);
   }

   @Override
   protected void method521(float hue, float sat, float bri) {
      int[] var4 = RGBAColor.method190((double)hue * 360.0, (double)sat, (double)bri);
      this.field1984.method1374().method192(var4[0], var4[1], var4[2], this.field1984.method1374().field411);
   }
}
