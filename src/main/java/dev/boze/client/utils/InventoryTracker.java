package dev.boze.client.utils;

import dev.boze.client.events.OpenScreenEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.util.collection.DefaultedList;

public class InventoryTracker implements IMinecraft {
   public static final DefaultedList<ItemStack> field1360 = DefaultedList.ofSize(27, ItemStack.EMPTY);
   private static int state;

   public static void method597(BlockState blockState) {
      if (blockState.getBlock() instanceof EnderChestBlock && state == 0) {
         state = 1;
      }
   }

   @EventHandler
   private static void method1675(OpenScreenEvent var0) {
      if (state == 1 && var0.screen instanceof GenericContainerScreen) {
         state = 2;
      } else if (state != 0) {
         if (mc.currentScreen instanceof GenericContainerScreen) {
            GenericContainerScreenHandler var4 = (GenericContainerScreenHandler)((GenericContainerScreen)mc.currentScreen).getScreenHandler();
            if (var4 != null) {
               Inventory var5 = var4.getInventory();

               for (int var6 = 0; var6 < 27; var6++) {
                  field1360.set(var6, var5.getStack(var6));
               }

               state = 0;
            }
         }
      }
   }
}
