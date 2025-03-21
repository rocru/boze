package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.ac.Grim;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RaycastUtil;
import mapped.Class3062;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

import java.util.HashSet;
import java.util.Set;

class NCPPlace implements IPlace, IMinecraft {
    private final AutoCrystal field219;
    private final PlaceHelper field220;

    NCPPlace(AutoCrystal var1, PlaceHelper var2) {
        this.field219 = var1;
        this.field220 = var2;
    }

    @Override
    public BlockHitResult method113(BlockPos candidatePos) {
        Box var4 = new Box(candidatePos);
        ClientPlayerEntityAccessor var5 = (ClientPlayerEntityAccessor)mc.player;
        Vec3d var6 = new Vec3d(var5.getLastX(), var5.getLastY(), var5.getLastZ());
        Vec3d var7 = this.method145(var5.getLastPitch(), var5.getLastYaw());
        Pair var8 = this.method135(var4, var6, var7);
        return var8 != null ? new BlockHitResult((Vec3d)var8.getLeft(), (Direction)var8.getRight(), candidatePos, var4.contains(var6)) : null;
    }

    @Override
    public BlockHitResult method114(BlockPos candidatePos) {
        Set var5 = this.method121(candidatePos);
        if (!var5.isEmpty()) {
            BlockHitResult var6 = this.method122(candidatePos, var5);
            if (var6 != null) {
                return var6;
            }
        }

        boolean var14 = candidatePos.getY() == mc.world.getTopY() - 1;
        Box var7 = new Box(candidatePos);
        Vec3d var8 = mc.player.getPos();
        Vec3d var9 = var8.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
        Vec3d var10 = this.method123(var7, var9);
        double var11 = var9.distanceTo(var10);
        if (var11 > (double)this.field219.autoCrystalPlace.field138.getValue()) {
            return null;
        } else {
            if (var11 > (double)this.field219.autoCrystalPlace.field139.getValue()) {
                BlockHitResult var13 = mc.world.raycast(new RaycastContext(var9, var10, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
                if (var13 != null && var13.getType() == Type.BLOCK && !var13.getBlockPos().equals(candidatePos)) {
                    return null;
                }
            }

            Direction var15 = Direction.getFacing(var9.x - var10.x, var9.y - var10.y, var9.z - var10.z);
            if (var14 && var15 == Direction.UP) {
                var15 = Direction.DOWN;
            }

            return new BlockHitResult(var10, var15, candidatePos, var7.contains(var9));
        }
    }

    Set<Direction> method121(BlockPos var1) {
        HashSet var5 = new HashSet();

        for (Direction var9 : Direction.values()) {
            if (Grim.field1831.method568(var1, var9)) {
                var5.add(var9);
            }
        }

        return var5;
    }

    BlockHitResult method122(BlockPos var1, Set<Direction> var2) {
        Vec3d var6 = new Vec3d((double)var1.getX() + 0.5, (double)var1.getY() + 1.0, (double)var1.getZ() + 0.5);
        Box var7 = EntityType.END_CRYSTAL.getDimensions().getBoxAt(var6);
        BlockHitResult var8 = null;
        double var9 = Double.MAX_VALUE;
        Box var11 = new Box(var1);
        Vec3d var12 = mc.player.getPos();
        Vec3d var13 = var12.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);

        for (Direction var15 : var2) {
            Vec3d[] var16 = BlockSelection.method1452(var1, var15, 0.05);

            for (Vec3d var20 : var16) {
                double var21 = var13.distanceTo(var20);
                if (!(var21 > (double)this.field219.autoCrystalPlace.field138.getValue())) {
                    if (var21 > (double)this.field219.autoCrystalPlace.field139.getValue()) {
                        BlockHitResult var23 = mc.world.raycast(new RaycastContext(var13, var20, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
                        if (var23 != null && var23.getType() == Type.BLOCK && !var23.getBlockPos().equals(var1)) {
                            continue;
                        }
                    }

                    Pair var26 = this.method138(var7, var12, var20);
                    if (var26 != null && var26.getLeft() != null && var26.getRight() != null) {
                        double var24 = var13.distanceTo((Vec3d)var26.getLeft());
                        if (var24 < var9
                                && var24 <= (double)this.field219.autoCrystalBreak.field178.getValue()
                                && (
                                var24 <= (double)this.field219.autoCrystalBreak.field179.getValue()
                                        || RaycastUtil.method117(var13, (Vec3d)var26.getLeft())
                        )) {
                            var8 = new BlockHitResult(var20, var15, var1, var11.contains(var13));
                            var9 = var24;
                        }
                    }
                }
            }
        }

        return var8;
    }

    @Override
    public Vec3d method115(Vec3d endCrystalPos) {
        Box var5 = EntityType.END_CRYSTAL.getDimensions().getBoxAt(endCrystalPos);
        Vec3d var6 = mc.player.getPos();
        Vec3d var7 = var6.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
        double var8 = var7.distanceTo(endCrystalPos);
        if (!(var8 <= (double)this.field219.autoCrystalBreak.field178.getValue())
                || !(var8 <= (double)this.field219.autoCrystalBreak.field179.getValue()) && !RaycastUtil.method117(var7, endCrystalPos)) {
            Vec3d var10 = this.method123(var5, var6.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0));
            double var11 = var7.distanceTo(var10);
            return !(var11 <= (double)this.field219.autoCrystalBreak.field178.getValue())
                    || !(var11 <= (double)this.field219.autoCrystalBreak.field179.getValue()) && !RaycastUtil.method117(var7, var10)
                    ? null
                    : var10;
        } else {
            return endCrystalPos;
        }
    }

    @Override
    public boolean method116(Vec3d endCrystalPos) {
        ClientPlayerEntityAccessor var4 = (ClientPlayerEntityAccessor)mc.player;
        Vec3d var5 = new Vec3d(var4.getLastX(), var4.getLastY(), var4.getLastZ());
        return this.method119(endCrystalPos, var5, new float[]{var4.getLastYaw(), var4.getLastPitch()});
    }

    @Override
    public boolean method118(Vec3d endCrystalPos, float[] rotation) {
        Vec3d var5 = mc.player.getPos();
        return this.method119(endCrystalPos, var5, rotation);
    }

    @Override
    public boolean method119(Vec3d endCrystalPos, Vec3d playerPos, float[] rotation) {
        Vec3d var7 = this.method145(rotation[1], rotation[0]);
        Box var8 = EntityType.END_CRYSTAL.getDimensions().getBoxAt(endCrystalPos);
        Vec3d var9 = this.method136(var8, playerPos, var7);
        if (var9 == null) {
            return false;
        } else {
            Vec3d var10 = playerPos.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
            double var11 = var10.distanceTo(var9);
            return var11 <= (double)this.field219.autoCrystalBreak.field178.getValue()
                    && (var11 <= (double)this.field219.autoCrystalBreak.field179.getValue() || RaycastUtil.method117(var10, var9));
        }
    }

    @Override
    public boolean method117(Vec3d endCrystalPos, Vec3d playerPos) {
        return this.method115(endCrystalPos) != null;
    }

    private Vec3d method123(Box var1, Vec3d var2) {
        return Class3062.method5991(var2, var1);
    }

    private Pair<Vec3d, Direction> method135(Box var1, Vec3d var2, Vec3d var3) {
        Pair var7 = null;
        double var8 = Double.MAX_VALUE;
        Vec3d var10 = var2.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
        Vec3d var11 = var10.add(var3.getX() * 6.0, var3.getY() * 6.0, var3.getZ() * 6.0);
        Pair var12 = this.method138(var1, var10, var11);
        if (var12.getLeft() != null) {
            if (this.method137(var1, var10)) {
                var8 = 0.0;
                var7 = var12;
            }

            double var13 = var10.distanceTo((Vec3d)var12.getLeft());
            if (var13 < var8) {
                var7 = var12;
                var8 = var13;
            }
        }

        return var8 > (double)this.field219.autoCrystalPlace.field138.getValue() ? null : var7;
    }

    private Vec3d method136(Box var1, Vec3d var2, Vec3d var3) {
        Vec3d var6 = var2.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
        Vec3d var7 = var6.add(var3.getX() * 6.0, var3.getY() * 6.0, var3.getZ() * 6.0);
        return (Vec3d)this.method138(var1, var6, var7).getLeft();
    }

    private boolean method137(Box var1, Vec3d var2) {
        return var2.getX() > var1.minX
                && var2.getX() < var1.maxX
                && var2.getY() > var1.minY
                && var2.getY() < var1.maxY
                && var2.getZ() > var1.minZ
                && var2.getZ() < var1.maxZ;
    }

    private Pair<Vec3d, Direction> method138(Box var1, Vec3d var2, Vec3d var3) {
        Vec3d var7 = this.method139(var2, var3, var1.minX);
        Vec3d var8 = this.method139(var2, var3, var1.maxX);
        Vec3d var9 = this.method140(var2, var3, var1.minY);
        Vec3d var10 = this.method140(var2, var3, var1.maxY);
        Vec3d var11 = this.method141(var2, var3, var1.minZ);
        Vec3d var12 = this.method141(var2, var3, var1.maxZ);
        Direction var13 = null;
        if (this.field219.field1041.field210.getValue()) {
            if (!this.method142(var1, var7)) {
                var7 = null;
            }

            if (!this.method142(var1, var8)) {
                var8 = null;
            }

            if (!this.method143(var1, var9)) {
                var9 = null;
            }

            if (!this.method143(var1, var10)) {
                var10 = null;
            }

            if (!this.method144(var1, var11)) {
                var11 = null;
            }

            if (!this.method144(var1, var12)) {
                var12 = null;
            }
        }

        Vec3d var14 = null;
        if (var7 != null) {
            var14 = var7;
            var13 = Direction.WEST;
        }

        if (var8 != null && (var14 == null || var2.distanceTo(var8) < var2.distanceTo(var14))) {
            var14 = var8;
            var13 = Direction.EAST;
        }

        if (var9 != null && (var14 == null || var2.distanceTo(var9) < var2.distanceTo(var14))) {
            var14 = var9;
            var13 = Direction.DOWN;
        }

        if (var10 != null && (var14 == null || var2.distanceTo(var10) < var2.distanceTo(var14))) {
            var14 = var10;
            var13 = Direction.UP;
        }

        if (var11 != null && (var14 == null || var2.distanceTo(var11) < var2.distanceTo(var14))) {
            var14 = var11;
            var13 = Direction.NORTH;
        }

        if (var12 != null && (var14 == null || var2.distanceTo(var12) < var2.distanceTo(var14))) {
            var14 = var12;
            var13 = Direction.SOUTH;
        }

        return new Pair(var14, var13);
    }

    private Vec3d method139(Vec3d var1, Vec3d var2, double var3) {
        double var8 = var2.getX() - var1.getX();
        double var10 = var2.getY() - var1.getY();
        double var12 = var2.getZ() - var1.getZ();
        if (var8 * var8 < 1.0E-7F) {
            return null;
        } else {
            double var14 = (var3 - var1.getX()) / var8;
            return var14 >= 0.0 && var14 <= 1.0 ? new Vec3d(var1.getX() + var8 * var14, var1.getY() + var10 * var14, var1.getZ() + var12 * var14) : null;
        }
    }

    private Vec3d method140(Vec3d var1, Vec3d var2, double var3) {
        double var8 = var2.getX() - var1.getX();
        double var10 = var2.getY() - var1.getY();
        double var12 = var2.getZ() - var1.getZ();
        if (var10 * var10 < 1.0E-7F) {
            return null;
        } else {
            double var14 = (var3 - var1.getY()) / var10;
            return var14 >= 0.0 && var14 <= 1.0 ? new Vec3d(var1.getX() + var8 * var14, var1.getY() + var10 * var14, var1.getZ() + var12 * var14) : null;
        }
    }

    private Vec3d method141(Vec3d var1, Vec3d var2, double var3) {
        double var8 = var2.getX() - var1.getX();
        double var10 = var2.getY() - var1.getY();
        double var12 = var2.getZ() - var1.getZ();
        if (var12 * var12 < 1.0E-7F) {
            return null;
        } else {
            double var14 = (var3 - var1.getZ()) / var12;
            return var14 >= 0.0 && var14 <= 1.0 ? new Vec3d(var1.getX() + var8 * var14, var1.getY() + var10 * var14, var1.getZ() + var12 * var14) : null;
        }
    }

    private boolean method142(Box var1, Vec3d var2) {
        return var2 != null && var2.getY() >= var1.minY && var2.getY() <= var1.maxY && var2.getZ() >= var1.minZ && var2.getZ() <= var1.maxZ;
    }

    private boolean method143(Box var1, Vec3d var2) {
        return var2 != null && var2.getX() >= var1.minX && var2.getX() <= var1.maxX && var2.getZ() >= var1.minZ && var2.getZ() <= var1.maxZ;
    }

    private boolean method144(Box var1, Vec3d var2) {
        return var2 != null && var2.getX() >= var1.minX && var2.getX() <= var1.maxX && var2.getY() >= var1.minY && var2.getY() <= var1.maxY;
    }

    private Vec3d method145(float var1, float var2) {
        float var3 = var1 * (float) (Math.PI / 180.0);
        float var4 = -var2 * (float) (Math.PI / 180.0);
        float var5 = MathHelper.cos(var4);
        float var6 = MathHelper.sin(var4);
        float var7 = MathHelper.cos(var3);
        float var8 = MathHelper.sin(var3);
        return new Vec3d((double)(var6 * var7), (double)(-var8), (double)(var5 * var7));
    }
}
