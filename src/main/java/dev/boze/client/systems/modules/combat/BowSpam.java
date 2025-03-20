package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.BowspamMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.InventoryHelper;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BowSpam extends Module {
   public static final BowSpam INSTANCE = new BowSpam();
   private final EnumSetting<BowspamMode> mode = new EnumSetting<BowspamMode>("Mode", BowspamMode.Anarchy, "Mode for bow spamming", BowSpam::lambda$new$0);
   private final IntSetting charge = new IntSetting("Charge", 10, 3, 20, 1, "Bow charge, in ticks", this::method1494);
   private final IntArraySetting releaseDelay = new IntArraySetting("Delay", new int[]{19, 21}, 3, 25, 1, "Delay to release bow in ticks", this::method1493);
   private final BooleanSetting pull = new BooleanSetting("Pull", false, "Automatically pull bow", this::method1493);
   private final IntArraySetting pullDelay = new IntArraySetting("Delay", new int[]{2, 4}, 0, 10, 1, "Delay to pull bow in ticks", this::method1493, this.pull);
   private boolean field2549 = false;
   private boolean field2550 = false;
   private int field2551 = 0;

   private boolean method1493() {
      return Options.INSTANCE.method1971() || this.mode.getValue() == BowspamMode.Ghost;
   }

   private boolean method1494() {
      return !this.method1493();
   }

   public BowSpam() {
      super("BowSpam", "Spams your bow\nCan also be used as perfect bow release\n", Category.Combat);
      this.field435 = true;
   }

   @Override
   public void onEnable() {
      this.field2549 = false;
      this.field2550 = false;
      this.field2551 = this.pullDelay.method1376();
   }

   @Override
   public void onDisable() {
      this.method1497(false);
   }

   @EventHandler
   public void method1495(RotationEvent event) {
      if (!this.method1494() && !event.method555(RotationMode.Vanilla, true)) {
         if (mc.player.getAbilities().creativeMode || InventoryHelper.method169(BowSpam::lambda$onInteract$1) != -1) {
            boolean var5 = mc.player.getMainHandStack().getItem() == Items.BOW;
            if (var5) {
               if (mc.options.useKey.isPressed()) {
                  if (mc.player.getItemUseTime() >= this.releaseDelay.method1367()) {
                     this.method1497(false);
                     this.releaseDelay.method1376();
                  }
               } else if (this.pull.getValue()) {
                  if (this.field2551 > 0) {
                     this.field2551--;
                  } else {
                     this.method1497(true);
                     this.field2551 = this.pullDelay.method1376();
                  }
               }
            }
         }
      }
   }

   @EventHandler
   private void method1496(MovementEvent var1) {
      if (!this.method1493()) {
         if (mc.player.getAbilities().creativeMode || InventoryHelper.method169(BowSpam::lambda$onSendMovementPackets$2) != -1) {
            if (mc.options.useKey.isPressed()) {
               boolean var5 = mc.player.getMainHandStack().getItem() == Items.BOW;
               if (!var5 && this.field2549) {
                  this.method1497(false);
               }

               this.field2549 = var5;
               if (!var5) {
                  return;
               }

               if (mc.player.getItemUseTime() >= this.charge.method434()) {
                  mc.player.stopUsingItem();
                  mc.interactionManager.stopUsingItem(mc.player);
               } else {
                  this.method1497(true);
               }

               this.field2550 = mc.options.useKey.isPressed();
            } else if (this.field2550) {
               this.method1497(false);
               this.field2550 = false;
            }
         }
      }
   }

   private void method1497(boolean var1) {
      mc.options.useKey.setPressed(var1);
   }

   private static boolean lambda$onSendMovementPackets$2(ItemStack var0) {
      return var0.getItem() instanceof ArrowItem;
   }

   private static boolean lambda$onInteract$1(ItemStack var0) {
      return var0.getItem() instanceof ArrowItem;
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
