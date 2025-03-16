package dev.boze.client.enums;

enum SmartMineInventoryMode {
   Logout,
   Stay,
   Return,
   ReturnLog;

   private static final SmartMineInventoryMode[] field11 = method6();

   private static SmartMineInventoryMode[] method6() {
      return new SmartMineInventoryMode[]{Logout, Stay, Return, ReturnLog};
   }
}
