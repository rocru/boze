package dev.boze.client.mixin;

import dev.boze.client.systems.modules.misc.BeaconSelector;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen.EffectButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BeaconScreen.class})
public abstract class BeaconScreenMixin extends HandledScreen<BeaconScreenHandler> {
   @Shadow
   protected abstract <T extends ClickableWidget> void addButton(T var1);

   public BeaconScreenMixin(BeaconScreenHandler handler, PlayerInventory inventory, Text title) {
      super(handler, inventory, title);
   }

   @Inject(
      method = {"init"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screen/ingame/BeaconScreen;addButton(Lnet/minecraft/client/gui/widget/ClickableWidget;)V",
         ordinal = 1,
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void onInitAddButton(CallbackInfo var1) {
      if (BeaconSelector.INSTANCE.isEnabled()) {
         List var4 = BeaconBlockEntity.EFFECTS_BY_LEVEL.stream().flatMap(Collection::stream).toList();
         if (MinecraftClient.getInstance().currentScreen instanceof BeaconScreen var5) {
            for (int var12 = 0; var12 < 3; var12++) {
               for (int var7 = 0; var7 < 2; var7++) {
                  RegistryEntry var8 = (RegistryEntry)var4.get(var12 * 2 + var7);
                  int var9 = this.x + var12 * 25;
                  int var10 = this.y + var7 * 25;
                  Objects.requireNonNull(var5);
                  this.addButton(new EffectButtonWidget(var5, var9 + 27, var10 + 32, var8, true, -1));
                  Objects.requireNonNull(var5);
                  EffectButtonWidget var11 = new EffectButtonWidget(var5, var9 + 133, var10 + 32, var8, false, 3);
                  if (((BeaconScreenHandler)this.getScreenHandler()).getProperties() != 4) {
                     var11.active = false;
                  }

                  this.addButton(var11);
               }
            }
         }

         var1.cancel();
      }
   }

   @Inject(
      method = {"drawBackground"},
      at = {@At("TAIL")}
   )
   private void onDrawBackground(DrawContext var1, float var2, int var3, int var4, CallbackInfo var5) {
      if (BeaconSelector.INSTANCE.isEnabled()) {
         var1.fill(this.x + 10, this.y + 7, this.x + 220, this.y + 98, -14606047);
      }
   }
}
