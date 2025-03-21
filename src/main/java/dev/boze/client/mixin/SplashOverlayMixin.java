package dev.boze.client.mixin;

import dev.boze.client.core.Version;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.IntSupplier;

@Mixin({SplashOverlay.class})
public class SplashOverlayMixin {
    @Mutable
    @Shadow
    @Final
    private static IntSupplier BRAND_ARGB;

    @Inject(
            method = {"init"},
            at = {@At("HEAD")}
    )
    private static void onInit(MinecraftClient var0, CallbackInfo var1) {
        BRAND_ARGB = SplashOverlayMixin::lambda$onInit$0;
    }

    @Unique
    private static int lambda$onInit$0() {
        return Version.isBeta ? 0 : 6179555;
    }
}
