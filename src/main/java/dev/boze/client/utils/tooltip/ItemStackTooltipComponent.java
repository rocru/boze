package dev.boze.client.utils.tooltip;

import dev.boze.client.systems.modules.render.Tooltips;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ItemStackTooltipComponent implements TooltipComponent, BozeTooltipData, IMinecraft {
    private final ItemStack[] field1059;

    public ItemStackTooltipComponent(ItemStack[] items) {
        this.field1059 = items;
    }

    @Override
    public TooltipComponent method498() {
        return this;
    }

    public int getHeight() {
        return (int) (54.0 * Tooltips.INSTANCE.field3764.getValue()) + 7;
    }

    public int getWidth(TextRenderer textRenderer) {
        return (int) (162.0 * Tooltips.INSTANCE.field3764.getValue()) + 7;
    }

    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int var8 = 0;
        int var9 = 0;
        MatrixStack var10 = context.getMatrices();

        for (ItemStack var14 : this.field1059) {
            var10.push();
            var10.scale(Tooltips.INSTANCE.field3764.getValue().floatValue(), Tooltips.INSTANCE.field3764.getValue().floatValue(), 1.0F);
            context.drawItem(
                    var14,
                    (int) ((float) (x + 4) / Tooltips.INSTANCE.field3764.getValue().floatValue() + (float) (var9 * 18)),
                    (int) (((double) y + 3.5) / (double) Tooltips.INSTANCE.field3764.getValue().floatValue() + (double) (var8 * 18))
            );
            context.drawItemInSlot(
                    mc.textRenderer,
                    var14,
                    (int) ((float) (x + 4) / Tooltips.INSTANCE.field3764.getValue().floatValue() + (float) (var9 * 18)),
                    (int) (((double) y + 3.5) / (double) Tooltips.INSTANCE.field3764.getValue().floatValue() + (double) (var8 * 18)),
                    null
            );
            var10.pop();
            if (++var9 >= 9) {
                var8++;
                var9 = 0;
            }
        }
    }
}
