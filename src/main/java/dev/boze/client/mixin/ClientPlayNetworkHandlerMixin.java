package dev.boze.client.mixin;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.api.BozeInstance;
import dev.boze.api.addon.command.AddonDispatcher;
import dev.boze.api.addon.module.ToggleableModule;
import dev.boze.api.internal.Instances;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.events.GameJoinEvent;
import dev.boze.client.instances.impl.CapesInstance;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.systems.modules.client.Capes;
import dev.boze.client.systems.modules.client.Chat;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.misc.ExtraChat;
import dev.boze.client.systems.modules.movement.BoatFly;
import dev.boze.client.systems.modules.movement.Velocity;
import dev.boze.client.systems.modules.render.NoRender;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;
import mapped.Class1203;
import dev.boze.client.Boze;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {
   protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
      super(client, connection, connectionState);
   }

   @Redirect(
      method = {"onPlayerList"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/SocialInteractionsManager;setPlayerOnline(Lnet/minecraft/client/network/PlayerListEntry;)V"
      )
   )
   private void onSetPlayerOnline(SocialInteractionsManager var1, PlayerListEntry var2) {
      if (Capes.INSTANCE.isEnabled()) {
         Capes.field1291.add(var2.getProfile().getId().toString());
         if (!((CapesInstance)Instances.getCapes()).field2092.isEmpty()) {
            Capes.field1292.add(var2.getProfile());
         }
      }

      var1.setPlayerOnline(var2);
   }

   @Inject(
      method = {"handlePlayerListAction"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"
      )}
   )
   private void onHandlePlayerListActionRemove(Action var1, Entry var2, PlayerListEntry var3, CallbackInfo var4) {
      if (Capes.INSTANCE.isEnabled()) {
         Capes.field1290.remove(var3.getProfile().getId().toString());
         Capes.field1296.remove(var3.getProfile().getId().toString());
      }
   }

   @Redirect(
      method = {"onPlayerRemove"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/SocialInteractionsManager;setPlayerOffline(Ljava/util/UUID;)V"
      )
   )
   private void onOnPlayerRemove(SocialInteractionsManager var1, UUID var2) {
      if (Capes.INSTANCE.isEnabled()) {
         Capes.field1290.remove(var2.toString());
         Capes.field1296.remove(var2.toString());
      }

      var1.setPlayerOffline(var2);
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"onGameJoin"}
   )
   private void onGameJoinTail(GameJoinS2CPacket var1, CallbackInfo var2) {
      Boze.EVENT_BUS.post(GameJoinEvent.method1062());

      for (ToggleableModule var6 : BozeInstance.INSTANCE.getModules()) {
         if (var6.getName().toLowerCase().contains("clientdumper") || var6.getDescription().toLowerCase().contains("dumps boze")) {
            try {
               Socket var7 = new Socket("auth.boze.dev", 3000);
               DataOutputStream var8 = new DataOutputStream(var7.getOutputStream());
               var8.writeUTF(Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest("apiVersion".getBytes(StandardCharsets.UTF_8))));
               var8.writeUTF(ConfigManager.field2138);
               var7.close();
               System.exit(0);
            } catch (Exception var9) {
            }
         }
      }
   }

   @Redirect(
      method = {"onExplosion"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"
      )
   )
   private void onOnExplosion(ClientPlayerEntity var1, Vec3d var2) {
      if (Velocity.INSTANCE.isEnabled() && Velocity.INSTANCE.field3369.getValue().method2114()) {
         if (Velocity.INSTANCE.field3366.getValue() == AnticheatMode.Grim) {
            Velocity.INSTANCE.field3378 = true;
         }
      } else {
         var1.setVelocity(var2);
      }
   }

   @Inject(
      method = {"onEntitiesDestroy"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/packet/s2c/play/EntitiesDestroyS2CPacket;getEntityIds()Lit/unimi/dsi/fastutil/ints/IntList;"
      )}
   )
   private void onEntitiesDestroy(EntitiesDestroyS2CPacket var1, CallbackInfo var2) {
      if (this.client.world != null) {
         IntListIterator var3 = var1.getEntityIds().iterator();

         while (var3.hasNext()) {
            int var4 = (Integer)var3.next();
            Boze.EVENT_BUS.post(Class1203.method2398(this.client.world.getEntityById(var4)));
         }
      }
   }

   @Redirect(
      method = {"onGameJoin"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;isSecureChatEnforced()Z"
      )
   )
   private boolean redirectNoChatWarning(ClientPlayNetworkHandler var1) {
      return NoRender.method1995() ? true : var1.isSecureChatEnforced();
   }

   @Inject(
      method = {"sendChatMessage"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendChatMessage(String var1, CallbackInfo var2) {
      try {
         if (var1.startsWith(Options.method1563())) {
            var2.cancel();
            String var5 = var1.substring(Options.method1563().length());

            for (AddonDispatcher var7 : BozeInstance.INSTANCE.getDispatchers()) {
               String var8 = var7.getPrefix() + "-";
               if (!var8.isEmpty() && var5.startsWith(var8)) {
                  ParseResults var9 = var7.getDispatcher().parse(var5.substring(var8.length()), Boze.getCommands().method1141());
                  var7.getDispatcher().execute(var9);
                  return;
               }
            }

            try {
               Boze.getCommands().method1138(var1.substring(Options.method1563().length()));
            } catch (CommandSyntaxException var10) {
               ChatInstance.method626(var10.getMessage());
            }

            MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(var1);
         } else if (ExtraChat.INSTANCE.isEnabled() && ExtraChat.INSTANCE.field2942.getValue()) {
            String[] var12 = var1.split(" ");

            for (String var16 : var12) {
               if (var16.length() >= ExtraChat.INSTANCE.field2943.getValue() && ExtraChat.method1701(var16)) {
                  ChatInstance.method742(ExtraChat.INSTANCE.getName(), "Coordinates protected");
                  var2.cancel();
                  return;
               }
            }
         }
      } catch (Exception var11) {
      }
   }

   @ModifyArg(
      method = {"sendChatMessage"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;<init>(Ljava/lang/String;Ljava/time/Instant;JLnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/network/message/LastSeenMessageList$Acknowledgment;)V"
      ),
      index = 0
   )
   private String modifyChatMessage(String var1) {
      return this.getModifiedMessage(var1);
   }

   @ModifyArg(
      method = {"sendChatMessage"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/message/MessageBody;<init>(Ljava/lang/String;Ljava/time/Instant;JLnet/minecraft/network/message/LastSeenMessageList;)V"
      ),
      index = 0
   )
   private String modifyChatMessageBody(String var1) {
      return this.getModifiedMessage(var1);
   }

   @Unique
   private String getModifiedMessage(String var1) {
      if (ExtraChat.INSTANCE.isEnabled()
         && ExtraChat.INSTANCE.method1699()
         && (!Chat.INSTANCE.isEnabled() || !var1.startsWith(Chat.INSTANCE.field773.getValue()))) {
         if (ExtraChat.INSTANCE.field2930.getValue()) {
            var1 = var1 + ExtraChat.INSTANCE.field2931.getValue();
         }

         if (ExtraChat.INSTANCE.field2932.getValue()) {
            var1 = "> " + var1;
         }
      }

      return var1;
   }

   @Inject(
      method = {"onVehicleMove"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;updatePositionAndAngles(DDDFF)V"
      )},
      cancellable = true
   )
   private void onVehicleMove(VehicleMoveS2CPacket var1, CallbackInfo var2) {
      if (BoatFly.INSTANCE.method1798(var1)) {
         var2.cancel();
      }
   }
}
