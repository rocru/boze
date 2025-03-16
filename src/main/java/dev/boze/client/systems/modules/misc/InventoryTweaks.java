package dev.boze.client.systems.modules.misc;

import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.network.BozeExecutor;
import dev.boze.client.utils.player.InvUtils;
import dev.boze.client.utils.player.SlotUtils;
import net.minecraft.screen.ScreenHandler;

public class InventoryTweaks extends Module {
   public static final InventoryTweaks INSTANCE = new InventoryTweaks();
   public final BooleanSetting field2961 = new BooleanSetting("GuiButtons", true, "Show Steal/Dump buttons in GUIs");
   private final MinMaxSetting field2962 = new MinMaxSetting("StealDelay", 1.0, 0.0, 20.0, 0.1, "Steal delay in ticks");
   private final MinMaxSetting field2963 = new MinMaxSetting("Randomness", 0.25, 0.0, 1.0, 0.01, "Steal delay randomness percentage");
   public String field2964 = "";
   public boolean field2965 = false;
   public double field2966;
   public double field2967;

   public InventoryTweaks() {
      super("InventoryTweaks", "Various inventory tweaks and improvements", Category.Misc);
   }

   public void method1719(ScreenHandler handler) {
      BozeExecutor.method2200(this::lambda$steal$0);
   }

   public void method1720(ScreenHandler handler) {
      int var2 = SlotUtils.method1541(9);
      BozeExecutor.method2200(this::lambda$dump$1);
   }

   private void method1721(ScreenHandler var1, int var2, int var3) {
      for (int var7 = var2; var7 < var3; var7++) {
         if (var1.getSlot(var7).hasStack()) {
            int var8 = this.method1722();
            if (var8 > 0) {
               try {
                  Thread.sleep((long)var8);
               } catch (InterruptedException var10) {
               }
            }

            if (mc.currentScreen == null || !MinecraftUtils.isClientActive()) {
               break;
            }

            InvUtils.method2204().method2219(var7);
         }
      }
   }

   private int method1722() {
      return (int)(this.field2962.getValue() + this.field2962.getValue() * this.field2963.getValue() * (Math.random() - 0.5)) * 50;
   }

   private void lambda$dump$1(ScreenHandler var1, int var2) {
      this.method1721(var1, var2, var2 + 36);
   }

   private void lambda$steal$0(ScreenHandler var1) {
      this.method1721(var1, 0, SlotUtils.method1541(9));
   }
}
