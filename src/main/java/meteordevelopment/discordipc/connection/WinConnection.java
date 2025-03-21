package meteordevelopment.discordipc.connection;

import com.google.gson.JsonParser;
import meteordevelopment.discordipc.Opcode;
import meteordevelopment.discordipc.Packet;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.function.Consumer;

public class WinConnection extends Connection {
   private final RandomAccessFile field4005;
   private final Consumer<Packet> field4006;

   WinConnection(String var1, Consumer<Packet> var2) throws IOException {
      this.field4005 = new RandomAccessFile(var1, "rw");
      this.field4006 = var2;
      Thread var5 = new Thread(this::method2303);
      var5.setName("Discord IPC - Read thread");
      var5.start();
   }

   @Override
   protected void method2300(ByteBuffer buffer) {
      try {
         this.field4005.write(buffer.array());
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   private void method2303() {
      ByteBuffer var1 = ByteBuffer.allocate(4);

      try {
         while (true) {
            this.method2304(var1);
            Opcode var2 = Opcode.method807(Integer.reverseBytes(var1.getInt(0)));
            this.method2304(var1);
            int var3 = Integer.reverseBytes(var1.getInt(0));
            ByteBuffer var4 = ByteBuffer.allocate(var3);
            this.method2304(var4);
            String var5 = Charset.defaultCharset().decode(var4.rewind()).toString();
            this.field4006.accept(new Packet(var2, JsonParser.parseString(var5).getAsJsonObject()));
         }
      } catch (Exception var6) {
      }
   }

   private void method2304(ByteBuffer var1) throws IOException {
      var1.rewind();

      while (this.field4005.length() < (long)var1.remaining()) {
         Thread.onSpinWait();

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var6) {
            var6.printStackTrace();
         }
      }

      while (var1.hasRemaining()) {
         this.field4005.getChannel().read(var1);
      }
   }

   @Override
   public void method2301() {
      try {
         this.field4005.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }
}
