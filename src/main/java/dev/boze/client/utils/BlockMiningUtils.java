package dev.boze.client.utils;

import dev.boze.client.enums.AutoMineSwapMode;
import dev.boze.client.enums.SwapMode;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;

public class BlockMiningUtils implements IMinecraft {
   public static int method590(BlockPos pos, AutoMineSwapMode mode) {
      return mode == AutoMineSwapMode.Off ? mc.player.getInventory().selectedSlot : method592(pos, mode.swapMode);
   }

   public static int method591(BlockState state, AutoMineSwapMode mode) {
      return mode == AutoMineSwapMode.Off ? mc.player.getInventory().selectedSlot : method593(state, mode.swapMode);
   }

   public static int method592(BlockPos pos, SwapMode mode) {
      BlockState var5 = mc.world.getBlockState(pos);
      return method593(var5, mode);
   }

   public static int method593(BlockState state, SwapMode mode) {
      Function var5 = BlockMiningUtils::lambda$findBestTool$0;
      int var6 = InventoryHelper.method174(var5, mode);
      return var6 == -1 ? mc.player.getInventory().selectedSlot : var6;
   }

   private static Float lambda$findBestTool$0(BlockState var0, ItemStack var1) {
      if (var1.isEmpty() || var1.getItem() == Items.AIR) {
         return -2.0F;
      } else if (!(var1.getItem() instanceof ToolItem)) {
         return -3.0F;
      } else {
         float var5 = var1.getMiningSpeedMultiplier(var0);
         if (var5 > 1.0F) {
            int var6;
            var5 += (var6 = ItemEnchantmentUtils.getEnchantmentLevel(var1, Enchantments.EFFICIENCY)) > 0 ? (float)(Math.pow((double)var6, 2.0) + 1.0) : 0.0F;
         }

         return var5;
      }
   }
}
