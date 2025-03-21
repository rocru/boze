package mapped;

import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.systems.modules.misc.Timer;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.player.RotationHandler;
import net.minecraft.entity.EntityPose;

public class Class2866 implements IMinecraft {
    public Class2866() {
        super();
    }

    public static void method1872(final TickInputPostEvent event) {
        if (event.field1954 == 0.0f && event.field1953 == 0.0f) {
            return;
        }
        if (!Timer.INSTANCE.field1034.field927) {
            return;
        }
        final float yaw = Class2866.mc.player.getYaw();
        final float method1384 = RotationHandler.field1546.method1384();
        final double n = event.field1953 * Math.cos(Math.toRadians(yaw)) - event.field1954 * Math.sin(Math.toRadians(yaw));
        final double n2 = event.field1954 * Math.cos(Math.toRadians(yaw)) + event.field1953 * Math.sin(Math.toRadians(yaw));
        double[] array = null;
        for (double n3 = (Class2866.mc.player.getPose() == EntityPose.STANDING) ? 1.0 : Math.max(Math.abs(event.field1954), Math.abs(event.field1953)), n4 = -n3; n4 <= n3; n4 += n3) {
            for (double n5 = -n3; n5 <= n3; n5 += n3) {
                final double n6 = n5 * Math.cos(Math.toRadians(method1384)) - n4 * Math.sin(Math.toRadians(method1384));
                final double n7 = n4 * Math.cos(Math.toRadians(method1384)) + n5 * Math.sin(Math.toRadians(method1384));
                final double n8 = n6 - n;
                final double n9 = n7 - n2;
                final double sqrt = Math.sqrt(n8 * n8 + n9 * n9);
                if (array == null || array[0] > sqrt) {
                    array = new double[]{sqrt, n4, n5};
                }
            }
        }
        if (array != null) {
            event.field1954 = (float) array[1];
            event.field1953 = (float) array[2];
        }
    }
}
