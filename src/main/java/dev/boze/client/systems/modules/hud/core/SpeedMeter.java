package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import mapped.Class3076;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

public class SpeedMeter extends HUDModule {
   private final BooleanSetting field2644 = new BooleanSetting("ShowPrefix", true, "Show prefix (Speed)");
   private final BooleanSetting field2645 = new BooleanSetting("BPS", false, "Show speed in bp/s");
   private final BooleanSetting field2646 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field2647 = new ColorSetting(
      "Prefix", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Prefix color", this.field2646
   );
   private final ColorSetting field2648 = new ColorSetting(
      "Number", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Number color", this.field2646
   );
   private final ColorSetting field2649 = new ColorSetting(
      "Suffix", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Suffix color", this.field2646
   );
   private final BooleanSetting field2650 = new BooleanSetting("Shadow", false, "Text shadow", this.field2646);
   public static final SpeedMeter INSTANCE = new SpeedMeter();
   private double field2651 = 0.0;
   private double[] field2652 = new double[30];
   private int field2653 = 0;
   private Vec3d field2654 = null;

   public SpeedMeter() {
      super("Speedometer", "Shows your current speed", 40.0, 40.0);
   }

   @EventHandler
   public void method1559(PostPlayerTickEvent event) {
      if (this.field2654 == null) {
         this.field2654 = new Vec3d(mc.player.getX(), 0.0, mc.player.getZ());
      } else {
         Vec3d var5 = new Vec3d(mc.player.getX(), 0.0, mc.player.getZ());
         this.field2651 = var5.distanceTo(this.field2654);
         this.field2654 = var5;
         if (this.field2653 > 29) {
            this.field2653 = 0;
         }

         this.field2652[this.field2653] = this.field2651;
         this.field2653++;
      }
   }

   @Override
   public void method295(DrawContext context) {
      if (this.field2644.getValue()) {
         this.method298(
            "Speed",
            String.format("%,.2f", this.method1560()),
            this.field2645.getValue() ? "b/s" : "km/h",
            this.field2646.getValue() ? this.field2647.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2646.getValue() ? this.field2648.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2646.getValue() ? this.field2649.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2646.getValue() ? this.field2650.getValue() : HUD.INSTANCE.field2384.getValue()
         );
      } else {
         this.method297(
            String.format("%,.2f", this.method1560()),
            this.field2645.getValue() ? "b/s" : "km/h",
            this.field2646.getValue() ? this.field2648.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2646.getValue() ? this.field2649.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2646.getValue() ? this.field2650.getValue() : HUD.INSTANCE.field2384.getValue()
         );
      }
   }

   private double method1560() {
      double var4 = 0.0;

      for (int var6 = 0; var6 < 30; var6++) {
         var4 += this.field2652[var6];
      }

      double var8 = var4 / 30.0;
      return this.field2645.getValue() ? var8 * 20.0 * (double)Class3076.method6027() : var8 * 20.0 * 3.6 * (double)Class3076.method6027();
   }
}
