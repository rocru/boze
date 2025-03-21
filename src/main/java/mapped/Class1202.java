package mapped;

import dev.boze.client.utils.*;
import net.minecraft.util.math.*;

public class Class1202
{
    public Class1202() {
        super();
    }

    public static RotationHelper method2391(final Vec3d from, final Vec3d to) {
        return method2392(to.subtract(from));
    }

    public static RotationHelper method2392(final Vec3d delta) {
        return new RotationHelper(method2394(delta.getX(), delta.getZ()), method2397(delta.getY(), delta.horizontalLength()));
    }

    public static float method2393(final double fromX, final double fromZ, final double toX, final double toZ) {
        return method2394(toX - fromX, toZ - fromZ);
    }

    public static float method2394(final double deltaX, final double deltaZ) {
        return (float)Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0f;
    }

    public static float method2395(final Vec3d delta) {
        return method2394(delta.getX(), delta.getZ());
    }

    public static float method2396(final Vec2f delta) {
        return (float)Math.toDegrees(Math.atan2(delta.y, delta.x)) - 90.0f;
    }

    private static float method2397(final double y, final double x) {
        return (float)(-Math.toDegrees(Math.atan2(y, x)));
    }
}
