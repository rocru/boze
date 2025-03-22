package dev.boze.client.mixin;

import dev.boze.api.BozeInstance;
import dev.boze.api.event.EventInput;
import dev.boze.client.Boze;
import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.utils.player.RotationHandler;
import mapped.Class2866;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends InputMixin {
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void onTickInputPost(boolean var1, float var2, CallbackInfo var3) {
        TickInputPostEvent var4 = TickInputPostEvent.method1096(this.movementSideways, this.movementForward, this.jumping, this.sneaking);
        if (RotationHandler.field1546.method2114()) {
            Class2866.method1872(var4);
        } else {
            Boze.EVENT_BUS.post(var4);
        }

        EventInput var5 = EventInput.get(var4.field1953, var4.field1954, var4.field1955, var4.field1956);
        BozeInstance.INSTANCE.post(var5);
        this.movementSideways = var5.movementSideways;
        this.movementForward = var5.movementForward;
        this.jumping = var5.jumping;
        this.sneaking = var5.sneaking;
    }
}
