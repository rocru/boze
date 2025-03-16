package dev.boze.client.utils;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.mixin.PlayerInteractEntityC2SPacketAccessor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import mapped.Class1204;
import mapped.Class27;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractType;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public class TargetTracker implements IMinecraft {
   private static final Map<Entity, Long> field1358 = new ConcurrentHashMap();
   public static HashMap<String, Integer> field1359 = new HashMap();

   public static void method488(Entity var0) {
      field1358.put(var0, System.currentTimeMillis());
   }

   public static void method594(Entity var0) {
      field1358.remove(var0);
   }

   public static Set<Entity> method560() {
      return field1358.keySet();
   }

   public static boolean method2055(Entity var0) {
      return field1358.containsKey(var0);
   }

   public static long method595(String var0) {
      for (Entity var5 : method560()) {
         if (var5.getName().getString().equalsIgnoreCase(var0)) {
            return (Long)field1358.get(var5);
         }
      }

      return -1L;
   }

   public static PlayerEntity method1520() {
      long var3 = 0L;
      PlayerEntity var5 = null;

      for (Entity var7 : method560()) {
         if (var7 instanceof PlayerEntity && (Long)field1358.get(var7) > var3) {
            var3 = (Long)field1358.get(var7);
            var5 = (PlayerEntity)var7;
         }
      }

      return var5;
   }

   @EventHandler
   public static void method1831(PrePlayerTickEvent var0) {
      method2142();

      for (PlayerEntity var5 : MinecraftClient.getInstance().world.getPlayers()) {
         if ((var5.getHealth() <= 0.0F || var5.isDead()) && field1359.containsKey(var5.getNameForScoreboard())) {
            field1359.remove(var5.getNameForScoreboard());
         }
      }
   }

   public static void method2142() {
      field1358.forEach(TargetTracker::lambda$refreshTargets$0);
   }

   @EventHandler
   public static void method2042(PacketBundleEvent var0) {
      if (MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().player != null) {
         if (var0.packet instanceof EntityStatusS2CPacket var4 && var4.getStatus() == 35) {
            Entity var5 = var4.getEntity(MinecraftClient.getInstance().world);
            if (var5 == null) {
               return;
            }

            if (field1359 == null) {
               field1359 = new HashMap();
            }

            try {
               if (field1359.get(var5.getNameForScoreboard()) == null) {
                  field1359.put(var5.getNameForScoreboard(), 1);
               } else if (field1359.get(var5.getNameForScoreboard()) != null) {
                  field1359.put(var5.getNameForScoreboard(), (Integer)field1359.get(var5.getNameForScoreboard()) + 1);
               }

               Class1204 var6 = new Class1204(var5, (Integer)field1359.get(var5.getNameForScoreboard()));
               Class27.EVENT_BUS.post(var6);
            } catch (NullPointerException var7) {
            }
         }
      }
   }

   @EventHandler
   public static void method1853(PrePacketSendEvent var0) {
      if (MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().player != null) {
         if (var0.packet instanceof PlayerInteractEntityC2SPacket var4
            && MinecraftUtils.isClientActive()
            && ((PlayerInteractEntityC2SPacketAccessor)var0.packet).getType().getType() == InteractType.ATTACK) {
            Entity var6 = mc.world.getEntityById(((PlayerInteractEntityC2SPacketAccessor)var4).getEntityId());
            if (var6 instanceof LivingEntity) {
               method488(var6);
            }
         }
      }
   }

   private static void lambda$refreshTargets$0(Entity var0, Long var1) {
      if (System.currentTimeMillis() - var1 > TimeUnit.SECONDS.toMillis(30L)) {
         field1358.remove(var0);
      }
   }
}
