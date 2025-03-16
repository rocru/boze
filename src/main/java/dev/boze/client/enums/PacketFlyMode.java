package dev.boze.client.enums;

enum PacketFlyMode {
   Factor,
   Fast,
   Setback,
   Slow;

   private static final PacketFlyMode[] field1726 = method844();

   private static PacketFlyMode[] method844() {
      return new PacketFlyMode[]{Factor, Fast, Setback, Slow};
   }
}
