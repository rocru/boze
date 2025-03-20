package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.PreferredWeapon;
import dev.boze.client.events.PostAttackEntityEvent;
import dev.boze.client.events.PreAttackEntityEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;

public class AutoWeapon extends Module {
   public static final AutoWeapon INSTANCE = new AutoWeapon();
   private final BooleanSetting silentSwap = new BooleanSetting("Silent", false, "Silent swap");
   private final EnumSetting<PreferredWeapon> weapon = new EnumSetting<PreferredWeapon>("Weapon", PreferredWeapon.Sword, "Preferred weapon");
   private final BooleanSetting preserve = new BooleanSetting("Preserve", false, "Don't equip weapons if they're about to break");
   private int field2548 = -1;

   public AutoWeapon() {
      super("AutoWeapon", "Automatically swaps to a weapon when you attack something", Category.Combat);
   }

   @EventHandler
   private void method1490(PreAttackEntityEvent var1) {
      if (var1.entity == null || !Friends.method2055(var1.entity)) {
         int var5 = this.method1492();
         if (var5 != mc.player.getInventory().selectedSlot && var5 != -1) {
            if (this.silentSwap.getValue()) {
               this.field2548 = mc.player.getInventory().selectedSlot;
            }

            mc.player.getInventory().selectedSlot = var5;
            ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
         }
      }
   }

   @EventHandler
   private void method1491(PostAttackEntityEvent var1) {
      if (this.field2548 != mc.player.getInventory().selectedSlot && this.field2548 != -1) {
         mc.player.getInventory().selectedSlot = this.field2548;
         this.field2548 = -1;
      }
   }

   private int method1492() {
      int var4 = mc.player.getInventory().selectedSlot;
      double var5 = 0.0;
      if (this.weapon.getValue() == PreferredWeapon.Sword) {
         for (int var11 = 0; var11 < 9; var11++) {
            if (mc.player.getInventory().getStack(var11).getItem() instanceof SwordItem
               && (
                  !this.preserve.getValue()
                     || mc.player.getInventory().getStack(var11).getMaxDamage() - mc.player.getInventory().getStack(var11).getDamage() > 15
               )) {
               double var10 = (double)(((SwordItem)mc.player.getInventory().getStack(var11).getItem()).getMaterial().getAttackDamage() + 2.0F);
               if (var10 > var5) {
                  var5 = var10;
                  var4 = var11;
               }
            }
         }

         return var4;
      } else {
         if (this.weapon.getValue() == PreferredWeapon.Axe) {
            for (int var9 = 0; var9 < 9; var9++) {
               if (mc.player.getInventory().getStack(var9).getItem() instanceof AxeItem
                  && (
                     !this.preserve.getValue()
                        || mc.player.getInventory().getStack(var9).getMaxDamage() - mc.player.getInventory().getStack(var9).getDamage() > 15
                  )) {
                  double var7 = (double)(((AxeItem)mc.player.getInventory().getStack(var9).getItem()).getMaterial().getAttackDamage() + 2.0F);
                  if (var7 > var5) {
                     var5 = var7;
                     var4 = var9;
                  }
               }
            }
         }

         return var4;
      }
   }
}
