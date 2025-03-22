package dev.boze.client.utils;

import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class HypixelServer implements MCServer, IMinecraft {
    private static final String[] field1264 = new String[]{"collectibles", "menu", "selector", "shop", "buy"};

    private static boolean method546(Item var0) {
        if (var0 != Items.GLASS_PANE && var0 != Items.BARRIER) {
            return var0 instanceof BlockItem var4 && var4.getBlock() instanceof StainedGlassPaneBlock;
        } else {
            return true;
        }
    }

    private static boolean lambda$isSpectator$1(ItemStack var0) {
        return var0.getItem() == Items.PAPER;
    }

    private static boolean lambda$isInLobby$0(ItemStack var0) {
        return var0.getItem() == Items.NETHER_STAR;
    }

    @Override
    public boolean method2117() {
        int var4 = InventoryHelper.method168(HypixelServer::lambda$isInLobby$0);
        return var4 != -1;
    }

    @Override
    public boolean method2118() {
        int var4 = InventoryHelper.method168(HypixelServer::lambda$isSpectator$1);
        return var4 != -1;
    }

    @Override
    public boolean method222() {
        if (!(mc.currentScreen instanceof GenericContainerScreen var4)) {
            return false;
        } else {
            String var5 = var4.getTitle().getString();

            for (String var9 : field1264) {
                if (var5.toLowerCase().contains(var9)) {
                    return true;
                }
            }

            for (Slot var11 : var4.getScreenHandler().slots) {
                if (var11.hasStack() && method546(var11.getStack().getItem())) {
                    return true;
                }
            }

            return false;
        }
    }
}
