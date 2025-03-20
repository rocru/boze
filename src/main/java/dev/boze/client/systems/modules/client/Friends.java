package dev.boze.client.systems.modules.client;

import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.settings.FriendsSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.legit.Teams;
import dev.boze.client.systems.modules.misc.ExtraChat;
import java.util.ArrayList;
import mapped.Class3063;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class Friends extends Module {
   public static final Friends INSTANCE = new Friends();
   private FriendsSetting field683 = new FriendsSetting("Friends", new ArrayList(), "Friended players");

   public Friends() {
      super("Friends", "Friend system", Category.Client);
      this.setEnabled(true);
   }

   public static boolean addFriend(String name) {
      if (!INSTANCE.field683.getValue().contains(new Class3063(name))) {
         INSTANCE.field683.getValue().add(new Class3063(name));
         if (ExtraChat.INSTANCE.isEnabled() && ExtraChat.INSTANCE.method1699() && ExtraChat.INSTANCE.field2941.getValue()) {
            ChatInstance.method1800("/msg " + name + " I just friended you!");
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean method343(Class3063 friend) {
      if (!INSTANCE.field683.getValue().contains(friend)) {
         INSTANCE.field683.getValue().add(friend);
         if (ExtraChat.INSTANCE.isEnabled() && ExtraChat.INSTANCE.method1699() && ExtraChat.INSTANCE.field2941.getValue()) {
            ChatInstance.method1800("/msg " + friend.method5992() + " I just friended you!");
         }

         return true;
      } else {
         return false;
      }
   }

   public static void method1750(String name) {
      INSTANCE.field683.getValue().removeIf(Friends::lambda$delFriend$0);
   }

   public static boolean method345(Class3063 friend) {
      return INSTANCE.field683.getValue().remove(friend);
   }

   public static void method1904() {
      INSTANCE.field683.getValue().clear();
   }

   public static boolean method2055(Entity entity) {
      if (!(entity instanceof PlayerEntity)) {
         return false;
      } else if (Teams.INSTANCE.isEnabled() && Teams.method1617((PlayerEntity)entity)) {
         return true;
      } else if (!INSTANCE.isEnabled()) {
         return false;
      } else {
         String var4 = entity.getName().getString();

         for (Class3063 var6 : INSTANCE.field683.getValue()) {
            if (var6.method5992().equalsIgnoreCase(var4)) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean method346(String name) {
      if (name != null && INSTANCE.isEnabled()) {
         for (Class3063 var5 : INSTANCE.field683.getValue()) {
            if (var5.method5992().equalsIgnoreCase(name)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static ArrayList<Class3063> method2120() {
      return INSTANCE.field683.getValue();
   }

   private static boolean lambda$delFriend$0(String var0, Class3063 var1) {
      return var1.method5992().equalsIgnoreCase(var0);
   }
}
