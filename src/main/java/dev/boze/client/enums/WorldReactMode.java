package dev.boze.client.enums;

enum WorldReactMode {
   Tick,
   Packet;

   private static final WorldReactMode[] field1696 = method815();

   private static WorldReactMode[] method815() {
      return new WorldReactMode[]{Tick, Packet};
   }
}
