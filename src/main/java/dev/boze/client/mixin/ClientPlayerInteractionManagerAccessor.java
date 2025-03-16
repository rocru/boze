package dev.boze.client.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ClientPlayerInteractionManager.class})
public interface ClientPlayerInteractionManagerAccessor {
   @Accessor
   float getCurrentBreakingProgress();

   @Accessor
   void setCurrentBreakingProgress(float var1);

   @Accessor
   boolean isBreakingBlock();

   @Accessor
   void setBreakingBlock(boolean var1);

   @Accessor
   int getLastSelectedSlot();

   @Accessor
   void setLastSelectedSlot(int var1);

   @Invoker
   void callSyncSelectedSlot();

   @Accessor
   BlockPos getCurrentBreakingPos();
}
