package mapped;

import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.ClientWorldAccessor;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class Class5913 implements IMinecraft {
   public static PlayerInteractItemC2SPacket method16(Hand hand) {
      return (PlayerInteractItemC2SPacket)method18(Class5913::lambda$interactItem$0);
   }

   public static PlayerInteractBlockC2SPacket method17(Hand hand, BlockHitResult blockHitResult) {
      return (PlayerInteractBlockC2SPacket)method18(Class5913::lambda$interactBlock$1);
   }

   public static Packet<ServerPlayPacketListener> method18(SequencedPacketCreator packetCreator) {
      PendingUpdateManager var4 = ((ClientWorldAccessor)mc.world).callGetPendingUpdateManager().incrementSequence();

      try {
         int var51 = var4.getSequence();
         Packet var9 = var5.predict(var51);
         mc.player.networkHandler.sendPacket(var9);
         var4.close();
         return var9;
      } catch (Throwable var8) {
         PendingUpdateManager var10000 = var4;

         try {
            var10000.close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         Packet var6 = var5.predict(0);
         mc.player.networkHandler.sendPacket(var6);
         return var6;
      }
   }

   private static Packet lambda$interactBlock$1(Hand var0, BlockHitResult var1, int var2) {
      return new PlayerInteractBlockC2SPacket(var0, var1, var2);
   }

   private static Packet lambda$interactItem$0(Hand var0, int var1) {
      return new PlayerInteractItemC2SPacket(
         var0, var1, ((ClientPlayerEntityAccessor)mc.player).getLastYaw(), ((ClientPlayerEntityAccessor)mc.player).getLastPitch()
      );
   }
}
