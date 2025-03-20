package dev.boze.client.utils.misc;

import com.google.gson.JsonObject;

public interface IJsonSerializable2<T> {
    JsonObject serialize();

    T deserialize(JsonObject var1);
}
