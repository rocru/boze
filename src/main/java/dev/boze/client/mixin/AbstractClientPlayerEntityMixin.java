package dev.boze.client.mixin;

import dev.boze.client.systems.modules.client.Media;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
    @Shadow
    @Nullable
    protected abstract PlayerListEntry getPlayerListEntry();

    @Inject(
            method = "getSkinTextures",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onGetSkinTextures(CallbackInfoReturnable<SkinTextures> info) {
        if (this.getPlayerListEntry() != null
                && Media.INSTANCE.isEnabled()
                && Media.INSTANCE.field2402.getValue()
                && Media.INSTANCE.field2403.getValue().containsKey(this.getPlayerListEntry().getProfile().getName().toLowerCase(Locale.ROOT))) {
            info.setReturnValue(DefaultSkinHelper.getSkinTextures(this.getPlayerListEntry().getProfile().getId()));
        }
    }
}
