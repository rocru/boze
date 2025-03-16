package dev.boze.client.mixin;

import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.MinecraftUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameMenuScreen.class})
public class GameMenuScreenMixin {
   @Inject(
      method = {"disconnect"},
      at = {@At("HEAD")}
   )
   private void onDisconnect(CallbackInfo var1) {
      if (AntiCheat.INSTANCE.field2321.method419() && !Options.INSTANCE.method1971() && MinecraftUtils.isClientActive()) {
         MinecraftClient.getInstance().player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(1000));
      }
   }
}
