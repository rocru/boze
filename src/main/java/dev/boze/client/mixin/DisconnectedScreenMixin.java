package dev.boze.client.mixin;

import dev.boze.client.enums.GUIMenu;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.misc.AutoReconnect;
import dev.boze.client.Boze;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ButtonWidget.Builder;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo.ServerType;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({DisconnectedScreen.class})
public abstract class DisconnectedScreenMixin extends Screen {
   @Shadow
   @Final
   private DirectionalLayoutWidget grid;
   @Unique
   private ButtonWidget reconnectBtn;
   @Unique
   private double remainingDelay = AutoReconnect.INSTANCE.delay.getValue();

   protected DisconnectedScreenMixin(Text title) {
      super(title);
   }

   @Unique
   private String getText() {
      String var3 = "Reconnect";
      if (AutoReconnect.INSTANCE.isEnabled()) {
         var3 = var3 + " " + String.format("(%.1f)", this.remainingDelay);
      }

      return var3;
   }

   @Inject(
      method = {"init"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;refreshPositions()V",
         shift = Shift.BEFORE
      )},
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void onInitPre(CallbackInfo var1, ButtonWidget var2) {
      if (AutoReconnect.INSTANCE.isEnabled() && Boze.getModules().field907.field1613 != null) {
         this.reconnectBtn = (ButtonWidget)this.grid.add(new Builder(Text.literal(this.getText()), this::lambda$onInitPre$0).build());
         this.grid
            .add(new Builder(Text.literal((AutoReconnect.INSTANCE.isEnabled() ? "Disable " : "Enable ") + "AutoReconnect"), this::lambda$onInitPre$1).build());
      }

      if (Options.INSTANCE.field986.getValue()) {
         this.addDrawableChild(new Builder(Text.literal("Accounts"), this::lambda$onInitPre$2).position(this.width - 106, 6).size(100, 20).build());
      }
   }

   public void tick() {
      if (AutoReconnect.INSTANCE.isEnabled() && Boze.getModules().field907.field1613 != null) {
         if (this.remainingDelay <= 0.0) {
            this.tryConnecting();
         } else {
            this.remainingDelay -= 0.05;
            if (this.reconnectBtn != null) {
               this.reconnectBtn.setMessage(Text.literal(this.getText()));
            }
         }
      }
   }

   @Unique
   private void tryConnecting() {
      ServerInfo var1 = Boze.getModules().field907.field1613;
      String var2 = var1.address;
      if (var2.contains(":")) {
         var2 = var2.substring(0, var2.indexOf(":"));
      }

      ConnectScreen.connect(
         new TitleScreen(),
         this.client,
         ServerAddress.parse(Boze.getModules().field907.field1613.address),
         new ServerInfo(I18n.translate("selectServer.defaultName", new Object[0]), Boze.getModules().field907.field1613.address, ServerType.OTHER),
         false,
         null
      );
   }

   @Unique
   private void lambda$onInitPre$2(ButtonWidget var1) {
      ClickGUI.field1335.field1332 = GUIMenu.AltManager;
      ClickGUI.field1335.field1338 = this;
      this.client.setScreen(ClickGUI.field1335);
   }

   @Unique
   private void lambda$onInitPre$1(ButtonWidget var1) {
      AutoReconnect.INSTANCE.setEnabled(!AutoReconnect.INSTANCE.isEnabled());
      this.reconnectBtn.setMessage(Text.literal(this.getText()));
      this.remainingDelay = AutoReconnect.INSTANCE.delay.getValue();
   }

   @Unique
   private void lambda$onInitPre$0(ButtonWidget var1) {
      this.tryConnecting();
   }
}
