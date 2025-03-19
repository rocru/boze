package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.trackers.TickRateTracker;
import net.minecraft.client.gui.DrawContext;

import java.text.DecimalFormat;

public class Ticks extends HUDModule {
   private final BooleanSetting field2656 = new BooleanSetting("ShowPrefix", false, "Show prefix (TickRate)");
   private final BooleanSetting field2657 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field2658 = new ColorSetting(
      "Prefix", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Prefix color", this.field2657
   );
   private final ColorSetting field2659 = new ColorSetting(
      "Number", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Number color", this.field2657
   );
   private final ColorSetting field2660 = new ColorSetting(
      "Suffix", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Suffix color", this.field2657
   );
   private final BooleanSetting field2661 = new BooleanSetting("Shadow", false, "Text shadow", this.field2657);
   public static final Ticks INSTANCE = new Ticks();

   public Ticks() {
      super("TPS", "Shows the server's current TickRate", 40.0, 40.0);
   }

   @Override
   public void method295(DrawContext context) {
      DecimalFormat var5 = new DecimalFormat("#.#");
      if (this.field2656.method419()) {
         this.method298(
            "TickRate",
            var5.format((double)TickRateTracker.getLastTickRate()),
            "tps",
            this.field2657.method419() ? this.field2658.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2657.method419() ? this.field2659.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2657.method419() ? this.field2660.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2657.method419() ? this.field2661.method419() : HUD.INSTANCE.field2384.method419()
         );
      } else {
         this.method297(
            var5.format((double)TickRateTracker.getLastTickRate()),
            "tps",
            this.field2657.method419() ? this.field2659.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2657.method419() ? this.field2660.method1362() : HUD.INSTANCE.field2383.method1362(),
            this.field2657.method419() ? this.field2661.method419() : HUD.INSTANCE.field2384.method419()
         );
      }
   }
}
