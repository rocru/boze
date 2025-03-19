package dev.boze.client.systems.accounts.types;

import com.mojang.authlib.Environment;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import de.florianmichael.waybackauthlib.WaybackAuthLib;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixin.MinecraftClientAccessor;
import dev.boze.client.mixin.YggdrasilMinecraftSessionServiceAccessor;
import dev.boze.client.systems.accounts.Account;
import dev.boze.client.systems.accounts.AccountType;
import net.minecraft.client.session.Session;

import java.util.Optional;

public class TheAlteningAccount extends Account<TheAlteningAccount> {
   private static final Environment field2298 = new Environment("http://sessionserver.thealtening.com", "http://authserver.thealtening.com", "The Altening");
   private static final YggdrasilAuthenticationService field2299 = new YggdrasilAuthenticationService(((MinecraftClientAccessor)mc).getProxy(), field2298);

   public TheAlteningAccount(String token) {
      super(AccountType.Altening, token);
   }

   @Override
   public boolean method2114() {
      WaybackAuthLib var1 = this.method1323();

      try {
         var1.method924();
         this.field1522.field1280 = var1.method935().getName();
         this.field1522.field1281 = var1.method935().getId().toString();
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   @Override
   public boolean method2115() {
      method676(
         field2299,
         YggdrasilMinecraftSessionServiceAccessor.initYggdrasilMinecraftSessionService(field2299.getServicesKeySet(), field2299.getProxy(), field2298)
      );
      WaybackAuthLib var3 = this.method1323();

      try {
         var3.method924();
         method675(
            new Session(
               var3.method935().getName(),
               var3.method935().getId(),
               var3.method931(),
               Optional.empty(),
               Optional.empty(),
               net.minecraft.client.session.Session.AccountType.MOJANG
            )
         );
         this.field1522.field1280 = var3.method935().getName();
         this.field1522.method2142();
         return true;
      } catch (Exception var5) {
         ChatInstance.method626("Error while logging in with TheAltening");
         return false;
      }
   }

   private WaybackAuthLib method1323() {
      WaybackAuthLib var3 = new WaybackAuthLib(field2298.servicesHost());
      var3.method928(this.field1521);
      var3.method930("password");
      return var3;
   }
}
