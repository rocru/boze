package dev.boze.client.enums;

enum PacketFlyType {
   Up,
   Preserve,
   Down,
   LimitJitter;

   private static final PacketFlyType[] field1656 = method775();

   private static PacketFlyType[] method775() {
      return new PacketFlyType[]{Up, Preserve, Down, LimitJitter};
   }
}
