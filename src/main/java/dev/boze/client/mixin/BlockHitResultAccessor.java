package dev.boze.client.mixin;

import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BlockHitResult.class})
public interface BlockHitResultAccessor {
   @Accessor
   boolean isMissed();
}
