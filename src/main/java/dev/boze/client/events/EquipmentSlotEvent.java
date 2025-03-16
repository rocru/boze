package dev.boze.client.events;

import net.minecraft.entity.EquipmentSlot;

public class EquipmentSlotEvent extends CancelableEvent {
   private static EquipmentSlotEvent field1916 = new EquipmentSlotEvent();
   public EquipmentSlot field1917;

   public static EquipmentSlotEvent method1060(EquipmentSlot equipmentSlot) {
      field1916.field1917 = equipmentSlot;
      field1916.method1021(false);
      return field1916;
   }
}
