package mapped;

import dev.boze.client.enums.BedTargetMode;
import dev.boze.client.events.GameJoinEvent;
import dev.boze.client.jumptable.pP;
import dev.boze.client.mixininterfaces.IExplosion;
import dev.boze.client.mixininterfaces.IVec3d;
import dev.boze.client.systems.modules.combat.AutoBed;
import dev.boze.client.systems.modules.misc.FakePlayer;
import dev.boze.client.utils.RaycastUtil;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.fakeplayer.FakePositions;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joml.Vector3d;

public class Class3069 {
   private static MinecraftClient field161;
   private static final Vec3d field162 = new Vec3d(0.0, 0.0, 0.0);
   private static Explosion field163;
   private static RaycastContext field164;

   @EventHandler
   private static void method6002(GameJoinEvent var0) {
      field161 = MinecraftClient.getInstance();
      field163 = new Explosion(field161.world, null, 0.0, 0.0, 0.0, 6.0F, false, DestructionType.DESTROY);
      field164 = new RaycastContext(null, null, ShapeType.COLLIDER, FluidHandling.ANY, field161.player);
   }

   public static double method6003(LivingEntity entity, Vec3d crystal, int extrapolation, BlockPos obsidianPos, boolean ignoreTerrain) {
      if (var3180 == null) {
         return 0.0;
      } else if (var3180 instanceof PlayerEntity && Class5926.method101((PlayerEntity)var3180) == GameMode.CREATIVE && !(var3180 instanceof FakePlayerEntity)) {
         return 0.0;
      } else {
         ((IVec3d)field162).boze$set(var3180.getPos().x, var3180.getPos().y, var3180.getPos().z);
         if (var3182 > 0) {
            if (var3180 == FakePlayer.INSTANCE.fakePlayer
               && FakePlayer.INSTANCE.isEnabled()
               && FakePlayer.INSTANCE.move.getValue()
               && !FakePlayer.INSTANCE.positions.isEmpty()) {
               int var18 = FakePlayer.INSTANCE.field2946 + var3182;
               if (var18 >= FakePlayer.INSTANCE.positions.size()) {
                  byte var19 = 0;
                  if (FakePlayer.INSTANCE.loop.getValue()) {
                     FakePositions var9 = (FakePositions)FakePlayer.INSTANCE.positions.get(var19);
                     ((IVec3d)field162).boze$set(var9.method2174().getX(), var9.method2174().getY(), var9.method2174().getZ());
                  }
               } else {
                  FakePositions var21 = (FakePositions)FakePlayer.INSTANCE.positions.get(var18);
                  ((IVec3d)field162).boze$set(var21.method2174().getX(), var21.method2174().getY(), var21.method2174().getZ());
               }
            } else {
               Vector3d var8 = Class5921.method55(var3180);
               ((IVec3d)field162).boze$set(field162.x + var8.x, field162.y + var8.y, field162.z + var8.z);
            }
         }

         double var20 = Math.sqrt(field162.squaredDistanceTo(var3181));
         if (var20 > 12.0) {
            return 0.0;
         } else {
            double var10 = RaycastUtil.method576(var3181, var3180, field162, var3180.getBoundingBox(), field164, var3183, var3184);
            double var12 = (1.0 - var20 / 12.0) * var10;
            double var14 = (var12 * var12 + var12) / 2.0 * 7.0 * 12.0 + 1.0;
            if (var3180 instanceof PlayerEntity) {
               var14 = method6007(var14);
            }

            try {
               var14 = (double)DamageUtil.getDamageLeft(
                  var3180,
                  (float)var14,
                  field161.world.getDamageSources().explosion(null),
                  (float)var3180.getArmor(),
                  (float)var3180.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue()
               );
               var14 = method6009(var3180, var14);
               ((IExplosion)field163).boze$set(var3181, 6.0F, false);
               var14 = method6008(var3180, var14, field163);
            } catch (Exception var17) {
            }

            return var14 < 0.0 ? 0.0 : var14;
         }
      }
   }

   public static double method6004(PlayerEntity player, Vec3d crystal) {
      return method6003(var3185, var3186, 0, null, false);
   }

