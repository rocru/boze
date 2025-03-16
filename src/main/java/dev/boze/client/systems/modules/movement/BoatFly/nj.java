package dev.boze.client.systems.modules.movement.BoatFly;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerTravelEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.systems.modules.movement.BoatFly;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;

abstract class nj implements IMinecraft {
   protected final BoatFly field1556;

   public nj(BoatFly module) {
      this.field1556 = module;
   }

   abstract void method2142();

   abstract void method1416();

   abstract void method2042(PacketBundleEvent var1);

   abstract void method1853(PrePacketSendEvent var1);

   void method1794(PlayerTravelEvent var1) {
   }

   void method2072(PreTickEvent var1) {
   }

   boolean method2114() {
      return false;
   }

   boolean method2115() {
      return false;
   }

   boolean method1798(VehicleMoveS2CPacket var1) {
      return false;
   }

   void method2071(Render3DEvent var1) {
   }
}
