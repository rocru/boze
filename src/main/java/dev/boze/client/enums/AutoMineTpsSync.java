package dev.boze.client.enums;

public enum AutoMineTpsSync {
   Off,
   Avg,
   Last,
   Min;

   private static final AutoMineTpsSync[] field1686 = method805();

   private static AutoMineTpsSync[] method805() {
      return new AutoMineTpsSync[]{Off, Avg, Last, Min};
   }
}
