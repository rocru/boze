package dev.boze.client.systems.modules.misc;

import baritone.api.BaritoneAPI;
import dev.boze.client.events.CrosshairEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.FoodUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoEat extends Module {
   public static final AutoEat INSTANCE = new AutoEat();
   public FloatSetting delay = new FloatSetting("Delay", 5.0F, 0.0F, 10.0F, 1.0F, "Delay");
   public BooleanSetting preferGap = new BooleanSetting("PreferGaps", true, "Prefer golden apples");
   public BooleanSetting eatChorus = new BooleanSetting("EatChorus", false, "Eat chorus");
   public FloatSetting health = new FloatSetting("Health", 15.0F, 0.0F, 19.0F, 1.0F, "Health");
   public FloatSetting hunger = new FloatSetting("Hunger", 19.0F, 0.0F, 19.0F, 1.0F, "Hunger");
   public BooleanSetting autoSwap = new BooleanSetting("AutoSwap", true, "Automatic swapping");
   public FloatSetting swapDelay = new FloatSetting("SwapDelay", 5.0F, 0.0F, 10.0F, 1.0F, "Swap Delay", this.autoSwap);
   private BooleanSetting pauseBaritone = new BooleanSetting("PauseBaritone", false, "Pause baritone while eating");
   private BooleanSetting pauseAura = new BooleanSetting("PauseAura", false, "Pause aura while eating");
   private dev.boze.client.utils.Timer timer = new dev.boze.client.utils.Timer();
   private dev.boze.client.utils.Timer timer = new dev.boze.client.utils.Timer();
   public boolean field2887;
   private boolean field2888;
   private boolean field2889;

   public AutoEat() {
      super("AutoEat", "Automatically eats food when you're hungry", Category.Misc);
      this.timer = new dev.boze.client.utils.Timer();
      this.field2887 = false;
      this.field2888 = false;
      this.field2889 = false;
   }

   @EventHandler
   public void method1661(PrePlayerTickEvent event) {
      if (this.timer.hasElapsed((double)(this.delay.getValue() * 100.0F)) && this.timer.hasElapsed((double)(this.swapDelay.getValue() * 100.0F))) {
         if (!mc.player.isCreative()) {
            if (this.method1664()) {
               this.field2887 = true;
               if (this.autoSwap.getValue() && !FoodUtil.isFood(mc.player.getMainHandStack()) && !FoodUtil.isFood(mc.player.getOffHandStack())) {
                  int var5 = this.method1665();
                  if (var5 != -1) {
                     mc.player.getInventory().selectedSlot = var5;
                     ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
                     this.timer.reset();
                     this.field2887 = false;
                     return;
                  }
               }

               if (FoodUtil.isFood(mc.player.getMainHandStack())) {
                  if ((mc.currentScreen == null || mc.currentScreen instanceof InventoryScreen) && mc.isWindowFocused()) {
                     KeyBinding.setKeyPressed(mc.options.useKey.getDefaultKey(), true);
                  } else {
                     mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                  }
               } else {
                  if (!FoodUtil.isFood(mc.player.getOffHandStack())) {
                     this.field2887 = false;
                     return;
                  }

                  if ((mc.currentScreen == null || mc.currentScreen instanceof InventoryScreen) && mc.isWindowFocused()) {
                     KeyBinding.setKeyPressed(mc.options.useKey.getDefaultKey(), true);
                  } else {
                     mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
                  }
               }

               if (this.pauseBaritone.getValue() && BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing() && !this.field2889) {
                  BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
                  this.field2889 = true;
               }

               this.field2888 = true;
            } else {
               this.field2887 = false;
               if (this.field2888) {
                  this.timer.reset();
                  KeyBinding.setKeyPressed(
                     mc.options.useKey.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.useKey.getDefaultKey().getCode())
                  );
                  this.field2888 = false;
                  if (this.field2889) {
                     try {
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
                     } catch (Exception var6) {
                     }

                     this.field2889 = false;
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void method1662(CrosshairEvent event) {
      if (this.field2887) {
         event.method1020();
      }
   }

   public static boolean method1663() {
      return INSTANCE.isEnabled() && INSTANCE.pauseAura.getValue() && INSTANCE.method1664() && INSTANCE.method1665() != -1;
   }

   public boolean method1664() {
      return mc.player.getHealth() + mc.player.getAbsorptionAmount() <= this.health.getValue()
         || (float)mc.player.getHungerManager().getFoodLevel() <= this.hunger.getValue();
   }

   private int method1665() {
      int var4 = -1;
      float var5 = 0.0F;

      for (int var6 = 0; var6 < 9; var6++) {
         ItemStack var7 = mc.player.getInventory().getStack(var6);
         if (FoodUtil.isFood(var7) && (var7.getItem() != Items.CHORUS_FRUIT || this.eatChorus.getValue())) {
            if (this.preferGap.getValue() && var7.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
               var4 = var6;
               break;
            }

            float var8 = FoodUtil.method2152(var7);
            if (var8 > var5) {
               var5 = var8;
               var4 = var6;
            }
         }
      }

      return var4;
   }
}
