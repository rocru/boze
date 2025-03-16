package dev.boze.client.enums;

enum AntiVoidType {
   Packet,
   PacketFloat,
   Hover,
   Motion,
   Flight;

   private static final AntiVoidType[] field1700 = method820();

   private static AntiVoidType[] method820() {
      return new AntiVoidType[]{Packet, PacketFloat, Hover, Motion, Flight};
   }
}
