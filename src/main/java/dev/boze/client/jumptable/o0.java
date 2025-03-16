package dev.boze.client.jumptable;

import dev.boze.client.systems.accounts.AccountType;

class o0 {
   static final int[] field2127 = new int[AccountType.values().length];

   static {
      try {
         field2127[AccountType.Cracked.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2127[AccountType.Altening.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2127[AccountType.Microsoft.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
