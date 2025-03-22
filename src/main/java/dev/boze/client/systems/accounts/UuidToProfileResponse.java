package dev.boze.client.systems.accounts;

import com.google.gson.annotations.SerializedName;

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

    class Property {
        @SerializedName("name")
        public String field2296;
        @SerializedName("value")
        public String field2297;
    }
}
