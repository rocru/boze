package dev.boze.client.mixin;

import dev.boze.client.Boze;
import net.minecraft.nbt.NbtSizeTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NbtSizeTracker.class)
public class NbtTagSizeTrackerMixin {
    @Shadow
    private int depth;
    @Shadow
    @Final
    private int maxDepth;

    @Inject(
            method = "pushStack",
            at = @At("HEAD")
    )
    private void pushStack(CallbackInfo var1) {
        if (this.depth >= this.maxDepth) {
            Boze.LOG.error("NBT Tag Size Tracker Overflow", new Exception("NBT Tag Size Tracker Overflow"));
            new Exception("NBT Tag Size Tracker Overflow").printStackTrace();
        }
    }
}
