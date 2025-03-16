package de.florianmichael.waybackauthlib;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.properties.Property;
import java.net.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import mapped.Class5930;
import mapped.Class5931;
import mapped.Class5932;
import mapped.Class5933;
import mapped.Class5934;
import mapped.Class5936;

public class WaybackAuthLib {
   public static final String field1815 = "https://authserver.mojang.com/";
   private static final String field1816 = "authenticate";
   private static final String field1817 = "refresh";
   private static final String field1818 = "invalidate";
   private static final String field1819 = "validate";
   private final URI field1820;
   private final String field1821;
   private final MinecraftClient field1822;
   private String field1823;
   private String field1824;
   private String field1825;
   private String field1826;
   private boolean field1827;
   private GameProfile field1828;
   private List<Property> field1829 = new ArrayList();
   private List<GameProfile> field1830 = new ArrayList();

   public WaybackAuthLib() {
      this("https://authserver.mojang.com/", "");
   }

   public WaybackAuthLib(String authHost) {
      this(authHost, "");
   }

   public WaybackAuthLib(String authHost, String clientToken) {
      this(authHost, clientToken, Proxy.NO_PROXY);
   }

   public WaybackAuthLib(String authHost, String clientToken, Proxy proxy) {
      if (authHost == null) {
         throw new IllegalArgumentException("Authentication is null");
      } else {
         if (!authHost.endsWith("/")) {
            authHost = authHost + "/";
         }

         this.field1820 = URI.create(authHost);
         if (clientToken == null) {
            throw new IllegalArgumentException("ClientToken is null");
         } else {
            this.field1821 = clientToken;
            this.field1822 = MinecraftClient.unauthenticated((Proxy)Objects.requireNonNullElse(proxy, Proxy.NO_PROXY));
         }
      }
   }

   public void method924() throws Exception {
      if (this.field1823 != null && !this.field1823.isEmpty()) {
         boolean var4 = this.field1825 != null && !this.field1825.isEmpty();
         boolean var5 = this.field1824 != null && !this.field1824.isEmpty();
         if (!var4 && !var5) {
            throw new InvalidCredentialsException("Invalid password or access token.");
         } else {
            Class5934 var6;
            if (var4) {
               var6 = (Class5934)this.field1822
                  .post(this.field1820.resolve("refresh").toURL(), new Class5932(this.field1821, this.field1825, null), Class5934.class);
            } else {
               var6 = (Class5934)this.field1822
                  .post(
                     this.field1820.resolve("authenticate").toURL(),
                     new Class5930(Agent.field1, this.field1823, this.field1824, this.field1821),
                     Class5934.class
                  );
            }

            if (var6 == null) {
               throw new InvalidRequestException("Server didn't sent a response.");
            } else if (!var6.field236.equals(this.field1821)) {
               throw new InvalidRequestException("Server token and provided token doesn't match.");
            } else {
               if (var6.field239 != null && var6.field239.field240 != null) {
                  this.field1826 = var6.field239.field240;
               } else {
                  this.field1826 = this.method927();
               }

               this.field1825 = var6.field235;
               this.field1830 = var6.field238 != null ? Arrays.asList(var6.field238) : Collections.emptyList();
               this.field1828 = var6.field237;
               this.field1829.clear();
               if (var6.field239 != null && var6.field239.field241 != null) {
                  this.field1829.addAll(var6.field239.field241);
               }

               this.field1827 = true;
            }
         }
      } else {
         throw new InvalidCredentialsException("Invalid username.");
      }
   }

   public boolean method925() {
      Class5936 var3 = new Class5936(this.field1825, this.field1821);

      try {
         this.field1822.post(this.field1820.resolve("validate").toURL(), var3, Class5933.class);
         return true;
      } catch (Exception var5) {
         return false;
      }
   }

   public void method926() throws Exception {
      Class5931 var4 = new Class5931(this.field1821, this.field1825);
      Class5933 var5 = (Class5933)this.field1822.post(this.field1820.resolve("invalidate").toURL(), var4, Class5933.class);
      if (!this.field1827) {
         throw new IllegalStateException("Cannot log out while not logged in.");
      } else if (var5 != null && var5.field232 != null && !var5.field232.isEmpty()) {
         throw new InvalidRequestException(var5.field232, var5.field233, var5.field234);
      } else {
         this.field1825 = null;
         this.field1827 = false;
         this.field1828 = null;
         this.field1829 = new ArrayList();
         this.field1830 = new ArrayList();
      }
   }

   public String method927() {
      return this.field1823;
   }

   public void method928(String username) {
      if (this.field1827 && this.field1828 != null) {
         throw new IllegalStateException("Cannot change username whilst logged in & online");
      } else {
         this.field1823 = username;
      }
   }

   public String method929() {
      return this.field1824;
   }

   public void method930(String password) {
      if (this.field1827 && this.field1828 != null) {
         throw new IllegalStateException("Cannot set password whilst logged in & online");
      } else {
         this.field1824 = password;
      }
   }

   public String method931() {
      return this.field1825;
   }

   public void method932(String accessToken) {
      if (this.field1827 && this.field1828 != null) {
         throw new IllegalStateException("Cannot set access token whilst logged in & online");
      } else {
         this.field1825 = accessToken;
      }
   }

   public String method933() {
      return this.field1826;
   }

   public boolean method934() {
      return this.field1827;
   }

   public GameProfile method935() {
      return this.field1828;
   }

   public List<Property> method936() {
      return this.field1829;
   }

   public List<GameProfile> method937() {
      return this.field1830;
   }

   public String toString() {
      List var3 = this.field1830;
      List var4 = this.field1829;
      GameProfile var5 = this.field1828;
      boolean var6 = this.field1827;
      String var7 = this.field1826;
      String var8 = this.field1825;
      String var9 = this.field1824;
      String var10 = this.field1823;
      MinecraftClient var11 = this.field1822;
      String var12 = this.field1821;
      URI var13 = this.field1820;
      return "WaybackAuthLib{baseURI="
         + var13
         + ", clientToken='"
         + var12
         + "', client="
         + var11
         + ", username='"
         + var10
         + "', password='"
         + var9
         + "', accessToken='"
         + var8
         + "', userId='"
         + var7
         + "', loggedIn="
         + var6
         + ", currentProfile="
         + var5
         + ", properties="
         + var4
         + ", profiles="
         + var3
         + "}";
   }
}
