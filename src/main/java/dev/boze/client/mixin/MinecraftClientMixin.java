package dev.boze.client.mixin;

import dev.boze.api.BozeInstance;
import dev.boze.api.event.EventTick.Post;
import dev.boze.api.event.EventTick.Pre;
import dev.boze.client.Boze;
import dev.boze.client.api.BozeAPI;
import dev.boze.client.events.HandleInputEvent;
import dev.boze.client.events.OpenScreenEvent;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.instances.BozeInstances;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.hud.core.Framerate;
import dev.boze.client.systems.modules.legit.AutoClicker;
import dev.boze.client.systems.modules.legit.NoMissDelay;
import dev.boze.client.systems.modules.misc.MiddleClickAction;
import dev.boze.client.systems.modules.misc.MultiTask;
import dev.boze.client.systems.modules.render.NoRender;
import dev.boze.client.utils.player.RotationHandler;
import mapped.Class3092;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {
    @Shadow
    @Final
    public static boolean IS_SYSTEM_MAC;
    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;
    @Shadow
    private int itemUseCooldown;
    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Shadow
    @Nullable
    public HitResult crosshairTarget;
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Nullable
    public ClientWorld world;
    @Shadow
    @Final
    public GameRenderer gameRenderer;
    @Shadow
    @Nullable
    private Overlay overlay;
    @Shadow
    @Nullable
    public Screen currentScreen;
    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    public abstract boolean isWindowFocused();

    @Shadow
    public abstract Framebuffer getFramebuffer();

    @Inject(
            method = {"<init>"},
            at = {@At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MinecraftClient;instance:Lnet/minecraft/client/MinecraftClient;"
            )}
    )
    public void onInitPre(RunArgs args, CallbackInfo ci) {
        Boze.FOLDER = new File(args.directories.runDir, "boze");
        BozeAPI.method951();
        BozeInstances.method1127();
    }

    @Inject(
            method = {"<init>"},
            at = {@At("TAIL")}
    )
    public void onInit(RunArgs args, CallbackInfo ci) {
        // new Boze().initialize();
        // ^ oninitialize
    }

    @Inject(
            method = {"getFramerateLimit"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onGetFramerateLimit(CallbackInfoReturnable<Integer> var1) {
        if (NoRender.method1971() && !this.isWindowFocused()) {
            var1.setReturnValue(1);
        }
    }

    @Inject(
            method = {"isWindowFocused"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onIsWindowFocused(CallbackInfoReturnable<Boolean> var1) {
        if (Options.INSTANCE.field987.getValue()) {
            var1.setReturnValue(true);
        }
    }

    @Redirect(
            method = {"handleBlockBreaking"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
            )
    )
    private boolean onIsUsingItemBlockBreaking(ClientPlayerEntity var1) {
        return !MultiTask.INSTANCE.isEnabled() && var1.isUsingItem();
    }

    @Redirect(
            method = {"doItemUse"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"
            )
    )
    private boolean onIsBreakingBlockItemUse(ClientPlayerInteractionManager var1) {
        return !MultiTask.INSTANCE.isEnabled() && var1.isBreakingBlock();
    }

    @Inject(
            method = {"doItemPick"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onItemPick(CallbackInfo var1) {
        if (MiddleClickAction.INSTANCE.isEnabled() && MiddleClickAction.INSTANCE.field492.getValue()) {
            var1.cancel();
        }
    }

    @Inject(
            method = {"setScreen"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onSetScreen(Screen var1, CallbackInfo var2) {
        if (Boze.EVENT_BUS.post(OpenScreenEvent.method1037(var1)).method1022()) {
            var2.cancel();
        }
    }

    @Inject(
            method = {"tick"},
            at = {@At("HEAD")}
    )
    private void tick(CallbackInfo var1) {
        RotationHandler.field1546.method2142();
        Boze.EVENT_BUS.post(PreTickEvent.method1092());
        Pre var2 = Pre.get();
        BozeInstance.INSTANCE.post(var2);
    }

    @Inject(
            method = {"tick"},
            at = {@At("TAIL")}
    )
    private void tickPost(CallbackInfo var1) {
        Boze.EVENT_BUS.post(PostTickEvent.method1088());
        Post var2 = Post.get();
        BozeInstance.INSTANCE.post(var2);
    }

    @Inject(
            method = {"doAttack"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onDoAttack(CallbackInfoReturnable<Boolean> var1) {
        if (NoMissDelay.INSTANCE.isEnabled() && NoMissDelay.INSTANCE.method1611() && this.crosshairTarget != null && this.crosshairTarget.getType() == Type.MISS) {
            if (AutoClicker.INSTANCE.isEnabled()
                    && AutoClicker.INSTANCE.field2732.getValue()
                    && AutoClicker.INSTANCE.field2734.getValue()
                    && this.options.attackKey.isPressed()) {
                return;
            }

            var1.setReturnValue(false);
        }
    }

    @Inject(
            method = {"handleInputEvents"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
            )},
            slice = {@Slice(
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z"
                    )
            )}
    )
    public void onHandleInputEvents(CallbackInfo ci) {
        RotationHandler.field1546.method1416();
    }

    @Inject(
            method = {"handleInputEvents"},
            at = {@At("HEAD")}
    )
    public void onHandleInputEventsHead(CallbackInfo ci) {
        HandleInputEvent var2 = HandleInputEvent.method1064();
        Boze.EVENT_BUS.post(var2);
    }

    @Inject(
            method = {"tick"},
            at = {@At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MinecraftClient;overlay:Lnet/minecraft/client/gui/screen/Overlay;"
            )}
    )
    public void onHandleInputEventsGui(CallbackInfo ci) {
        if (this.overlay != null || this.currentScreen != null) {
            RotationHandler.field1546.method1416();
        }
    }

    @Inject(
            method = {"render"},
            at = {@At("HEAD")}
    )
    public void renderPre(boolean tick, CallbackInfo ci) {
        Class3092.field219 = System.nanoTime();
    }

    @Inject(
            method = {"render"},
            at = {@At("TAIL")}
    )
    private void renderPost(boolean var1, CallbackInfo var2) {
        Class3092.field218 = (double) (System.nanoTime() - Class3092.field219) / 1000000.0;
        Framerate.INSTANCE.method1555();
        dev.boze.client.systems.modules.hud.graph.Framerate.INSTANCE.method1568();
    }
}
