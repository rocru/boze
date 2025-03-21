package dev.boze.client.events;

import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.utils.EntityUtil;
import net.minecraft.util.math.Vec3d;

public class ACRotationEvent extends CancelableEvent {
    private static final ACRotationEvent INSTANCE = new ACRotationEvent();
    private AnticheatMode antiCheat = null;
    public float yaw;
    public float pitch;

    public static ACRotationEvent method1016(AnticheatMode mode, float serverYaw, float severPitch) {
        INSTANCE.antiCheat = mode;
        INSTANCE.yaw = serverYaw;
        INSTANCE.pitch = severPitch;
        INSTANCE.method1021(false);
        return INSTANCE;
    }

    public AnticheatMode method1017() {
        return this.antiCheat;
    }

    public boolean method1018(AnticheatMode mode, boolean rotate) {
        return mode != this.antiCheat || rotate && this.method1022();
    }

    public void method1019(Vec3d pos) {
        float[] var2 = EntityUtil.method2146(pos);
        this.method1021(true);
        this.yaw = var2[0];
        this.pitch = var2[1];
    }
}
