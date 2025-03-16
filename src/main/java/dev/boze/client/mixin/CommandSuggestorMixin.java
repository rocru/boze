package dev.boze.client.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.boze.api.BozeInstance;
import dev.boze.api.addon.command.AddonDispatcher;
import dev.boze.client.systems.modules.client.Options;
import java.util.concurrent.CompletableFuture;
import mapped.Class27;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatInputSuggestor.SuggestionWindow;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ChatInputSuggestor.class})
public abstract class CommandSuggestorMixin {
   @Shadow
   private ParseResults<CommandSource> parse;
   @Shadow
   @Final
   private TextFieldWidget textField;
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   private boolean completingSuggestions;
   @Shadow
   private CompletableFuture<Suggestions> pendingSuggestions;
   @Shadow
   private SuggestionWindow window;

   @Shadow
   public abstract void show(boolean var1);

   @Inject(
      method = {"refresh"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/brigadier/StringReader;canRead()Z",
         remap = false
      )},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   public void onRefresh(CallbackInfo ci, String string, StringReader reader) {
      String var6 = Options.method1563();
      int var7 = var6.length();
      if (reader.canRead(var7) && reader.getString().startsWith(var6, reader.getCursor())) {
         for (AddonDispatcher var9 : BozeInstance.INSTANCE.getDispatchers()) {
            String var10 = var9.getPrefix() + "-";
            if (!var10.isEmpty()) {
               int var11 = var10.length();
               if (reader.canRead(var7 + var11) && reader.getString().startsWith(var6 + var10, reader.getCursor())) {
                  reader.setCursor(reader.getCursor() + var7 + var11);
                  if (this.client.player == null) {
                     return;
                  }

                  if (this.parse == null) {
                     this.parse = var9.getDispatcher().parse(reader, Class27.getCommands().method1141());
                  }

                  int var12 = this.textField.getCursor();
                  if (var12 >= 1 && (this.window == null || !this.completingSuggestions)) {
                     this.pendingSuggestions = var9.getDispatcher().getCompletionSuggestions(this.parse, var12);
                     this.pendingSuggestions.thenRun(this::lambda$onRefresh$0);
                  }

                  ci.cancel();
                  return;
               }
            }
         }

         reader.setCursor(reader.getCursor() + var7);
         if (this.client.player == null) {
            return;
         }

         CommandDispatcher var13 = Class27.getCommands().method1140();
         if (this.parse == null) {
            this.parse = var13.parse(reader, Class27.getCommands().method1141());
         }

         int var14 = this.textField.getCursor();
         if (var14 >= 1 && (this.window == null || !this.completingSuggestions)) {
            this.pendingSuggestions = var13.getCompletionSuggestions(this.parse, var14);
            this.pendingSuggestions.thenRun(this::lambda$onRefresh$1);
         }

         ci.cancel();
      }
   }

   private void lambda$onRefresh$1() {
      if (this.pendingSuggestions.isDone()) {
         this.show(false);
      }
   }

   private void lambda$onRefresh$0() {
      if (this.pendingSuggestions.isDone()) {
         this.show(false);
      }
   }
}
