package dev.boze.client.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;

public class FoodUtil {
    public static boolean isFood(ItemStack stack) {
        return stack.get(DataComponentTypes.FOOD) != null;
    }

    public static float method2152(ItemStack stack) {
        FoodComponent var4 = stack.get(DataComponentTypes.FOOD);
        return var4 != null ? (float) var4.nutrition() : 0.0F;
    }
}
