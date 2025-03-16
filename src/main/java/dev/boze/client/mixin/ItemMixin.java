package dev.boze.client.mixin;

import dev.boze.client.events.TooltipDataEvent;
import java.util.Optional;
import dev.boze.client.Boze;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Item.class})
public class ItemMixin {
   @Inject(
      method = {"getTooltipData"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onTooltipData(ItemStack var1, CallbackInfoReturnable<Optional<TooltipData>> var2) {
      TooltipDataEvent var3 = (TooltipDataEvent) Boze.EVENT_BUS.post(TooltipDataEvent.method1097(var1));
      if (var3.field1958 != null) {
         var2.setReturnValue(Optional.of(var3.field1958));
      }
   }
}
