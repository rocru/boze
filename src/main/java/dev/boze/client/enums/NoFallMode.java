package dev.boze.client.enums;

enum NoFallMode {
   Vanilla,
   Grim,
   Motion,
   Packet,
   PacketSpam,
   AirPlace,
   Ghost;

   private static final NoFallMode[] field1786 = method897();

   private static NoFallMode[] method897() {
      return new NoFallMode[]{Vanilla, Grim, Motion, Packet, PacketSpam, AirPlace, Ghost};
   }
}
