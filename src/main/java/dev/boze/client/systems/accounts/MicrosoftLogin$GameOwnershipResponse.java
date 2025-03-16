package dev.boze.client.systems.accounts;

import com.google.gson.annotations.SerializedName;
import dev.boze.client.systems.accounts.MicrosoftLogin.GameOwnershipResponse.Item;

class MicrosoftLogin$GameOwnershipResponse {
   @SerializedName("items")
   private Item[] field2280;

   private MicrosoftLogin$GameOwnershipResponse() {
   }

   private boolean method1317() {
      boolean var4 = false;
      boolean var5 = false;

      for (Item var9 : this.field2280) {
         if (var9.name.equals("product_minecraft")) {
            var4 = true;
         } else if (var9.name.equals("game_minecraft")) {
            var5 = true;
         }
      }

      return var4 && var5;
   }
}
