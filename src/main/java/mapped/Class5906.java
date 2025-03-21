package mapped;

import com.google.gson.JsonObject;
import dev.boze.client.Boze;
import dev.boze.client.utils.misc.IJsonSerializable2;

public class Class5906 implements IJsonSerializable2<Class5906> {
    public static Class5906 field1 = new Class5906();

    @Override
    public JsonObject serialize() {
        JsonObject var3 = new JsonObject();
        JsonObject var4 = Boze.getModules().serialize();
        var3.add("modules", var4);
        return var3;
    }

    @Override
    public Class5906 deserialize(JsonObject data) {
        if (data == null) {
            return this;
        } else {
            if (data.has("modules")) {
                Boze.getModules().deserialize(data.getAsJsonObject("modules"));
            }

            return this;
        }
    }

    // $VF: synthetic method
    // $VF: bridge method
    //  @Override
    //public Object deserialize(JsonObject jsonObject) {
    //   return this.method12(jsonObject);
    //}
}
