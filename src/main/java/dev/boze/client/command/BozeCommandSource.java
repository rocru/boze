package dev.boze.client.command;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;

public final class BozeCommandSource extends ClientCommandSource {
    public BozeCommandSource(MinecraftClient client) {
        super(null, client);
    }
}
