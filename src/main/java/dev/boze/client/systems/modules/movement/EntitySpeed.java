package dev.boze.client.systems.modules.movement;

import dev.boze.client.events.BoatEntityMoveEvent;
import dev.boze.client.events.LivingEntityMoveEvent;
import dev.boze.client.mixininterfaces.IVec3d;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class EntitySpeed extends Module {
   public static final EntitySpeed INSTANCE = new EntitySpeed();
   private final BooleanSetting field439 = new BooleanSetting("Boats", false, "Apply Speed to Boats");
   private final MinMaxSetting field440 = new MinMaxSetting("Speed", 1.0, 0.1, 10.0, 0.1, "Speed");
   private final BooleanSetting field441 = new BooleanSetting("FastFall", false, "Go down faster");
   private final BooleanSetting field442 = new BooleanSetting("NCPStrict", true, "NCPStrict");
   private final BooleanSetting field443 = new BooleanSetting("InWater", false, "Speed in water");

   public EntitySpeed() {
      super("EntitySpeed", "Go faster on entities", Category.Movement);
   }

   @EventHandler
   private void method237(LivingEntityMoveEvent var1) {
      if (MinecraftUtils.isClientActive() && var1.field1929.getControllingPassenger() == mc.player) {
         LivingEntity var5 = var1.field1929;
         if (!this.field442.method419() || var5.isOnGround()) {
            if (this.field443.method419() || !var5.isTouchingWater()) {
               Vec3d var6 = Class5924.method95(this.field440.getValue() * 10.0);
               ((IVec3d)var1.vec3).boze$set(var6.x, this.field441.method419() && var1.vec3.y < 0.0 ? -3.0 : var1.vec3.y, var6.z);
            }
         }
      }
   }

   @EventHandler
   private void method238(BoatEntityMoveEvent var1) {
      if (MinecraftUtils.isClientActive() && this.field439.method419() && var1.field1902.getControllingPassenger() == mc.player) {
         BoatEntity var5 = var1.field1902;
         if (!this.field442.method419() || var5.isOnGround()) {
            if (this.field443.method419() || !var5.isTouchingWater()) {
               Vec3d var6 = Class5924.method95(this.field440.getValue() * 10.0);
               ((IVec3d)var1.vec3).boze$set(var6.x, this.field441.method419() && var1.vec3.y < 0.0 ? -3.0 : var1.vec3.y, var6.z);
            }
         }
      }
   }
}
