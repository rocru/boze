package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.KeyAction;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.mixin.MinecraftClientAccessor;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.Bind;
import meteordevelopment.orbit.EventHandler;

public class Octopus extends Module {
   public static final Octopus INSTANCE = new Octopus();
   private final BooleanSetting leftClick = new BooleanSetting("LeftClick", true, "Left click");
   private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "Left click while using items", this.leftClick);
   private final BindSetting field3005 = new BindSetting("Slot0", Bind.create(), "Key to left click 0th slot", this.leftClick);
   private final BindSetting field3006 = new BindSetting("Slot1", Bind.create(), "Key to left click 1st slot", this.leftClick);
   private final BindSetting field3007 = new BindSetting("Slot2", Bind.create(), "Key to left click 2nd slot", this.leftClick);
   private final BindSetting field3008 = new BindSetting("Slot3", Bind.create(), "Key to left click 3rd slot", this.leftClick);
   private final BindSetting field3009 = new BindSetting("Slot4", Bind.create(), "Key to left click 4th slot", this.leftClick);
   private final BindSetting field3010 = new BindSetting("Slot5", Bind.create(), "Key to left click 5th slot", this.leftClick);
   private final BindSetting field3011 = new BindSetting("Slot6", Bind.create(), "Key to left click 6th slot", this.leftClick);
   private final BindSetting field3012 = new BindSetting("Slot7", Bind.create(), "Key to left click 7th slot", this.leftClick);
   private final BindSetting field3013 = new BindSetting("Slot8", Bind.create(), "Key to left click 8th slot", this.leftClick);
   private final BooleanSetting field3014 = new BooleanSetting("RightClick", true, "Right click");
   private final BooleanSetting field3015 = new BooleanSetting("MultiTask", false, "Right click while using items", this.field3014);
   private final BindSetting field3016 = new BindSetting("Slot0", Bind.create(), "Key to right click 0th slot", this.field3014);
   private final BindSetting field3017 = new BindSetting("Slot1", Bind.create(), "Key to right click 1st slot", this.field3014);
   private final BindSetting field3018 = new BindSetting("Slot2", Bind.create(), "Key to right click 2nd slot", this.field3014);
   private final BindSetting field3019 = new BindSetting("Slot3", Bind.create(), "Key to right click 3rd slot", this.field3014);
   private final BindSetting field3020 = new BindSetting("Slot4", Bind.create(), "Key to right click 4th slot", this.field3014);
   private final BindSetting field3021 = new BindSetting("Slot5", Bind.create(), "Key to right click 5th slot", this.field3014);
   private final BindSetting field3022 = new BindSetting("Slot6", Bind.create(), "Key to right click 6th slot", this.field3014);
   private final BindSetting field3023 = new BindSetting("Slot7", Bind.create(), "Key to right click 7th slot", this.field3014);
   private final BindSetting field3024 = new BindSetting("Slot8", Bind.create(), "Key to right click 8th slot", this.field3014);
   private final BindSetting[] field3025 = new BindSetting[]{
      this.field3005, this.field3006, this.field3007, this.field3008, this.field3009, this.field3010, this.field3011, this.field3012, this.field3013
   };
   private final BindSetting[] field3026 = new BindSetting[]{
      this.field3016, this.field3017, this.field3018, this.field3019, this.field3020, this.field3021, this.field3022, this.field3023, this.field3024
   };

   public Octopus() {
      super("Octopus", "Use items from all 9 hotbar slots", Category.Misc);
   }

   private void method1738(int var1, boolean var2) {
      if (mc.currentScreen == null) {
         if (this.leftClick.method419()) {
            for (int var6 = 0; var6 < 9; var6++) {
               if (this.field3025[var6].method476().matches(var2, var1) && !Options.method477(this.multiTask.method419())) {
                  int var7 = mc.player.getInventory().selectedSlot;
                  mc.player.getInventory().selectedSlot = var6;
                  ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
                  ((MinecraftClientAccessor)mc).callDoAttack();
                  mc.player.getInventory().selectedSlot = var7;
               }
            }
         }

         if (this.field3014.method419()) {
            for (int var8 = 0; var8 < 9; var8++) {
               if (this.field3026[var8].method476().matches(var2, var1) && !Options.method477(this.field3015.method419())) {
                  int var9 = mc.player.getInventory().selectedSlot;
                  mc.player.getInventory().selectedSlot = var8;
                  ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
                  ((MinecraftClientAccessor)mc).callDoItemUse();
                  mc.player.getInventory().selectedSlot = var9;
               }
            }
         }
      }
   }

   @EventHandler
   private void method1739(KeyEvent var1) {
      if (var1.action == KeyAction.Press) {
         this.method1738(var1.key, true);
      }
   }

   @EventHandler
   private void method1740(MouseButtonEvent var1) {
      if (var1.action == KeyAction.Press) {
         this.method1738(var1.button, false);
      }
   }
}
