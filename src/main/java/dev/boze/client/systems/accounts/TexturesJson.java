package dev.boze.client.systems.accounts;

import com.google.gson.annotations.SerializedName;
import dev.boze.client.systems.accounts.TexturesJson.Textures;

public class TexturesJson {
   class Textures {
      class Texture {
         @SerializedName("url")
         public String field2294;
      }

      @SerializedName("SKIN")
      public Texture field2293;
   }

   @SerializedName("textures")
   public Textures field2292;
}
