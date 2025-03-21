package meteordevelopment.discordipc.connection;

import com.google.gson.JsonParser;
import meteordevelopment.discordipc.Opcode;
import meteordevelopment.discordipc.Packet;

import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.function.Consumer;

public class UnixConnection extends Connection {
   private final Selector field4002 = Selector.open();
   private final SocketChannel field4003;
   private final Consumer<Packet> field4004;

   public UnixConnection(String name, Consumer<Packet> callback) throws IOException {
      this.field4003 = SocketChannel.open(UnixDomainSocketAddress.of(name));
      this.field4004 = callback;
      this.field4003.configureBlocking(false);
      this.field4003.register(this.field4002, 1);
      Thread var6 = new Thread(this::method2302);
      var6.setName("Discord IPC - Read thread");
      var6.start();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method2302() {
      State var4 = State.Opcode;
      ByteBuffer var5 = ByteBuffer.allocate(4);
      ByteBuffer var6 = null;
      Opcode var7 = null;

      try {
         while (true) {
            this.field4002.select();
            switch (var4) {
               case State.Opcode:
                  this.field4003.read(var5);
                  if (!var5.hasRemaining()) {
                     var7 = Opcode.method807(Integer.reverseBytes(var5.getInt(0)));
                     var4 = State.Length;
                     var5.rewind();
                  }
                  break;
               case State.Length:
                  this.field4003.read(var5);
                  if (!var5.hasRemaining()) {
                     var6 = ByteBuffer.allocate(Integer.reverseBytes(var5.getInt(0)));
                     var4 = State.Data;
                     var5.rewind();
                  }
                  break;
               case State.Data:
                  this.field4003.read(var6);
                  if (!var6.hasRemaining()) {
                     String var8 = Charset.defaultCharset().decode(var6.rewind()).toString();
                     this.field4004.accept(new Packet(var7, JsonParser.parseString(var8).getAsJsonObject()));
                     var6 = null;
                     var4 = State.Opcode;
                  }
            }
         }
      } catch (Exception var9) {
      }
   }

   @Override
   protected void method2300(ByteBuffer buffer) {
      try {
         this.field4003.write(buffer);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   @Override
   public void method2301() {
      try {
         this.field4002.close();
         this.field4003.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }
}
