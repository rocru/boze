package dev.boze.client.mixin;

import dev.boze.client.enums.KeyAction;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.gui.screens.ClickGUI;
import mapped.Class27;
import mapped.Class3077;
import mapped.Class5928;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Keyboard.class})
public abstract class KeyboardMixin {
   @Inject(
      method = {"onKey"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
      if (key != -1 && !Class3077.field174) {
         Class5928.method1738(key, action != 0);
         if (((KeyEvent)Class27.EVENT_BUS.post(KeyEvent.method1065(key, modifiers, KeyAction.method816(action)))).method1022()) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"onChar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onChar(long var1, int var3, int var4, CallbackInfo var5) {
      if (MinecraftClient.getInstance().currentScreen != null && MinecraftClient.getInstance().currentScreen instanceof ClickGUI) {
         ClickGUI.field1335.method583((char)var3);
         var5.cancel();
      }
   }
}
