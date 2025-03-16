package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.AutoLootInventory;
import dev.boze.client.events.FlipFrameEvent;
import dev.boze.client.mixin.HandledScreenAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.player.SlotUtils;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Vec2f;

public class AutoLoot extends Module {
   public static AutoLoot INSTANCE = new AutoLoot();
   private final BooleanSetting field2753 = new BooleanSetting("Durability", false, "Prioritize items with higher durability");
   private final EnumSetting<AutoLootInventory> field2754 = new EnumSetting<AutoLootInventory>(
      "Inventory",
      AutoLootInventory.Await,
      "How to handle inventory\n - Ignore: Don't check if inventory open\n - Await: Wait for inventory to be open\nIgnore is not recommended, it's somewhat blatant\n"
   );
   private final MinMaxDoubleSetting field2755 = new MinMaxDoubleSetting(
      "SwapDelay", new double[]{0.2, 0.4}, 0.0, 1.0, 0.01, "Delay in seconds between swapping item"
   );
   private final MinMaxSetting field2756 = new MinMaxSetting("RandomDelay", 0.25, 0.0, 1.0, 0.01, "Random delay");
   private final Timer field2757 = new Timer();
   private Vec2f field2758 = null;
   private double field2759 = 0.0;

   private AutoLoot() {
      super("AutoLoot", "Automatically throws out worse items, keeps best", Category.Legit);
   }

   @EventHandler
   public void method1589(FlipFrameEvent event) {
      if (!event.method1022()) {
         if (MinecraftUtils.isClientActive() && !(mc.currentScreen instanceof DownloadingTerrainScreen)) {
            if (this.field2754.method461() == AutoLootInventory.Await && !(mc.currentScreen instanceof AbstractInventoryScreen)) {
               this.field2757.reset();
               this.field2758 = null;
               this.field2759 = this.field2755.method1295() * 50.0;
            } else {
               PlayerScreenHandler var5 = mc.player != null ? mc.player.playerScreenHandler : null;
               if (var5 == null || var5.getCursorStack().isEmpty()) {
                  if (mc.currentScreen instanceof HandledScreen) {
                     HandledScreenAccessor var6 = (HandledScreenAccessor)mc.currentScreen;
                     if (this.field2758 == null) {
                        this.field2758 = new Vec2f((float)mc.getWindow().getScaledWidth() / 2.0F, (float)mc.getWindow().getScaledHeight() / 2.0F);
                     }

                     Slot var7 = SlotUtils.method665(var5, var6, this.field2758, this::lambda$onScreenInput$0);
                     if (this.field2757.hasElapsed(this.field2759) && var7 != null) {
                        Vec2f var8 = SlotUtils.method663(var6, var7)
                           .add(
                              new Vec2f(
                                 this.field2756.getValue() == 0.0
                                    ? 0.0F
                                    : (float)ThreadLocalRandom.current().nextDouble(-this.field2756.getValue() * 30.0, this.field2756.getValue() * 30.0),
                                 this.field2756.getValue() == 0.0
                                    ? 0.0F
                                    : (float)ThreadLocalRandom.current().nextDouble(-this.field2756.getValue() * 30.0, this.field2756.getValue() * 30.0)
                              )
                           );
                        float var9 = this.field2758.distanceSquared(var8);
                        this.field2758 = var8;
                        float var10 = (float)Math.sqrt((double)var9) / new Vec2f(176.0F, 166.0F).length();
                        this.field2759 = (double)((long)(this.field2755.method1296() * (double)var10));
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, var7.id, 1, SlotActionType.THROW, mc.player);
                        event.method1021(true);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean lambda$onScreenInput$0(Slot var1, List var2) {
      return var1.hasStack() && ChestStealer.method1591(var1, var2, this.field2753.method419());
   }
}
