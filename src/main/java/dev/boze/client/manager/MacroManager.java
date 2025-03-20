package dev.boze.client.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.Macro;
import dev.boze.client.utils.misc.IJsonSerializable;
import meteordevelopment.orbit.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class MacroManager implements IJsonSerializable<MacroManager>, IMinecraft {
    public List<Macro> field2140 = new ArrayList();

    @EventHandler
    private void method1148(KeyEvent var1) {
        if (var1.action != KeyAction.Release && mc.currentScreen == null) {
            for (Macro var6 : this.field2140) {
                if (var6.method490(true, var1.key)) {
                    break;
                }
            }
        }
    }

    @EventHandler
    private void method1149(MouseButtonEvent var1) {
        if (var1.action != KeyAction.Release && mc.currentScreen == null) {
            for (Macro var6 : this.field2140) {
                var6.method490(false, var1.button);
            }
        }
    }

    @Override
    public JsonObject serialize() {
        JsonObject var4 = new JsonObject();
        JsonArray var5 = new JsonArray();

        for (Macro var7 : this.field2140) {
            var5.add(var7.serialize());
        }

        var4.add("macros", var5);
        return var4;
    }

    @Override
    public MacroManager deserialize(JsonObject tag) {
        if (tag == null) {
            return this;
        } else {
            for (JsonElement var7 : tag.get("macros").getAsJsonArray()) {
                Macro var8 = new Macro();

                try {
                    var8.deserialize(var7.getAsJsonObject());
                    this.field2140.add(var8);
                } catch (Exception var10) {
                }
            }

            return this;
        }
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object deserialize(JsonObject jsonObject) {
    //   return this.method1150(jsonObject);
    //}
}
