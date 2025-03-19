package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.AutoToolMode;
import dev.boze.client.enums.AutoToolPrefer;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.HandleInputEvent;
import dev.boze.client.events.PreBlockBreakEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.ItemEnchantmentUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;

public class AutoTool extends Module {
   public static final AutoTool INSTANCE = new AutoTool();
   private final EnumSetting<AutoToolMode> field2898 = new EnumSetting<AutoToolMode>("Mode", AutoToolMode.Anarchy, "Mode for AutoTool", AutoTool::lambda$new$0);
   private final BooleanSetting field2899 = new BooleanSetting("SwapBack", false, "Swap back to previous item after breaking block", this::method1678);
   private final IntArraySetting field2900 = new IntArraySetting("SwapDelay", new int[]{1, 3}, 0, 20, 1, "Min delay in ticks between swaps", this::method1678);
   private final EnumSetting<SwapMode> field2901 = new EnumSetting<SwapMode>(
      "Swap",
      SwapMode.Normal,
      "Mode for swapping to tool\nHot-bar only modes:\n - Normal: Vanilla swap\n - Silent: Instantaneously swap to tool and back\nWhole inventory modes (you don't need to keep the tool in your hot-bar):\n - Alt: Alternative silent swap mode, may work where mode silent is patched\nNote: Whole inventory modes may not work on some servers\n",
      this::method1679
   );
   private final EnumSetting<AutoToolPrefer> field2902 = new EnumSetting<AutoToolPrefer>("Prefer", AutoToolPrefer.Both, "Preferred tool");
   private final EnumSetting<AutoToolPrefer> field2903 = new EnumSetting<AutoToolPrefer>("PreferOres", AutoToolPrefer.Fortune, "Preferred tool for ores");
   private final EnumSetting<AutoToolPrefer> field2904 = new EnumSetting<AutoToolPrefer>(
      "PreferGlowStone", AutoToolPrefer.SilkTouch, "Preferred tool for glowstone"
   );
   private final EnumSetting<AutoToolPrefer> field2905 = new EnumSetting<AutoToolPrefer>("EChests", AutoToolPrefer.Fortune, "Preferred tool for e-chests");
   private final BooleanSetting field2906 = new BooleanSetting("Preserve", false, "Don't equip tools if they're about to break");
   public int field2907 = -1;
   private int field2908 = -1;
   private final dev.boze.client.utils.Timer field2909 = new dev.boze.client.utils.Timer();
   private final dev.boze.client.utils.Timer field2910 = new dev.boze.client.utils.Timer();

   private boolean method1678() {
      return Options.INSTANCE.method1971() || this.field2898.method461() == AutoToolMode.Ghost;
   }

   private boolean method1679() {
      return !this.method1678();
   }

   public AutoTool() {
      super("AutoTool", "Automatically swaps to the best tool tool when you mine", Category.Misc);
      this.field435 = true;
   }

   @Override
   public void onDisable() {
      this.field2908 = -1;
      this.field2907 = -1;
   }

   @EventHandler
   public void method1680(HandleInputEvent event) {
      if (!this.method1679() && this.field2909.hasElapsed((double)(this.field2900.method1367() * 50))) {
         if (mc.interactionManager.isBreakingBlock()) {
            BlockPos var5 = ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).getCurrentBreakingPos();
            if (var5 == null) {
               return;
            }

            this.field2910.reset();
            int var6 = InventoryHelper.method174(AutoTool::lambda$handleHotbar$1, SwapMode.Normal);
            if (var6 != mc.player.getInventory().selectedSlot && var6 != -1) {
               if (this.field2899.method419()) {
                  this.field2908 = mc.player.getInventory().selectedSlot;
               }

               ((KeyBindingAccessor)mc.options.hotbarKeys[var6]).setTimesPressed(1);
               this.field2900.method1376();
            }
         } else if (this.field2908 != -1 && (!mc.options.attackKey.isPressed() || this.field2910.hasElapsed(1000.0))) {
            ((KeyBindingAccessor)mc.options.hotbarKeys[this.field2908]).setTimesPressed(1);
            this.field2900.method1376();
            this.field2908 = -1;
         }
      }
   }

   @EventHandler
   private void method1681(PreBlockBreakEvent var1) {
      if (!this.method1678()) {
         int var5 = InventoryHelper.method174(AutoTool::lambda$onMinePre$2, this.field2901.method461());
         if (var5 != -1) {
            this.field2910.reset();
         }

         if (var5 != mc.player.getInventory().selectedSlot && var5 != -1 && var5 != this.field2907) {
            InventoryUtil.method534(this, 7, this.field2901.method461(), var5);
            if (this.field2901.method461() != SwapMode.Normal) {
               this.field2907 = var5;
            }
         }
      }
   }

   @EventHandler
   private void method1682(RotationEvent var1) {
      if (!var1.method554(RotationMode.Sequential)) {
         if (!this.method1678()) {
            if (!mc.interactionManager.isBreakingBlock() && this.field2907 != -1 && this.field2910.hasElapsed(50.0)) {
               this.field2907 = -1;
               InventoryUtil.method396(this);
            }
         }
      }
   }

   public static float method1683(ItemStack itemStack, BlockState state) {
      if (!(itemStack.getItem() instanceof ToolItem) && !(itemStack.getItem() instanceof ShearsItem)) {
         return -1.0F;
      } else if (INSTANCE.field2906.method419() && itemStack.getMaxDamage() - itemStack.getDamage() <= 15) {
         return -1.0F;
      } else {
         float var5 = 0.0F;
         var5 += itemStack.getMiningSpeedMultiplier(state);
         if (var5 <= mc.player.getMainHandStack().getMiningSpeedMultiplier(state)) {
            return -1.0F;
         } else {
            var5 *= 1000.0F;
            var5 += (float)ItemEnchantmentUtils.getEnchantmentLevel(itemStack, Enchantments.UNBREAKING);
            var5 += (float)ItemEnchantmentUtils.getEnchantmentLevel(itemStack, Enchantments.EFFICIENCY);
            var5 += (float)ItemEnchantmentUtils.getEnchantmentLevel(itemStack, Enchantments.MENDING);
            AutoToolPrefer var6 = method1684(state.getBlock());
            if (var6 == AutoToolPrefer.Fortune) {
               var5 += (float)ItemEnchantmentUtils.getEnchantmentLevel(itemStack, Enchantments.FORTUNE);
            }

            if (var6 == AutoToolPrefer.SilkTouch) {
               var5 += (float)ItemEnchantmentUtils.getEnchantmentLevel(itemStack, Enchantments.SILK_TOUCH);
            }

            return var5;
         }
      }
   }

   private static AutoToolPrefer method1684(Block var0) {
      if (var0 instanceof ExperienceDroppingBlock || var0 instanceof RedstoneOreBlock || var0 == Blocks.NETHER_QUARTZ_ORE) {
         return INSTANCE.field2903.method461();
      } else if (var0 == Blocks.GLOWSTONE) {
         return INSTANCE.field2904.method461();
      } else {
         return var0 == Blocks.ENDER_CHEST ? INSTANCE.field2905.method461() : INSTANCE.field2902.method461();
      }
   }

   private static Float lambda$onMinePre$2(PreBlockBreakEvent var0, ItemStack var1) {
      return method1683(var1, mc.world.getBlockState(var0.method1024()));
   }

   private static Float lambda$handleHotbar$1(BlockPos var0, ItemStack var1) {
      return method1683(var1, mc.world.getBlockState(var0));
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
