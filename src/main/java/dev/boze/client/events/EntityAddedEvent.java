package dev.boze.client.events;

import net.minecraft.entity.Entity;

public class EntityAddedEvent {
    private static final EntityAddedEvent field1912 = new EntityAddedEvent();
    public Entity field1913;

    public static EntityAddedEvent method1058(Entity entity) {
        field1912.field1913 = entity;
        return field1912;
    }
}
