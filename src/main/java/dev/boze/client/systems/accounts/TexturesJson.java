package dev.boze.client.systems.accounts;

import com.google.gson.annotations.SerializedName;

public class TexturesJson {
   public class Textures {
      public class Texture {
         @SerializedName("url")
         public String field2294;
      }

      @SerializedName("SKIN")
      public Texture field2293;
   }

   @SerializedName("textures")
   public Textures field2292;
}
