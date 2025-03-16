package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.ThrowPotMode;
import dev.boze.client.enums.ThrowPotStage;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.KeyPressedEvent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.MinecraftUtils;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;

public class ThrowPot extends Module {
   public static final ThrowPot INSTANCE = new ThrowPot();
   private final EnumSetting<ThrowPotMode> field2830 = new EnumSetting<ThrowPotMode>("Mode", ThrowPotMode.Buff, "What type of potions to throw");
   private ThrowPotStage field2831;
   private int field2832;

   public ThrowPot() {
      super("ThrowPot", "Auto swaps and throws a potion", Category.Legit);
   }

   @Override
   public void onEnable() {
      this.field2831 = ThrowPotStage.Throw;
      this.field2832 = -1;
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      }
   }

   @EventHandler
   public void method1618(ACRotationEvent event) {
      if (this.field2831 == ThrowPotStage.SwapBack && event.method1017() == AnticheatMode.Grim && this.field2832 != -1) {
         mc.player.getInventory().selectedSlot = this.field2832;
         this.setEnabled(false);
      }
   }

   @EventHandler
   public void method1619(KeyPressedEvent event) {
      if (event.method1067().equals(mc.options.useKey) && this.field2831 == ThrowPotStage.Throw) {
         int var5 = InventoryHelper.method168(this::lambda$onKeyBindingIsPressed$0);
         if (var5 == -1) {
            this.setEnabled(false);
            return;
         }

         this.field2832 = mc.player.getInventory().selectedSlot;
         mc.player.getInventory().selectedSlot = var5;
         event.method1070(true);
         this.field2831 = ThrowPotStage.SwapBack;
      }
   }

   private boolean lambda$onKeyBindingIsPressed$0(ItemStack var1) {
      if (!(var1.getItem() instanceof SplashPotionItem)) {
         return false;
      } else {
         Iterator var5 = ((PotionContentsComponent)var1.get(DataComponentTypes.POTION_CONTENTS)).getEffects().iterator();
         if (var5 != null && var5.hasNext()) {
            while (var5.hasNext()) {
               StatusEffectInstance var6 = (StatusEffectInstance)var5.next();
               if (((StatusEffect)var6.getEffectType().value()).isBeneficial() && this.field2830.method461() == ThrowPotMode.DeBuff) {
                  return false;
               }

               if (!((StatusEffect)var6.getEffectType().value()).isBeneficial() && this.field2830.method461() == ThrowPotMode.Buff) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
