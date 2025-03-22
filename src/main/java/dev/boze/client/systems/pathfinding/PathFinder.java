package dev.boze.client.systems.pathfinding;

import dev.boze.client.systems.modules.movement.BoatFly;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PathFinder implements IMinecraft {
    private static final EntityDimensions field3879 = EntityDimensions.fixed(1.375F, 0.5625F);
    public final BlockPos field3882;
    protected final HashMap<PathPos, PathPos> field3889 = new HashMap();
    private final PathPos field3880;
    private final PathRules field3887;
    private final HashMap<PathPos, Float> field3888 = new HashMap();
    private final PathQueue field3890 = new PathQueue();
    private final ArrayList<PathPos> field3896 = new ArrayList();
    protected PathPos field3881;
    protected int field3891 = 8192;
    protected int field3892 = 20;
    protected boolean field3894;
    protected boolean field3895;
    private Vec3d field3883;
    private Vec3d field3884;
    private double field3885;
    private boolean field3886;
    private int field3893;

    public PathFinder(BlockPos goal, PathRules rules) {
        if (mc.player.isOnGround()) {
            this.field3880 = new PathPos(BlockPos.ofFloored(mc.player.getX(), mc.player.getY() + 0.5, mc.player.getZ()));
        } else {
            this.field3880 = new PathPos(BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ()));
        }

        this.field3882 = goal;
        this.field3887 = rules;
        this.field3888.put(new PathPos(this.field3880), 0.0F);
        this.field3890.method2124(this.field3880, this.method2119(this.field3880));
    }

    public PathFinder(BlockPos startPos, BlockPos goal, PathRules rules) {
        this.field3880 = new PathPos(startPos);
        this.field3882 = goal;
        this.field3887 = rules;
        this.field3888.put(new PathPos(this.field3880), 0.0F);
        this.field3890.method2124(this.field3880, this.method2119(this.field3880));
    }

    private static boolean lambda$canGoThrough$0(Entity var0) {
        return !(var0 instanceof BoatEntity) && !(var0 instanceof EndCrystalEntity);
    }

    public void method2098() {
        if (!this.field3894) {
            int var4;
            for (var4 = 0; var4 < this.field3891 && !this.method2116(); var4++) {
                this.field3881 = this.field3890.method2128();
                if (this.method2115()) {
                    return;
                }

                for (PathPos var6 : this.method2100(this.field3881)) {
                    float var7 = this.field3888.get(this.field3881) + this.method2099(this.field3881, var6);
                    if (!this.field3888.containsKey(var6) || !(this.field3888.get(var6) <= var7)) {
                        this.field3888.put(var6, var7);
                        this.field3889.put(var6, this.field3881);
                        this.field3890.method2124(var6, var7 + this.method2119(var6));
                    }
                }
            }

            this.field3893 += var4;
        }
    }

    private float method2099(BlockPos var1, BlockPos var2) {
        float[] var6 = new float[]{0.5F, 0.5F};
        BlockPos[] var7 = new BlockPos[]{var1, var2};

        for (int var8 = 0; var8 < var7.length; var8++) {
            BlockPos var9 = var7[var8];
            Block var10 = mc.world.getBlockState(var9).getBlock();
            if (var10 == Blocks.WATER) {
                var6[var8] *= 1.3164438F;
            } else if (var10 == Blocks.LAVA) {
                var6[var8] *= 4.5395155F;
            }

            if (!this.field3887.isFlying() && mc.world.getBlockState(var9.down()).getBlock() instanceof SoulSandBlock) {
                var6[var8] *= 2.5F;
            }
        }

        float var11 = var6[0] + var6[1];
        if (var1.getX() != var2.getX() && var1.getZ() != var2.getZ()) {
            var11 *= 1.4142135F;
        }

        return var11;
    }

    private ArrayList<PathPos> method2100(PathPos var1) {
        ArrayList var5 = new ArrayList();
        if (Math.abs(this.field3880.getX() - var1.getX()) <= 256 && Math.abs(this.field3880.getZ() - var1.getZ()) <= 256) {
            if (this.field3886) {
                Vec3d var6 = new Vec3d((double) var1.getX() + 0.5, (double) var1.getY() + 0.5, (double) var1.getZ() + 0.5);
                Vec3d var7 = this.method2122(var6, this.field3883, this.field3884);
                if (var6.distanceTo(var7) > this.field3885) {
                    return var5;
                }
            }

            BlockPos var17 = var1.north();
            BlockPos var18 = var1.east();
            BlockPos var8 = var1.south();
            BlockPos var9 = var1.west();
            BlockPos var10 = var17.east();
            BlockPos var11 = var8.east();
            BlockPos var12 = var8.west();
            BlockPos var13 = var17.west();
            BlockPos var14 = var1.up();
            BlockPos var15 = var1.down();
            boolean var16 = this.method2101(var15);
            if (this.field3887.isFlying() || var16 || var1.isJumping() || this.method2102(var1) || this.method2103(var1.down())) {
                if (this.method2104(var1, var17)) {
                    var5.add(new PathPos(var17));
                }

                if (this.method2104(var1, var18)) {
                    var5.add(new PathPos(var18));
                }

                if (this.method2104(var1, var8)) {
                    var5.add(new PathPos(var8));
                }

                if (this.method2104(var1, var9)) {
                    var5.add(new PathPos(var9));
                }

                if (this.method2105(var1, Direction.NORTH, Direction.EAST)) {
                    var5.add(new PathPos(var10));
                }

                if (this.method2105(var1, Direction.SOUTH, Direction.EAST)) {
                    var5.add(new PathPos(var11));
                }

                if (this.method2105(var1, Direction.SOUTH, Direction.WEST)) {
                    var5.add(new PathPos(var12));
                }

                if (this.method2105(var1, Direction.NORTH, Direction.WEST)) {
                    var5.add(new PathPos(var13));
                }
            }

            if (!this.field3887.shouldBoat()) {
                if (var1.getY() < mc.world.getTopY()
                        && this.method2110(var14.up())
                        && (this.field3887.isFlying() || var16 || this.method2103(var1))
                        && (
                        this.field3887.isFlying()
                                || this.method2103(var1)
                                || this.field3882.equals(var14)
                                || this.method2112(var17)
                                || this.method2112(var18)
                                || this.method2112(var8)
                                || this.method2112(var9)
                )) {
                    var5.add(new PathPos(var14, var16));
                }

                if (var1.getY() > mc.world.getBottomY()
                        && this.method2110(var15)
                        && this.method2111(var15.down())
                        && (this.field3887.isFlying() || this.method2113(var1))) {
                    var5.add(new PathPos(var15));
                }
            }

            return var5;
        } else {
            return var5;
        }
    }

    protected boolean method2101(BlockPos pos) {
        BlockState var5 = mc.world.getBlockState(pos);
        Block var6 = var5.getBlock();
        return var5.blocksMovement() && !(var6 instanceof AbstractSignBlock)
                || var6 instanceof LadderBlock
                || this.field3887.hasJesus() && (var6 == Blocks.WATER || var6 == Blocks.LAVA);
    }

    private boolean method2102(BlockPos var1) {
        Block var5 = mc.world.getBlockState(var1).getBlock();
        if (!(var5 instanceof FluidBlock) && !(var5 instanceof LadderBlock) && !(var5 instanceof VineBlock) && !(var5 instanceof CobwebBlock)) {
            Block var6 = mc.world.getBlockState(var1.up()).getBlock();
            return var6 instanceof FluidBlock || var6 instanceof CobwebBlock;
        } else {
            return true;
        }
    }

    private boolean method2103(BlockPos var1) {
        Block var5 = mc.world.getBlockState(var1).getBlock();
        if (!(var5 instanceof LadderBlock) && !(var5 instanceof VineBlock)) {
            return false;
        } else {
            BlockPos var6 = var1.up();
            return this.method2101(var1.north())
                    || this.method2101(var1.east())
                    || this.method2101(var1.south())
                    || this.method2101(var1.west())
                    || this.method2101(var6.north())
                    || this.method2101(var6.east())
                    || this.method2101(var6.south())
                    || this.method2101(var6.west());
        }
    }

    private boolean method2104(BlockPos var1, BlockPos var2) {
        return this.method2106(var2) && (this.field3887.isFlying() || this.method2110(var2.down()) || this.method2112(var2.down()));
    }

    private boolean method2105(BlockPos var1, Direction var2, Direction var3) {
        if (this.field3887.shouldBoat() && !BoatFly.INSTANCE.field3172.getValue()) {
            return false;
        } else {
            BlockPos var7 = var1.offset(var2);
            BlockPos var8 = var1.offset(var3);
            BlockPos var9 = var7.offset(var3);
            return this.method2107(var7) && this.method2107(var8) && this.method2104(var1, var9);
        }
    }

    protected boolean method2106(BlockPos pos) {
        if (!this.method2108(pos, true)) {
            return false;
        } else {
            BlockPos var5 = pos.up();
            return this.method2110(var5) && this.method2111(pos.down());
        }
    }

    protected boolean method2107(BlockPos pos) {
        if (!this.method2108(pos, true)) {
            return false;
        } else {
            BlockPos var5 = pos.up();
            return this.method2110(var5) && this.method2111(pos.down());
        }
    }

    private boolean method2108(BlockPos var1, boolean var2) {
        if (this.field3887.shouldBoat() && var2) {
            double var6 = var1.getY();
            if (mc.player.getVehicle() instanceof BoatEntity var8) {
                var6 = var8.getY();
            }

            Box var15 = field3879.getBoxAt((double) var1.getX() + 0.5, var1.getY(), (double) var1.getZ() + 0.5);
            var15 = var15.expand(BoatFly.INSTANCE.field3176.getValue(), 0.0, BoatFly.INSTANCE.field3176.getValue());
            if (!mc.world.getOtherEntities(mc.player, var15, PathFinder::lambda$canGoThrough$0).isEmpty()) {
                return false;
            } else {
                double var17 = (double) var1.getY() + var15.getLengthY();
                int var11 = (int) Math.floor(var6);
                int var12 = (int) Math.ceil(var17);
                BlockPos var13 = new BlockPos(var1.getX(), var11, var1.getZ());
                BlockPos var14 = new BlockPos(var1.getX(), var12, var1.getZ());
                return var13.equals(var14) ? this.method2109(var13) : this.method2109(var13) && this.method2109(var14);
            }
        } else {
            return this.method2110(var1);
        }
    }

    private boolean method2109(BlockPos var1) {
        return this.method2110(var1)
                && this.method2110(var1.north())
                && this.method2110(var1.east())
                && this.method2110(var1.south())
                && this.method2110(var1.west())
                && this.method2110(var1.north().east())
                && this.method2110(var1.south().east())
                && this.method2110(var1.south().west())
                && this.method2110(var1.north().west());
    }

    private boolean method2110(BlockPos var1) {
        if (!mc.world.isChunkLoaded(var1)) {
            return false;
        } else {
            BlockState var5 = mc.world.getBlockState(var1);
            Block var6 = var5.getBlock();
            if (var5.blocksMovement() && !(var6 instanceof AbstractSignBlock)) {
                return false;
            } else {
                return !(var6 instanceof TripwireBlock) && !(var6 instanceof PressurePlateBlock) && (this.method2114() || var6 != Blocks.LAVA && !(var6 instanceof AbstractFireBlock));
            }
        }
    }

    private boolean method2111(BlockPos var1) {
        Block var5 = mc.world.getBlockState(var1).getBlock();
        return !(var5 instanceof FenceBlock) && !(var5 instanceof WallBlock) && !(var5 instanceof FenceGateBlock);
    }

    private boolean method2112(BlockPos var1) {
        if (!this.method2101(var1)) {
            return false;
        } else {
            BlockState var5 = mc.world.getBlockState(var1);
            Fluid var6 = var5.getFluidState().getFluid();
            return !(var5.getBlock() instanceof CactusBlock) && (!(var6 instanceof LavaFluid) || this.method2114());
        }
    }

    private boolean method2113(PathPos var1) {
        BlockPos var5 = var1.down(2);
        if (this.field3887.hasNoFall() && this.method2110(var5)) {
            return true;
        } else if (!this.method2112(var5)) {
            return false;
        } else if (this.field3887.hasNoFall()) {
            return true;
        } else if (mc.world.getBlockState(var5).getBlock() instanceof SlimeBlock) {
            return true;
        } else {
            Object var6 = var1;

            for (int var7 = 0; var7 <= 3; var7++) {
                if (var6 == null) {
                    return true;
                }

                if (!var1.up(var7).equals(var6)) {
                    return true;
                }

                BlockState var8 = mc.world.getBlockState((BlockPos) var6);
                Block var9 = var8.getBlock();
                if (var8.getFluidState().getFluid() instanceof WaterFluid
                        || var9 instanceof LadderBlock
                        || var9 instanceof VineBlock
                        || var9 instanceof CobwebBlock) {
                    return true;
                }

                var6 = this.field3889.get(var6);
            }

            return false;
        }
    }

    private boolean method2114() {
        return mc.player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }

    protected boolean method2115() {
        return this.field3894 = this.field3882.equals(this.field3881);
    }

    private boolean method2116() {
        return this.field3895 = this.field3890.method2123() || this.field3893 >= this.field3891 * this.field3892;
    }

    public boolean method2117() {
        return this.field3894;
    }

    public boolean method2118() {
        return this.field3895;
    }

    private float method2119(BlockPos var1) {
        float var2 = (float) Math.abs(var1.getX() - this.field3882.getX());
        float var3 = (float) Math.abs(var1.getY() - this.field3882.getY());
        float var4 = (float) Math.abs(var1.getZ() - this.field3882.getZ());
        return 1.001F * (var2 + var3 + var4 - 0.58578646F * Math.min(var2, var4));
    }

    public ArrayList<PathPos> method2120() {
        if (!this.field3894 && !this.field3895) {
            return null;
        } else if (!this.field3896.isEmpty()) {
            return this.field3896;
        } else {
            PathPos var4;
            if (!this.field3895) {
                var4 = this.field3881;
            } else {
                var4 = this.field3880;

                for (PathPos var6 : this.field3889.keySet()) {
                    if (this.method2119(var6) < this.method2119(var4) && (this.field3887.isFlying() || this.method2101(var6.down()))) {
                        var4 = var6;
                    }
                }
            }

            while (var4 != null) {
                this.field3896.add(var4);
                var4 = this.field3889.get(var4);
            }

            Collections.reverse(this.field3896);
            return this.field3896;
        }
    }

    public void method2121(Vec3d start, Vec3d direction, double maxDistance) {
        this.field3883 = start;
        this.field3884 = direction;
        this.field3885 = maxDistance;
        this.field3886 = true;
    }

    private Vec3d method2122(Vec3d var1, Vec3d var2, Vec3d var3) {
        Vec3d var4 = var1.subtract(var2);
        double var5 = var4.dotProduct(var3);
        return var2.add(var3.multiply(var5));
    }
}
