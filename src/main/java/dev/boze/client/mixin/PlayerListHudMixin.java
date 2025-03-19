package dev.boze.client.mixin;

import dev.boze.client.systems.modules.misc.BetterTab;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerListHud.class})
public abstract class PlayerListHudMixin {
   @Shadow
   @Final
   private MinecraftClient client;

   @ModifyConstant(
      constant = {@Constant(
         longValue = 80L
      )},
      method = {"collectPlayerEntries"}
   )
   private long modifyCount(long var1) {
      return BetterTab.INSTANCE.isEnabled() ? (long)BetterTab.INSTANCE.field2914.method434().intValue() : var1;
   }

   @Inject(
      method = {"getPlayerName"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getPlayerName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> info) {
      if (BetterTab.INSTANCE.isEnabled()) {
         info.setReturnValue(BetterTab.INSTANCE.method1690(playerListEntry));
      }
   }

   @ModifyArg(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Ljava/lang/Math;min(II)I"
      ),
      index = 0
   )
   private int modifyWidth(int var1) {
      return BetterTab.INSTANCE.isEnabled() && BetterTab.INSTANCE.field2916.method419() ? var1 + 30 : var1;
   }

   @ModifyConstant(
      constant = {@Constant(
         intValue = 20
      )},
      method = {"render"}
   )
   private int modifyHeight(int var1) {
      return BetterTab.INSTANCE.isEnabled() ? BetterTab.INSTANCE.field2915.method434() : var1;
   }

   @Inject(
      method = {"renderLatencyIcon"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderLatencyIcon(DrawContext var1, int var2, int var3, int var4, PlayerListEntry var5, CallbackInfo var6) {
      if (BetterTab.INSTANCE.isEnabled() && BetterTab.INSTANCE.field2916.method419()) {
         MinecraftClient var9 = MinecraftClient.getInstance();
         TextRenderer var10 = var9.textRenderer;
         int var11 = MathHelper.clamp(var5.getLatency(), 0, 9999);
         int var12 = var11 < 150 ? '\ue970' : (var11 < 300 ? 15192096 : 14107192);
         String var13 = var11 + "ms";
         var1.drawTextWithShadow(var10, var13, var3 + var2 - var10.getWidth(var13), var4, var12);
         var6.cancel();
      }
   }
}
