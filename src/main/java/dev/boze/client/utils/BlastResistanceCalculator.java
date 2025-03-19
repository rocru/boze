package dev.boze.client.utils;

import dev.boze.client.enums.InteractionBlockMode;
import dev.boze.client.settings.StringModeSetting;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.function.Function;

public class BlastResistanceCalculator {
   public static final Function<ItemStack, Float> field3905 = BlastResistanceCalculator::lambda$static$0;
   public static final Function<ItemStack, Float> field3906 = BlastResistanceCalculator::lambda$static$1;
   public static final Function<ItemStack, Float> field3907 = BlastResistanceCalculator::lambda$static$2;
   public static final Function<ItemStack, Float> field3908 = BlastResistanceCalculator::lambda$static$3;

   public static Function<ItemStack, Float> method2130(InteractionBlockMode blockTypes, StringModeSetting blockList) {
      if (blockTypes == InteractionBlockMode.Obsidian) {
         return field3907;
      } else {
         return blockTypes == InteractionBlockMode.BlastProof ? field3908 : BlastResistanceCalculator::lambda$getBlockPredicate$4;
      }
   }

   private static Float lambda$getBlockPredicate$4(StringModeSetting var0, ItemStack var1) {
      if (var1.getItem() instanceof BlockItem var5 && var0.method2032().contains(var5.getBlock())) {
         return 2000.0F;
      }

      return -1.0F;
   }

   private static Float lambda$static$3(ItemStack var0) {
      if (var0.getItem() instanceof BlockItem var4) {
         return var4.getBlock() == Blocks.OBSIDIAN ? 2000.0F : var4.getBlock().getBlastResistance();
      } else {
         return -1.0F;
      }
   }

   private static Float lambda$static$2(ItemStack var0) {
      if (var0.getItem() instanceof BlockItem var4 && var4.getBlock() == Blocks.OBSIDIAN) {
         return 2000.0F;
      }

      return -1.0F;
   }

   private static Float lambda$static$1(ItemStack var0) {
      if (var0.getItem() instanceof BlockItem var4 && var4.getBlock() == Blocks.COBWEB) {
         return 2000.0F;
      }

      return -1.0F;
   }

   private static Float lambda$static$0(ItemStack var0) {
      return var0.getItem() == Items.WATER_BUCKET ? 2000.0F : -1.0F;
   }
}
