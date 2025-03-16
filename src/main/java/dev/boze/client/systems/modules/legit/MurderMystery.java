package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.MurderMysteryWeapon;
import dev.boze.client.enums.Server;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.ItemSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import java.util.HashSet;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

public class MurderMystery extends Module {
   public static final MurderMystery INSTANCE = new MurderMystery();
   private final EnumSetting<MurderMysteryWeapon> field2810 = new EnumSetting<MurderMysteryWeapon>(
      "Weapon",
      MurderMysteryWeapon.Sword,
      "Weapon to detect\n - Sword: Detects any sword\n - Custom: Detects custom item\n - Hypixel: WIP advanced mode for Hypixel\nCommand to specify custom mode item:\n.set murdermystery item <item>\n"
   );
   private final ItemSetting field2811 = new ItemSetting("Item", "Murderer's weapon");
   private final IntSetting field2812 = new IntSetting("Range", 100, 10, 250, 5, "Range within to check for weapon");
   private final BooleanSetting field2813 = new BooleanSetting(
      "BowWarning", true, "If you're the murderer, warns you when others have bows", this::lambda$new$0
   );
   private PlayerEntity field2814 = null;
   private final HashSet<PlayerEntity> field2815 = new HashSet();
   private final HashSet<PlayerEntity> field2816 = new HashSet();

   private MurderMystery() {
      super("MurderMystery", "Detects murderer based on the item they're holding\n", Category.Legit);
   }

   private void reset() {
      this.field2814 = null;
      this.field2815.clear();
      this.field2816.clear();
   }

   @Override
   public void onEnable() {
      this.reset();
   }

   @EventHandler
   public void method1608(ACRotationEvent event) {
      if (event.method1017() != AnticheatMode.NCP) {
         if (Server.method539().method538().method2115()) {
            this.reset();
         } else {
            mc.world.getPlayers().forEach(this::lambda$onRotate$1);
         }
      }
   }

   private boolean method1609(PlayerEntity var1) {
      return this.field2810.method461() == MurderMysteryWeapon.Hypixel ? this.field2815.contains(var1) : var1.equals(this.field2814);
   }

   private boolean method1610(ItemStack var1) {
      Item var5 = var1.getItem();
      if (var1.isEmpty()) {
         return false;
      } else if (this.field2810.method461() == MurderMysteryWeapon.Hypixel) {
         return !(var5 instanceof EmptyMapItem)
            && !(var5 instanceof FilledMapItem)
            && !(var5 instanceof ArmorStandItem)
            && !(var5 instanceof BedItem)
            && !(var5 instanceof SwordItem);
      } else {
         return this.field2810.method461() == MurderMysteryWeapon.Sword ? var5 instanceof SwordItem : var5.equals(this.field2811.method447());
      }
   }

   private void lambda$onRotate$1(AbstractClientPlayerEntity var1) {
      if (var1.distanceTo(mc.player) <= (float)this.field2812.method434().intValue()) {
         if (!this.method1610(var1.getMainHandStack()) && !this.method1610(var1.getOffHandStack())) {
            if (var1.equals(this.field2814) && this.field2810.method461() != MurderMysteryWeapon.Hypixel) {
               this.field2814 = null;
            } else if (this.field2810.method461() == MurderMysteryWeapon.Hypixel
               && this.field2813.method419()
               && !var1.equals(mc.player)
               && this.method1609(mc.player)
               && var1.getMainHandStack().getItem() == Items.BOW
               && this.field2816.add(var1)) {
               ChatInstance.method742(this.getName(), "(highlight)%s(default) has a bow", var1.getName().getString());
            }
         } else {
            if (this.method1609(var1)) {
               return;
            }

            if (!var1.equals(mc.player)) {
               ChatInstance.method742(this.getName(), "(highlight)%s(default) is the murderer", var1.getName().getString());
            }

            if (this.field2810.method461() == MurderMysteryWeapon.Hypixel) {
               this.field2815.add(var1);
            } else {
               this.field2814 = var1;
            }
         }
      }
   }

   private boolean lambda$new$0() {
      return this.field2810.method461() == MurderMysteryWeapon.Hypixel;
   }
}
