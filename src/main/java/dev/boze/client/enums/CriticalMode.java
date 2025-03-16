package dev.boze.client.enums;

public enum CriticalMode {
   Module,
   Category,
   Full;

   private static final CriticalMode[] field12 = method7();

   private static CriticalMode[] method7() {
      return new CriticalMode[]{Module, Category, Full};
   }
}
