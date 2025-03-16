package dev.boze.client.systems.accounts;

import com.google.gson.annotations.SerializedName;
import dev.boze.client.systems.accounts.MicrosoftLogin.XblXstsResponse.DisplayClaims;

class MicrosoftLogin$XblXstsResponse {
   @SerializedName("Token")
   public String field2288;
   @SerializedName("DisplayClaims")
   public DisplayClaims field2289;

   private MicrosoftLogin$XblXstsResponse() {
   }
}
