package dev.boze.client.events;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;

public class TooltipDataEvent {
   private static final TooltipDataEvent field1957 = new TooltipDataEvent();
   public TooltipData field1958;
   public ItemStack field1959;

   public static TooltipDataEvent method1097(ItemStack itemStack) {
      field1957.field1958 = null;
      field1957.field1959 = itemStack;
      return field1957;
   }
}
