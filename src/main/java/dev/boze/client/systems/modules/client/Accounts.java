package dev.boze.client.systems.modules.client;

import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.AccountManagerComponent;
import dev.boze.client.settings.AccountSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class Accounts extends Module {
   public static final Accounts INSTANCE = new Accounts();
   public final AccountSetting field2313 = new AccountSetting("Alts", "Manage alts");

   public Accounts() {
      super("Accounts", "Manage alts", Category.Client);
      this.method219(this::lambda$new$0);
   }

   @Override
   public boolean setEnabled(boolean newState) {
      return false;
   }

   private ScaledBaseComponent lambda$new$0() {
      return new AccountManagerComponent(this.field2313);
   }
}
