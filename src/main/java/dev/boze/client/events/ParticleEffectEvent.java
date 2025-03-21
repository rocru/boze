package dev.boze.client.events;

import net.minecraft.particle.ParticleEffect;

public class ParticleEffectEvent extends dX {
    private static final ParticleEffectEvent field1935 = new ParticleEffectEvent();
    public ParticleEffect field1936;

    public static ParticleEffectEvent method1077(ParticleEffect emmiter) {
        field1935.field1936 = emmiter;
        field1935.method1021(false);
        return field1935;
    }
}
