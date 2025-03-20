package dev.boze.client.utils;

import dev.boze.client.mixin.ContainerComponentAccessor;
import dev.boze.client.utils.trackers.InventoryTracker;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

import java.util.Arrays;

public class StackDeserializer implements IMinecraft {
    public static boolean method1756(ItemStack itemStack) {
        ContainerComponentAccessor var4 = (ContainerComponentAccessor) (Object) itemStack.get(DataComponentTypes.CONTAINER);
        if (var4 != null && !var4.getStacks().isEmpty()) {
            return true;
        } else {
            NbtCompound var5 = itemStack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).getNbt();
            return var5 != null && var5.contains("Items", 9);
        }
    }

    public static void method670(ItemStack itemStack, ItemStack[] items) {
        if (itemStack.getItem() == Items.ENDER_CHEST) {
            for (int var10 = 0; var10 < InventoryTracker.field1360.size(); var10++) {
                items[var10] = InventoryTracker.field1360.get(var10);
            }
        } else {
            Arrays.fill(items, ItemStack.EMPTY);
            ComponentMap var5 = itemStack.getComponents();
            if (var5.contains(DataComponentTypes.CONTAINER)) {
                ContainerComponentAccessor var6 = (ContainerComponentAccessor) (Object) var5.get(DataComponentTypes.CONTAINER);
                DefaultedList var7 = var6.getStacks();

                for (int var8 = 0; var8 < var7.size(); var8++) {
                    if (var8 >= 0 && var8 < items.length) {
                        items[var8] = (ItemStack) var7.get(var8);
                    }
                }
            } else if (var5.contains(DataComponentTypes.BLOCK_ENTITY_DATA)) {
                NbtComponent var11 = var5.get(DataComponentTypes.BLOCK_ENTITY_DATA);
                if (var11.contains("Items")) {
                    NbtList var12 = (NbtList) var11.getNbt().get("Items");

                    for (int var13 = 0; var13 < var12.size(); var13++) {
                        byte var9 = var12.getCompound(var13).getByte("Slot");
                        if (var9 >= 0 && var9 < items.length) {
                            items[var9] = ItemStack.fromNbtOrEmpty(mc.player.getRegistryManager(), var12.getCompound(var13));
                        }
                    }
                }
            }
        }
    }
}
