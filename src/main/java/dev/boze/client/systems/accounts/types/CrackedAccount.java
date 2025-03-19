package dev.boze.client.systems.accounts.types;

import dev.boze.client.systems.accounts.Account;
import dev.boze.client.systems.accounts.AccountType;
import net.minecraft.client.session.Session;
import net.minecraft.util.Uuids;

import java.util.Optional;

public class CrackedAccount extends Account<CrackedAccount> {
   public CrackedAccount(String name) {
      super(AccountType.Cracked, name);
   }

   @Override
   public boolean method2114() {
      this.field1522.field1280 = this.field1521;
      return true;
   }

   @Override
   public boolean method2115() {
      super.method2115();
      this.field1522.method2142();
      method675(
         new Session(
            this.field1521,
            Uuids.getOfflinePlayerUuid(this.field1521),
            "",
            Optional.empty(),
            Optional.empty(),
            net.minecraft.client.session.Session.AccountType.MOJANG
         )
      );
      return true;
   }

   public boolean equals(Object o) {
      return o instanceof CrackedAccount && ((CrackedAccount) o).method210().equals(this.method210());
   }
}