   public static double method6005(LivingEntity player, Vec3d bed, BlockPos pos, boolean ignoreTerrain) {
      if (var3187 instanceof PlayerEntity && Class5926.method101((PlayerEntity)var3187) == GameMode.CREATIVE && !(var3187 instanceof FakePlayerEntity)) {
         return 0.0;
      } else {
         ((IVec3d)field162).boze$set(var3187.getPos().x, var3187.getPos().y, var3187.getPos().z);
         double var7 = Math.sqrt(var3187.squaredDistanceTo(var3188));
         if (var7 > 10.0) {
            return 0.0;
         } else {
            double var9 = RaycastUtil.method576(var3188, var3187, field162, var3187.getBoundingBox(), field164, var3189, var3190);
            double var11 = (1.0 - var7 / 10.0) * var9;
            double var13 = (var11 * var11 + var11) / 2.0 * 7.0 * 10.0 + 1.0;
            if (var3187 instanceof PlayerEntity) {
               var13 = method6007(var13);
            }

            try {
               var13 = (double)DamageUtil.getDamageLeft(
                  var3187,
                  (float)var13,
                  field161.world.getDamageSources().explosion(null),
                  (float)var3187.getArmor(),
                  (float)var3187.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue()
               );
               var13 = method6009(var3187, var13);
               ((IExplosion)field163).boze$set(var3188, 5.0F, true);
               var13 = method6008(var3187, var13, field163);
            } catch (Exception var16) {
            }

            if (var13 < 0.0) {
               var13 = 0.0;
            }

            if (AutoBed.INSTANCE.targetMode.getValue() == BedTargetMode.Semi && var3187 != field161.player && var3187.getBoundingBox().contains(var3188)) {
               var13 += 4000.0;
            }

            return var13;
         }
      }
   }

   public static double method6006(PlayerEntity player, Vec3d anchor, BlockPos pos) {
      if (var3191.getAbilities().creativeMode) {
         return 0.0;
      } else {
         double var6 = Math.sqrt(var3191.squaredDistanceTo(var3192));
         if (var6 > 10.0) {
            return 0.0;
         } else {
            BlockState var8 = field161.world.getBlockState(var3193);
            field161.world.removeBlock(var3193, false);
            double var9 = (double)Explosion.getExposure(var3192, var3191);
            double var11 = (1.0 - var6 / 10.0) * var9;
            double var13 = (var11 * var11 + var11) / 2.0 * 7.0 * 10.0 + 1.0;
            var13 = method6007(var13);
            var13 = method6009(var3191, var13);
            var13 = (double)DamageUtil.getDamageLeft(
               var3191,
               (float)var13,
               field161.world.getDamageSources().explosion(null),
               (float)var3191.getArmor(),
               (float)var3191.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue()
            );
            ((IExplosion)field163).boze$set(var3192, 5.0F, true);
            var13 = method6008(var3191, var13, field163);
            if (var13 < 0.0) {
               var13 = 0.0;
            }

            field161.world.setBlockState(var3193, var8);
            return var13;
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static double method6007(double var0) {
      return switch (pP.field2128[field161.world.getDifficulty().ordinal()]) {
         case 1 -> 0.0;
         case 2 -> Math.min(var0 / 2.0 + 1.0, var0);
         case 3 -> var0 * 3.0 / 2.0;
         default -> var0;
      };
   }

   private static double method6008(LivingEntity var0, double var1, Explosion var3) {
      float var7 = 0.0F;
      var7 = (float)method6010(var0.getArmorItems());
      if (var7 > 20.0F) {
         var7 = 20.0F;
      }

      var1 *= 1.0 - (double)var7 / 25.0;
      return var1 < 0.0 ? 0.0 : var1;
   }

   private static double method6009(LivingEntity var0, double var1) {
      if (var0.hasStatusEffect(StatusEffects.RESISTANCE)) {
         int var6 = var0.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1;
         var1 *= 1.0 - (double)var6 * 0.2;
      }

      return var1 < 0.0 ? 0.0 : var1;
   }

   public static int method6010(Iterable<ItemStack> equipment) {
      MutableInt var1 = new MutableInt();
      var3194.forEach(Class3069::lambda$getProtectionAmount$0);
      return var1.intValue();
   }

   public static int method6011(ItemStack stack) {
      int var1 = EnchantmentHelper.getLevel(
         (RegistryEntry)field161.world.getRegistryManager().get(Enchantments.BLAST_PROTECTION.getRegistryRef()).getEntry(Enchantments.BLAST_PROTECTION).get(),
         var3195
      );
      int var2 = EnchantmentHelper.getLevel(
         (RegistryEntry)field161.world.getRegistryManager().get(Enchantments.PROTECTION.getRegistryRef()).getEntry(Enchantments.PROTECTION).get(), var3195
      );
      return var1 * 2 + var2;
   }

   private static void lambda$getProtectionAmount$0(MutableInt var0, ItemStack var1) {
      var0.add(method6011(var1));
   }
}
