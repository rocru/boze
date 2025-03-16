package dev.boze.client.enums;

enum SafetyMode {
   None,
   Lethal,
   Health;

   private static final SafetyMode[] field1750 = method865();

   private static SafetyMode[] method865() {
      return new SafetyMode[]{None, Lethal, Health};
   }
}
