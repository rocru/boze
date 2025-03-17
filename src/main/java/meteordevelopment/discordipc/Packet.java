package meteordevelopment.discordipc;

import com.google.gson.JsonObject;

public class Packet {
   private final Opcode field2530;
   private final JsonObject field2531;

   public Packet(Opcode opcode, JsonObject data) {
      this.field2530 = opcode;
      this.field2531 = data;
   }

   public Opcode method1473() {
      return this.field2530;
   }

   public JsonObject method1474() {
      return this.field2531;
   }
}
