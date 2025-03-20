package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import mapped.Class5929;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class Inventory extends HUDModule implements Class5929 {
   public static final Inventory INSTANCE = new Inventory();
   private static final MatrixStack field625 = new MatrixStack();

   public Inventory() {
      super("Inventory", "Shows your inventory", 164.0, 56.0);
      this.field595.setValue(1.333);
   }

   @Override
   public void method295(DrawContext context) {
      this.method314(164.0 * this.method336());
      this.method316(56.0 * this.method336());
      if (HUD.INSTANCE.field2394.getValue()) {
         HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
      }
   }

   @Override
   public void method332(DrawContext context) {
      for (int var5 = 0; var5 < 3; var5++) {
         for (int var6 = 0; var6 < 9; var6++) {
            ItemStack var7 = mc.player.getInventory().getStack(9 + var5 * 9 + var6);
            if (var7 != null) {
               int var8 = (int)(this.method1391() + (double)(3 + var6 * 18) * this.method336());
               int var9 = (int)(this.method305() + (double)(3 + var5 * 18) * this.method336());
               method335(context, var7, var8, var9, this.method336());
            }
         }
      }
   }

   public static void method335(DrawContext context, ItemStack itemStack, int x, int y, double scale) {
      MatrixStack var6 = context.getMatrices();
      var6.push();
      var6.scale((float)scale, (float)scale, 1.0F);
      context.drawItem(itemStack, (int)((double)x / scale), (int)((double)y / scale));
      context.drawItemInSlot(mc.textRenderer, itemStack, (int)((double)x / scale), (int)((double)y / scale), null);
      var6.pop();
   }

   private double method336() {
      return this.field595.getValue() * HUD.INSTANCE.field2375.getValue();
   }
}
