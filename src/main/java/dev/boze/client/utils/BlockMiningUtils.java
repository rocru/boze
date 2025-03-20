package dev.boze.client.utils;

import dev.boze.client.enums.AutoMineSwapMode;
import dev.boze.client.enums.SwapMode;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public class BlockMiningUtils
        implements IMinecraft {
    public static int method590(BlockPos pos, AutoMineSwapMode mode) {
        if (mode == AutoMineSwapMode.Off) {
            return BlockMiningUtils.mc.player.getInventory().selectedSlot;
        }
        return BlockMiningUtils.method592(pos, mode.swapMode);
    }

    public static int method591(BlockState state, AutoMineSwapMode mode) {
        if (mode == AutoMineSwapMode.Off) {
            return BlockMiningUtils.mc.player.getInventory().selectedSlot;
        }
        return BlockMiningUtils.method593(state, mode.swapMode);
    }

    public static int method592(BlockPos pos, SwapMode mode) {
        BlockState blockState = BlockMiningUtils.mc.world.getBlockState(pos);
        return BlockMiningUtils.method593(blockState, mode);
    }

    public static int method593(BlockState state, SwapMode mode) {
        Function<ItemStack, Float> function = arg_0 -> BlockMiningUtils.lambda$findBestTool$0(state, arg_0);
        int n = InventoryHelper.method174(function, mode);
        if (n == -1) {
            return BlockMiningUtils.mc.player.getInventory().selectedSlot;
        }
        return n;
    }

    private static Float lambda$findBestTool$0(BlockState blockState, ItemStack itemStack) {
        if (itemStack.isEmpty() || itemStack.getItem() == Items.AIR) {
            return Float.valueOf(-2.0f);
        }
        if (!(itemStack.getItem() instanceof ToolItem)) {
            return Float.valueOf(-3.0f);
        }
        float f = itemStack.getMiningSpeedMultiplier(blockState);
        if (f > 1.0f) {
            int n = ItemEnchantmentUtils.getEnchantmentLevel(itemStack, Enchantments.EFFICIENCY);
            f += n > 0 ? (float) (Math.pow(n, 2.0) + 1.0) : 0.0f;
        }
        return Float.valueOf(f);
    }
}