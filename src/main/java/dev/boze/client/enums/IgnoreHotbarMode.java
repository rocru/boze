package dev.boze.client.enums;

enum IgnoreHotbarMode {
   Off,
   Semi,
   SemiLast,
   Full;

   private static final IgnoreHotbarMode[] field1814 = method923();

   private static IgnoreHotbarMode[] method923() {
      return new IgnoreHotbarMode[]{Off, Semi, SemiLast, Full};
   }
}
