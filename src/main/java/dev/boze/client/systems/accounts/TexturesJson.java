package dev.boze.client.systems.accounts;

import com.google.gson.annotations.SerializedName;

public class TexturesJson {
    @SerializedName("textures")
    public Textures field2292;

    public class Textures {
        @SerializedName("SKIN")
        public Texture field2293;

        public class Texture {
            @SerializedName("url")
            public String field2294;
        }
    }
}
