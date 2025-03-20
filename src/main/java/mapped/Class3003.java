package mapped;

import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;

public class Class3003 extends StaticColor {
   private final GradientColor field128;

   public Class3003(GradientColor var1) {
      super(-1, -1, -1);
      this.field128 = var1;
   }

   @Override
   public float method1384() {
      return 1.0F - Class3032.method5931(this.field128.field423);
   }

   @Override
   public float method1385() {
      return this.field128.field426;
   }

   @Override
   public float method215() {
      return 1.0F;
   }
}
