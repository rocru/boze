package mapped;

import dev.boze.client.utils.ColorWrapper;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;

public class Class3031 {
   public static final ColorWrapper field130 = new ColorWrapper(new StaticColor(149, 123, 211), 0.25F, 1.0F);
   public static final ColorWrapper field131 = new ColorWrapper(new StaticColor(60, 60, 60), 0.7F, 0.0F);
   public static final ColorWrapper field132 = new ColorWrapper(new GradientColor("_default_Blue"), 1.0F, 1.0F);

   static {
      GradientColor var6 = (GradientColor)field132.field3910;
      var6.field422.add(new StaticColor(0, 255, 140));
      var6.field422.add(new StaticColor(0, 140, 255));
      var6.field424 = true;
      var6.field425 = true;
      var6.field426 = 0.5F;
      var6.field428 = 0.25F;
   }
}
