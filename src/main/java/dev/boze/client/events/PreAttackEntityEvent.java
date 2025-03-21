package dev.boze.client.events;

import net.minecraft.entity.Entity;

public class PreAttackEntityEvent extends EntityEvent {
    private static final PreAttackEntityEvent INSTANCE = new PreAttackEntityEvent();

    public static PreAttackEntityEvent method1089(Entity entity) {
        INSTANCE.entity = entity;
        return INSTANCE;
    }
}
