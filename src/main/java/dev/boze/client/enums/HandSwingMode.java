package dev.boze.client.enums;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MinecraftUtils;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;

public enum HandSwingMode implements IMinecraft {
    Off,
    Packet,
    Vanilla;

    public void method671(Hand hand) {
        if (MinecraftUtils.isClientActive()) {
            if (this == Packet) {
                mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
            } else if (this == Vanilla) {
                mc.player.swingHand(hand);
            }
        }
    }

    // $VF: synthetic method
    private static HandSwingMode[] method672() {
        return new HandSwingMode[]{Off, Packet, Vanilla};
    }
}
