package dev.boze.client.enums;

enum AntiKickMode {
   Off,
   Strong,
   Strict,
   Toggle;

   private static final AntiKickMode[] field1699 = method819();

   private static AntiKickMode[] method819() {
      return new AntiKickMode[]{Off, Strong, Strict, Toggle};
   }
}
