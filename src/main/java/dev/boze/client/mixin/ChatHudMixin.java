package dev.boze.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import dev.boze.client.events.AddMessageEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixininterfaces.IChatHud;
import dev.boze.client.mixininterfaces.IChatHudLine;
import dev.boze.client.mixininterfaces.IChatHudLineVisible;
import dev.boze.client.systems.modules.misc.ExtraChat;
import dev.boze.client.utils.RGBAColor;
import java.util.List;
import dev.boze.client.Boze;
import mapped.Class2780;
import mapped.Class3071;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({ChatHud.class})
public abstract class ChatHudMixin implements IChatHud {
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   @Final
   private List<ChatHudLine> messages;
   @Shadow
   @Final
   private List<Visible> visibleMessages;
   @Unique
   private static int lastAge;
   @Unique
   private boolean internalAdd;
   @Unique
   private int addId;

   @Shadow
   public abstract boolean isChatFocused();

   @Shadow
   public abstract int getWidth();

   @Shadow
   public abstract void addMessage(Text var1, @Nullable MessageSignatureData var2, @Nullable MessageIndicator var3);

   @Shadow
   public abstract void addMessage(Text var1);

   @Override
   public void boze$addMessage(Text message, int id) {
      this.addId = id;
      this.addMessage(message, null, ChatInstance.field1636);
      this.addId = 0;
   }

   @Inject(
      method = {"addVisibleMessage"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/List;add(ILjava/lang/Object;)V",
         shift = Shift.AFTER
      )}
   )
   private void onAddMessageAfterNewChatHudLineVisible(ChatHudLine var1, CallbackInfo var2) {
      if (!this.visibleMessages.isEmpty()) {
         ((IChatHudLine)this.visibleMessages.get(0)).boze$setID(this.addId);
      }
   }

   @Inject(
      method = {"addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/List;add(ILjava/lang/Object;)V",
         shift = Shift.AFTER
      )}
   )
   private void onAddMessageAfterNewChatHudLine(ChatHudLine var1, CallbackInfo var2) {
      if (!this.messages.isEmpty()) {
         ((IChatHudLine)this.messages.get(0)).boze$setID(this.addId);
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"},
      cancellable = true
   )
   private void onAddMessage(Text var1, MessageSignatureData var2, MessageIndicator var3, CallbackInfo var4) {
      if (!this.internalAdd) {
         AddMessageEvent var5 = (AddMessageEvent) Boze.EVENT_BUS.post(AddMessageEvent.method1040(var1, var3));
         if (var5.method1022()) {
            var4.cancel();
         } else {
            this.visibleMessages.removeIf(this::lambda$onAddMessage$0);

            for (int var6 = this.messages.size() - 1; var6 > -1; var6--) {
               if (((IChatHudLine)this.messages.get(var6)).boze$getID() == this.addId && this.addId != 0) {
                  this.messages.remove(var6);
               }
            }

            if (var5.method1045()) {
               var4.cancel();
               this.internalAdd = true;
               this.addMessage(var5.method1041(), var2, var5.method1042());
               this.internalAdd = false;
            }
         }
      }
   }

   @Inject(
      method = {"getMessageOpacityMultiplier"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetMessageOpacityMultiplier(int var0, CallbackInfoReturnable<Double> var1) {
      lastAge = var0;
      if (ExtraChat.INSTANCE.isEnabled() && ExtraChat.INSTANCE.field2934.method419() && var0 < ExtraChat.INSTANCE.field2935.method434()) {
         double var4 = Class3071.method6022(0.0, 1.0, (double)var0 / (double)ExtraChat.INSTANCE.field2935.method434().intValue());
         var4 = MathHelper.clamp(var4, 0.0, 1.0);
         var4 *= var4;
         var1.setReturnValue(var4);
      }
   }

   @ModifyArgs(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"
      )
   )
   public void slideModify(Args args) {
      if (ExtraChat.INSTANCE.isEnabled()) {
         if (ExtraChat.INSTANCE.field2936.method419() && !this.isChatFocused()) {
            double var4 = Class3071.method6022(
               0.0,
               1.0,
               (double)((float)lastAge + this.client.getRenderTickCounter().getTickDelta(true)) / (double)ExtraChat.INSTANCE.field2937.method434().intValue()
            );
            var4 = MathHelper.clamp(var4, 0.0, 1.0);
            var4 *= var4;
            var4 = 1.0 - var4;
            int var6 = this.client.textRenderer.getWidth((OrderedText)args.get(1)) + 4;
            args.set(2, -MathHelper.ceil((double)var6 * var4));
         }

         args.set(4, ExtraChat.INSTANCE.field2933.method1347().copy().method196(RGBAColor.method188((Integer)args.get(4))).method2010());
      }
   }

   @ModifyExpressionValue(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/math/MathHelper;ceil(F)I"
      )}
   )
   private int modifyChatWidth(int var1) {
      if (ExtraChat.INSTANCE.isEnabled() && ExtraChat.INSTANCE.field2939.method419()) {
         var1 += 10;
      }

      return var1;
   }

   @ModifyReceiver(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"
      )}
   )
   private DrawContext addHeads(DrawContext var1, TextRenderer var2, OrderedText var3, int var4, int var5, int var6, @Local Visible var7) {
      ExtraChat.INSTANCE.method1702(var1, var7, var5, var6);
      return var1;
   }

   @ModifyExpressionValue(
      method = {"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"},
      at = {@At(
         value = "NEW",
         target = "(ILnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)Lnet/minecraft/client/gui/hud/ChatHudLine;"
      )}
   )
   private ChatHudLine getSenderProfileFromLine(ChatHudLine var1) {
      Class2780 var2 = (Class2780)this.client.getMessageHandler();
      if (var2 == null) {
         return var1;
      } else {
         ((IChatHudLine)var1).boze$setSenderProfile(var2.boze$getSenderProfile());
         return var1;
      }
   }

   @ModifyExpressionValue(
      method = {"addVisibleMessage"},
      at = {@At(
         value = "NEW",
         target = "(ILnet/minecraft/text/OrderedText;Lnet/minecraft/client/gui/hud/MessageIndicator;Z)Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;"
      )}
   )
   private Visible getSenderProfileFromVisible(Visible var1, @Local(ordinal = 1) int var2) {
      Class2780 var3 = (Class2780)this.client.getMessageHandler();
      if (var3 == null) {
         return var1;
      } else {
         IChatHudLineVisible var4 = (IChatHudLineVisible)var1;
         var4.boze$setSenderProfile(var3.boze$getSenderProfile());
         var4.boze$setFirst(var2 == 0);
         return var1;
      }
   }

   @Unique
   private boolean lambda$onAddMessage$0(Visible var1) {
      return ((IChatHudLine)var1).boze$getID() == this.addId && this.addId != 0;
   }
}
