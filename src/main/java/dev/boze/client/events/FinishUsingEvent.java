package dev.boze.client.events;

import net.minecraft.item.ItemStack;

public class FinishUsingEvent {
   private static final FinishUsingEvent field1918 = new FinishUsingEvent();
   public ItemStack field1919;

   public static FinishUsingEvent method1061(ItemStack itemStack) {
      field1918.field1919 = itemStack;
      return field1918;
   }
}
