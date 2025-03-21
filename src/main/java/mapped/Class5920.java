package mapped;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.joml.Vector3d;

public class Class5920 implements IMinecraft {
    public Class5920() {
        super();
    }

    public static Vector3d method52(final LivingEntity entity, final int extrapolation, final Vector3d vec) {
        if (entity.getVelocity().length() == 0.0) {
            return vec.set(0.0, 0.0, 0.0);
        }
        final double n = entity.getX() - entity.prevX;
        double n2 = entity.getY() - entity.prevY;
        final double n3 = entity.getZ() - entity.prevZ;
        double n4 = 0.0;
        double n5 = 0.0;
        double n6 = 0.0;
        for (int i = 0; i < extrapolation; ++i) {
            double n7 = n4 + n;
            double n8 = n5 + n2;
            double n9 = n6 + n3;
            if (!Class5920.mc.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(n7, n8, n9))) {
                if (Class5920.mc.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(0.0, n8, 0.0))) {
                    n7 = n4;
                    n9 = n6;
                } else if (Class5920.mc.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(n7, 0.0, n8))) {
                    n8 = n5;
                } else {
                    n7 = n4;
                    n8 = n5;
                    n9 = n6;
                }
            }
            n4 = n7;
            n5 = n8;
            n6 = n9;
            n2 -= 0.0784;
        }
        return vec.set(entity.getX() + n4, entity.getY() + n5, entity.getZ() + n6);
    }
}
