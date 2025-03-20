package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import dev.boze.client.enums.AutoWalkMode;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.MathHelper;

public class AutoWalk extends Module {
   public static final AutoWalk INSTANCE = new AutoWalk();
   public final BooleanSetting field3147 = new BooleanSetting("Backwards", false, "Walk backwards");
   public final EnumSetting<AutoWalkMode> field3148 = new EnumSetting<AutoWalkMode>("Mode", AutoWalkMode.Key, "Mode");
   private Timer field3149 = new Timer();
   private Float field3150 = null;

   public AutoWalk() {
      super("AutoWalk", "Makes you walk automatically", Category.Movement);
   }

   @Override
   public void onDisable() {
      this.field3150 = null;
      if (MinecraftUtils.isClientActive()) {
         BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
         if (this.field3147.getValue()) {
            mc.options.backKey.setPressed(false);
         } else {
            mc.options.forwardKey.setPressed(false);
         }
      }
   }

   @EventHandler
   public void method1788(PrePlayerTickEvent event) {
      if (this.field3148.getValue() == AutoWalkMode.Key) {
         if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() != null) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
         }

         if (this.field3147.getValue()) {
            mc.options.backKey.setPressed(true);
         } else {
            mc.options.forwardKey.setPressed(true);
         }
      }
   }

   @EventHandler(
      priority = 20
   )
   public void method1789(TickInputPostEvent event) {
      if (this.field3148.getValue() == AutoWalkMode.Input) {
         if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() != null) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
         }

         event.field1954 = this.field3147.getValue() ? -1.0F : 1.0F;
      }
   }

   @EventHandler
   public void method1790(PostPlayerTickEvent event) {
      if (this.field3148.getValue() == AutoWalkMode.Baritone && BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() == null) {
         if (this.field3150 == null) {
            this.field3150 = mc.player.getYaw();
         }

         float var5 = (float)Math.toRadians((double)MathHelper.wrapDegrees(this.field3150 - (float)(this.field3147.getValue() ? 180 : 0)));
         int var6 = (int)Math.floor(mc.player.getPos().x - (double)MathHelper.sin(var5) * 30.0);
         int var7 = (int)Math.floor(mc.player.getPos().z + (double)MathHelper.cos(var5) * 30.0);
         BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ(var6, var7));
         this.field3149.reset();
      }
   }
}
