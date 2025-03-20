package dev.boze.client.utils.trackers;

import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.ItemSlot;
import net.minecraft.item.ItemStack;

public class ItemTracker implements IMinecraft {
    private static final ItemSlot[] field1550 = new ItemSlot[2];
    private static int field1551 = 0;

    private static boolean method2114() {
        return !Options.INSTANCE.method1971() && AntiCheat.INSTANCE.field2323.method419();
    }

    public static boolean method686(int slot, ItemStack stack) {
        return method2114() && method687(slot, stack);
    }

    public static void method1964(int slotA, int slotB) {
        if (method2114()) {
            if (slotB < 9) {
                slotB += 36;
            }

            ItemStack var5 = mc.player.currentScreenHandler.getSlot(slotA).getStack();
            ItemStack var6 = mc.player.currentScreenHandler.getSlot(slotB).getStack();
            field1550[0] = new ItemSlot(slotB, var5);
            field1550[1] = new ItemSlot(slotA, var6);
            field1551 = 2;
        }
    }

    private static boolean method687(int var0, ItemStack var1) {
        if (field1551 != 0) {
            for (int var5 = 0; var5 < field1551; var5++) {
                ItemSlot var6 = field1550[var5];

                if (var6 != null && var6.field3915 == var0 && var6.field3916.getItem().equals(var1.getItem())) {
                    return true;
                }
            }

        }
        return false;
    }

    public static void method2142() {
        field1550[0] = null;
        field1550[1] = null;
        field1551 = 0;
    }
}
