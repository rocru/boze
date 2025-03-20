package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import mapped.Class5929;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Totems extends HUDModule implements Class5929 {
   private final BooleanSetting field644 = new BooleanSetting("Item", false, "Show as item stack");
   public final BooleanSetting field645 = new BooleanSetting("Custom", false, "Use custom theme settings", this::lambda$new$0);
   public final ColorSetting field646 = new ColorSetting(
      "Primary", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field645
   );
   public final ColorSetting field647 = new ColorSetting(
      "Secondary", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field645
   );
   public final BooleanSetting field648 = new BooleanSetting("Shadow", false, "Text shadow", this.field645);
   public static final Totems INSTANCE = new Totems();

   public Totems() {
      super("Totems", "Shows your totem count", 40.0, 40.0);
   }

   @Override
   public void method295(DrawContext context) {
      if (this.field644.getValue()) {
         double var5 = this.field595.getValue() * HUD.INSTANCE.field2375.getValue();
         this.method314(24.0 * var5);
         this.method316(24.0 * var5);
         if (HUD.INSTANCE.field2394.getValue() && (this.method1547() > 0 || mc.currentScreen == ClickGUI.field1335 && ClickGUI.field1335.field1336)) {
            HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
         }
      } else {
         this.method297(
            "Totems:",
            Integer.toString(this.method1547()),
            this.field645.getValue() ? this.field646.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field645.getValue() ? this.field647.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field645.getValue() ? this.field648.getValue() : HUD.INSTANCE.field2384.getValue()
         );
      }
   }

   @Override
   public void method332(DrawContext context) {
      if (this.field644.getValue()) {
         double var5 = this.field595.getValue() * HUD.INSTANCE.field2375.getValue();
         ItemStack var7 = new ItemStack(Items.TOTEM_OF_UNDYING, this.method1547());
         Inventory.method335(context, var7, (int)(this.method1391() + 4.0 * var5), (int)(this.method305() + 4.0 * var5), var5);
      }
   }

   private int method1547() {
      if (mc.player == null) {
         return 0;
      } else {
         int var4 = 0;

         for (int var5 = 0; var5 < 45; var5++) {
            if (mc.player.getInventory().getStack(var5).getItem() == Items.TOTEM_OF_UNDYING) {
               var4++;
            }
         }

         return var4;
      }
   }

   private boolean lambda$new$0() {
      return !this.field644.getValue();
   }
}
