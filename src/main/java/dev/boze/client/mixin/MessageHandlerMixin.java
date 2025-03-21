package dev.boze.client.mixin;

import com.mojang.authlib.GameProfile;
import mapped.Class2780;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType.Parameters;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

@Mixin({MessageHandler.class})
public abstract class MessageHandlerMixin implements Class2780 {
    @Unique
    private GameProfile sender;

    @Inject(
            method = {"processChatMessageInternal"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
                    shift = Shift.BEFORE
            )}
    )
    private void onBeforeAddMessage(
            Parameters var1, SignedMessage var2, Text var3, GameProfile var4, boolean var5, Instant var6, CallbackInfoReturnable<Boolean> var7
    ) {
        this.sender = var4;
    }

    @Inject(
            method = {"processChatMessageInternal"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
                    shift = Shift.AFTER
            )}
    )
    private void onAfterAddMessage(
            Parameters var1, SignedMessage var2, Text var3, GameProfile var4, boolean var5, Instant var6, CallbackInfoReturnable<Boolean> var7
    ) {
        this.sender = null;
    }

    @Override
    public GameProfile boze$getSenderProfile() {
        return this.sender;
    }
}
