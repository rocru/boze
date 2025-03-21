package dev.boze.client.events;

import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class PlayerMoveEvent extends CancelableEvent {
    private static final PlayerMoveEvent INSTANCE = new PlayerMoveEvent();
    public MovementType movementType;
    public Vec3d vec3;
    public boolean field1892;

    public static PlayerMoveEvent method1036(MovementType type, Vec3d movement) {
        INSTANCE.movementType = type;
        INSTANCE.vec3 = movement;
        INSTANCE.field1892 = false;
        INSTANCE.method1021(false);
        return INSTANCE;
    }
}
