package mapped;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.StaticColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Class3071 {
    public Class3071() {
        super();
    }

    public static BozeDrawColor method6015(final BozeDrawColor one, final BozeDrawColor two, final double partial) {
        final float[] method191 = RGBAColor.method191(one.field408, one.field409, one.field410);
        final float[] method192 = RGBAColor.method191(two.field408, two.field409, two.field410);
        if (method192[0] - method191[0] > 0.5f) {
            final float[] array = method191;
            final int n = 0;
            ++array[n];
        } else if (method191[0] - method192[0] > 0.5f) {
            final float[] array2 = method192;
            final int n2 = 0;
            ++array2[n2];
        }
        final int[] method193 = RGBAColor.method190(method6022(method191[0], method192[0], partial) * 360.0, method6022(method191[1], method192[1], partial), method6022(method191[2], method192[2], partial));
        return new BozeDrawColor(method193[0], method193[1], method193[2], (int) method6022(one.field411, two.field411, partial), (partial < 0.5) ? one.field1842 : two.field1842, method6022(one.field1843, two.field1843, partial), method6022(one.field1844, two.field1844, partial), method6023(one.field1845, two.field1845, partial), method6023(one.field1846, two.field1846, partial));
    }

    public static RGBAColor method6016(final RGBAColor one, final RGBAColor two, final double partial) {
        final float[] method191 = RGBAColor.method191(one.field408, one.field409, one.field410);
        final float[] method192 = RGBAColor.method191(two.field408, two.field409, two.field410);
        if (method192[0] - method191[0] > 0.5f) {
            final float[] array = method191;
            final int n = 0;
            ++array[n];
        } else if (method191[0] - method192[0] > 0.5f) {
            final float[] array2 = method192;
            final int n2 = 0;
            ++array2[n2];
        }
        final int[] method193 = RGBAColor.method190(method6022(method191[0], method192[0], partial) * 360.0, method6022(method191[1], method192[1], partial), method6022(method191[2], method192[2], partial));
        return new RGBAColor(method193[0], method193[1], method193[2], (int) method6022(one.field411, two.field411, partial));
    }

    public static StaticColor method6017(final StaticColor one, final StaticColor two, final double partial) {
        return new StaticColor((int) method6022(one.field430, two.field430, partial), (int) method6022(one.field431, two.field431, partial), (int) method6022(one.field432, two.field432, partial));
    }

    public static StaticColor method6018(final StaticColor one, final StaticColor two, final double partial) {
        final float[] method191 = RGBAColor.method191(one.field430, one.field431, one.field432);
        final float[] method192 = RGBAColor.method191(two.field430, two.field431, two.field432);
        if (method192[0] - method191[0] > 0.5f) {
            final float[] array = method191;
            final int n = 0;
            ++array[n];
        } else if (method191[0] - method192[0] > 0.5f) {
            final float[] array2 = method192;
            final int n2 = 0;
            ++array2[n2];
        }
        final int[] method193 = RGBAColor.method190(method6022(method191[0], method192[0], partial) * 360.0, method6022(method191[1], method192[1], partial), method6022(method191[2], method192[2], partial));
        return new StaticColor(method193[0], method193[1], method193[2]);
    }

    public static Vec3d method6019(final Entity entity) {
        final double[] method6020 = method6020(entity);
        return new Vec3d(method6020[0], method6020[1], method6020[2]);
    }

    public static double[] method6020(final Entity entity) {
        if (entity.prevX == 0.0 && entity.prevY == 0.0 && entity.prevZ == 0.0) {
            entity.prevX = entity.getX();
            entity.prevY = entity.getY();
            entity.prevZ = entity.getZ();
        }
        return new double[]{method6022(entity.lastRenderX, entity.getX(), MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)), method6022(entity.lastRenderY, entity.getY(), MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)), method6022(entity.lastRenderZ, entity.getZ(), MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true))};
    }

    public static Box method6021(final Box a, final Box b, final double partial) {
        return new Box(method6022(a.minX, b.minX, partial), method6022(a.minY, b.minY, partial), method6022(a.minZ, b.minZ, partial), method6022(a.maxX, b.maxX, partial), method6022(a.maxY, b.maxY, partial), method6022(a.maxZ, b.maxZ, partial));
    }

    public static double method6022(final double a, final double b, final double partial) {
        return a * (1.0 - partial) + b * partial;
    }

    public static double[] method6023(final double[] a, final double[] b, final double partial) {
        return new double[]{a[0] * (1.0 - partial) + b[0] * partial, a[1] * (1.0 - partial) + b[1] * partial};
    }
}
