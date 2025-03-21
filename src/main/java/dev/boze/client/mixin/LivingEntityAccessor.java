package dev.boze.client.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({LivingEntity.class})
public interface LivingEntityAccessor {
    @Invoker("swimUpward")
    void swimUpwards(TagKey<Fluid> var1);

    @Accessor("jumpingCooldown")
    int getJumpCooldown();

    @Accessor("jumpingCooldown")
    void setJumpCooldown(int var1);

    @Accessor
    double getServerX();

    @Accessor
    double getServerY();

    @Accessor
    double getServerZ();

    @Accessor
    double getServerYaw();

    @Accessor
    double getServerPitch();

    @Accessor
    double getServerHeadYaw();
}
