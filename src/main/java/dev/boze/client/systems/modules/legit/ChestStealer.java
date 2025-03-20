package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.Server;
import dev.boze.client.events.FlipFrameEvent;
import dev.boze.client.mixin.HandledScreenAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.ItemEnchantmentUtils;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.player.SlotUtils;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ChestStealer extends Module {
   public static final ChestStealer INSTANCE = new ChestStealer();
   private final BooleanSetting field2760 = new BooleanSetting("Smart", false, "Steal only items that are better than yours");
   private final BooleanSetting field2761 = new BooleanSetting("Durability", false, "Prioritize items with higher durability", this.field2760);
   private final MinMaxSetting field2762 = new MinMaxSetting("SuccessRate", 1.0, 0.0, 1.0, 0.01, "Chance of stealing an item");
   private final MinMaxDoubleSetting field2763 = new MinMaxDoubleSetting("Delay", new double[]{2.0, 5.0}, 0.0, 20.0, 0.01, "Delay between stealing items");
   private final MinMaxSetting field2764 = new MinMaxSetting("RandomDelay", 0.25, 0.0, 1.0, 0.01, "Random stealing delay");
   private final MinMaxSetting field2765 = new MinMaxSetting("InitialDelay", 2.0, 0.0, 20.0, 0.01, "Initial delay before stealing items");
   private final MinMaxSetting field2766 = new MinMaxSetting("FinalDelay", 2.0, 0.0, 20.0, 0.01, "Delay after stealing before closing the chest");
   private final Timer field2767 = new Timer();
   private boolean field2768 = true;
   private Vec2f field2769 = null;
   private long field2770 = 0L;

   public ChestStealer() {
      super("ChestStealer", "Steals items from chests", Category.Legit);
   }

   @Override
   public void onEnable() {
      this.field2768 = true;
      this.field2769 = null;
      this.field2770 = 0L;
   }

   @EventHandler
   public void method1590(FlipFrameEvent event) {
      try {
         if (event.method1022()) {
            return;
         }

         if (Server.method539().method538().method2116()) {
            return;
         }

         if (mc.currentScreen instanceof DownloadingTerrainScreen) {
            return;
         }

         if (!(mc.currentScreen instanceof GenericContainerScreen) || !(mc.currentScreen instanceof HandledScreen)) {
            this.field2767.reset();
            this.field2768 = true;
            this.field2769 = null;
            return;
         }

         if (!(mc.currentScreen instanceof HandledScreen)) {
            return;
         }

         HandledScreenAccessor var5 = (HandledScreenAccessor)mc.currentScreen;
         GenericContainerScreen var6 = (GenericContainerScreen)mc.currentScreen;
         GenericContainerScreenHandler var7 = (GenericContainerScreenHandler)var6.getScreenHandler();
         if (var7 != null && !var7.getCursorStack().isEmpty()) {
            return;
         }

         if (this.field2769 == null) {
            this.field2769 = new Vec2f((float)mc.getWindow().getScaledWidth() / 2.0F, (float)mc.getWindow().getScaledHeight() / 2.0F);
         }

         Slot var8 = SlotUtils.method665(var7, var5, this.field2769, this::lambda$onScreenInput$0);
         if (!this.field2767
            .hasElapsed(this.field2768 ? this.field2765.getValue() * 50.0 : (var8 == null ? this.field2766.getValue() * 50.0 : (double)this.field2770))) {
            return;
         }

         this.field2768 = false;
         this.field2767.reset();
         if (var8 == null) {
            mc.currentScreen.close();
         } else {
            if (mc.player.getInventory().getEmptySlot() == -1) {
               mc.currentScreen.close();
               return;
            }

            Vec2f var9 = SlotUtils.method663(var5, var8)
               .add(
                  new Vec2f(
                     this.field2764.getValue() == 0.0
                        ? 0.0F
                        : (float)ThreadLocalRandom.current().nextDouble(-this.field2764.getValue() * 30.0, this.field2764.getValue() * 30.0),
                     this.field2764.getValue() == 0.0
                        ? 0.0F
                        : (float)ThreadLocalRandom.current().nextDouble(-this.field2764.getValue() * 30.0, this.field2764.getValue() * 30.0)
                  )
               );
            if ((double)ThreadLocalRandom.current().nextInt(100) > this.field2762.getValue() * 100.0) {
               Vec2f var10 = this.field2769.add(var9.add(this.field2769.negate()).multiply((float)ThreadLocalRandom.current().nextDouble(0.0, 1.0)));
               Slot var11 = SlotUtils.method665(var7, var5, var10, ChestStealer::lambda$onScreenInput$1);
               if (var11 != null) {
                  var8 = var11;
               }
            }

            float var13 = this.field2769.distanceSquared(var9);
            this.field2769 = var9;
            float var14 = (float)Math.sqrt((double)var13) / new Vec2f((float)var5.getBackgroundWidth(), (float)var5.getBackgroundHeight()).length();
            this.field2770 = (long)(this.field2763.method1296() * (double)var14 * 50.0);
            mc.interactionManager.clickSlot(var7.syncId, var8.id, 0, SlotActionType.QUICK_MOVE, mc.player);
            event.method1021(true);
         }
      } catch (Exception var12) {
      }
   }

   public static boolean method1591(Slot slot, List<Slot> slotList, boolean considerDurability) {
      ArrayList var6 = new ArrayList(slotList);
      var6.remove(slot);
      var6.removeIf(ChestStealer::lambda$hasBetterEquivalent$2);
      List var7 = (List)var6.stream().map(Slot::method_7677).distinct().filter(ChestStealer::lambda$hasBetterEquivalent$3).collect(Collectors.toList());
      List var8 = (List)SlotUtils.method664(mc.player != null ? mc.player.playerScreenHandler : null)
         .stream()
         .filter(Slot::method_7681)
         .collect(Collectors.toList());
      ArrayList var9 = new ArrayList();
      var9.addAll(var7);
      var9.addAll((Collection)var8.stream().map(Slot::method_7677).collect(Collectors.toList()));
      return method1595(slot.getStack(), var9, considerDurability);
   }

   private static boolean method1592(Slot var0) {
      PlayerScreenHandler var4 = mc.player != null ? mc.player.playerScreenHandler : null;
      return var4 == null ? false : var4.slots.stream().filter(Slot::method_7682).anyMatch(ChestStealer::lambda$canTransfer$4);
   }

   private static float method1593(ItemStack var0) {
      if (var0.getItem() instanceof SwordItem) {
         return ((SwordItem)var0.getItem()).getMaterial().getAttackDamage();
      } else if (var0.getItem() instanceof ToolItem) {
         return (float)((ToolItem)var0.getItem()).getMaterial().getDurability();
      } else if (var0.getItem() instanceof ArmorItem) {
         ArmorItem var4 = (ArmorItem)var0.getItem();
         return (float)var4.getProtection() * (1.0F + ((ArmorMaterial)var4.getMaterial().value()).knockbackResistance());
      } else {
         return 0.0F;
      }
   }

   private static boolean method1594(ItemStack var0, ItemStack var1) {
      if (var0.getItem() instanceof SwordItem && var1.getItem() instanceof SwordItem) {
         return true;
      } else if (var0.getItem() instanceof AxeItem && var1.getItem() instanceof AxeItem) {
         return true;
      } else if (var0.getItem() instanceof PickaxeItem && var1.getItem() instanceof PickaxeItem) {
         return true;
      } else if (var0.getItem() instanceof HoeItem && var1.getItem() instanceof HoeItem) {
         return true;
      } else if (var0.getItem() instanceof ShovelItem && var1.getItem() instanceof ShovelItem) {
         return true;
      } else if (var0.getItem() instanceof ArmorItem && var1.getItem() instanceof ArmorItem) {
         ArmorItem var5 = (ArmorItem)var0.getItem();
         ArmorItem var6 = (ArmorItem)var1.getItem();
         return var5.getSlotType() == var6.getSlotType();
      } else {
         return var0.getItem() == var1.getItem();
      }
   }

   public static boolean method1595(ItemStack stack, List<ItemStack> list, boolean considerDurability) {
      if (stack.getItem().getEnchantability() == 0) {
         return false;
      } else {
         float var6 = method1593(stack);
         Object2IntArrayMap var7 = new Object2IntArrayMap();
         ItemEnchantmentUtils.populateEnchantments(stack, var7);

         for (ItemStack var9 : list) {
            if (method1594(stack, var9) && (!considerDurability || var9.getDamage() <= stack.getDamage())) {
               float var10 = method1593(var9);
               boolean var11 = var10 > var6;
               if (var11) {
                  Object2IntMap var12 = ItemEnchantmentUtils.getEnchantments(var9);
                  boolean var13 = var7.isEmpty() && !var12.isEmpty();
                  if (var13) {
                     return true;
                  }

                  AtomicBoolean var14 = new AtomicBoolean(false);
                  var7.forEach(ChestStealer::lambda$hasBetterEquivalent$5);
                  if (var14.get()) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private static void lambda$hasBetterEquivalent$5(Object2IntMap var0, AtomicBoolean var1, RegistryEntry var2, Integer var3) {
      if (var0.containsKey(var2) && var0.getInt(var2) < var3) {
         var1.set(true);
      }
   }

   private static boolean lambda$canTransfer$4(Slot var0, Slot var1) {
      return !var1.hasStack()
         || ItemStack.areItemsAndComponentsEqual(var1.getStack(), var0.getStack()) && var1.getStack().getCount() < var1.getStack().getMaxCount();
   }

   private static boolean lambda$hasBetterEquivalent$3(Slot var0, ItemStack var1) {
      return var1 != var0.getStack();
   }

   private static boolean lambda$hasBetterEquivalent$2(Slot var0, Slot var1) {
      return ItemStack.areItemsAndComponentsEqual(var0.getStack(), var1.getStack());
   }

   private static boolean lambda$onScreenInput$1(Slot var0, List var1) {
      return !var0.hasStack();
   }

   private boolean lambda$onScreenInput$0(GenericContainerScreenHandler var1, Slot var2, List var3) {
      return var2.hasStack()
         && var2.id < var1.getInventory().size()
         && (!this.field2760.getValue() || !method1591(var2, var3, this.field2761.getValue()))
         && method1592(var2);
   }
}
