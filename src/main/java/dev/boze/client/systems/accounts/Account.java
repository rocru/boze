package dev.boze.client.systems.accounts;

import com.google.gson.JsonObject;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import dev.boze.client.mixin.FileCacheAccessor;
import dev.boze.client.mixin.MinecraftClientAccessor;
import dev.boze.client.mixin.PlayerSkinProviderAccessor;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.misc.IJsonSerializable;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.session.ProfileKeys;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.report.AbuseReportContext;
import net.minecraft.client.session.report.ReporterEnvironment;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.texture.PlayerSkinProvider.FileCache;
import net.minecraft.network.encryption.SignatureVerifier;
import net.minecraft.util.Util;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public abstract class Account<T extends Account<?>> implements IJsonSerializable<T>, IMinecraft {
    protected AccountType field1520;
    protected String field1521;
    protected final AccountCache field1522;

    protected Account(AccountType type, String name) {
        this.field1520 = type;
        this.field1521 = name;
        this.field1522 = new AccountCache();
    }

    public abstract boolean method2114();

    public boolean method2115() {
        YggdrasilAuthenticationService var1 = new YggdrasilAuthenticationService(((MinecraftClientAccessor) mc).getProxy());
        method676(var1, var1.createMinecraftSessionService());
        return true;
    }

    public String method210() {
        return this.field1522.field1280.isEmpty() ? this.field1521 : this.field1522.field1280;
    }

    public AccountType method673() {
        return this.field1520;
    }

    public AccountCache method674() {
        return this.field1522;
    }

    public static void method675(Session session) {
        MinecraftClientAccessor var4 = (MinecraftClientAccessor) mc;
        var4.setSession(session);
        UserApiService var5 = var4.getAuthenticationService().createUserApiService(session.getAccessToken());
        var4.setUserApiService(var5);
        var4.setSocialInteractionsManager(new SocialInteractionsManager(mc, var5));
        var4.setProfileKeys(ProfileKeys.create(var5, session, mc.runDirectory.toPath()));
        var4.setAbuseReportContext(AbuseReportContext.create(ReporterEnvironment.ofIntegratedServer(), var5));
        var4.setGameProfileFuture(CompletableFuture.supplyAsync(Account::lambda$setSession$0, Util.getIoWorkerExecutor()));
    }

    public static void method676(YggdrasilAuthenticationService authService, MinecraftSessionService sessService) {
        MinecraftClientAccessor var5 = (MinecraftClientAccessor) mc;
        var5.setAuthenticationService(authService);
        SignatureVerifier.create(authService.getServicesKeySet(), ServicesKeyType.PROFILE_KEY);
        var5.setSessionService(sessService);
        FileCache var6 = ((PlayerSkinProviderAccessor) mc.getSkinProvider()).getSkinCache();
        Path var7 = ((FileCacheAccessor) var6).getDirectory();
        var5.setSkinProvider(new PlayerSkinProvider(mc.getTextureManager(), var7, sessService, mc));
    }

    @Override
    public JsonObject serialize() {
        JsonObject var3 = new JsonObject();
        var3.addProperty("type", this.field1520.name());
        var3.addProperty("name", this.field1521);
        var3.add("data", this.field1522.serialize());
        return var3;
    }

    @Override
    public T deserialize(JsonObject object) {
        if (object.has("name") && object.has("data")) {
            this.field1521 = object.get("name").getAsString();
            this.field1522.deserialize(object.getAsJsonObject("data"));
            return (T) this;
        } else {
            this.field1521 = "INVALID";
            return (T) this;
        }
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    ////public Object deserialize(JsonObject jsonObject) {
    //   return this.method677(jsonObject);
    //}

    private static ProfileResult lambda$setSession$0() {
        return mc.getSessionService().fetchProfile(mc.getSession().getUuidOrNull(), true);
    }
}
