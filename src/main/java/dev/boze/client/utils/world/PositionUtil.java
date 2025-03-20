package dev.boze.client.utils.world;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class PositionUtil implements IMinecraft {
    public static Vec3d getPlayerPosition() {
        return getPosition(mc.player);
    }

    public static Vec3d getPosition(Entity entity) {
        return new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
    }
}
