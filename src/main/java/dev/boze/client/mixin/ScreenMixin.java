package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(
            method = "renderBackground",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderBackground(CallbackInfo var1) {
        if (NoRender.method1979()) {
            var1.cancel();
        }
    }
}
