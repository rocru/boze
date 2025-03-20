package dev.boze.client.systems.modules.render;

import dev.boze.client.enums.ColorTypes;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.font.IFontRender;
import dev.boze.client.settings.WeirdColorSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class3031;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;

public class NewRenderTest extends Module {
   public static final NewRenderTest INSTANCE = new NewRenderTest();
   private final WeirdColorSetting field3607 = new WeirdColorSetting("Color", Class3031.field130, "Color");

   private NewRenderTest() {
      super("NewRenderTest", "New render system test", Category.Render);
      this.field3607.method427(ColorTypes.SIMPLE);
   }

   @EventHandler
   private void method1969(Render2DEvent var1) {
      IFontRender.method500(true).startBuilding();
      IFontRender.method500(true).drawText("Hello world!", 10.0, 10.0, this.field3607.getValue());
      IFontRender.method500(true).endBuilding();
      IFontRender.method500(false).startBuilding();
      IFontRender.method500(false).drawText("Hello world!", 10.0, 100.0, this.field3607.getValue());
      IFontRender.method500(false).endBuilding();
   }

   @EventHandler
   private void method1970(Render3DEvent var1) {
      if (mc.crosshairTarget != null && mc.crosshairTarget instanceof BlockHitResult var5 && var5.getType() == Type.BLOCK) {
         var1.field1950.method1220(new Box(var5.getBlockPos()), this.field3607.getValue());
      }
   }
}
