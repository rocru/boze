package dev.boze.client.systems.modules.movement;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.mixin.FireworkRocketEntityAccessor;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoFirework extends Module {
   public static final AutoFirework INSTANCE = new AutoFirework();
   private final FloatSetting field3145 = new FloatSetting("MinDelay", 1.0F, 1.0F, 60.0F, 0.5F, "Minimum delay between firing fireworks (in seconds)");
   private final Timer field3146 = new Timer();

   public AutoFirework() {
      super("AutoFirework", "Automatically fires fireworks", Category.Movement);
   }

   @EventHandler
   public void method1787(MovementEvent event) {
      if (this.field3146.hasElapsed((double)(this.field3145.method423() * 1000.0F))) {
         if (mc.player.isFallFlying()) {
            for (Entity var6 : mc.world.getEntities()) {
               if (var6 instanceof FireworkRocketEntity
                  && (
                     ((FireworkRocketEntityAccessor)var6).getShooter() != null && ((FireworkRocketEntityAccessor)var6).getShooter().equals(mc.player)
                        || var6.distanceTo(mc.player) < 3.0F
                  )) {
                  return;
               }
            }

            if (mc.player.getOffHandStack().getItem() == Items.FIREWORK_ROCKET) {
               mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
               this.field3146.reset();
            } else if (mc.player.getMainHandStack().getItem() == Items.FIREWORK_ROCKET) {
               mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
               this.field3146.reset();
            } else {
               for (int var7 = 0; var7 < 9; var7++) {
                  if (mc.player.getInventory().getStack(var7).getItem() == Items.FIREWORK_ROCKET) {
                     int var8 = mc.player.getInventory().selectedSlot;
                     mc.player.getInventory().selectedSlot = var7;
                     mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                     mc.player.getInventory().selectedSlot = var8;
                     this.field3146.reset();
                     break;
                  }
               }
            }
         }
      }
   }
}
