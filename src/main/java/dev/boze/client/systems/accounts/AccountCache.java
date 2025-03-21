package dev.boze.client.systems.accounts;

import com.google.gson.JsonObject;
import com.mojang.util.UndashedUuid;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.misc.IJsonSerializable;
import dev.boze.client.utils.render.PlayerHeadTexture;
import dev.boze.client.utils.render.PlayerHeadUtils;

import java.util.UUID;

public class AccountCache implements IJsonSerializable<AccountCache>, IMinecraft {
    public String field1280 = "";
    public String field1281 = "";
    private PlayerHeadTexture field1282;

    public PlayerHeadTexture method551() {
        return this.field1282 != null ? this.field1282 : PlayerHeadUtils.field3957;
    }

    public void method2142() {
        if (this.field1281 != null && !this.field1281.isBlank()) {
            this.field1282 = PlayerHeadUtils.method2230(UndashedUuid.fromStringLenient(this.field1281));
        }
    }

    @Override
    public JsonObject serialize() {
        JsonObject var3 = new JsonObject();
        var3.addProperty("username", this.field1280);
        var3.addProperty("uuid", this.field1281);
        return var3;
    }

    @Override
    public AccountCache deserialize(JsonObject object) {
        if (object.has("username") && object.has("uuid")) {
            this.field1280 = object.get("username").getAsString();
            this.field1281 = object.get("uuid").getAsString();
            this.method2142();
            return this;
        } else {
            this.field1280 = "INVALID";
            this.field1281 = UUID.randomUUID().toString();
            return this;
        }
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object deserialize(JsonObject jsonObject) {
    //   return this.method552(jsonObject);
    //}
}
