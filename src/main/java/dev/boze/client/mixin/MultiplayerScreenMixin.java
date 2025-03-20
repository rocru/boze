package dev.boze.client.mixin;

import dev.boze.client.enums.GUIMenu;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Options;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.Builder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MultiplayerScreen.class})
public class MultiplayerScreenMixin extends Screen {
   protected MultiplayerScreenMixin(Text title) {
      super(title);
   }

   @Inject(
      method = {"init"},
      at = {@At("TAIL")}
   )
   public void onInit(CallbackInfo ci) {
      if (Options.INSTANCE.field986.getValue()) {
         boolean var2 = FabricLoaderImpl.INSTANCE.isModLoaded("viafabricplus");
         this.addDrawableChild(new Builder(Text.literal("Accounts"), this::lambda$onInit$0).position(this.width - (var2 ? 212 : 106), 6).size(100, 20).build());
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (Options.INSTANCE.field986.getValue()) {
         context.drawText(this.client.textRenderer, Text.translatable("Logged in as " + this.client.getSession().getUsername()), 6, 11, -1, true);
      }
   }

   @Unique
   private void lambda$onInit$0(ButtonWidget var1) {
      ClickGUI.field1335.field1332 = GUIMenu.AltManager;
      this.client.setScreen(ClickGUI.field1335);
   }
}
