package dev.boze.client.events;

import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class BoatEntityMoveEvent {
    private static final BoatEntityMoveEvent field1901 = new BoatEntityMoveEvent();
    public BoatEntity field1902;
    public Vec3d vec3;

    public static BoatEntityMoveEvent method1051(BoatEntity entity, Vec3d movement) {
        field1901.field1902 = entity;
        field1901.vec3 = movement;
        return field1901;
    }
}
