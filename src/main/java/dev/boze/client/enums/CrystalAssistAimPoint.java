package dev.boze.client.enums;

enum CrystalAssistAimPoint {
   MinAngle,
   Closest,
   Clamped,
   Center,
   Head;

   private static final CrystalAssistAimPoint[] field1765 = method877();

   private static CrystalAssistAimPoint[] method877() {
      return new CrystalAssistAimPoint[]{MinAngle, Closest, Clamped, Center, Head};
   }
}
