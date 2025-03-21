package dev.boze.client.events;

import net.minecraft.client.network.ClientPlayerEntity;

public class PostPlayerTickEvent extends PlayerTickEvent {
    private static final PostPlayerTickEvent INSTANCE = new PostPlayerTickEvent();

    public static PostPlayerTickEvent method1085(ClientPlayerEntity player) {
        INSTANCE.field1941 = player;
        return INSTANCE;
    }
}
