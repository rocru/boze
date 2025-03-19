package dev.boze.client.mixin;

import dev.boze.client.systems.modules.misc.NoGhostBlocks;
import mapped.Class2811;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({BlockItem.class})
public class BlockItemMixin {
   @Redirect(
      method = {"canPlace"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/World;canPlace(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Z"
      )
   )
   private boolean redirectCanPlace(World var1, BlockState var2, BlockPos var3, ShapeContext var4) {
      VoxelShape var5 = var2.getCollisionShape(var1, var3, var4);
      Entity var6 = null;
      if (Class2811.field100) {
         List var7 = var1.getOtherEntities(null, new Box(var3), BlockItemMixin::lambda$redirectCanPlace$0);
         if (!var7.isEmpty()) {
            var6 = (Entity)var7.get(0);
         }
      }

      return var5.isEmpty() || var1.doesNotIntersectEntities(var6, var5.offset((double)var3.getX(), (double)var3.getY(), (double)var3.getZ()));
   }

   @Inject(
      method = {"place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/item/ItemPlacementContext;getBlockPos()Lnet/minecraft/util/math/BlockPos;"
      )},
      cancellable = true
   )
   private void onPlace(ItemPlacementContext var1, CallbackInfoReturnable<ActionResult> var2) {
      if (NoGhostBlocks.INSTANCE.isEnabled()) {
         var2.setReturnValue(ActionResult.success(MinecraftClient.getInstance().world.isClient));
      }
   }

   @Unique
   private static boolean lambda$redirectCanPlace$0(Entity var0) {
      return var0 instanceof EndCrystalEntity;
   }
}
