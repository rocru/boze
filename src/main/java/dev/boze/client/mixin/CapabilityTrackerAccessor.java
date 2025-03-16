package dev.boze.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(
   targets = {"com.mojang.blaze3d.platform.GlStateManager$CapabilityTracker"}
)
public interface CapabilityTrackerAccessor {
   @Accessor("state")
   boolean getState();

   @Invoker("setState")
   void iSetState(boolean var1);
}
