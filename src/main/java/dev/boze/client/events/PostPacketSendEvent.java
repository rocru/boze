package dev.boze.client.events;

import net.minecraft.network.packet.Packet;

public class PostPacketSendEvent extends PacketEvent {
   public PostPacketSendEvent(Packet<?> packet) {
      this.packet = packet;
      this.method1021(false);
   }
}
