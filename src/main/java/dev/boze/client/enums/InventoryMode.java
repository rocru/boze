package dev.boze.client.enums;

enum InventoryMode {
   Ignore,
   Await,
   Auto;

   private static final InventoryMode[] field1643 = method762();

   private static InventoryMode[] method762() {
      return new InventoryMode[]{Ignore, Await, Auto};
   }
}
