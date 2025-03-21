package mapped;

import dev.boze.client.enums.BedTargetMode;
import dev.boze.client.events.GameJoinEvent;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.explosion.Explosion;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joml.Vector3d;

public class Class3069 {
    private static MinecraftClient field161;
    private static final Vec3d field162;
    private static Explosion field163;
    private static RaycastContext field164;

    public Class3069() {
        super();
    }

    @EventHandler
    private static void method6002(final GameJoinEvent gameJoinEvent) {
        Class3069.field161 = MinecraftClient.getInstance();
        Class3069.field163 = new Explosion(Class3069.field161.world, null, 0.0, 0.0, 0.0, 6.0f, false, Explosion.DestructionType.DESTROY);
        Class3069.field164 = new RaycastContext(null, null, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, Class3069.field161.player);
    }

    public static double method6003(final LivingEntity entity, final Vec3d crystal, final int extrapolation, final BlockPos obsidianPos, final boolean ignoreTerrain) {
        if (entity == null) {
            return 0.0;
        }
        if (entity instanceof PlayerEntity && Class5926.method101((PlayerEntity) entity) == GameMode.CREATIVE && !(entity instanceof FakePlayerEntity)) {
            return 0.0;
        }
        ((IVec3d) Class3069.field162).boze$set(entity.getPos().x, entity.getPos().y, entity.getPos().z);
        if (extrapolation > 0) {
            if (entity == FakePlayer.INSTANCE.fakePlayer && FakePlayer.INSTANCE.isEnabled() && FakePlayer.INSTANCE.move.getValue() && !FakePlayer.INSTANCE.positions.isEmpty()) {
                final int n = FakePlayer.INSTANCE.field2946 + extrapolation;
                if (n >= FakePlayer.INSTANCE.positions.size()) {
                    final int n2 = 0;
                    if (FakePlayer.INSTANCE.loop.getValue()) {
                        final FakePositions fakePositions = FakePlayer.INSTANCE.positions.get(n2);
                        ((IVec3d) Class3069.field162).boze$set(fakePositions.method2174().getX(), fakePositions.method2174().getY(), fakePositions.method2174().getZ());
                    }
                } else {
                    final FakePositions fakePositions2 = FakePlayer.INSTANCE.positions.get(n);
                    ((IVec3d) Class3069.field162).boze$set(fakePositions2.method2174().getX(), fakePositions2.method2174().getY(), fakePositions2.method2174().getZ());
                }
            } else {
                final Vector3d method55 = Class5921.method55(entity);
                ((IVec3d) Class3069.field162).boze$set(Class3069.field162.x + method55.x, Class3069.field162.y + method55.y, Class3069.field162.z + method55.z);
            }
        }
        final double sqrt = Math.sqrt(Class3069.field162.squaredDistanceTo(crystal));
        if (sqrt > 12.0) {
            return 0.0;
        }
        final double n3 = (1.0 - sqrt / 12.0) * RaycastUtil.method576(crystal, entity, Class3069.field162, entity.getBoundingBox(), Class3069.field164, obsidianPos, ignoreTerrain);
        double n4 = (n3 * n3 + n3) / 2.0 * 7.0 * 12.0 + 1.0;
        if (entity instanceof PlayerEntity) {
            n4 = method6007(n4);
        }
        try {
            n4 = DamageUtil.getDamageLeft(entity, (float) n4, Class3069.field161.world.getDamageSources().explosion(null), (float) entity.getArmor(), (float) entity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());
            n4 = method6009(entity, n4);
            ((IExplosion) Class3069.field163).boze$set(crystal, 6.0f, false);
            n4 = method6008(entity, n4, Class3069.field163);
        } catch (final Exception ex) {
        }
        return (n4 < 0.0) ? 0.0 : n4;
    }

    public static double method6004(final PlayerEntity player, final Vec3d crystal) {
        return method6003(player, crystal, 0, null, false);
    }

