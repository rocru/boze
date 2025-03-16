package dev.boze.client.enums;

enum MiddleClick {
   None(false),
   Friend(false),
   XP(true),
   EP(true),
   Rocket(true);

   final boolean field1797;
   private static final MiddleClick[] field1798 = method908();

   private MiddleClick(boolean var3) {
      this.field1797 = var3;
   }

   private static MiddleClick[] method908() {
      return new MiddleClick[]{None, Friend, XP, EP, Rocket};
   }
}
