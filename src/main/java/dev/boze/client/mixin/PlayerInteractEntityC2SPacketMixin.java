package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IPlayerInteractEntityC2SPacket;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractType;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractTypeHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({PlayerInteractEntityC2SPacket.class})
public class PlayerInteractEntityC2SPacketMixin implements IPlayerInteractEntityC2SPacket {
   @Shadow
   @Final
   private InteractTypeHandler type;
   @Shadow
   @Final
   private int entityId;

   @Override
   public InteractType boze$getType() {
      return this.type.getType();
   }

   @Override
   public Entity boze$getEntity() {
      return IMinecraft.mc.world == null ? null : IMinecraft.mc.world.getEntityById(this.entityId);
   }
}
