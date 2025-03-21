package dev.boze.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({Window.class})
public class WindowMixin {
    @ModifyArg(
            method = {"<init>"},
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V",
                    ordinal = 5
            ),
            index = 1
    )
    private int onEnableForwardCompat(int var1) {
        return MinecraftClient.IS_SYSTEM_MAC ? var1 : 0;
    }
}
