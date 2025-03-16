package dev.boze.client.systems.accounts;

public class MicrosoftLogin$LoginData {
   public String field2281;
   public String field2282;
   public String field2283;
   public String field2284;

   public MicrosoftLogin$LoginData() {
   }

   public MicrosoftLogin$LoginData(String mcToken, String newRefreshToken, String uuid, String username) {
      this.field2281 = mcToken;
      this.field2282 = newRefreshToken;
      this.field2283 = uuid;
      this.field2284 = username;
   }

   public boolean method1320() {
      return this.field2281 != null;
   }
}
