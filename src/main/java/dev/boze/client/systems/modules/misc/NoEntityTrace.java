package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.CrosshairEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.hit.HitResult.Type;

public class NoEntityTrace extends Module {
   public static final NoEntityTrace INSTANCE = new NoEntityTrace();
   private BooleanSetting picOnly = new BooleanSetting("PickaxeOnly", true, "Only skip trace when holding a pickaxe");
   private BooleanSetting swordOnly = new BooleanSetting("SwordOnly", false, "Only skip trace when holding a sword");

   public NoEntityTrace() {
      super("NoEntityTrace", "Skip entity tracing", Category.Misc);
   }

   @EventHandler
   public void method1728(CrosshairEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (!mc.options.pickItemKey.isPressed() || !MiddleClickAction.INSTANCE.isEnabled()) {
            if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
               if (!this.picOnly.getValue()
                  || mc.player.getMainHandStack().getItem() instanceof PickaxeItem
                  || this.swordOnly.getValue() && mc.player.getMainHandStack().getItem() instanceof SwordItem) {
                  event.method1021(true);
               }
            }
         }
      }
   }
}
