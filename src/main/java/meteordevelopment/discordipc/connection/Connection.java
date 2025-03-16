package meteordevelopment.discordipc.connection;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.function.Consumer;
import meteordevelopment.discordipc.Opcode;
import meteordevelopment.discordipc.Packet;

public abstract class Connection {
   private static final String[] field4001 = new String[]{"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"};

   public static Connection method2298(Consumer<Packet> callback) {
      String var4 = System.getProperty("os.name").toLowerCase();
      if (var4.contains("win")) {
         for (int var13 = 0; var13 < 10; var13++) {
            try {
               return new WinConnection("\\\\?\\pipe\\discord-ipc-" + var13, callback);
            } catch (IOException var10) {
            }
         }
      } else {
         String var5 = null;

         for (String var9 : field4001) {
            var5 = System.getenv(var9);
            if (var5 != null) {
               break;
            }
         }

         if (var5 == null) {
            var5 = "/tmp";
         }

         var5 = var5 + "/discord-ipc-";

         for (int var14 = 0; var14 < 10; var14++) {
            try {
               return new UnixConnection(var5 + var14, callback);
            } catch (IOException var11) {
            }
         }
      }

      return null;
   }

   public void method2299(Opcode opcode, JsonObject o) {
      o.addProperty("nonce", UUID.randomUUID().toString());
      byte[] var6 = o.toString().getBytes();
      ByteBuffer var7 = ByteBuffer.allocate(var6.length + 8);
      var7.putInt(Integer.reverseBytes(opcode.ordinal()));
      var7.putInt(Integer.reverseBytes(var6.length));
      var7.put(var6);
      var7.rewind();
      this.method2300(var7);
   }

   protected abstract void method2300(ByteBuffer var1);

   public abstract void method2301();
}
