package dev.boze.client.ac;

import dev.boze.client.systems.modules.combat.autocrystal.BlockSelection;
import dev.boze.client.utils.BoundingBoxUtil;
import dev.boze.client.utils.BoxUtil;
import dev.boze.client.utils.RaycastUtil;
import dev.boze.client.utils.world.PositionUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.List;

public class NCP implements Anticheat {
    public static final NCP field1836 = new NCP();
    private double field1837 = 4.5;
    private double field1838 = 0.0;

    private NCP() {
    }

    public void method941(double reach, double wallsReach) {
        this.field1837 = reach;
        this.field1838 = wallsReach;
    }

    public void method942() {
        this.field1837 = 4.5;
        this.field1838 = 0.0;
    }

    @Override
    public BlockHitResult method565(ArrayList<BlockHitResult> results) {
        return NearestHitCalculator.method947(results);
    }

    @Override
    public BlockHitResult method566(BlockPos pos, Direction direction) {
        Vec3d var6 = PositionUtil.getPlayerPosition();
        Vec3d[] var7 = BlockSelection.method1451(pos, direction);
        Vec3d[] var8 = new Vec3d[var7.length + 1];
        System.arraycopy(var7, 0, var8, 1, var7.length);
        var8[0] = BoxUtil.method2132(var6, BoundingBoxUtil.calculateBoundingBox(pos, direction));
        Vec3d var9 = null;
        double var10 = Double.MAX_VALUE;

        for (Vec3d var15 : var8) {
            double var16 = var15.distanceTo(var6);
            if (!(var16 > this.field1837)) {
                boolean var18 = RaycastUtil.method116(var15);
                if (!var18) {
                    if (var16 > this.field1838) {
                        continue;
                    }

                    var16 += 10.0;
                }

                if (var16 < var10) {
                    var9 = var15;
                    var10 = var16;
                }
            }
        }

        if (var9 == null) {
            return null;
        } else {
            Box var19 = BoundingBoxUtil.getVoxelShapeBoundingBox(pos);
            return new BlockHitResult(var9, direction, pos, var19.contains(var6));
        }
    }

    @Override
    public boolean method567(BlockPos pos, Direction direction, boolean strictDirection, boolean strictPos) {
        if (!strictDirection) {
            return true;
        } else {
            BlockPos var8 = BlockPos.ofFloored(mc.player.getX(), mc.player.getEyeY(), mc.player.getZ());
            if (this.method943(pos, var8)) {
                return true;
            } else {
                VoxelShape var9 = mc.world.getBlockState(pos).getCollisionShape(mc.world, pos);
                boolean var10 = var9 == VoxelShapes.fullCube();
                List<Direction> var11 = this.method944(pos, var8, var10);
                return var11.contains(direction) && !this.method946(pos, var11, direction, var10);
            }
        }
    }

    private boolean method943(BlockPos var1, BlockPos var2) {
        return var2.getX() == var1.getX() && var2.getY() == var1.getY() && var2.getZ() == var1.getZ();
    }

    private List<Direction> method944(BlockPos var1, BlockPos var2, boolean var3) {
        int var4 = var2.getX();
        int var5 = var2.getY();
        int var6 = var2.getZ();
        int var7 = var1.getX();
        int var8 = var1.getY();
        int var9 = var1.getZ();
        return this.method945(var4 - var7, var5 - var8, var6 - var9, var3);
    }

    private List<Direction> method945(int var1, int var2, int var3, boolean var4) {
       List<Direction> var8 = new ArrayList<>(6);
       if (!var4) {
          if (var1 == 0) {
             var8.add(Direction.EAST);
             var8.add(Direction.WEST);
          }

          if (var3 == 0) {
             var8.add(Direction.SOUTH);
             var8.add(Direction.NORTH);
          }
       }

       if (var2 == 0) {
          var8.add(Direction.UP);
          var8.add(Direction.DOWN);
       } else {
          var8.add(var2 > 0 ? Direction.UP : Direction.DOWN);
       }

       if (var1 != 0) {
          var8.add(var1 > 0 ? Direction.EAST : Direction.WEST);
       }

       if (var3 != 0) {
          var8.add(var3 > 0 ? Direction.SOUTH : Direction.NORTH);
       }

       return var8;
    }

    private boolean method946(BlockPos var1, List<Direction> var2, Direction var3, boolean var4) {
        if (var4) {
            BlockPos var13 = var1.offset(var3);
            BlockState var14 = mc.world.getBlockState(var13);
            return var14.getCollisionShape(mc.world, var1) == VoxelShapes.fullCube() && !var14.getCollisionShape(mc.world, var13).isEmpty();
        } else {
            for (Direction var9 : var2) {
                BlockPos var10 = var1.offset(var9);
                BlockState var11 = mc.world.getBlockState(var10);
                boolean var12 = var11.getCollisionShape(mc.world, var1) == VoxelShapes.fullCube();
                if (!var12 || !var11.getCollisionShape(mc.world, var10).isEmpty()) {
                    return false;
                }
            }

            return true;
        }
    }
}
