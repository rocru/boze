package dev.boze.client.systems.accounts.types;

import com.mojang.util.UndashedUuid;
import dev.boze.client.systems.accounts.Account;
import dev.boze.client.systems.accounts.AccountType;
import dev.boze.client.systems.accounts.MicrosoftLogin;
import dev.boze.client.systems.accounts.MicrosoftLogin.LoginData;
import net.minecraft.client.session.Session;

import java.util.Optional;

public class MicrosoftAccount extends Account<MicrosoftAccount> {
   public MicrosoftAccount(String refreshToken) {
      super(AccountType.Microsoft, refreshToken);
   }

   @Override
   public boolean method2114() {
      return this.method1322() != null;
   }

   @Override
   public boolean method2115() {
      super.method2115();
      String var4 = this.method1322();
      if (var4 == null) {
         return false;
      } else {
         this.field1522.method2142();
         method675(
            new Session(
               this.field1522.field1280,
               UndashedUuid.fromStringLenient(this.field1522.field1281),
               var4,
               Optional.empty(),
               Optional.empty(),
               net.minecraft.client.session.Session.AccountType.MSA
            )
         );
         return true;
      }
   }

   private String method1322() {
      LoginData var4 = MicrosoftLogin.login(this.field1521);
      if (!var4.method1320()) {
         return null;
      } else {
         this.field1521 = var4.field2282;
         this.field1522.field1280 = var4.field2284;
         this.field1522.field1281 = var4.field2283;
         return var4.field2281;
      }
   }

   public boolean equals(Object o) {
      return !(o instanceof MicrosoftAccount) ? false : ((MicrosoftAccount)o).field1521.equals(this.field1521);
   }
}
