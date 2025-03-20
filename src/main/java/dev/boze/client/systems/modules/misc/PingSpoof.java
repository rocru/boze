package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;

import java.util.concurrent.ConcurrentHashMap;

public class PingSpoof extends Module {
   public static final PingSpoof INSTANCE = new PingSpoof();
   private final IntSetting field3052 = new IntSetting("Delay", 200, 1, 2000, 1, "Delay in ms");
   private final ConcurrentHashMap<Long, Long> field3053 = new ConcurrentHashMap();

   public PingSpoof() {
      super("PingSpoof", "Spoof your server ping (latency)", Category.Misc);
   }

   @EventHandler
   private void method1745(MovementEvent var1) {
      this.field3053.forEach(this::lambda$onSendMovementPackets$0);
   }

   @EventHandler
   private void method1746(PrePacketSendEvent var1) {
      if (var1.packet instanceof KeepAliveC2SPacket && !this.field3053.containsKey(((KeepAliveC2SPacket)var1.packet).getId())) {
         this.field3053.put(((KeepAliveC2SPacket)var1.packet).getId(), System.currentTimeMillis());
         var1.method1020();
      }
   }

   private void lambda$onSendMovementPackets$0(Long var1, Long var2) {
      if (System.currentTimeMillis() - var2 >= (long)this.field3052.getValue().intValue()) {
         mc.player.networkHandler.sendPacket(new KeepAliveC2SPacket(var1));
         this.field3053.remove(var1);
      }
   }
}
