package dev.boze.client.utils;

import dev.boze.client.enums.AutoMineSwapMode;
import dev.boze.client.enums.SlotSwapMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.systems.modules.misc.XCarry;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class InventoryHelper implements IMinecraft {
   public static int method2010() {
      return method168(ItemStack::method_7960);
   }

   public static int method161(int start, int end, Block... blocks) {
      return method170(InventoryHelper::lambda$findBlock$0, start, end);
   }

   public static int method162(Block... blocks) {
      return method161(0, mc.player.getInventory().size(), blocks);
   }

   public static int method163(Block... blocks) {
      return method168(InventoryHelper::lambda$findBlockHotbar$1);
   }

   public static int method164(Block... blocks) {
      return method161(9, mc.player.getInventory().size(), blocks);
   }

   public static int method165(Predicate<ItemStack> test, AutoMineSwapMode mode) {
      return mode == AutoMineSwapMode.Off ? -1 : method166(test, mode.swapMode);
   }

   public static int method166(Predicate<ItemStack> test, SwapMode mode) {
      return mode != SwapMode.Normal && mode != SwapMode.Silent ? method169(test) : method168(test);
   }

   public static int method167(Predicate<ItemStack> test, SlotSwapMode mode) {
      return mode == SlotSwapMode.Normal ? method168(test) : method169(test);
   }

   public static int method168(Predicate<ItemStack> test) {
      return test.test(mc.player.getMainHandStack()) ? mc.player.getInventory().selectedSlot : method170(test, 0, 8);
   }

   public static int method169(Predicate<ItemStack> test) {
      return method170(test, 0, mc.player.getInventory().size());
   }

   public static int method170(Predicate<ItemStack> test, int start, int end) {
      for (int var6 = start; var6 <= end; var6++) {
         ItemStack var7 = mc.player.getInventory().getStack(var6);
         if (test.test(var7)) {
            return var6;
         }
      }

      return -1;
   }

   public static int method171(BiPredicate<ItemStack, Integer> test, int start, int end) {
      for (int var6 = start; var6 <= end; var6++) {
         ItemStack var7 = mc.player.getInventory().getStack(var6);
         if (test.test(var7, var6)) {
            return var6;
         }
      }

      return -1;
   }

   public static int method172(BiPredicate<ItemStack, Integer> test) {
      if (XCarry.INSTANCE.isEnabled()) {
         for (int var4 = 1; var4 <= 4; var4++) {
            Slot var5 = mc.player.playerScreenHandler.getSlot(var4);
            int var6 = 420 + var5.id;
            if (test.test(var5.getStack(), var6)) {
               return 420 + var5.id;
            }
         }
      }

      return -1;
   }

   public static int method173(Predicate<ItemStack> test) {
      if (XCarry.INSTANCE.isEnabled()) {
         for (int var4 = 1; var4 <= 4; var4++) {
            Slot var5 = mc.player.playerScreenHandler.getSlot(var4);
            if (test.test(var5.getStack())) {
               return 420 + var5.id;
            }
         }
      }

      return -1;
   }

   public static int method174(Function<ItemStack, Float> score, SwapMode mode) {
      return mode != SwapMode.Normal && mode != SwapMode.Silent ? method177(score) : method176(score);
   }

   public static int method175(Function<ItemStack, Float> score, SlotSwapMode mode) {
      return mode == SlotSwapMode.Normal ? method176(score) : method177(score);
   }

   public static int method176(Function<ItemStack, Float> score) {
      return method178(score, 0, 8);
   }

   public static int method177(Function<ItemStack, Float> score) {
      return method178(score, 0, mc.player.getInventory().size());
   }

   public static int method178(Function<ItemStack, Float> score, int start, int end) {
      int var6 = -1;
      float var7 = -1.0F;

      for (int var8 = start; var8 <= end; var8++) {
         ItemStack var9 = mc.player.getInventory().getStack(var8);
         float var10 = (Float)score.apply(var9);
         if (var10 > var7) {
            var7 = var10;
            var6 = var8;
         }
      }

      return var6;
   }

   private static boolean lambda$findBlockHotbar$1(Block[] var0, ItemStack var1) {
      for (Block var8 : var0) {
         if (var1.getItem() instanceof BlockItem && ((BlockItem)var1.getItem()).getBlock() == var8) {
            return true;
         }
      }

      return false;
   }

   private static boolean lambda$findBlock$0(Block[] var0, ItemStack var1) {
      for (Block var8 : var0) {
         if (var1.getItem() instanceof BlockItem && ((BlockItem)var1.getItem()).getBlock() == var8) {
            return true;
         }
      }

      return false;
   }
}
