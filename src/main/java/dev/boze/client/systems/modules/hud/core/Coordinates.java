package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.systems.modules.render.FreeCam;
import net.minecraft.client.gui.DrawContext;

public class Coordinates extends HUDModule {
   public static final Coordinates INSTANCE = new Coordinates();
   private final BooleanSetting field2606 = new BooleanSetting("FreecamCoords", true, "Show freecam coords when in freecam");
   private final BooleanSetting field2607 = new BooleanSetting("NetherCoords", true, "Show nether coords when in overworld and vice-versa");
   private final BooleanSetting field2608 = new BooleanSetting("NoDecimal", false, "Hide decimals");
   public final BooleanSetting field2609 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field2610 = new ColorSetting(
      "XYZ", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "XYZ color", this.field2609
   );
   private final ColorSetting field2611 = new ColorSetting(
      "Coords", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Coords color", this.field2609
   );
   private final ColorSetting field2612 = new ColorSetting(
      "Nether", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Nether coords color", this.field2609
   );
   private final BooleanSetting field2613 = new BooleanSetting("Shadow", false, "Text shadow", this.field2609);

   public Coordinates() {
      super("Coordinates", "Shows your current coords", Category.Hud, 0.0, 0.0, 3, 40.0, 40.0);
      this.setEnabled(true);
   }

   @Override
   public void method295(DrawContext context) {
      String var5 = this.field2608.getValue() ? "%.0f" : "%.1f";
      Object var6 = mc.player;
      if (mc.player.getVehicle() != null) {
         var6 = mc.player.getVehicle();
      }

      String var7 = String.format(var5, this.field2606.getValue() && FreeCam.INSTANCE.isEnabled() ? FreeCam.INSTANCE.field3540 : var6.getX());
      String var8 = String.format(var5, this.field2606.getValue() && FreeCam.INSTANCE.isEnabled() ? FreeCam.INSTANCE.field3541 : var6.getY());
      String var9 = String.format(var5, this.field2606.getValue() && FreeCam.INSTANCE.isEnabled() ? FreeCam.INSTANCE.field3542 : var6.getZ());
      String var10 = " " + var7 + ", " + var8 + ", " + var9;
      if (this.field2607.getValue()) {
         String var11 = !this.method1552() ? String.format(var5, var6.getX() / 8.0) : String.format(var5, var6.getX() * 8.0);
         String var12 = !this.method1552() ? String.format(var5, var6.getZ() / 8.0) : String.format(var5, var6.getZ() * 8.0);
         this.method298(
            "XYZ",
            var10,
            "[" + var11 + ", " + var12 + "]",
            this.field2609.getValue() ? this.field2610.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2609.getValue() ? this.field2611.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2609.getValue() ? this.field2612.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2609.getValue() ? this.field2613.getValue() : HUD.INSTANCE.field2384.getValue()
         );
      } else {
         this.method297(
            "XYZ",
            var10,
            this.field2609.getValue() ? this.field2610.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2609.getValue() ? this.field2611.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2609.getValue() ? this.field2613.getValue() : HUD.INSTANCE.field2384.getValue()
         );
      }
   }

   private boolean method1552() {
      return mc.world.getRegistryKey().getValue().getPath().equals("the_nether");
   }
}
