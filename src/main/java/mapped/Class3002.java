package mapped;

import dev.boze.client.Boze;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.mixin.PlayerPositionLookS2CPacketAccessor;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

class Class3002 {
   private Class3002() {
   }

   @EventHandler
   public static void method5871(PacketBundleEvent event) {
      if (var3116.packet instanceof PlayerPositionLookS2CPacket && MinecraftUtils.isClientActive()) {
         PlayerPositionLookS2CPacket var4 = (PlayerPositionLookS2CPacket)var3116.packet;
         ((PlayerPositionLookS2CPacketAccessor)var4).setYaw(IMinecraft.mc.player.getYaw());
         ((PlayerPositionLookS2CPacketAccessor)var4).setPitch(IMinecraft.mc.player.getPitch());
         var4.getFlags().remove(PositionFlag.X_ROT);
         var4.getFlags().remove(PositionFlag.Y_ROT);
         Boze.EVENT_BUS.unsubscribe(Class3002.class);
      }
   }
}
