package dev.boze.client.utils;

import dev.boze.client.enums.AutoMineSwapMode;
import dev.boze.client.enums.SlotSwapMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.systems.modules.misc.XCarry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class InventoryHelper
        implements IMinecraft {
    public static int method2010() {
        return InventoryHelper.method168(ItemStack::isEmpty);
    }

    public static int method161(int start, int end, Block... blocks) {
        return InventoryHelper.method170(arg_0 -> InventoryHelper.lambda$findBlock$0(blocks, arg_0), start, end);
    }

    public static int method162(Block... blocks) {
        return InventoryHelper.method161(0, InventoryHelper.mc.player.getInventory().size(), blocks);
    }

    public static int method163(Block... blocks) {
        return InventoryHelper.method168(arg_0 -> InventoryHelper.lambda$findBlockHotbar$1(blocks, arg_0));
    }

    public static int method164(Block... blocks) {
        return InventoryHelper.method161(9, InventoryHelper.mc.player.getInventory().size(), blocks);
    }

    public static int method165(Predicate<ItemStack> test, AutoMineSwapMode mode) {
        if (mode == AutoMineSwapMode.Off) {
            return -1;
        }
        return InventoryHelper.method166(test, mode.swapMode);
    }

    public static int method166(Predicate<ItemStack> test, SwapMode mode) {
        if (mode == SwapMode.Normal || mode == SwapMode.Silent) {
            return InventoryHelper.method168(test);
        }
        return InventoryHelper.method169(test);
    }

    public static int method167(Predicate<ItemStack> test, SlotSwapMode mode) {
        if (mode == SlotSwapMode.Normal) {
            return InventoryHelper.method168(test);
        }
        return InventoryHelper.method169(test);
    }

    public static int method168(Predicate<ItemStack> test) {
        if (test.test(InventoryHelper.mc.player.getMainHandStack())) {
            return InventoryHelper.mc.player.getInventory().selectedSlot;
        }
        return InventoryHelper.method170(test, 0, 8);
    }

    public static int method169(Predicate<ItemStack> test) {
        return InventoryHelper.method170(test, 0, InventoryHelper.mc.player.getInventory().size());
    }

    public static int method170(Predicate<ItemStack> test, int start, int end) {
        for (int i = start; i <= end; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.getInventory().getStack(i);
            if (!test.test(itemStack)) continue;
            return i;
        }
        return -1;
    }

    public static int method171(BiPredicate<ItemStack, Integer> test, int start, int end) {
        for (int i = start; i <= end; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.getInventory().getStack(i);
            if (!test.test(itemStack, i)) continue;
            return i;
        }
        return -1;
    }

    public static int method172(BiPredicate<ItemStack, Integer> test) {
        if (XCarry.INSTANCE.isEnabled()) {
            for (int i = 1; i <= 4; ++i) {
                Slot slot = InventoryHelper.mc.player.playerScreenHandler.getSlot(i);
                int n = 420 + slot.id;
                if (!test.test(slot.getStack(), n)) continue;
                return 420 + slot.id;
            }
        }
        return -1;
    }

    public static int method173(Predicate<ItemStack> test) {
        if (XCarry.INSTANCE.isEnabled()) {
            for (int i = 1; i <= 4; ++i) {
                Slot slot = InventoryHelper.mc.player.playerScreenHandler.getSlot(i);
                if (!test.test(slot.getStack())) continue;
                return 420 + slot.id;
            }
        }
        return -1;
    }

    public static int method174(Function<ItemStack, Float> score, SwapMode mode) {
        if (mode == SwapMode.Normal || mode == SwapMode.Silent) {
            return InventoryHelper.method176(score);
        }
        return InventoryHelper.method177(score);
    }

    public static int method175(Function<ItemStack, Float> score, SlotSwapMode mode) {
        if (mode == SlotSwapMode.Normal) {
            return InventoryHelper.method176(score);
        }
        return InventoryHelper.method177(score);
    }

    public static int method176(Function<ItemStack, Float> score) {
        return InventoryHelper.method178(score, 0, 8);
    }

    public static int method177(Function<ItemStack, Float> score) {
        return InventoryHelper.method178(score, 0, InventoryHelper.mc.player.getInventory().size());
    }

    public static int method178(Function<ItemStack, Float> score, int start, int end) {
        int n = -1;
        float f = -1.0f;
        for (int i = start; i <= end; ++i) {
            ItemStack itemStack = InventoryHelper.mc.player.getInventory().getStack(i);
            float f2 = score.apply(itemStack).floatValue();
            if (!(f2 > f)) continue;
            f = f2;
            n = i;
        }
        return n;
    }

    private static boolean lambda$findBlockHotbar$1(Block[] blockArray, ItemStack itemStack) {
        for (Block block : blockArray) {
            if (!(itemStack.getItem() instanceof BlockItem) || ((BlockItem) itemStack.getItem()).getBlock() != block)
                continue;
            return true;
        }
        return false;
    }

    private static boolean lambda$findBlock$0(Block[] blockArray, ItemStack itemStack) {
        for (Block block : blockArray) {
            if (!(itemStack.getItem() instanceof BlockItem) || ((BlockItem) itemStack.getItem()).getBlock() != block)
                continue;
            return true;
        }
        return false;
    }
}
