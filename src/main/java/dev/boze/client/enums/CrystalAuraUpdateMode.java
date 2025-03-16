package dev.boze.client.enums;

enum CrystalAuraUpdateMode {
   Tick,
   Spawn,
   Packet;

   private static final CrystalAuraUpdateMode[] field1658 = method777();

   private static CrystalAuraUpdateMode[] method777() {
      return new CrystalAuraUpdateMode[]{Tick, Spawn, Packet};
   }
}
