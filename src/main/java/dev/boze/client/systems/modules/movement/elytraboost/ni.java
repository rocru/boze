package dev.boze.client.systems.modules.movement.elytraboost;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.Timer;
import dev.boze.client.Boze;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

class ni {
   private final Timer field3215;
   final nh field3216;

   private ni(nh var1) {
      this.field3216 = var1;
      this.field3215 = new Timer();
   }

   @EventHandler
   public void method1824(PreTickEvent event) {
      if (this.field3215.hasElapsed(2500.0) && !this.field3215.hasElapsed(10000.0)) {
         Boze.EVENT_BUS.unsubscribe(this);
      }
   }

   @EventHandler
   public void method1825(PacketBundleEvent event) {
      if (event.packet instanceof PlaySoundS2CPacket var5 && this.field3216.field3209 && (IMinecraft.mc.player.isFallFlying() || this.field3216.field3210)) {
         SoundEvent var10 = (SoundEvent)var5.getSound().value();
         if (var10 == SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA
            || var10 == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC
            || var10 == SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND
            || var10 == SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE) {
            event.method1020();
         }

         return;
      }

      if (event.packet instanceof PlaySoundFromEntityS2CPacket var6
         && this.field3216.field3209
         && (IMinecraft.mc.player.isFallFlying() || this.field3216.field3210)) {
         SoundEvent var9 = (SoundEvent)var6.getSound().value();
         if (var9 == SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA
            || var9 == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC
            || var9 == SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND
            || var9 == SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE) {
            event.method1020();
         }
      }
   }
}
