package dev.boze.client.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;

public class NetworkHandlerWrapper extends ClientPlayNetworkHandler implements IMinecraft {
    private NetworkHandlerWrapper() {
        super(null, null, null);
    }

    public static NetworkHandlerWrapper method739() {
        try {
            return (NetworkHandlerWrapper) UnsafeProvider.get().allocateInstance(NetworkHandlerWrapper.class);
        } catch (Exception var3) {
            throw new RuntimeException("Error creating dummy class", var3);
        }
    }

    public void sendPacket(Packet<?> packet) {
    }

    public GameProfile getProfile() {
        return mc.getGameProfile();
    }

    public FeatureSet getEnabledFeatures() {
        return FeatureFlags.DEFAULT_ENABLED_FEATURES;
    }
}
