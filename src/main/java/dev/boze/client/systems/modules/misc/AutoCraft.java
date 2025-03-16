package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.AutoCraftMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.ItemSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.player.SlotUtils;
import java.util.LinkedList;
import java.util.Queue;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class AutoCraft extends Module {
   public static final AutoCraft INSTANCE = new AutoCraft();
   public final ItemSetting item = new ItemSetting("Item", "Item to craft");
   private final BooleanSetting fireworks = new BooleanSetting("FD3", false, "Only craft fireworks with flight duration 3", this::lambda$new$0);
   private final EnumSetting<AutoCraftMode> mode = new EnumSetting<AutoCraftMode>(
      "Mode", AutoCraftMode.Store, "Mode for AutoCraft\nStore: Store crafted items in inventory\nDrop: Drop crafted items on the ground"
   );
   private final IntSetting delay = new IntSetting("Delay", 1, 1, 10, 1, "Tick delay between crafting items");
   private final BooleanSetting shiftCraft = new BooleanSetting(
      "ShiftCraft", false, "Craft items with shift click\nWill craft as many items as possible with the items in the inventory"
   );
   private final BooleanSetting strict = new BooleanSetting("Strict", false, "Strict mode\nIf your inventory is getting de-synced, try enabling this");
   private final BooleanSetting autoClose = new BooleanSetting("AutoClose", false, "Automatically close the crafting table after crafting");
   private Queue<Runnable> field2884 = new LinkedList();
   private int field2885 = 0;
   private boolean field2886 = false;

   private AutoCraft() {
      super("AutoCraft", "Automatically crafts items\nUse .autocraft item <item> to set the item to craft", Category.Misc);
   }

   @EventHandler
   public void method1658(MovementEvent event) {
      if (!(mc.player.currentScreenHandler instanceof CraftingScreenHandler)) {
         this.field2886 = false;
      } else if (!this.field2884.isEmpty()) {
         ((Runnable)this.field2884.poll()).run();
      } else if (this.item.method447() != null) {
         if (this.field2885 > 0) {
            this.field2885--;
         } else {
            if (this.strict.method419()) {
               mc.player.getInventory().updateItems();
            }

            Item var5 = this.item.method447();
            if (var5 == Items.FIREWORK_ROCKET && this.fireworks.method419()) {
               int var12 = InventoryHelper.method169(AutoCraft::lambda$onSendMovementPackets$1);
               int var13 = InventoryHelper.method169(AutoCraft::lambda$onSendMovementPackets$2);
               if (var12 != -1 && var13 != -1) {
                  this.field2884.add(AutoCraft::lambda$onSendMovementPackets$3);
                  this.field2884.add(AutoCraft::lambda$onSendMovementPackets$4);
                  this.field2884.add(AutoCraft::lambda$onSendMovementPackets$5);
                  ((Runnable)this.field2884.poll()).run();
               } else {
                  if (this.field2886 && this.autoClose.method419()) {
                     mc.player.closeHandledScreen();
                  }
               }
            } else {
               CraftingScreenHandler var6 = (CraftingScreenHandler)mc.player.currentScreenHandler;

               for (RecipeResultCollection var9 : mc.player.getRecipeBook().getOrderedResults()) {
                  for (RecipeEntry var11 : var9.getRecipes(true)) {
                     if (var11.value().getResult(mc.world.getRegistryManager()).getItem().equals(var5)) {
                        mc.interactionManager.clickRecipe(var6.syncId, var11, this.shiftCraft.method419());
                        mc.interactionManager
                           .clickSlot(
                              var6.syncId, 0, 1, this.mode.method461() == AutoCraftMode.Drop ? SlotActionType.THROW : SlotActionType.QUICK_MOVE, mc.player
                           );
                        this.field2886 = true;
                        this.field2885 = this.delay.method434() - 1;
                        return;
                     }
                  }
               }

               if (this.field2886 && this.autoClose.method419()) {
                  mc.player.closeHandledScreen();
               }
            }
         }
      }
   }

   private static void lambda$onSendMovementPackets$5() {
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 0, 0, SlotActionType.QUICK_MOVE, mc.player);
   }

   private static void lambda$onSendMovementPackets$4(int var0) {
      boolean var4 = mc.player.currentScreenHandler.getCursorStack().isEmpty();
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, SlotUtils.method1541(var0), 0, SlotActionType.PICKUP, mc.player);
      mc.interactionManager
         .clickSlot(mc.player.currentScreenHandler.syncId, -999, ScreenHandler.packQuickCraftData(0, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager
         .clickSlot(mc.player.currentScreenHandler.syncId, -999, ScreenHandler.packQuickCraftData(2, 0), SlotActionType.QUICK_CRAFT, mc.player);
      if (var4 && !mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
         mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, SlotUtils.method1541(var0), 0, SlotActionType.PICKUP, mc.player);
      }
   }

   private static void lambda$onSendMovementPackets$3(int var0) {
      boolean var4 = mc.player.currentScreenHandler.getCursorStack().isEmpty();
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, SlotUtils.method1541(var0), 0, SlotActionType.PICKUP, mc.player);
      mc.interactionManager
         .clickSlot(mc.player.currentScreenHandler.syncId, -999, ScreenHandler.packQuickCraftData(0, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 1, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 2, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 3, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager
         .clickSlot(mc.player.currentScreenHandler.syncId, -999, ScreenHandler.packQuickCraftData(2, 0), SlotActionType.QUICK_CRAFT, mc.player);
      if (var4 && !mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
         mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, SlotUtils.method1541(var0), 0, SlotActionType.PICKUP, mc.player);
      }
   }

   private static boolean lambda$onSendMovementPackets$2(ItemStack var0) {
      return var0.getItem() == Items.PAPER;
   }

   private static boolean lambda$onSendMovementPackets$1(ItemStack var0) {
      return var0.getItem() == Items.GUNPOWDER;
   }

   private boolean lambda$new$0() {
      return this.item.method447() == Items.FIREWORK_ROCKET;
   }
}
