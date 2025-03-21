package mapped;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class Class3062 {
    public Class3062() {
        super();
    }

    public static Vector3d method5989(final Vector3d vec, final Vec3d v) {
        vec.x = v.x;
        vec.y = v.y;
        vec.z = v.z;
        return vec;
    }

    public static Vector3d method5990(final Vector3d vec, final Entity entity, final double tickDelta) {
        vec.x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        vec.y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        vec.z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
        return vec;
    }

    public static Vec3d method5991(final Vec3d vectorCutTo, final Box box) {
        return new Vec3d(MathHelper.clamp(vectorCutTo.getX(), box.minX, box.maxX), MathHelper.clamp(vectorCutTo.getY(), box.minY, box.maxY), MathHelper.clamp(vectorCutTo.getZ(), box.minZ, box.maxZ));
    }
}
