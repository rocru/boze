package mapped;

import dev.boze.client.gui.components.DoubleArrayComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;

class Class5910 extends DoubleArrayComponent {
   final ColorSetting field3;
   final ColorSettingComponent field4;

   Class5910(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field4 = var1;
      this.field3 = var11;
   }

   @Override
   protected void setPosition(double x, double y) {
      this.field3.method1374().field1845[0] = x;
      this.field3.method1374().field1845[1] = y;
   }

   @Override
   protected double[] getOffset() {
      return this.field3.method1374().field1845;
   }
}
