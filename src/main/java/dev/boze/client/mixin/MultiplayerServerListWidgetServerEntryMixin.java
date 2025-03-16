package dev.boze.client.mixin;

import dev.boze.client.systems.modules.client.Playtime;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget.ServerEntry;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ServerEntry.class})
public abstract class MultiplayerServerListWidgetServerEntryMixin {
   @Shadow
   public abstract ServerInfo getServer();

   @Redirect(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"
      )
   )
   private int onRenderPlayerCount(DrawContext var1, TextRenderer var2, Text var3, int var4, int var5, int var6, boolean var7) {
      if (Playtime.INSTANCE.isEnabled()) {
         String var10 = this.getServer().address;
         if (var10 != null) {
            long var11 = (Long)Playtime.INSTANCE.field2406.getOrDefault(var10, 0L);
            String var13 = String.format("%dh", var11 / 3600L);
            var1.drawText(var2, var13, var4 - var2.getWidth(var13) - 3, var5, var6, false);
         }
      }

      return var1.drawText(var2, var3, var4, var5, var6, false);
   }
}
