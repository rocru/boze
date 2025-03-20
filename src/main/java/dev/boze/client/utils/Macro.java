package dev.boze.client.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.boze.client.Boze;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.misc.IJsonSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Macro implements IJsonSerializable<Macro>, IMinecraft {
    public String field1048 = "";
    public Bind field1049 = Bind.create();
    public boolean field1050 = true;
    public List<String> field1051 = new ArrayList(1);

    public boolean method490(boolean isKey, int value) {
        if (mc.player == null || mc.currentScreen != null || !this.field1050) {
            return false;
        } else if (this.field1049.matches(isKey, value)) {
            this.method2142();
            return true;
        } else {
            return false;
        }
    }

    public void method2142() {
        for (String var5 : this.field1051) {
            if (var5.startsWith(Options.method1563())) {
                try {
                    Boze.getCommands().method1138(var5.substring(Options.method1563().length()));
                } catch (Exception var7) {
                }
            } else {
                ChatInstance.method1800(var5);
            }
        }
    }

    public String method210() {
        return this.field1048;
    }

    @Override
    public JsonObject serialize() {
        JsonObject var4 = new JsonObject();
        var4.addProperty("name", this.field1048);
        var4.add("bind", this.field1049.serialize());
        var4.addProperty("active", this.field1050);
        JsonArray var5 = new JsonArray();

        for (String var7 : this.field1051) {
            var5.add(var7);
        }

        var4.add("commands", var5);
        return var4;
    }

    @Override
    public Macro deserialize(JsonObject object) {
        this.field1048 = object.get("name").getAsString();
        this.field1049 = this.field1049.deserialize(object.get("bind").getAsJsonObject());
        this.field1050 = object.get("active").getAsBoolean();
        this.field1051.clear();

        for (JsonElement var6 : object.get("commands").getAsJsonArray()) {
            this.field1051.add(var6.getAsString());
        }

        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Macro var5 = (Macro) o;
            return Objects.equals(this.field1048, var5.field1048);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.field1048);
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object deserialize(JsonObject jsonObject) {
    //   return this.method491(jsonObject);
    //}
}
