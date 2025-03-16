package dev.boze.client.mixin;

import dev.boze.client.events.FinishUsingEvent;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.Boze;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ItemStack.class})
public class ItemStackMixin {
   @Inject(
      method = {"finishUsing"},
      at = {@At("HEAD")}
   )
   private void onFinishUsing(World var1, LivingEntity var2, CallbackInfoReturnable<ItemStack> var3) {
      if (var2 == IMinecraft.mc.player) {
         Boze.EVENT_BUS.post(FinishUsingEvent.method1061((ItemStack)this));
      }
   }
}
