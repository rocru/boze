package dev.boze.client.mixin;

import dev.boze.client.Boze;
import dev.boze.client.events.SoundPlayEvent;
import dev.boze.client.mixininterfaces.ISoundSystem;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SoundSystem.class})
public class SoundSystemMixin implements ISoundSystem {
    @Unique
    private boolean paused;

    @Inject(
            method = {"play(Lnet/minecraft/client/sound/SoundInstance;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void play(SoundInstance soundInstance, CallbackInfo ci) {
        if (this.paused) {
            ci.cancel();
        } else {
            SoundPlayEvent var3 = Boze.EVENT_BUS.post(SoundPlayEvent.method1095(soundInstance));
            if (var3.method1022()) {
                ci.cancel();
            }
        }
    }

    @Inject(
            method = {"play(Lnet/minecraft/client/sound/SoundInstance;I)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void play(SoundInstance soundInstance, int delay, CallbackInfo ci) {
        if (this.paused) {
            ci.cancel();
        } else {
            SoundPlayEvent var4 = Boze.EVENT_BUS.post(SoundPlayEvent.method1095(soundInstance));
            if (var4.method1022()) {
                ci.cancel();
            }
        }
    }

    @Inject(
            method = {"playNextTick"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void playNextTick(TickableSoundInstance sound, CallbackInfo ci) {
        if (this.paused) {
            ci.cancel();
        } else {
            SoundPlayEvent var3 = Boze.EVENT_BUS.post(SoundPlayEvent.method1095(sound));
            if (var3.method1022()) {
                ci.cancel();
            }
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
}
