package mapped;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Class5917 implements IMinecraft {
    public Class5917() {
        super();
    }

    public static Vec2f method31(final Vec2f line1Begin, final Vec2f line1End, final Vec2f line2Begin, final Vec2f line2End) {
        final float n = (line1Begin.x - line1End.x) * (line2Begin.y - line2End.y) - (line1Begin.y - line1End.y) * (line2Begin.x - line2End.x);
        return new Vec2f(((line1Begin.x * line1End.y - line1Begin.y * line1End.x) * (line2Begin.x - line2End.x) - (line1Begin.x - line1End.x) * (line2Begin.x * line2End.y - line2Begin.y * line2End.x)) / n, ((line1Begin.x * line1End.y - line1Begin.y * line1End.x) * (line2Begin.y - line2End.y) - (line1Begin.y - line1End.y) * (line2Begin.x * line2End.y - line2Begin.y * line2End.x)) / n);
    }

    public static double method32(final double time, final double bias) {
        return time / ((1.0 / bias - 2.0) * (1.0 - time) + 1.0);
    }

    public static Vec3d method33(final Vec3d start, final Box box) {
        return new Vec3d(MathHelper.clamp(start.getX(), box.minX, box.maxX), MathHelper.clamp(start.getY(), box.minY, box.maxY), MathHelper.clamp(start.getZ(), box.minZ, box.maxZ));
    }

    public static Vec3d method34(final Box box) {
        final Vec3d eyePos = Class5917.mc.player.getEyePos();
        if (box.minX < eyePos.x && eyePos.x < box.maxX && box.minZ < eyePos.z && eyePos.z < box.maxZ) {
            return new Vec3d(box.minX + (box.maxX - box.minX) / 2.0, Math.max(box.minY, Math.min(eyePos.y, box.maxY)), box.minZ + (box.maxZ - box.minZ) / 2.0);
        }
        return method33(eyePos, box);
    }

    public static double method35(final double value, final double goal, final double increment) {
        if (value < goal) {
            return Math.min(value + increment, goal);
        }
        return Math.max(value - increment, goal);
    }

    public static Vec3d method136(final Box box, final Vec3d look, final Vec3d eyes) {
        double n = Double.MAX_VALUE;
        Vec3d center = box.getCenter();
        final double n2 = (box.minX + box.maxX) / 2.0;
        final double n3 = (box.minY + box.maxY) / 2.0;
        final double n4 = (box.minZ + box.maxZ) / 2.0;
        final Vec3d[] array = {new Vec3d(box.minX, n3, n4), new Vec3d(box.maxX, n3, n4), new Vec3d(n2, box.minY, n4), new Vec3d(n2, box.maxY, n4), new Vec3d(n2, n3, box.minZ), new Vec3d(n2, n3, box.maxZ)};
        for (int length = array.length, i = 0; i < length; ++i) {
            final Vec3d vec3d = array[i];
            final Vec3d vec3d2 = new Vec3d(vec3d.x - eyes.x, vec3d.y - eyes.y, vec3d.z - eyes.z);
            final double acos = Math.acos(look.dotProduct(vec3d2) / (look.length() * vec3d2.length()));
            if (acos < n) {
                n = acos;
                center = vec3d;
            }
        }
        final double[] array2 = {box.minX, box.maxX, box.minX, box.maxX};
        final double[] array3 = {n3, n3, box.minY, box.maxY};
        final double[] array4 = {n4, n4, n4, n4};
        for (int j = 0; j < 4; ++j) {
            final Vec3d vec3d3 = new Vec3d(array2[j], array3[j], array4[j]);
            final Vec3d vec3d4 = new Vec3d(vec3d3.x - eyes.x, vec3d3.y - eyes.y, vec3d3.z - eyes.z);
            final double acos2 = Math.acos(look.dotProduct(vec3d4) / (look.length() * vec3d4.length()));
            if (acos2 < n) {
                n = acos2;
                center = vec3d3;
            }
        }
        return center;
    }

    public static Vec3d method123(final Box box, final Vec3d eyes) {
        double n = Double.MAX_VALUE;
        Vec3d center = box.getCenter();
        final double n2 = (box.minX + box.maxX) / 2.0;
        final double n3 = (box.minY + box.maxY) / 2.0;
        final double n4 = (box.minZ + box.maxZ) / 2.0;
        final Vec3d[] array = {new Vec3d(box.minX, n3, n4), new Vec3d(box.maxX, n3, n4), new Vec3d(n2, box.minY, n4), new Vec3d(n2, box.maxY, n4), new Vec3d(n2, n3, box.minZ), new Vec3d(n2, n3, box.maxZ)};
        for (int length = array.length, i = 0; i < length; ++i) {
            final Vec3d vec3d = array[i];
            final double sqrt = Math.sqrt(Math.pow(vec3d.x - eyes.x, 2.0) + Math.pow(vec3d.y - eyes.y, 2.0) + Math.pow(vec3d.z - eyes.z, 2.0));
            if (sqrt < n) {
                n = sqrt;
                center = vec3d;
            }
        }
        final double[] array2 = {box.minX, box.maxX, box.minX, box.maxX};
        final double[] array3 = {n3, n3, box.minY, box.maxY};
        final double[] array4 = {n4, n4, n4, n4};
        for (int j = 0; j < 4; ++j) {
            final Vec3d vec3d2 = new Vec3d(array2[j], array3[j], array4[j]);
            final double sqrt2 = Math.sqrt(Math.pow(vec3d2.x - eyes.x, 2.0) + Math.pow(vec3d2.y - eyes.y, 2.0) + Math.pow(vec3d2.z - eyes.z, 2.0));
            if (sqrt2 < n) {
                n = sqrt2;
                center = vec3d2;
            }
        }
        return center;
    }
}
