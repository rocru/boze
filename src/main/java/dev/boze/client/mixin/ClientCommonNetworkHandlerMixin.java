package dev.boze.client.mixin;

import dev.boze.client.systems.modules.misc.FastUse;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientCommonNetworkHandler.class})
public class ClientCommonNetworkHandlerMixin {
    @Inject(
            method = {"sendPacket"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof PlayerMoveC2SPacket && FastUse.field2947 && FastUse.INSTANCE.isEnabled() && FastUse.INSTANCE.field2948.getValue()) {
            ci.cancel();
        }
    }
}
