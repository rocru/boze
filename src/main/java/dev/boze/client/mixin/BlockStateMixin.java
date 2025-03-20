package dev.boze.client.mixin;

import com.mojang.serialization.MapCodec;
import dev.boze.client.utils.trackers.InventoryTracker;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({BlockState.class})
public abstract class BlockStateMixin extends AbstractBlockState {
   public BlockStateMixin(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> mapCodec) {
      super(block, propertyMap, mapCodec);
   }

   public ActionResult onUse(World world, PlayerEntity player, BlockHitResult hit) {
      InventoryTracker.method597((BlockState)this);
      return super.onUse(world, player, hit);
   }
}
