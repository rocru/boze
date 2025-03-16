package dev.boze.client.enums;

enum TargetMode {
   Distance,
   Health;

   private static final TargetMode[] field50 = method43();

   private static TargetMode[] method43() {
      return new TargetMode[]{Distance, Health};
   }
}
