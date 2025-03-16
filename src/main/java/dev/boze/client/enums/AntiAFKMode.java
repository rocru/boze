package dev.boze.client.enums;

enum AntiAFKMode {
   Jump,
   Sneak;

   private static final AntiAFKMode[] field15 = method10();

   private static AntiAFKMode[] method10() {
      return new AntiAFKMode[]{Jump, Sneak};
   }
}
