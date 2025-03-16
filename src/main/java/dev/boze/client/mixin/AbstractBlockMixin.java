package dev.boze.client.mixin;

import dev.boze.client.events.AmbientOcclusionEvent;
import dev.boze.client.events.CollisionEvent;
import dev.boze.client.events.CollisionType;
import mapped.Class27;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractBlock.class})
public abstract class AbstractBlockMixin {
   @Inject(
      method = {"getAmbientOcclusionLightLevel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetAmbientOcclusionLightLevel(BlockState var1, BlockView var2, BlockPos var3, CallbackInfoReturnable<Float> var4) {
      AmbientOcclusionEvent var5 = (AmbientOcclusionEvent)Class27.EVENT_BUS.post(AmbientOcclusionEvent.method1050());
      if (var5.field1900 != -1.0F) {
         var4.setReturnValue(var5.field1900);
      }
   }

   @Inject(
      method = {"getCollisionShape"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetCollisionShape(BlockState var1, BlockView var2, BlockPos var3, ShapeContext var4, CallbackInfoReturnable<VoxelShape> var5) {
      if (!var1.getFluidState().isEmpty()) {
         CollisionEvent var6 = (CollisionEvent)Class27.EVENT_BUS
            .post(CollisionEvent.method1056(var1.getFluidState().getBlockState(), var3, CollisionType.FLUID));
         if (var6.voxelShape != null) {
            var5.setReturnValue(var6.voxelShape);
         }
      } else {
         CollisionEvent var7 = (CollisionEvent)Class27.EVENT_BUS.post(CollisionEvent.method1056(var1, var3, CollisionType.BLOCK));
         if (var7.voxelShape != null) {
            var5.setReturnValue(var7.voxelShape);
         }
      }
   }
}
