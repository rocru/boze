package dev.boze.client.events;

import net.minecraft.entity.Entity;

public class EntityRemovedEvent {
    private static final EntityRemovedEvent field1914 = new EntityRemovedEvent();
    public Entity field1915;

    public static EntityRemovedEvent method1059(Entity entity) {
        field1914.field1915 = entity;
        return field1914;
    }
}
