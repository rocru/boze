package mapped;

import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.combat.OffHand;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.InventoryUtil;
import java.util.function.Predicate;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

class Class2923 implements IMinecraft {
   static final Predicate<ItemStack> field126 = Class2923::lambda$static$0;
   static final Predicate<ItemStack> field127 = Class2923::lambda$static$1;

   static boolean method2114() {
      return mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL || OffHand.INSTANCE.isEnabled() && OffHand.INSTANCE.ab == Items.END_CRYSTAL;
   }

   static void method2142() {
      if (InventoryUtil.method2114() && InventoryUtil.method532() == AutoCrystal.INSTANCE) {
         InventoryUtil.method396(AutoCrystal.INSTANCE);
      }
   }

   private static boolean lambda$static$1(ItemStack var0) {
      return var0.getItem() instanceof SwordItem;
   }

   private static boolean lambda$static$0(ItemStack var0) {
      return var0.getItem() instanceof EndCrystalItem;
   }
}
