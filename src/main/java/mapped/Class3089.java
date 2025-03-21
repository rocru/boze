package mapped;

import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class Class3089 extends HitResult {
    public static final Class3089 field215 = new Class3089();

    private Class3089() {
        super(new Vec3d(0.0, 0.0, 0.0));
    }

    public Type getType() {
        return Type.MISS;
    }
}
