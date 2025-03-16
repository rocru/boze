package dev.boze.client.systems.modules.combat.AutoMine;

import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingBlock;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.util.math.BlockPos;

class ProneEscape implements IMinecraft, SettingsGroup {
   public final BooleanSetting field1632 = new BooleanSetting("ProneEscape", false, "Whether or not to enable prone escape", ProneEscape::lambda$new$0);
   final BindSetting field1633 = new BindSetting("Bind", Bind.create(), "The bind to toggle prone escape (optional)", this.field1632);
   private final SettingBlock field1634 = new SettingBlock(
      "ProneEscape", "Escapes from 1x1x1 positions by mining block above\n", this.field1632, this.field1633
   );

   ProneEscape(AutoMine var1) {
      this.field1634.setVisibility(var1.advanced::method419);
   }

   @Override
   public Setting<?>[] get() {
      return this.field1634.method472();
   }

   BlockDirectionInfo method1462() {
      return mc.player.isInSwimmingPose()
         ? new BlockLocationInfo(BlockPos.ofFloored(mc.player.getPos().add(0.0, 1.0, 0.0)), false).method1468(ProneEscape::lambda$update$1)
         : null;
   }

   private static Boolean lambda$update$1(BlockPos var0) {
      return mc.player.isInSwimmingPose();
   }

   private static boolean lambda$new$0() {
      return AutoMine.INSTANCE.advanced.method419();
   }
}
