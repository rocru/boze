package mapped;

import dev.boze.client.gui.components.DoubleArrayComponent;
import dev.boze.client.gui.components.scaled.SettingColorComponent;

class Class5909 extends DoubleArrayComponent {
   final SettingColorComponent field2;

   Class5909(SettingColorComponent var1, String var2, double var3, double var5, double var7, double var9) {
      super(var2, var3, var5, var7, var9);
      this.field2 = var1;
   }

   @Override
   protected void setPosition(double x, double y) {
      this.field2.method1362().field1845[0] = x;
      this.field2.method1362().field1845[1] = y;
   }

   @Override
   protected double[] getOffset() {
      return this.field2.method1362().field1845;
   }
}
