package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.mixin.MinecraftClientAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import net.minecraft.client.gui.DrawContext;

public class Framerate extends HUDModule {
   private final BooleanSetting field2620 = new BooleanSetting("ShowPrefix", false, "Show prefix (FrameRate)");
   private final BooleanSetting field2621 = new BooleanSetting("FastUpdate", false, "Fast update");
   private final BooleanSetting field2622 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field2623 = new ColorSetting(
      "Prefix", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Prefix color", this.field2622
   );
   private final ColorSetting field2624 = new ColorSetting(
      "Number", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Number color", this.field2622
   );
   private final ColorSetting field2625 = new ColorSetting(
      "Suffix", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Suffix color", this.field2622
   );
   private final BooleanSetting field2626 = new BooleanSetting("Shadow", false, "Text shadow", this.field2622);
   public static final Framerate INSTANCE = new Framerate();
   private long field2627;
   private long field2628;

   public Framerate() {
      super("FPS", "Shows your current FrameRate", 40.0, 40.0);
   }

   @Override
   public void method295(DrawContext context) {
      String var5 = this.field2621.method419() ? Long.toString(1000000000L / this.field2628) : Integer.toString(((MinecraftClientAccessor)mc).getCurrentFps());
      if (this.field2620.method419()) {
         this.method298(
            "FrameRate",
            var5,
            "fps",
            this.field2622.method419() ? this.field2623.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2622.method419() ? this.field2624.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2622.method419() ? this.field2625.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2622.method419() ? this.field2626.method419() : HUD.INSTANCE.field2384.method419()
         );
      } else {
         this.method297(
            var5,
            "fps",
            this.field2622.method419() ? this.field2624.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2622.method419() ? this.field2625.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2622.method419() ? this.field2626.method419() : HUD.INSTANCE.field2384.method419()
         );
      }
   }

   public void method1555() {
      this.field2628 = System.nanoTime() - this.field2627;
      this.field2627 = System.nanoTime();
   }
}
