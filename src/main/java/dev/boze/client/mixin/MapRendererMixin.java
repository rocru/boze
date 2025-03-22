package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapRenderer.class)
public class MapRendererMixin {
    @Inject(
            method = "draw",
            at = @At("HEAD"),
            cancellable = true
    )
    public void draw(
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, MapIdComponent id, MapState state, boolean hidePlayerIcons, int light, CallbackInfo ci
    ) {
        if (NoRender.method1996()) {
            ci.cancel();
        }
    }
}
