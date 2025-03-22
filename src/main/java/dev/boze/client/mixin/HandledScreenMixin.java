package dev.boze.client.mixin;

import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.misc.InventoryTweaks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.Builder;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
    public HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onOnMouseClick(Slot var1, int var2, int var3, SlotActionType var4, CallbackInfo var5) {
        if (var4 == SlotActionType.SWAP && var3 != 40 && Options.INSTANCE.field992.getValue()) {
            var5.cancel();
        }
    }

    @Shadow
    public abstract T getScreenHandler();

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private void onInit(CallbackInfo var1) {
        if (InventoryTweaks.INSTANCE.isEnabled()
                && InventoryTweaks.INSTANCE.field2961.getValue()
                && this.getScreenHandler() instanceof GenericContainerScreenHandler) {
            this.addDrawableChild(new Builder(Text.literal("Steal"), this::lambda$onInit$0).position(this.width - 120, 10).size(50, 20).build());
            this.addDrawableChild(new Builder(Text.literal("Dump"), this::lambda$onInit$1).position(this.width - 60, 10).size(50, 20).build());
        }
    }

    @Unique
    private void lambda$onInit$1(ButtonWidget var1) {
        InventoryTweaks.INSTANCE.method1720(this.getScreenHandler());
    }

    @Unique
    private void lambda$onInit$0(ButtonWidget var1) {
        InventoryTweaks.INSTANCE.method1719(this.getScreenHandler());
    }
}
