package dev.boze.client.mixin;

import dev.boze.client.events.BossBarIteratorEvent;
import dev.boze.client.events.BossBarNameEvent;
import dev.boze.client.events.BossBarSpacingEvent;
import dev.boze.client.systems.modules.render.NoRender;
import java.util.Collection;
import java.util.Iterator;
import dev.boze.client.Boze;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BossBarHud.class})
public class BossBarHudMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render(CallbackInfo var1) {
      if (NoRender.method1974()) {
         var1.cancel();
      }
   }

   @Redirect(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;"
      )
   )
   public Iterator<ClientBossBar> onIterate(Collection<ClientBossBar> collection) {
      BossBarIteratorEvent var2 = (BossBarIteratorEvent) Boze.EVENT_BUS.post(BossBarIteratorEvent.method1052(collection.iterator()));
      return var2.field1904;
   }

   @Redirect(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/hud/ClientBossBar;getName()Lnet/minecraft/text/Text;"
      )
   )
   public Text onGetName(ClientBossBar clientBossBar) {
      BossBarNameEvent var2 = (BossBarNameEvent) Boze.EVENT_BUS.post(BossBarNameEvent.method1053(clientBossBar, clientBossBar.getName()));
      return var2.text;
   }

   @ModifyConstant(
      method = {"render"},
      constant = {@Constant(
         intValue = 9,
         ordinal = 1
      )}
   )
   public int changeSpacing(int j) {
      BossBarSpacingEvent var2 = (BossBarSpacingEvent) Boze.EVENT_BUS.post(BossBarSpacingEvent.method1054(j));
      return var2.field1908;
   }
}
