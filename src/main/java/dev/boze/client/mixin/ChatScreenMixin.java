package dev.boze.client.mixin;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.api.BozeInstance;
import dev.boze.api.addon.command.AddonDispatcher;
import dev.boze.client.Boze;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.DebugHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChatScreen.class})
public abstract class ChatScreenMixin {
   @Inject(
      method = {"sendMessage"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendChatMessage(String var1, boolean var2, CallbackInfo var3) {
      try {
         if (var1.startsWith("--debug-mode")) {
            DebugHandler.execute(var1);
            var3.cancel();
         } else if (var1.startsWith(Options.method1563())) {
            var3.cancel();
            String var6 = var1.substring(Options.method1563().length());

            for (AddonDispatcher var8 : BozeInstance.INSTANCE.getDispatchers()) {
               String var9 = var8.getPrefix() + "-";
               if (!var9.isEmpty() && var6.startsWith(var9)) {
                  ParseResults var10 = var8.getDispatcher().parse(var6.substring(var9.length()), Boze.getCommands().method1141());
                  var8.getDispatcher().execute(var10);
                  return;
               }
            }

            try {
               Boze.getCommands().method1138(var1.substring(Options.method1563().length()));
            } catch (CommandSyntaxException var11) {
               ChatInstance.method626(var11.getMessage());
            }

            MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(var1);
         }
      } catch (Exception var12) {
      }
   }
}
