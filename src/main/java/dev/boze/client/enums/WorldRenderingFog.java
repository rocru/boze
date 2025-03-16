package dev.boze.client.enums;

enum WorldRenderingFog {
   Off,
   Terrain,
   Sky,
   Both;

   private static final WorldRenderingFog[] field1781 = method892();

   private static WorldRenderingFog[] method892() {
      return new WorldRenderingFog[]{Off, Terrain, Sky, Both};
   }
}
