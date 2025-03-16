package dev.boze.client.mixin;

import net.minecraft.entity.LimbAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({LimbAnimator.class})
public interface LimbAnimatorAccessor {
   @Accessor
   void setPrevSpeed(float var1);

   @Accessor
   void setSpeed(float var1);

   @Accessor
   void setPos(float var1);
}
