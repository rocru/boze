package dev.boze.client.mixin;

import dev.boze.client.systems.modules.misc.SoundFX;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public abstract class BowItemMixin {
    @Shadow
    public abstract void onStoppedUsing(ItemStack var1, World var2, LivingEntity var3, int var4);

    @Inject(
            method = "onStoppedUsing",
            at = @At("HEAD")
    )
    private void onStoppedUsingBow(ItemStack var1, World var2, LivingEntity var3, int var4, CallbackInfo var5) {
        if (SoundFX.INSTANCE.isEnabled()) {
            SoundFX.INSTANCE.method1773(var1, var4);
        }
    }
}
