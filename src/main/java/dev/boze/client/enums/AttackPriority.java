package dev.boze.client.enums;

enum AttackPriority {
   Distance,
   FOV,
   Health;

   private static final AttackPriority[] field1805 = method915();

   private static AttackPriority[] method915() {
      return new AttackPriority[]{Distance, FOV, Health};
   }
}
