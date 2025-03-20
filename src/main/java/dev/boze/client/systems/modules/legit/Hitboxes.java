package dev.boze.client.systems.modules.legit;

import dev.boze.client.jumptable.nb;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;

public class Hitboxes extends Module {
   public static final Hitboxes INSTANCE = new Hitboxes();
   private final MinMaxSetting field2790 = new MinMaxSetting("Expand", 0.5, 0.0, 2.5, 0.1, "How much to expand hitbox");
   private final BooleanSetting field2791 = new BooleanSetting("OnlyWeapon", false, "Only expand hitbox when holding weapon");
   private final SettingCategory field2792 = new SettingCategory("Targets", "Entities to target");
   private final BooleanSetting field2793 = new BooleanSetting("Players", true, "Target players", this.field2792);
   private final BooleanSetting field2794 = new BooleanSetting("Friends", false, "Target friends", this.field2792);
   private final BooleanSetting field2795 = new BooleanSetting("Animals", false, "Target animals", this.field2792);
   private final BooleanSetting field2796 = new BooleanSetting("Monsters", false, "Target monsters", this.field2792);

   public Hitboxes() {
      super("Hitboxes", "Expands entity hitboxes", Category.Legit);
   }

   public double method1603(Entity entity) {
      if (!this.isEnabled()) {
         return 0.0;
      } else if (this.method1604(entity)) {
         return this.field2791.getValue()
               && !(mc.player.getMainHandStack().getItem() instanceof SwordItem)
               && !(mc.player.getMainHandStack().getItem() instanceof AxeItem)
               && !(mc.player.getMainHandStack().getItem() instanceof TridentItem)
            ? 0.0
            : this.field2790.getValue();
      } else {
         return 0.0;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method1604(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (((PlayerEntity)var1).isCreative()) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else if (Friends.method2055(var1)) {
            return this.field2794.getValue();
         } else {
            return AntiBots.method2055(var1) ? false : this.field2793.getValue();
         }
      } else {
         switch (nb.field2117[var1.getType().getSpawnGroup().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
               return this.field2795.getValue();
            case 5:
               return this.field2796.getValue();
            default:
               return false;
         }
      }
   }
}
