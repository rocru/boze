package dev.boze.client.ac;

import dev.boze.client.enums.PlayerAimPoint;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.utils.BoundingBoxUtil;
import dev.boze.client.utils.BoxUtil;
import dev.boze.client.utils.RaycastUtil;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.world.PositionUtil;
import mapped.Class1202;
import mapped.Class5917;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Ghost implements Anticheat {
    public static final Ghost field1313 = new Ghost();
    private PlayerAimPoint field1314 = null;
    private double field1315;
    private int field1316;

    public void method569(EnumSetting<PlayerAimPoint> aimPoint, MinMaxDoubleSetting scale, IntSetting resolution) {
        this.field1314 = aimPoint.getValue();
        this.field1315 = scale.method1295();
        this.field1316 = resolution.getValue();
    }

    private Vec3d method570(BlockPos var1, Direction var2, Box var3, Vec3d var4) {
        double var8 = Reach.method1614();
        Vec3d var11 = new Vec3d(var4.x, var4.y, var4.z);
        Vec3d var12 = var3.getCenter();
        Vec3d var13 = PositionUtil.getPlayerPosition();
        double var14 = 1.0
                - Class5917.method32(
                var13.distanceTo(var11) / Math.max(var8, var8 + GhostRotations.INSTANCE.field749.getValue()), 1.0 - GhostRotations.INSTANCE.field748.getValue()
        );
        var11 = var11.add(
                (var12.x - var11.x) * var14,
                (var12.y - var11.y) * (GhostRotations.INSTANCE.field758.getValue() ? 1.0 - var14 : 1.0) * GhostRotations.INSTANCE.field757.getValue(),
                (var12.z - var11.z) * var14
        );
        Vec3d var16 = new Vec3d(mc.player.prevX - mc.player.getX(), mc.player.prevY - mc.player.getY(), mc.player.prevZ - mc.player.getZ());
        if (var16.lengthSquared() > 0.0) {
            double var17 = GhostRotations.INSTANCE.field752.getValue() ? var16.horizontalLength() : 1.0;
            var11 = var11.add(
                    Math.sin((double) System.currentTimeMillis() * GhostRotations.INSTANCE.field755.getValue() * 0.01) * var17,
                    Math.cos((double) System.currentTimeMillis() * GhostRotations.INSTANCE.field756.getValue() * 0.01)
                            * (var16.y + var17 * GhostRotations.INSTANCE.field753.getValue()),
                    Math.sin((double) System.currentTimeMillis() * GhostRotations.INSTANCE.field755.getValue() * 0.01) * var17
            );
        }

        var11 = var11.subtract(var16.multiply(GhostRotations.INSTANCE.field751.getValue()));
        if (var13.getY() >= var4.getY()) {
            RotationHelper var22 = GhostRotations.INSTANCE.field760 != null ? GhostRotations.INSTANCE.field760 : new RotationHelper(mc.player);
            double var18 = (double) var22.method605(Class1202.method2391(mc.player.getEyePos(), var11)) / 255.0;
            var11 = var11.add(0.0, -var18 * var3.getLengthY() * (GhostRotations.INSTANCE.field750.getValue() - 1.0), 0.0);
        }

        double var23 = mc.player.getEyePos().squaredDistanceTo(var4);
        if (var23 <= var8 * var8) {
            while (mc.player.getEyePos().squaredDistanceTo(var11) > var8 * var8) {
                var11 = new Vec3d(
                        Class5917.method35(var11.x, var4.x, GhostRotations.INSTANCE.field746.getValue()),
                        Class5917.method35(var11.y, var4.y, GhostRotations.INSTANCE.field746.getValue()),
                        Class5917.method35(var11.z, var4.z, GhostRotations.INSTANCE.field746.getValue())
                );
            }

            if (!this.method571(var1, var2, var11)) {
                var11 = var4;
            }
        }

        return Class5917.method33(var11, var3);
    }

    @Override
    public BlockHitResult method565(ArrayList<BlockHitResult> results) {
        if (this.field1314 == PlayerAimPoint.Angle) {
            BlockHitResult var5 = null;
            double var6 = Double.MAX_VALUE;

            for (BlockHitResult var9 : results) {
                RotationHelper var10 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
                double var11 = Class1202.method2391(mc.player.getEyePos(), var9.getPos()).method1384()
                        - var10.method1384()
                        + (Class1202.method2391(mc.player.getEyePos(), var9.getPos()).method1385() - var10.method1385());
                if (var11 < var6) {
                    var5 = var9;
                    var6 = var11;
                }
            }

            return var5;
        } else {
            return NearestHitCalculator.method947(results);
        }
    }

    @Override
    public BlockHitResult method566(BlockPos pos, Direction direction) {
        Box var6 = BoundingBoxUtil.calculateScaledBoundingBox(pos, direction, this.field1315);
        Vec3d var7 = PositionUtil.getPlayerPosition();
        Vec3d var8 = this.method570(pos, direction, var6, BoxUtil.method2132(var7, var6));
        if (this.method571(pos, direction, var8)) {
            Box var20 = BoundingBoxUtil.getVoxelShapeBoundingBox(pos);
            return new BlockHitResult(var8, direction, pos, var20.contains(var7));
        } else {
            Vec3d[] var9 = this.method573(pos, direction, this.field1315, this.field1316);
            Vec3d var10 = null;
            double var11 = Double.MAX_VALUE;

            for (Vec3d var16 : var9) {
                Vec3d var17 = this.method570(pos, direction, var6, var16);
                if (this.method571(pos, direction, var17)) {
                    double var18 = var17.distanceTo(var7);
                    if (var18 < var11) {
                        var10 = var17;
                        var11 = var18;
                    }
                }
            }

            if (var10 != null) {
                Box var21 = BoundingBoxUtil.getVoxelShapeBoundingBox(pos);
                return new BlockHitResult(var10, direction, pos, var21.contains(var7));
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean method567(BlockPos pos, Direction direction, boolean strictDirection, boolean strictPos) {
        return Grim.field1831.method567(pos, direction, true, true);
    }

    private boolean method571(BlockPos var1, Direction var2, Vec3d var3) {
        BlockHitResult var7 = RaycastUtil.method575(Reach.method1614(), Class1202.method2391(mc.player.getEyePos(), var3), true);
        return var7.getType() != Type.MISS && var7.getBlockPos().equals(var1) && var7.getSide() == var2;
    }

    public Vec3d[] method572(BlockPos pos, Direction direction) {
        Vec3d[] var6 = this.method573(pos, direction, this.field1315, this.field1316);
        Vec3d[] var7 = new Vec3d[var6.length];

        for (int var8 = 0; var8 < var6.length; var8++) {
            Vec3d var9 = var6[var8];
            Vec3d var10 = this.method570(pos, direction, BoundingBoxUtil.calculateScaledBoundingBox(pos, direction, this.field1315), var9);
            var7[var8] = var10;
        }

        return var7;
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    public Vec3d[] method573(BlockPos pos, Direction direction, double scale, int n) {
        if (scale < 0.0 || scale > 1.0) {
            throw new IllegalArgumentException("Scale must be between 0.0 and 1.0");
        } else if (n < 2) {
            throw new IllegalArgumentException("Grid size n must be at least 2");
        } else {
            ArrayList<Vec3d> var9 = new ArrayList();
            double var10 = pos.getX();
            double var12 = pos.getY();
            double var14 = pos.getZ();
            double var16 = var10 + 1.0;
            double var18 = var12 + 1.0;
            double var20 = var14 + 1.0;
            double var22 = (var10 + var16) / 2.0;
            double var24 = (var12 + var18) / 2.0;
            double var26 = (var14 + var20) / 2.0;
            double var28 = scale / 2.0;
            double var30 = scale / (double) (n - 1);
            switch (direction) {
                case Direction.NORTH:
                    for (int var38 = 0; var38 < n; var38++) {
                        for (int var43 = 0; var43 < n; var43++) {
                            var9.add(new Vec3d(var22 - var28 + (double) var38 * var30, var24 - var28 + (double) var43 * var30, var14));
                        }
                    }

                    if (n % 2 == 0) {
                        var9.add(new Vec3d(var22, var24, var14));
                    }
                    break;
                case Direction.SOUTH:
                    for (int var37 = 0; var37 < n; var37++) {
                        for (int var42 = 0; var42 < n; var42++) {
                            var9.add(new Vec3d(var22 - var28 + (double) var37 * var30, var24 - var28 + (double) var42 * var30, var20));
                        }
                    }

                    if (n % 2 == 0) {
                        var9.add(new Vec3d(var22, var24, var20));
                    }
                    break;
                case Direction.EAST:
                    for (int var36 = 0; var36 < n; var36++) {
                        for (int var41 = 0; var41 < n; var41++) {
                            var9.add(new Vec3d(var16, var24 - var28 + (double) var36 * var30, var26 - var28 + (double) var41 * var30));
                        }
                    }

                    if (n % 2 == 0) {
                        var9.add(new Vec3d(var16, var24, var26));
                    }
                    break;
                case Direction.WEST:
                    for (int var35 = 0; var35 < n; var35++) {
                        for (int var40 = 0; var40 < n; var40++) {
                            var9.add(new Vec3d(var10, var24 - var28 + (double) var35 * var30, var26 - var28 + (double) var40 * var30));
                        }
                    }

                    if (n % 2 == 0) {
                        var9.add(new Vec3d(var10, var24, var26));
                    }
                    break;
                case Direction.UP:
                    for (int var34 = 0; var34 < n; var34++) {
                        for (int var39 = 0; var39 < n; var39++) {
                            var9.add(new Vec3d(var22 - var28 + (double) var34 * var30, var18, var26 - var28 + (double) var39 * var30));
                        }
                    }

                    if (n % 2 == 0) {
                        var9.add(new Vec3d(var22, var18, var26));
                    }
                    break;
                case Direction.DOWN:
                    for (int var32 = 0; var32 < n; var32++) {
                        for (int var33 = 0; var33 < n; var33++) {
                            var9.add(new Vec3d(var22 - var28 + (double) var32 * var30, var12, var26 - var28 + (double) var33 * var30));
                        }
                    }

                    if (n % 2 == 0) {
                        var9.add(new Vec3d(var22, var12, var26));
                    }
            }

            return (Vec3d[]) var9.toArray(new Vec3d[0]);
        }
    }
}
