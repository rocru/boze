package dev.boze.client.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.boze.client.jumptable.o0;
import dev.boze.client.systems.accounts.Account;
import dev.boze.client.systems.accounts.AccountType;
import dev.boze.client.systems.accounts.types.CrackedAccount;
import dev.boze.client.systems.accounts.types.MicrosoftAccount;
import dev.boze.client.systems.accounts.types.TheAlteningAccount;
import dev.boze.client.utils.misc.IJsonSerializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccountManager implements IJsonSerializable<AccountManager> {
   private List<Account<?>> field2132 = new ArrayList();

   public Account<?> method1129(int i) {
      return (Account<?>)this.field2132.get(i);
   }

   public void method1130(Account<?> account) {
      this.field2132.add(account);
   }

   public boolean method1131(Account<?> account) {
      return this.field2132.contains(account);
   }

   public void method1132(Account<?> account) {
      this.field2132.remove(account);
   }

   public int method1133() {
      return this.field2132.size();
   }

   public Iterator<Account<?>> method1134() {
      return this.field2132.iterator();
   }

   public List<Account<?>> method1135() {
      return this.field2132;
   }

   @Override
   public JsonObject serialize() {
      if (this.field2132.isEmpty()) {
         return null;
      } else {
         JsonObject var4 = new JsonObject();
         JsonArray var5 = new JsonArray();

         for (Account var7 : this.field2132) {
            var5.add(var7.serialize());
         }

         var4.add("accounts", var5);
         return var4;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public AccountManager method1136(JsonObject object) {
      if (object == null) {
         return this;
      } else {
         for (JsonElement var7 : object.get("accounts").getAsJsonArray()) {
            JsonObject var8 = var7.getAsJsonObject();
            if (var8.has("type")) {
               AccountType var9 = AccountType.valueOf(var8.get("type").getAsString());

               Object var10 = switch (o0.field2127[var9.ordinal()]) {
                  case 1 -> (CrackedAccount)new CrackedAccount(null).method677(var8);
                  case 2 -> (TheAlteningAccount)new TheAlteningAccount(null).method677(var8);
                  case 3 -> (MicrosoftAccount)new MicrosoftAccount(null).method677(var8);
                  default -> throw new IncompatibleClassChangeError();
               };
               this.field2132.add(var10);
            }
         }

         return this;
      }
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object deserialize(JsonObject jsonObject) {
      return this.method1136(jsonObject);
   }
}
