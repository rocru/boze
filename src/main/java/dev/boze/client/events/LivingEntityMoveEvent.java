package dev.boze.client.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class LivingEntityMoveEvent {
    private static final LivingEntityMoveEvent field1928 = new LivingEntityMoveEvent();
    public LivingEntity field1929;
    public Vec3d vec3;

    public static LivingEntityMoveEvent method1071(LivingEntity entity, Vec3d movement) {
        field1928.field1929 = entity;
        field1928.vec3 = movement;
        return field1928;
    }
}
