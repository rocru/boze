package dev.boze.client.utils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class InventoryRenderer implements IMinecraft {
    public static void method157(DrawContext drawContext, ItemStack itemStack, int x, int y, float scale, boolean overlay, String countOverride) {
        MatrixStack var10 = drawContext.getMatrices();
        var10.push();
        var10.scale(scale, scale, 1.0F);
        var10.translate(0.0F, 0.0F, 420.0F);
        int var11 = (int) ((float) x / scale);
        int var12 = (int) ((float) y / scale);
        drawContext.drawItem(itemStack, var11, var12);
        if (overlay) {
            drawContext.drawItemInSlot(mc.textRenderer, itemStack, var11, var12, countOverride);
        }

        var10.pop();
    }

    public static void method158(DrawContext drawContext, ItemStack itemStack, int x, int y, float scale, boolean overlay) {
        method157(drawContext, itemStack, x, y, scale, overlay, null);
    }
}
