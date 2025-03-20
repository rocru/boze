package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerModelPart;

public class SkinBlinker extends Module {
   public static final SkinBlinker INSTANCE = new SkinBlinker();
   private final FloatSetting field3113 = new FloatSetting("Delay", 0.0F, 0.0F, 20.0F, 0.1F, "Delay in seconds");
   private final BooleanSetting field3114 = new BooleanSetting("Random", true, "Randomize skin parts to flash");
   private dev.boze.client.utils.Timer field3115 = new dev.boze.client.utils.Timer();

   public SkinBlinker() {
      super("SkinBlinker", "Flashes your skin parts", Category.Misc);
   }

   @EventHandler
   public void method1770(Render3DEvent event) {
      if (this.field3115.hasElapsed((double)(this.field3113.getValue() * 1000.0F))) {
         PlayerModelPart[] var5 = PlayerModelPart.values();

         for (int var6 = 0; var6 < var5.length; var6++) {
            PlayerModelPart var7 = var5[var6];
            mc.options.togglePlayerModelPart(var7, this.field3114.getValue() ? Math.random() < 0.5 : !mc.options.isPlayerModelPartEnabled(var7));
         }

         this.field3115.reset();
      }
   }
}
