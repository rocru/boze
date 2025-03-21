package dev.boze.client.mixin;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({EntityVelocityUpdateS2CPacket.class})
public interface EntityVelocityUpdateS2CPacketAccessor {
    @Mutable
    @Accessor("velocityX")
    void setVelocityX(int var1);

    @Accessor("velocityX")
    int getVelocityX();

    @Mutable
    @Accessor("velocityY")
    void setVelocityY(int var1);

    @Accessor("velocityY")
    int getVelocityY();

    @Mutable
    @Accessor("velocityZ")
    void setVelocityZ(int var1);

    @Accessor("velocityZ")
    int getVelocityZ();
}
