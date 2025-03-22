package dev.boze.client.mixin;

import dev.boze.client.systems.modules.movement.IceSpeed;
import dev.boze.client.systems.modules.render.XRay;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock implements ItemConvertible {
    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(
            method = "shouldDrawSide",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void onShouldDrawSide(BlockState var0, BlockView var1, BlockPos var2, Direction var3, BlockPos var4, CallbackInfoReturnable<Boolean> var5) {
        if (XRay.INSTANCE.isEnabled()) {
            var5.setReturnValue(XRay.INSTANCE.method2086(var0, var1, var2, var3, var5.getReturnValueZ()));
        }
    }

    @Inject(
            method = "getSlipperiness",
            at = @At("RETURN"),
            cancellable = true
    )
    public void onGetSlipperiness(CallbackInfoReturnable<Float> cir) {
        Block var4 = (Block) (Object) this;
        if (IceSpeed.INSTANCE.isEnabled() && (var4 == Blocks.ICE || var4 == Blocks.BLUE_ICE || var4 == Blocks.FROSTED_ICE || var4 == Blocks.PACKED_ICE)) {
            cir.setReturnValue(this.slipperiness * IceSpeed.INSTANCE.field3283.getValue());
        }
    }
}
