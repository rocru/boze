package dev.boze.client.mixin;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractTypeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({PlayerInteractEntityC2SPacket.class})
public interface PlayerInteractEntityC2SPacketAccessor {
   @Mutable
   @Accessor
   void setEntityId(int var1);

   @Accessor
   InteractTypeHandler getType();

   @Accessor
   int getEntityId();
}
