package dev.boze.client.events;

import net.minecraft.fluid.FluidState;

public class CanWalkOnFluidEvent extends CancelableEvent {
    private static final CanWalkOnFluidEvent INSTANCE = new CanWalkOnFluidEvent();
    public FluidState fluidState;

    public static CanWalkOnFluidEvent method1049(FluidState fluid) {
        INSTANCE.fluidState = fluid;
        INSTANCE.method1021(false);
        return INSTANCE;
    }
}
