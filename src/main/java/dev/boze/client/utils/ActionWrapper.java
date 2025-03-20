package dev.boze.client.utils;

import dev.boze.api.interaction.RotateInteraction;

public class ActionWrapper {
    public final boolean field3900;
    public final boolean field3901;
    public final float field3902;
    public final float field3903;
    public final Runnable field3904;

    public ActionWrapper(Runnable action) {
        this.field3900 = false;
        this.field3901 = false;
        this.field3902 = this.field3903 = 0.0F;
        this.field3904 = action;
    }

    public ActionWrapper(Runnable action, float yaw, float pitch) {
        this.field3900 = true;
        this.field3901 = false;
        this.field3902 = yaw;
        this.field3903 = pitch;
        this.field3904 = action;
    }

    public ActionWrapper(float yaw, float pitch) {
        this.field3900 = true;
        this.field3901 = false;
        this.field3902 = yaw;
        this.field3903 = pitch;
        this.field3904 = null;
    }

    public ActionWrapper(RotateInteraction rotateInteraction) {
        this.field3900 = true;
        this.field3901 = false;
        this.field3902 = rotateInteraction.getRotation().yaw;
        this.field3903 = rotateInteraction.getRotation().pitch;
        this.field3904 = rotateInteraction::execute;
    }

    public ActionWrapper(float yaw, float pitch, boolean hardRotate) {
        this.field3900 = true;
        this.field3901 = hardRotate;
        this.field3902 = yaw;
        this.field3903 = pitch;
        this.field3904 = null;
    }

    public ActionWrapper(Runnable action, float yaw, float pitch, boolean rotate) {
        this.field3900 = rotate;
        this.field3901 = false;
        this.field3902 = yaw;
        this.field3903 = pitch;
        this.field3904 = action;
    }

    public ActionWrapper(PlaceAction placement) {
        this(placement.method2167(), placement.method2157(), placement.method2159(), placement.method2168());
    }
}
