package dev.boze.client.mixin;

import dev.boze.client.Boze;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InventoryScreen.class})
public class InventoryScreenMixin {
   @Inject(
      method = {"drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V"},
      at = {@At("HEAD")}
   )
   private static void onDrawEntityPre(
      DrawContext var0, int var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, LivingEntity var9, CallbackInfo var10
   ) {
      Boze.isInventory = true;
   }

   @Inject(
      method = {"drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V"},
      at = {@At("TAIL")}
   )
   private static void onDrawEntityPost(
      DrawContext var0, int var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, LivingEntity var9, CallbackInfo var10
   ) {
      Boze.isInventory = false;
   }
}
