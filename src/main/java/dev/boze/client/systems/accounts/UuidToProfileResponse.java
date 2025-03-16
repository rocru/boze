package dev.boze.client.systems.accounts;

import com.google.gson.annotations.SerializedName;
import dev.boze.client.systems.accounts.UuidToProfileResponse.Property;

public class UuidToProfileResponse {
   @SerializedName("properties")
   public Property[] field2295;

   public String method1321(String name) {
      for (Property var8 : this.field2295) {
         if (var8.field2296.equals(name)) {
            return var8.field2297;
         }
      }

      return null;
   }
}
