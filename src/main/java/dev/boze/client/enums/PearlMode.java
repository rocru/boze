package dev.boze.client.enums;

enum PearlMode {
   Forward,
   Omni,
   Collision;

   private static final PearlMode[] field19 = method14();

   private static PearlMode[] method14() {
      return new PearlMode[]{Forward, Omni, Collision};
   }
}