    public static double method6005(final LivingEntity player, final Vec3d bed, final BlockPos pos, final boolean ignoreTerrain) {
        if (player instanceof PlayerEntity && Class5926.method101((PlayerEntity) player) == GameMode.CREATIVE && !(player instanceof FakePlayerEntity)) {
            return 0.0;
        }
        ((IVec3d) Class3069.field162).boze$set(player.getPos().x, player.getPos().y, player.getPos().z);
        final double sqrt = Math.sqrt(player.squaredDistanceTo(bed));
        if (sqrt > 10.0) {
            return 0.0;
        }
        final double n = (1.0 - sqrt / 10.0) * RaycastUtil.method576(bed, player, Class3069.field162, player.getBoundingBox(), Class3069.field164, pos, ignoreTerrain);
        double n2 = (n * n + n) / 2.0 * 7.0 * 10.0 + 1.0;
        if (player instanceof PlayerEntity) {
            n2 = method6007(n2);
        }
        try {
            n2 = DamageUtil.getDamageLeft(player, (float) n2, Class3069.field161.world.getDamageSources().explosion(null), (float) player.getArmor(), (float) player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());
            n2 = method6009(player, n2);
            ((IExplosion) Class3069.field163).boze$set(bed, 5.0f, true);
            n2 = method6008(player, n2, Class3069.field163);
        } catch (final Exception ex) {
        }
        if (n2 < 0.0) {
            n2 = 0.0;
        }
        if (AutoBed.INSTANCE.targetMode.getValue() == BedTargetMode.Semi && player != Class3069.field161.player && player.getBoundingBox().contains(bed)) {
            n2 += 4000.0;
        }
        return n2;
    }

    public static double method6006(final PlayerEntity player, final Vec3d anchor, final BlockPos pos) {
        if (player.getAbilities().creativeMode) {
            return 0.0;
        }
        final double sqrt = Math.sqrt(player.squaredDistanceTo(anchor));
        if (sqrt > 10.0) {
            return 0.0;
        }
        final BlockState blockState = Class3069.field161.world.getBlockState(pos);
        Class3069.field161.world.removeBlock(pos, false);
        final double n = (1.0 - sqrt / 10.0) * Explosion.getExposure(anchor, player);
        final double n2 = DamageUtil.getDamageLeft(player, (float) method6009(player, method6007((n * n + n) / 2.0 * 7.0 * 10.0 + 1.0)), Class3069.field161.world.getDamageSources().explosion(null), (float) player.getArmor(), (float) player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());
        ((IExplosion) Class3069.field163).boze$set(anchor, 5.0f, true);
        double method6008 = method6008(player, n2, Class3069.field163);
        if (method6008 < 0.0) {
            method6008 = 0.0;
        }
        Class3069.field161.world.setBlockState(pos, blockState);
        return method6008;
    }

    private static double method6007(final double b) {
        return switch (Class3069.field161.world.getDifficulty()) {
            case Difficulty.PEACEFUL -> 0.0;
            case Difficulty.EASY -> Math.min(b / 2.0 + 1.0, b);
            case Difficulty.HARD -> b * 3.0 / 2.0;
            default -> b;
        };
    }

    private static double method6008(final LivingEntity livingEntity, double n, final Explosion explosion) {
        float n2 = (float) method6010(livingEntity.getArmorItems());
        if (n2 > 20.0f) {
            n2 = 20.0f;
        }
        n *= 1.0 - n2 / 25.0;
        return (n < 0.0) ? 0.0 : n;
    }

    private static double method6009(final LivingEntity livingEntity, double n) {
        if (livingEntity.hasStatusEffect(StatusEffects.RESISTANCE)) {
            n *= 1.0 - (livingEntity.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 0.2;
        }
        return (n < 0.0) ? 0.0 : n;
    }

    public static int method6010(final Iterable<ItemStack> equipment) {
        final MutableInt mutableInt = new MutableInt();
        equipment.forEach(eqp -> lambda$getProtectionAmount$0(mutableInt, eqp));
        return mutableInt.intValue();
    }

    public static int method6011(final ItemStack stack) {
        return EnchantmentHelper.getLevel(Class3069.field161.world.getRegistryManager().get(Enchantments.BLAST_PROTECTION.getRegistryRef()).getEntry(Enchantments.BLAST_PROTECTION).get(), stack) * 2 + EnchantmentHelper.getLevel(Class3069.field161.world.getRegistryManager().get(Enchantments.PROTECTION.getRegistryRef()).getEntry(Enchantments.PROTECTION).get(), stack);
    }

    private static void lambda$getProtectionAmount$0(final MutableInt mutableInt, final ItemStack stack) {
        mutableInt.add(method6011(stack));
    }

    static {
        field162 = new Vec3d(0.0, 0.0, 0.0);
    }
}
