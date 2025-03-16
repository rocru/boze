package dev.boze.client.systems.modules.hud;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import net.minecraft.client.gui.DrawContext;

public abstract class ColorHUDModule extends HUDModule {
   public final BooleanSetting field606 = new BooleanSetting("Custom", false, "Use custom theme settings");
   public final ColorSetting field607 = new ColorSetting(
      "Primary", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field606
   );
   public final ColorSetting field608 = new ColorSetting(
      "Secondary", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field606
   );
   public final BooleanSetting field609 = new BooleanSetting("Shadow", false, "Text shadow", this.field606);

   public ColorHUDModule(String name, String description) {
      super(name, description, 20.0, 20.0);
   }

   public ColorHUDModule(String name, String description, double offsetX, double offsetY, int anchor) {
      super(name, description, Category.Hud, offsetX, offsetY, anchor, 20.0, 20.0);
   }

   @Override
   public void method295(DrawContext context) {
      this.method297(
         this.method1562(),
         this.method1563(),
         this.field606.method419() ? this.field607.method1362() : HUD.INSTANCE.field2383.method1362(),
         this.field606.method419() ? this.field608.method1362() : HUD.INSTANCE.field2383.method1362(),
         this.field606.method419() ? this.field609.method419() : HUD.INSTANCE.field2384.method419()
      );
   }

   protected abstract String method1562();

   protected abstract String method1563();
}
