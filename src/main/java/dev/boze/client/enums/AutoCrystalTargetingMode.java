package dev.boze.client.enums;

enum AutoCrystalTargetingMode {
   Optimal,
   Exposure,
   Distance,
   Health;

   private static final AutoCrystalTargetingMode[] field39 = method34();

   private static AutoCrystalTargetingMode[] method34() {
      return new AutoCrystalTargetingMode[]{Optimal, Exposure, Distance, Health};
   }
}
