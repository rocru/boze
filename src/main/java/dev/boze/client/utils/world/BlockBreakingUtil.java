package dev.boze.client.utils.world;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.ItemEnchantmentUtils;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class BlockBreakingUtil implements IMinecraft {
    public static float method506(BlockPos pos, ItemStack stack) {
        return method507(pos, stack, false);
    }

    public static float method507(BlockPos pos, ItemStack stack, boolean ignoreAir) {
        BlockState var6 = mc.world.getBlockState(pos);
        float var7 = var6.getHardness(mc.world, pos);
        if (var7 == -1.0F) {
            return 0.0F;
        } else {
            int var8 = method508(var6, stack) ? 30 : 100;
            return method509(var6, stack, ignoreAir) / var7 / (float) var8;
        }
    }

    public static boolean method508(BlockState state, ItemStack stack) {
        return !state.isToolRequired() || stack.isSuitableFor(state);
    }

    private static float method509(BlockState var0, ItemStack var1, boolean var2) {
        float var6 = var1.getMiningSpeedMultiplier(var0);
        if (var6 > 1.0F) {
            float var7 = (float) MathHelper.square(ItemEnchantmentUtils.getEnchantmentLevel(var1, Enchantments.EFFICIENCY)) + 1.0F;
            var6 += var7;
        }

        if (StatusEffectUtil.hasHaste(mc.player)) {
            var6 *= 1.0F + (float) (StatusEffectUtil.getHasteAmplifier(mc.player) + 1) * 0.2F;
        }

        if (mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            var6 *= switch (mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
        }

        if (mc.player.isSubmergedIn(FluidTags.WATER)) {
            var6 *= (float) mc.player.getAttributeInstance(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED).getValue();
        }

        if (!mc.player.isOnGround() && !var2) {
            var6 /= 5.0F;
        }

        return var6;
    }
}
