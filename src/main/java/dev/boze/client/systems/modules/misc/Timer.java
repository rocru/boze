package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.FoodUtil;
import mapped.Class3076;
import meteordevelopment.orbit.EventHandler;

import java.text.DecimalFormat;

public class Timer extends Module {
   public static final Timer INSTANCE = new Timer();
   private FloatSetting field1029 = new FloatSetting("Speed", 1.1F, 0.2F, 50.0F, 0.1F, "Timer speed");
   private BooleanSetting field1030 = new BooleanSetting("Switch", false, "Timer Switch");
   private IntSetting field1031 = new IntSetting("Active", 5, 1, 20, 1, "Active ticks", this.field1030);
   private IntSetting field1032 = new IntSetting("Inactive", 5, 1, 20, 1, "Inactive ticks", this.field1030);
   private FloatSetting field1033 = new FloatSetting("InactiveSpeed", 0.9F, 0.2F, 50.0F, 0.1F, "Inactive speed", this.field1030);
   public final SettingCategory field1034 = new SettingCategory("Pause", "Choose when to pause Timer");
   private final BooleanSetting field1035 = new BooleanSetting("WhileEating", false, "Pause while eating", this.field1034);
   private final BooleanSetting field1036 = new BooleanSetting("WhileMining", false, "Pause while mining", this.field1034);
   private int field1037 = 0;

   public Timer() {
      super("Timer", "Changes game speed", Category.Misc);
   }

   @Override
   public String method1322() {
      DecimalFormat var3 = new DecimalFormat("#.##");
      return var3.format((double)Class3076.method6027());
   }

   @EventHandler
   public void method1831(PrePlayerTickEvent event) {
      float var5 = this.field1029.getValue();
      if (this.field1036.getValue() && mc.interactionManager.isBreakingBlock()) {
         Class3076.method6025(this);
      } else if (!this.field1035.getValue()
         || (!mc.player.isUsingItem() || !FoodUtil.isFood(mc.player.getActiveItem()))
            && (!mc.options.useKey.isPressed() || !FoodUtil.isFood(mc.player.getInventory().getMainHandStack()))) {
         if (this.field1030.getValue()) {
            if (this.field1037 > this.field1031.getValue() + this.field1032.getValue()) {
               this.field1037 = 0;
            }

            if (this.field1037 > this.field1031.getValue()) {
               var5 = this.field1033.getValue();
            }
         }

         Class3076.method6024(this, 5, var5);
         this.field1037++;
      } else {
         Class3076.method6025(this);
      }
   }

   @Override
   public void onDisable() {
      Class3076.method6025(this);
      this.field1037 = 0;
   }
}
