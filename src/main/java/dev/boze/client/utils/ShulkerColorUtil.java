package dev.boze.client.utils;

import dev.boze.client.systems.modules.render.Tooltips;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

public class ShulkerColorUtil {
    public static RGBAColor method2169(ItemStack shulkerItem) {
        if (shulkerItem.getItem() instanceof BlockItem var4) {
            Block var9 = var4.getBlock();
            if (var9 == Blocks.ENDER_CHEST) {
                return Tooltips.field3757;
            }

            if (var9 instanceof ShulkerBoxBlock var6) {
                DyeColor var7 = var6.getColor();
                if (var7 == null) {
                    return RGBAColor.field402;
                }

                int var8 = var7.getEntityColor();
                return new RGBAColor(var8);
            }
        }

        return RGBAColor.field402;
    }
}
