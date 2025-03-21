package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.XRay;
import net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(
        value = {LightDataAccess.class},
        remap = false
)
public class LightDataAccessMixin {
    @Unique
    private static final int FULL_BRIGHT = 15728880;
    @Shadow
    protected BlockRenderView level;
    @Shadow
    @Final
    private Mutable pos;

    @ModifyVariable(
            method = {"compute"},
            at = @At("TAIL"),
            name = {"bl"}
    )
    private int compute_modifyAO(int var1) {
        if (XRay.INSTANCE.isEnabled()) {
            BlockState var4 = this.level.getBlockState(this.pos);
            if (XRay.INSTANCE.method2088(var4.getBlock())) {
                return 15728880;
            }
        }

        return var1;
    }
}
