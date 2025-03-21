package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.Tint;
import net.minecraft.client.particle.TotemParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({TotemParticle.class})
public class TotemParticleMixin {
    @Redirect(
            method = {"<init>"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/TotemParticle;setColor(FFF)V",
                    ordinal = 0
            )
    )
    private void setColorYellow(TotemParticle var1, float var2, float var3, float var4) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3730.getValue()) {
            var2 = Tint.INSTANCE.field3731.getValue() * (1.0F - Tint.INSTANCE.field3732.getValue())
                    + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3732.getValue() * Tint.INSTANCE.field3731.getValue();
            var3 = Tint.INSTANCE.field3733.getValue() * (1.0F - Tint.INSTANCE.field3734.getValue())
                    + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3734.getValue() * Tint.INSTANCE.field3733.getValue();
            var4 = Tint.INSTANCE.field3735.getValue() * (1.0F - Tint.INSTANCE.field3736.getValue())
                    + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3736.getValue() * Tint.INSTANCE.field3735.getValue();
        }

        var1.setColor(var2, var3, var4);
    }

    @Redirect(
            method = {"<init>"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/TotemParticle;setColor(FFF)V",
                    ordinal = 1
            )
    )
    private void setColorGreen(TotemParticle var1, float var2, float var3, float var4) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3730.getValue()) {
            var2 = Tint.INSTANCE.field3737.getValue() * (1.0F - Tint.INSTANCE.field3738.getValue())
                    + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3738.getValue() * Tint.INSTANCE.field3737.getValue();
            var3 = Tint.INSTANCE.field3739.getValue() * (1.0F - Tint.INSTANCE.field3740.getValue())
                    + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3740.getValue() * Tint.INSTANCE.field3739.getValue();
            var4 = Tint.INSTANCE.field3741.getValue() * (1.0F - Tint.INSTANCE.field3742.getValue())
                    + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3742.getValue() * Tint.INSTANCE.field3741.getValue();
        }

        var1.setColor(var2, var3, var4);
    }
}
