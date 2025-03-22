package dev.boze.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.Boze;
import dev.boze.client.enums.PlayerOverlay;
import dev.boze.client.events.PlayerOverlayEvent;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.render.HUDRenderer;
import dev.boze.client.systems.modules.render.Crosshair;
import dev.boze.client.systems.modules.render.NoRender;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class3032;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private static Identifier PUMPKIN_BLUR;

    @Inject(
            method = "renderHeldItemTooltip",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderHeldItemTooltip(DrawContext var1, CallbackInfo var2) {
        if (NoRender.method1975()) {
            var2.cancel();
        }
    }

    @Inject(
            method = "renderPortalOverlay",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderPortalOverlay(DrawContext var1, float var2, CallbackInfo var3) {
        if (NoRender.method1976()) {
            var3.cancel();
        }
    }

    @Inject(
            method = "renderSpyglassOverlay",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderSpyglassOverlay(DrawContext var1, float var2, CallbackInfo var3) {
        if (NoRender.method1977()) {
            var3.cancel();
        }
    }

    @ModifyArgs(
            method = "renderMiscOverlays",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
                    ordinal = 1
            )
    )
    private void onRenderPowderedSnowOverlay(Args var1) {
        if (NoRender.method1978()) {
            var1.set(2, 0.0F);
        }
    }

    @Inject(
            method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At("HEAD")
    )
    private void onRenderPre(DrawContext var1, RenderTickCounter var2, CallbackInfo var3) {
        if (RenderUtil.field3968 && MinecraftUtils.isClientActive()) {
            HUDRenderer.renderHud(var1);
            NotificationManager.method1152();
        }
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void onRenderPost(DrawContext var1, RenderTickCounter var2, CallbackInfo var3) {
        if (RenderSystem.isOnRenderThread()) {
            if (!RenderUtil.field3968) {
                RenderUtil.method2232();
            }

            Class3032.method332(var1);
            Boze.EVENT_BUS.post(Render2DEvent.method1093(var1, var1.getScaledWindowWidth(), var1.getScaledWindowHeight(), var2.getTickDelta(true)));
            RenderSystem.applyModelViewMatrix();
        }
    }

    @Inject(
            method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderScoreboard(DrawContext var1, ScoreboardObjective var2, CallbackInfo var3) {
        if (NoRender.method1973()) {
            var3.cancel();
        }
    }

    @Inject(
            method = "renderOverlay",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderPumpkinOverlay(DrawContext var1, Identifier var2, float var3, CallbackInfo var4) {
        if (var2.equals(PUMPKIN_BLUR)) {
            PlayerOverlayEvent var5 = Boze.EVENT_BUS.post(PlayerOverlayEvent.method1080(PlayerOverlay.Pumpkin));
            if (var5.method1022()) {
                var4.cancel();
            }
        }
    }

    @Inject(
            method = "renderStatusEffectOverlay",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderStatusEffectOverlay(DrawContext var1, RenderTickCounter var2, CallbackInfo var3) {
        if (NoRender.method1981()) {
            var3.cancel();
        }
    }

    @Inject(
            method = "renderCrosshair",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderCrosshair(DrawContext var1, RenderTickCounter var2, CallbackInfo var3) {
        if (Crosshair.INSTANCE.isEnabled()) {
            var3.cancel();
        }
    }
}
