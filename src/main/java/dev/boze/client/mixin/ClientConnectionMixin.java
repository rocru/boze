package dev.boze.client.mixin;

import dev.boze.client.events.ConnectEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PostPacketSendEvent;
import dev.boze.client.events.PrePacketSendEvent;
import java.net.InetSocketAddress;
import java.util.Iterator;
import dev.boze.client.Boze;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientConnection.class})
public abstract class ClientConnectionMixin {
   @Inject(
      method = {"connect(Ljava/net/InetSocketAddress;ZLnet/minecraft/network/ClientConnection;)Lio/netty/channel/ChannelFuture;"},
      at = {@At("HEAD")}
   )
   private static void onConnect(InetSocketAddress var0, boolean var1, ClientConnection var2, CallbackInfoReturnable<?> var3) {
      Boze.EVENT_BUS.post(ConnectEvent.method1057());
   }

   @Inject(
      method = {"send(Lnet/minecraft/network/packet/Packet;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendPacketHead(Packet<?> var1, CallbackInfo var2) {
      PrePacketSendEvent var3 = (PrePacketSendEvent) Boze.EVENT_BUS.post(new PrePacketSendEvent(var1));
      if (var3.method1022()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"send(Lnet/minecraft/network/packet/Packet;)V"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private void onSendPacketTail(Packet<?> var1, CallbackInfo var2) {
      PostPacketSendEvent var3 = (PostPacketSendEvent) Boze.EVENT_BUS.post(new PostPacketSendEvent(var1));
      if (var3.method1022()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"handlePacket"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static <T extends PacketListener> void onHandlePacket(Packet<T> var0, PacketListener var1, CallbackInfo var2) {
      if (var0 instanceof BundleS2CPacket var5) {
         Iterator var4 = var5.getPackets().iterator();

         while (var4.hasNext()) {
            if (((PacketBundleEvent) Boze.EVENT_BUS.post(new PacketBundleEvent((Packet<?>)var4.next()))).method1022()) {
               var4.remove();
            }
         }
      } else {
         PacketBundleEvent var3 = (PacketBundleEvent) Boze.EVENT_BUS.post(new PacketBundleEvent(var0));
         if (var3.method1022()) {
            var2.cancel();
         }
      }
   }
}
