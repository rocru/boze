package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.Boze;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.AutoCrystalAction;
import dev.boze.client.enums.AutoCrystalMaxDamage;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import mapped.Class3087;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class CrystalHelper implements IMinecraft {
    private final AutoCrystal field1626;

    public CrystalHelper(AutoCrystal var1) {
        this.field1626 = var1;
    }

    public Pair<Class3087, Double> method724(BlockPos blockPos) {
        boolean var5 = blockPos.getY() == mc.world.getTopY() - 1;
        if (!this.method2101(blockPos)) {
            return null;
        } else {
            BlockPos var6 = blockPos.up();
            if (!var5 && !this.method2102(var6)) {
                return null;
            } else {
                BlockPos var7 = blockPos.up(2);
                if (!var5 && this.field1626.protocol.getValue() && !this.method2102(var7)) {
                    return null;
                } else {
                    double var8 = this.field1626.protocol.getValue() ? 2.0 : 1.0;
                    Box var10 = new Box(blockPos).stretch(0.0, var8, 0.0);
                    if (this.method726(var10)) {
                        return null;
                    } else {
                        Vec3d var11 = this.field1626.ac.method510(mc.player, AutoCrystalAction.Place);
                        Vec3d var12 = var11.add(0.0, mc.player.getEyeHeight(mc.player.getPose()), 0.0);
                        Vec3d var13 = this.method725(blockPos);
                        float[] var14 = EntityUtil.method2147(var12, var13);
                        boolean var15 = true;
                        Vec3d var16 = new Vec3d((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 1.0, (double) blockPos.getZ() + 0.5);
                        if (!this.field1626.field1041.method119(var16, var11, var14)) {
                            var15 = false;
                            if (!this.field1626.field1041.method117(var16, var11)) {
                                return null;
                            }
                        }

                        BlockHitResult var17 = this.field1626.field1041.method114(blockPos);
                        if (var17 == null) {
                            return null;
                        } else {
                            double var18 = this.field1626.field1047.method5665(mc.player, AutoCrystalAction.Full, var16, blockPos, true);
                            return (double) (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= var18 + 2.0
                                    ? null
                                    : new Pair(new Class3087(blockPos, var17, var15), var18);
                        }
                    }
                }
            }
        }
    }

    private boolean method2101(BlockPos var1) {
        Block var5 = mc.world.getBlockState(var1).getBlock();
        return var5 == Blocks.BEDROCK || var5 == Blocks.OBSIDIAN;
    }

    public boolean method2102(BlockPos var1) {
        return mc.world.getBlockState(var1).isAir();
    }

    public Vec3d method725(BlockPos var1) {
        if (this.field1626.field1041.field205.getValue() == AnticheatMode.Grim) {
            Set var5 = this.field1626.field1041.grim.method121(var1);
            if (!var5.isEmpty()) {
                BlockHitResult var6 = this.field1626.field1041.grim.method122(var1, var5);
                if (var6 != null) {
                    return var6.getPos();
                }
            }
        }

        return new Vec3d((double) var1.getX() + 0.5, (double) var1.getY() + 1.0, (double) var1.getZ() + 0.5);
    }

    public boolean method726(Box var1) {
        try {
            for (Entity var6 : mc.world.getEntities()) {
                if (var6 != null) {
                    if (var6 instanceof EndCrystalEntity var7) {
                        if (var1.intersects(var6.getBoundingBox()) && this.method728(var7)) {
                            return true;
                        }
                    } else if (var6 instanceof PlayerEntity var8) {
                        if (var1.intersects(this.field1626.ac.method511(var8, AutoCrystalAction.Place))) {
                            return true;
                        }
                    } else if (var1.intersects(var6.getBoundingBox())) {
                        return true;
                    }
                }
            }
        } catch (Exception var9) {
        }

        return false;
    }

    public boolean method727(double var1) {
        return this.field1626.field1042.field118.getValue()
                || this.field1626.field1042.field115.getValue() == AutoCrystalMaxDamage.Balance
                || !(var1 > (double) this.field1626.field1042.field116.getValue().floatValue());
    }

    private boolean method728(EndCrystalEntity var1) {
        if ((float) var1.age < this.field1626.ticksExisted.getValue()) {
            return false;
        } else if (((IEndCrystalEntity) var1).boze$isAbandoned()) {
            return true;
        } else {
            return (double) (System.currentTimeMillis() - ((IEndCrystalEntity) var1).boze$getLastAttackTime()) > Boze.getModules().field905.field1519
                    && var1 != this.field1626.autoCrystalTracker.field1529 && var1.age >= this.field1626.hitTicks.getValue();
        }
    }
}
