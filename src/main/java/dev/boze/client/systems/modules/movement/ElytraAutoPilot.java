package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import baritone.api.Settings;
import dev.boze.client.events.OpenScreenEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.movement.elytraautopilot.pi;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DeathScreen;

public class ElytraAutoPilot extends Module {
   public static final ElytraAutoPilot INSTANCE = new ElytraAutoPilot();
   public final BooleanSetting field3201 = new BooleanSetting("Replenish", true, "Replenish fireworks");
   public final BooleanSetting field3202 = new BooleanSetting("Predict", false, "Predict (only use in 1.12 chunks)");
   public final BooleanSetting field3203 = new BooleanSetting("Conserve", false, "Conserve fireworks");
   public final MinMaxSetting field3204 = new MinMaxSetting("BaritoneSpeed", 1.2, 0.001, 2.0, 0.001, "Firework speed for baritone", this.field3203);
   public final IntArraySetting field3205 = new IntArraySetting("HeightBounds", new int[]{200, 380}, 100, 500, 10, "Y-level to stay within for overworld bound");
   public final Settings field3206 = BaritoneAPI.getSettings();

   public ElytraAutoPilot() {
      super("AutoPilot", "Elytra auto pilot", Category.Movement);
      this.field3203.method401(this::lambda$new$0);
      this.field3204.method401(this::lambda$new$1);
      this.field3202.method401(this::lambda$new$2);
   }

   @Override
   public void onDisable() {
      pi.method2142();
   }

   @EventHandler
   public void method1816(PreTickEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (!mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
            pi.method1904();
         }
      }
   }

   @EventHandler
   public void method1817(Render3DEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (!mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
            pi.method1198();
         }
      }
   }

   @EventHandler
   private void method1818(OpenScreenEvent var1) {
      if (var1.screen instanceof DeathScreen) {
         pi.method2142();
      }
   }

   private void lambda$new$2(Boolean var1) {
      this.field3206.elytraPredictTerrain.value = var1;
   }

   private void lambda$new$1(Double var1) {
      if (this.field3203.method419()) {
         this.field3206.elytraFireworkSpeed.value = var1;
      } else {
         this.field3206.elytraFireworkSpeed.value = 1.2;
      }
   }

   private void lambda$new$0(Boolean var1) {
      this.field3206.elytraConserveFireworks.value = var1;
      if (!var1) {
         this.field3206.elytraFireworkSpeed.value = 1.2;
      }
   }
}
