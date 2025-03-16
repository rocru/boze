package dev.boze.client.mixin;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.session.ProfileKeys;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.report.AbuseReportContext;
import net.minecraft.client.texture.PlayerSkinProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({MinecraftClient.class})
public interface MinecraftClientAccessor {
   @Accessor("currentFps")
   int getCurrentFps();

   @Mutable
   @Accessor("session")
   void setSession(Session var1);

   @Accessor("networkProxy")
   Proxy getProxy();

   @Accessor("itemUseCooldown")
   void setItemUseCooldown(int var1);

   @Accessor("itemUseCooldown")
   int getItemUseCooldown();

   @Invoker
   boolean callDoAttack();

   @Invoker("doItemUse")
   void callDoItemUse();

   @Mutable
   @Accessor("profileKeys")
   void setProfileKeys(ProfileKeys var1);

   @Mutable
   @Accessor
   void setUserApiService(UserApiService var1);

   @Invoker("createUserApiService")
   UserApiService callCreateUserApiService(YggdrasilAuthenticationService var1, RunArgs var2);

   @Accessor("authenticationService")
   YggdrasilAuthenticationService getAuthenticationService();

   @Mutable
   @Accessor("socialInteractionsManager")
   void setSocialInteractionsManager(SocialInteractionsManager var1);

   @Accessor("abuseReportContext")
   void setAbuseReportContext(AbuseReportContext var1);

   @Mutable
   @Accessor("gameProfileFuture")
   void setGameProfileFuture(CompletableFuture<ProfileResult> var1);

   @Mutable
   @Accessor
   void setAuthenticationService(YggdrasilAuthenticationService var1);

   @Mutable
   @Accessor
   void setSessionService(MinecraftSessionService var1);

   @Mutable
   @Accessor
   void setSkinProvider(PlayerSkinProvider var1);
}
