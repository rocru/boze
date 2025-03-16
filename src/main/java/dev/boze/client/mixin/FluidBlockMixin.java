package dev.boze.client.mixin;

import dev.boze.client.events.CollisionEvent;
import dev.boze.client.events.CollisionType;
import mapped.Class27;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FluidBlock.class})
public abstract class FluidBlockMixin extends Block implements FluidDrainable {
   public FluidBlockMixin(Settings settings) {
      super(settings);
   }

   @Inject(
      method = {"getCollisionShape"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetCollisionShape(BlockState var1, BlockView var2, BlockPos var3, ShapeContext var4, CallbackInfoReturnable<VoxelShape> var5) {
      CollisionEvent var6 = (CollisionEvent)Class27.EVENT_BUS.post(CollisionEvent.method1056(var1, var3, CollisionType.FLUID));
      if (var6.voxelShape != null) {
         var5.setReturnValue(var6.voxelShape);
      }
   }
}
