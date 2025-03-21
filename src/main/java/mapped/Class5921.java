package mapped;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Class5921 implements IMinecraft {
    private static final Vector3d field18;
    private static final Class3064<Vector3d> field19;
    private static final HashMap<Integer, Vector3d> field20;
    public static double field21;

    public Class5921() {
        super();
    }

    public static void method2142() {
        final Iterator<Vector3d> iterator = Class5921.field20.values().iterator();
        while (iterator.hasNext()) {
            Class5921.field19.method5994(iterator.next());
        }
        Class5921.field20.clear();
    }

    public static HashMap<Integer, Vector3d> method1282() {
        return Class5921.field20;
    }

    public static Vector3d method55(final LivingEntity entity) {
        return Class5921.field20.getOrDefault(entity.getId(), Class5921.field18);
    }

    public static Vector3d method56(final LivingEntity entity, final int extrapolation, final boolean simulate) {
        if (entity == null) {
            return Class5921.field18;
        }
        if (Class5921.field20.containsKey(entity.getId())) {
            return Class5921.field20.get(entity.getId());
        }
        final Vector3d method57 = method57(entity, extrapolation, simulate, Class5921.field19.method5993());
        Class5921.field20.put(entity.getId(), method57);
        return method57;
    }

    public static Vector3d method57(final LivingEntity entity, final int extrapolation, final boolean simulate, final Vector3d vec) {
        if (entity instanceof final PlayerEntity baseEntity) {
            if (simulate) {
                final Pair<ClientPlayerEntity, ArrayList<Vec3d>> method38 = Class5918.method38(extrapolation, baseEntity);
                if (method38 != null) {
                    final Vec3d pos = method38.getLeft().getPos();
                    return vec.set(pos.x - entity.getX(), pos.y - entity.getY(), pos.z - entity.getZ());
                }
            }
        }
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
            if (!Class5921.mc.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(n7, n8, n9))) {
                if (Class5921.mc.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(0.0, n8, 0.0))) {
                    n7 = n4;
                    n9 = n6;
                } else if (Class5921.mc.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(n7, 0.0, n9))) {
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
            n2 -= Class5921.field21;
        }
        return vec.set(n4, n5, n6);
    }

    static {
        field18 = new Vector3d(0.0, 0.0, 0.0);
        field19 = new Class3064<Vector3d>(Vector3d::new);
        field20 = new HashMap<Integer, Vector3d>();
        Class5921.field21 = 0.0784;
    }
}
