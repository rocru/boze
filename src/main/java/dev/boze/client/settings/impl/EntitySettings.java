package dev.boze.client.settings.impl;

import dev.boze.client.mixin.LimbAnimatorAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntitySettings implements SettingsGroup, IMinecraft {
   final BooleanSetting field92 = new BooleanSetting("Wardens", false, "Don't render wardens");
   final BooleanSetting field93 = new BooleanSetting(
      "CancelPackets",
      false,
      "Cancel packets for warden spawns\nNote: This will cancel all warden spawns, not just rendering, you'll need to re-join the game with this off to see wardens again",
      this.field92
   );
   private final IntSetting field94 = new IntSetting("MaxDistance", 0, 0, 100, 1, "Max distance to render wardens", this::lambda$new$0, this.field92);
   final BooleanSetting field95 = new BooleanSetting("Dying", false, "Don't render dying entities");
   final BooleanSetting field96 = new BooleanSetting("InsidePlayers", false, "Don't render players inside camera");
   private final BooleanSetting field97 = new BooleanSetting("NoLimbInterp", false, "Don't interpolate limbs");
   final BooleanSetting field98 = new BooleanSetting("ForceSneak", false, "Make all players sneak");
   private final SettingCategory field99 = new SettingCategory("Armor", "Options for not rendering armor pieces");
   private final BooleanSetting field100 = new BooleanSetting("ArmorStands", false, "Apply to armor stands", this.field99);
   private final BooleanSetting field101 = new BooleanSetting("Head", false, "Don't render head armor", this.field99);
   private final BooleanSetting field102 = new BooleanSetting("Chest", false, "Don't render chest armor", this.field99);
   private final BooleanSetting field103 = new BooleanSetting("Legs", false, "Don't render leg armor", this.field99);
   private final BooleanSetting field104 = new BooleanSetting("Feet", false, "Don't render foot armor", this.field99);
   private final BooleanSetting field105 = new BooleanSetting("DroppedItems", false, "Don't render dropped items");
   private final SettingBlock field106 = new SettingBlock(
      "Entities",
      "Entity rendering settings",
      this.field92,
      this.field93,
      this.field94,
      this.field95,
      this.field96,
      this.field97,
      this.field98,
      this.field99,
      this.field100,
      this.field101,
      this.field102,
      this.field103,
      this.field104,
      this.field105
   );

   @Override
   public Setting<?>[] get() {
      return this.field106.method472();
   }

   public boolean method2055(Entity entity) {
      if (!(entity instanceof WardenEntity)
         || !this.field92.getValue()
         || this.field94.method434() != 0 && !this.field93.getValue() && !(entity.distanceTo(mc.player) > (float)this.field94.method434().intValue())) {
         if (entity instanceof PlayerEntity && this.field97.getValue()) {
            ((LimbAnimatorAccessor)((PlayerEntity)entity).limbAnimator).setPos(0.0F);
            ((LimbAnimatorAccessor)((PlayerEntity)entity).limbAnimator).setSpeed(0.0F);
            ((LimbAnimatorAccessor)((PlayerEntity)entity).limbAnimator).setPrevSpeed(0.0F);
         } else if (entity instanceof ItemEntity && this.field105.getValue()) {
            return true;
         }

         return false;
      } else {
         return true;
      }
   }

   boolean method1993(LivingEntity var1, EquipmentSlot var2) {
      return var1 instanceof ArmorStandEntity && !this.field100.getValue()
         ? false
         : var2 == EquipmentSlot.HEAD && this.field101.getValue()
            || var2 == EquipmentSlot.CHEST && this.field102.getValue()
            || var2 == EquipmentSlot.LEGS && this.field103.getValue()
            || var2 == EquipmentSlot.FEET && this.field104.getValue();
   }

   private boolean lambda$new$0() {
      return !this.field93.getValue();
   }
}
