package dev.boze.client.enums;

public enum TpsSyncMode {
   Off,
   Avg,
   Last,
   Min;

   private static final TpsSyncMode[] field48 = method41();

   private static TpsSyncMode[] method41() {
      return new TpsSyncMode[]{Off, Avg, Last, Min};
   }
}
