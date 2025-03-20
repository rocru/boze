package dev.boze.client.utils.player;

import dev.boze.api.BozeInstance;
import dev.boze.api.event.EventGrim.Interact;
import dev.boze.api.event.EventGrim.Rotate;
import dev.boze.client.Boze;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationHandler implements IMinecraft {
    public static final RotationHandler field1546 = new RotationHandler();
    private boolean field1547;
    private float field1548;
    private float field1549;

    public void method2142() {
        if (mc.player != null && mc.interactionManager != null) {
            ACRotationEvent var4 = ACRotationEvent.method1016(AnticheatMode.Grim, method215(), method520());
            Boze.EVENT_BUS.post(var4);
            this.field1547 = var4.method1022();
            this.field1548 = var4.yaw;
            this.field1549 = var4.pitch;
            Rotate var5 = Rotate.get(var4.yaw, var4.pitch, this.field1547);
            BozeInstance.INSTANCE.post(var5);
            if (var5.isSet()) {
                this.field1547 = true;
                this.field1548 = var5.getYaw();
                this.field1549 = var5.getPitch();
            }
        }
    }

    public void method1416() {
        if (mc.player != null && mc.interactionManager != null) {
            RotationEvent var4 = RotationEvent.method553(RotationMode.Vanilla, method215(), this.method1385(), method1954(), field1546.method2114());
            Boze.EVENT_BUS.post(var4);
            Interact var5 = Interact.get(var4.method2114());
            BozeInstance.INSTANCE.post(var5);
        }
    }

    public boolean method2114() {
        return this.field1547;
    }

    public float method1384() {
        return this.method2114() ? this.field1548 : mc.player.getYaw();
    }

    public float method1385() {
        return this.method2114() ? this.field1549 : mc.player.getPitch();
    }

    public static Vec3d method1954() {
        float var3 = method520() * (float) (Math.PI / 180.0);
        float var4 = -method215() * (float) (Math.PI / 180.0);
        float var5 = MathHelper.cos(var4);
        float var6 = MathHelper.sin(var4);
        float var7 = MathHelper.cos(var3);
        float var8 = MathHelper.sin(var3);
        return new Vec3d(var6 * var7, -var8, var5 * var7);
    }

    public static float method215() {
        return ((ClientPlayerEntityAccessor) mc.player).getLastYaw();
    }

    public static float method520() {
        return ((ClientPlayerEntityAccessor) mc.player).getLastPitch();
    }
}
