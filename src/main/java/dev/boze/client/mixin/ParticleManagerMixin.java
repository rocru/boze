package dev.boze.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.Boze;
import dev.boze.client.events.ParticleEffectEvent;
import dev.boze.client.mixininterfaces.IParticleManager;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.render.Chams;
import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin implements IParticleManager {
    @Shadow
    @Final
    private static List<ParticleTextureSheet> PARTICLE_TEXTURE_SHEETS;
    @Shadow
    @Final
    private Map<ParticleTextureSheet, Queue<Particle>> particles;
    @Shadow
    @Final
    private TextureManager textureManager;
    @Unique
    private boolean paused;

    @Redirect(
            method = "renderParticles",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/Particle;buildGeometry(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/Camera;F)V"
            )
    )
    private void onBuildGeometry(Particle var1, VertexConsumer var2, Camera var3, float var4) {
        if (!(var1 instanceof ExplosionLargeParticle) || !Chams.INSTANCE.isEnabled() || !Chams.INSTANCE.ax.getValue() || Chams.INSTANCE.ay.getValue()) {
            var1.buildGeometry(var2, var3, var4);
        }
    }

    @Inject(
            method = "renderParticles",
            at = @At("TAIL")
    )
    public void onRenderParticlesTail(LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ax.getValue()) {
            ChamsShaderRenderer.method1310(
                    () -> $lambda$onRenderParticlesTail$0(lightmapTextureManager, camera, tickDelta),
                    Chams.INSTANCE.method1922(),
                    Chams.INSTANCE.aC.getValue(),
                    Chams.INSTANCE.az,
                    Chams.INSTANCE.aA,
                    Chams.INSTANCE.aG.getValue(),
                    Chams.INSTANCE.aH.getValue(),
                    Chams.INSTANCE.aE.getValue(),
                    Chams.INSTANCE.aF.getValue(),
                    Chams.INSTANCE.aD.getValue(),
                    Chams.INSTANCE.aN
            );
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            cancellable = true
    )
    public void onAddParticleWithParams(
            ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir
    ) {
        if (NoRender.method1998(parameters)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "addParticle(Lnet/minecraft/client/particle/Particle;)V",
            cancellable = true
    )
    public void onAddParticle(Particle particle, CallbackInfo ci) {
        if (this.paused) {
            ci.cancel();
        } else {
            if (NoRender.method2000(particle)) {
                ci.cancel();
            }
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;)V",
            cancellable = true
    )
    public void onAddEmmiter(Entity entity, ParticleEffect particleEffect, CallbackInfo ci) {
        ParticleEffectEvent var4 = Boze.EVENT_BUS.post(ParticleEffectEvent.method1077(particleEffect));
        if (var4.method1022()) {
            ci.cancel();
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V",
            cancellable = true
    )
    public void onAddEmmiterAged(Entity entity, ParticleEffect particleEffect, int maxAge, CallbackInfo ci) {
        ParticleEffectEvent var5 = Boze.EVENT_BUS.post(ParticleEffectEvent.method1077(particleEffect));
        if (var5.method1022()) {
            ci.cancel();
        }
    }

    @Override
    public boolean boze$isPaused() {
        return this.paused;
    }

    @Override
    public void boze$setPaused(boolean paused) {
        this.paused = paused;
    }

    @Unique
    private void $lambda$onRenderParticlesTail$0(LightmapTextureManager var1, Camera var2, float var3) {
        var1.enable();
        RenderSystem.enableDepthTest();
        ParticleTextureSheet var4 = ParticleTextureSheet.PARTICLE_SHEET_LIT;
        Iterable<Particle> var5 = this.particles.get(var4);
        if (var5 != null) {
            RenderSystem.setShader(GameRenderer::getParticleProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            Tessellator var6 = Tessellator.getInstance();
            BufferBuilder var7 = var4.begin(var6, this.textureManager);

            for (Particle var9 : var5) {
                if (var9 instanceof ExplosionLargeParticle) {
                    try {
                        var9.buildGeometry(var7, var2, var3);
                    } catch (Throwable var13) {
                        CrashReport var11 = CrashReport.create(var13, "Rendering Particle");
                        CrashReportSection var12 = var11.addElement("Particle being rendered");
                        var12.add("Particle", var9::toString);
                        var12.add("Particle Type", var4::toString);
                        throw new CrashException(var11);
                    }
                }
            }

            BuiltBuffer var14 = var7.endNullable();
            if (var14 != null) {
                BufferRenderer.drawWithGlobalProgram(var14);
            }
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        var1.disable();
    }
}
