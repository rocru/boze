package dev.boze.client.systems.modules.combat;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelfBow extends Module {
   public static final SelfBow INSTANCE = new SelfBow();
   private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "Shoot yourself even if you're already using an item");
   private final MinMaxSetting progress = new MinMaxSetting("Progress", 0.2, 0.1, 0.5, 0.01, "Min progress to shoot yourself");
   private final BooleanSetting swiftness = new BooleanSetting("Swiftness", true, "Shoot yourself with swiftness arrows");
   private final BooleanSetting strength = new BooleanSetting("Strength", true, "Shoot yourself with strength arrows");
   private final BooleanSetting regeneration = new BooleanSetting("Regeneration", true, "Shoot yourself with regeneration arrows");
   private final BooleanSetting ignoreEffects = new BooleanSetting("IgnoreEffects", false, "Shoot yourself even if you already have the effect");
   private final List<Integer> field2563 = new ArrayList();
   private int field2564;

   public SelfBow() {
      super("SelfBow", "Shoot yourself", Category.Combat);
   }

   @Override
   public void onEnable() {
      if (MinecraftUtils.isClientActive() && !Options.method477(this.multiTask.method419())) {
         this.field2564 = -1;
         int var4 = this.method1524();
         if (var4 == -1) {
            NotificationManager.method1151(new Notification(this.getName(), "Bow not found", Notifications.WARNING, NotificationPriority.Yellow));
            this.setEnabled(false);
         } else {
            mc.options.useKey.setPressed(false);
            mc.interactionManager.stopUsingItem(mc.player);
            if (mc.player.getInventory().selectedSlot != var4) {
               this.field2564 = mc.player.getInventory().selectedSlot;
               mc.player.getInventory().selectedSlot = var4;
               ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
            }

            this.field2563.clear();
            ArrayList var5 = new ArrayList();

            for (int var6 = mc.player.getInventory().size(); var6 > 0; var6--) {
               if (var6 != mc.player.getInventory().selectedSlot) {
                  ItemStack var7 = mc.player.getInventory().getStack(var6);
                  if (var7.getItem() == Items.TIPPED_ARROW) {
                     Iterator var8 = ((PotionContentsComponent)var7.get(DataComponentTypes.POTION_CONTENTS)).getEffects().iterator();
                     if (var8.hasNext()) {
                        RegistryEntry var9 = ((StatusEffectInstance)var8.next()).getEffectType();
                        if (this.method1521(var9) && !var5.contains(var9) && (!this.method1522(var9) || this.ignoreEffects.method419())) {
                           var5.add(var9);
                           this.field2563.add(var6);
                        }
                     }
                  }
               }
            }
         }
      } else {
         this.setEnabled(false);
      }
   }

   private boolean method1521(RegistryEntry<StatusEffect> var1) {
      if (var1 == StatusEffects.SPEED) {
         return this.swiftness.method419();
      } else if (var1 == StatusEffects.STRENGTH) {
         return this.strength.method419();
      } else {
         return var1 == StatusEffects.REGENERATION ? this.regeneration.method419() : false;
      }
   }

   private boolean method1522(RegistryEntry<StatusEffect> var1) {
      for (StatusEffectInstance var6 : mc.player.getStatusEffects()) {
         if (var6.getEffectType() == var1) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onDisable() {
      if (MinecraftUtils.isClientActive() && this.field2564 != -1) {
         mc.player.getInventory().selectedSlot = this.field2564;
      }
   }

   @EventHandler(
      priority = 68
   )
   private void method1523(MovementEvent var1) {
      if (!this.field2563.isEmpty() && mc.player.getInventory().getMainHandStack().getItem() == Items.BOW) {
         boolean var5 = mc.options.useKey.isPressed();
         if (!var5) {
            InvUtils.method2201().method2207((Integer)this.field2563.get(0)).method2213(9);
            mc.options.useKey.setPressed(true);
         } else if ((double)BowItem.getPullProgress(mc.player.getItemUseTime()) >= this.progress.getValue()
            && var1.field1930 == ((ClientPlayerEntityAccessor)mc.player).getLastX()
            && var1.field1932 == ((ClientPlayerEntityAccessor)mc.player).getLastZ()) {
            var1.method1074(new ActionWrapper(this::lambda$onSendMovementPackets$0, mc.player.getYaw(), -90.0F));
         }
      } else {
         this.setEnabled(false);
      }
   }

   public int method1524() {
      int var4 = -1;
      if (mc.player.getMainHandStack().getItem() == Items.BOW) {
         var4 = mc.player.getInventory().selectedSlot;
      }

      if (var4 == -1) {
         for (int var5 = 0; var5 < 9; var5++) {
            if (mc.player.getInventory().getStack(var5).getItem() instanceof BowItem) {
               var4 = var5;
               break;
            }
         }
      }

      return var4;
   }

   private void lambda$onSendMovementPackets$0() {
      int var3 = (Integer)this.field2563.get(0);
      this.field2563.remove(0);
      mc.options.useKey.setPressed(false);
      mc.interactionManager.stopUsingItem(mc.player);
      if (var3 != 9) {
         InvUtils.method2201().method2207(9).method2213(var3);
      }
   }
}
