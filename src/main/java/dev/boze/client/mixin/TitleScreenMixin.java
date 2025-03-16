package dev.boze.client.mixin;

import dev.boze.client.core.Version;
import dev.boze.client.enums.GUIMenu;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.Boze;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.AccessibilityOnboardingButtons;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsNotificationsScreen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public abstract class TitleScreenMixin extends Screen {
   @Unique
   private int copyrightTextWidth;
   @Unique
   private int copyrightTextX;
   @Shadow
   private RealmsNotificationsScreen realmsNotificationGui;
   @Shadow
   @Final
   private static Text COPYRIGHT;
   @Shadow
   @Nullable
   public SplashTextRenderer splashText;
   @Unique
   private static int aTX;
   @Unique
   private static int aTY;

   @Shadow
   protected abstract void initWidgetsDemo(int var1, int var2);

   @Shadow
   protected abstract void initWidgetsNormal(int var1, int var2);

   @Shadow
   protected abstract boolean isRealmsNotificationsGuiDisplayed();

   protected TitleScreenMixin(Text title) {
      super(title);
   }

   @Inject(
      method = {"init"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onInit(CallbackInfo ci) {
      if (Options.INSTANCE.field985.method419()) {
         ci.cancel();
         if (this.splashText == null) {
            this.splashText = this.client.getSplashTextLoader().get();
         }

         int var2 = this.textRenderer.getWidth(COPYRIGHT);
         int var3 = this.width - var2 - 2;
         int var5 = this.height / 4 + 48;
         if (this.client.isDemo()) {
            this.initWidgetsDemo(var5, 24);
         } else {
            this.initWidgetsNormal(var5, 24);
         }

         this.addDrawableChild(
            ButtonWidget.builder(Text.literal("Boze"), this::lambda$onInit$0).position(this.width / 2 - 100, this.height / 4 + 48 + 72).size(200, 20).build()
         );
         TextIconButtonWidget var6 = (TextIconButtonWidget)this.addDrawableChild(
            AccessibilityOnboardingButtons.createLanguageButton(20, this::lambda$onInit$1, true)
         );
         var6.setPosition(this.width / 2 - 124, var5 + 72 + 12 + 24);
         this.addDrawableChild(
            ButtonWidget.builder(Text.translatable("menu.options"), this::lambda$onInit$2)
               .dimensions(this.width / 2 - 100, var5 + 72 + 12 + 24, 98, 20)
               .build()
         );
         this.addDrawableChild(
            ButtonWidget.builder(Text.translatable("menu.quit"), this::lambda$onInit$3).dimensions(this.width / 2 + 2, var5 + 72 + 12 + 24, 98, 20).build()
         );
         TextIconButtonWidget var7 = (TextIconButtonWidget)this.addDrawableChild(
            AccessibilityOnboardingButtons.createAccessibilityButton(20, this::lambda$onInit$4, true)
         );
         var7.setPosition(this.width / 2 + 104, var5 + 72 + 12 + 24);
         this.addDrawableChild(new PressableTextWidget(var3, this.height - 10, var2, 10, COPYRIGHT, this::lambda$onInit$5, this.textRenderer));
         if (this.realmsNotificationGui == null) {
            this.realmsNotificationGui = new RealmsNotificationsScreen();
         }

         if (this.isRealmsNotificationsGuiDisplayed()) {
            this.realmsNotificationGui.init(this.client, this.width, this.height);
         }
      }

      aTX = this.width / 2;
      aTY = this.height / 4 + 186;
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (Options.INSTANCE.field985.method419()) {
         context.drawText(this.client.textRenderer, Text.literal("Boze " + Version.tag), 2, this.height - 20, -1, true);
         if (!Boze.BUILD.isEmpty()) {
            context.drawText(this.client.textRenderer, Text.literal("Build " + Boze.BUILD), 2, this.height - 30, -1, true);
         }

         if (this.client.getSession() != null) {
            MutableText var6 = Text.literal("Logged in as " + this.client.getSession().getUsername());
            context.drawText(this.client.textRenderer, var6, this.width - 2 - this.textRenderer.getWidth(var6), this.height - 20, -1, true);
         }

         if (!ConfigManager.field2139) {
            context.drawCenteredTextWithShadow(
               this.client.textRenderer, Text.literal("Please link your Discord on the dashboard at boze.dev/dashboard"), aTX, aTY, -65536
            );
         }
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   public void beforeRenderTitle(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (!ShaderRegistry.field2273) {
         ShaderRegistry.method1315();
      }
   }

   @Unique
   private void lambda$onInit$5(ButtonWidget var1) {
      this.client.setScreen(new CreditsAndAttributionScreen(this));
   }

   @Unique
   private void lambda$onInit$4(ButtonWidget var1) {
      this.client.setScreen(new AccessibilityOptionsScreen(this, this.client.options));
   }

   @Unique
   private void lambda$onInit$3(ButtonWidget var1) {
      this.client.scheduleStop();
   }

   @Unique
   private void lambda$onInit$2(ButtonWidget var1) {
      this.client.setScreen(new OptionsScreen(this, this.client.options));
   }

   @Unique
   private void lambda$onInit$1(ButtonWidget var1) {
      this.client.setScreen(new LanguageOptionsScreen(this, this.client.options, this.client.getLanguageManager()));
   }

   @Unique
   private void lambda$onInit$0(ButtonWidget var1) {
      ClickGUI.field1335.field1332 = GUIMenu.Normal;
      this.client.setScreen(ClickGUI.field1335);
      ClickGUI.field1335.field1336 = false;
   }
}
