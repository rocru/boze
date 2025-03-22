package dev.boze.client.mixin;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExplosionS2CPacket.class)
public interface ExplosionS2CPacketAccessor {
    @Accessor
    float getPlayerVelocityX();

    @Mutable
    @Accessor
    void setPlayerVelocityX(float var1);

    @Accessor
    float getPlayerVelocityY();

    @Mutable
    @Accessor
    void setPlayerVelocityY(float var1);

    @Accessor
    float getPlayerVelocityZ();

    @Mutable
    @Accessor
    void setPlayerVelocityZ(float var1);
}
