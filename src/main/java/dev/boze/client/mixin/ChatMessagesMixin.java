package dev.boze.client.mixin;

import dev.boze.client.systems.modules.client.Media;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatMessages.class)
public class ChatMessagesMixin {
    @Inject(
            method = "getRenderedChatMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGetRenderedChatMessage(String var0, CallbackInfoReturnable<String> var1) {
        var1.setReturnValue(MinecraftClient.getInstance().options.getChatColors().getValue() ? Media.method1341(var0) : Formatting.strip(Media.method1341(var0)));
    }
}
