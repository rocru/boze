package mapped;

import com.google.gson.JsonObject;
import dev.boze.client.utils.misc.IJsonSerializable2;

public class Class5906 implements IJsonSerializable2<Class5906> {
   public static Class5906 field1 = new Class5906();

   @Override
   public JsonObject serialize() {
      JsonObject var3 = new JsonObject();
      JsonObject var4 = Class27.getModules().serialize();
      var3.add("modules", var4);
      return var3;
   }

   public Class5906 method12(JsonObject data) {
      if (var1 == null) {
         return this;
      } else {
         if (var1.has("modules")) {
            Class27.getModules().method400(var1.getAsJsonObject("modules"));
         }

         return this;
      }
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object deserialize(JsonObject jsonObject) {
      return this.method12(jsonObject);
   }
}
