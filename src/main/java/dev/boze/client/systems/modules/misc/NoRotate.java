package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.mixin.PlayerPositionLookS2CPacketAccessor;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

public class NoRotate extends Module {
    public static final NoRotate INSTANCE = new NoRotate();

    public NoRotate() {
        super("NoRotate", "Prevent server packets from rotating you", Category.Misc);
    }

    @EventHandler
    public void method1730(PacketBundleEvent event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket var5 && MinecraftUtils.isClientActive()) {
            ((PlayerPositionLookS2CPacketAccessor) var5).setYaw(mc.player.getYaw());
            ((PlayerPositionLookS2CPacketAccessor) var5).setPitch(mc.player.getPitch());
            var5.getFlags().remove(PositionFlag.X_ROT);
            var5.getFlags().remove(PositionFlag.Y_ROT);
        }
    }
}
