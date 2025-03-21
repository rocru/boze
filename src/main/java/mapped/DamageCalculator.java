package mapped;

import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.AutoCrystalAction;
import dev.boze.client.events.GameJoinEvent;
import dev.boze.client.jumptable.mI;
import dev.boze.client.mixininterfaces.IExplosion;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.misc.FakePlayer;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RaycastUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class DamageCalculator implements IMinecraft {
    private final AutoCrystal field123;
    private Explosion field124;
    private RaycastContext field125;

    public DamageCalculator(AutoCrystal ac) {
        this.field123 = ac;
    }

    @EventHandler
    public void method1966(GameJoinEvent event) {
        this.field124 = new Explosion(mc.world, null, 0.0, 0.0, 0.0, 6.0F, false, DestructionType.DESTROY);
        this.field125 = new RaycastContext(null, null, ShapeType.COLLIDER, FluidHandling.ANY, mc.player);
    }

    public double method5665(LivingEntity entity, AutoCrystalAction type, Vec3d crystal, BlockPos obsidianPos, boolean ignoreTerrain) {
        if (entity == null) {
            return 0.0;
        } else if (entity instanceof PlayerEntity
                && Class5926.method101((PlayerEntity) entity) == GameMode.CREATIVE
                && entity != FakePlayer.INSTANCE.fakePlayer) {
            return 0.0;
        } else {
            Vec3d var9 = this.field123.ac.method510(entity, type);
            double var10 = Math.sqrt(var9.squaredDistanceTo(crystal));
            if (var10 > 12.0) {
                return 0.0;
            } else {
                double var12 = RaycastUtil.method576(crystal, entity, var9, this.field123.ac.method511(entity, type), this.field125, obsidianPos, ignoreTerrain);
                double var14 = (1.0 - var10 / 12.0) * var12;
                double var16 = (var14 * var14 + var14) / 2.0 * 7.0 * 12.0 + 1.0;
                if (entity instanceof PlayerEntity) {
                    var16 = this.method5666(var16);
                }

                try {
                    var16 = DamageUtil.getDamageLeft(
                            entity,
                            (float) var16,
                            mc.world.getDamageSources().explosion(null),
                            (float) entity.getArmor(),
                            (float) entity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue()
                    );
                    var16 = this.method5668(entity, var16);
                    ((IExplosion) this.field124).boze$set(crystal, 6.0F, false);
                    var16 = this.method5667(entity, var16, this.field124);
                } catch (Exception ignored) {
                }

                return Math.max(var16, 0.0);
            }
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private double method5666(double var1) {
        return switch (mI.field2107[mc.world.getDifficulty().ordinal()]) {
            case 1 -> 0.0;
            case 2 -> Math.min(var1 / 2.0 + 1.0, var1);
            case 3 -> var1 * 3.0 / 2.0;
            default -> var1;
        };
    }

    private double method5667(LivingEntity var1, double var2, Explosion var4) {
        int var8 = 0;
        if (this.field123.field1041.field211.getValue() && this.field123.field1041.field205.getValue() == AnticheatMode.Grim) {
            for (ItemStack var10 : var1.getArmorItems()) {
                if (!var10.getEnchantments().isEmpty()) {
                    var8 += 4;
                    Item var12 = var10.getItem();
                    if (var12 instanceof ArmorItem var11) {
                        if (var11.getSlotType() == EquipmentSlot.LEGS) {
                            var8 += 4;
                        }
                    }
                }
            }
        } else {
            var8 = Class3069.method6010(var1.getArmorItems());
        }

        if (var8 > 20) {
            var8 = 20;
        }

        var2 *= 1.0 - (double) var8 / 25.0;
        return Math.max(var2, 0.0);
    }

    private double method5668(LivingEntity var1, double var2) {
        if (var1.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int var7 = var1.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1;
            var2 *= 1.0 - (double) var7 * 0.2;
        }

        return Math.max(var2, 0.0);
    }
}
