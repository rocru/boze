package dev.boze.client.enums;

enum TpsSyncMode {
   Off,
   Avg,
   Last,
   Min;

   private static final TpsSyncMode[] field48 = method41();

   private static TpsSyncMode[] method41() {
      return new TpsSyncMode[]{Off, Avg, Last, Min};
   }
}
