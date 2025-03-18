package dev.boze.client.systems.pathfinding;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3i;

public class PathPos extends BlockPos {
    private final boolean jumping;

    public PathPos(BlockPos pos) {
        this(pos, false);
    }

    public PathPos(BlockPos pos, boolean jumping) {
        super(pos);
        this.jumping = jumping;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof PathPos var5 && this.getX() == var5.getX() && this.getY() == var5.getY() && this.getZ() == var5.getZ() && this.isJumping() == var5.isJumping();
        }
    }

    public int hashCode() {
        return super.hashCode() * 2 + (this.isJumping() ? 1 : 0);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos crossProduct(Vec3i vec3i) {
        return super.crossProduct(vec3i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos offset(Axis axis, int i) {
        return super.offset(axis, i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos offset(Direction direction, int i) {
        return super.offset(direction, i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos offset(Direction direction) {
        return super.offset(direction);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos east(int i) {
        return super.east(i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos east() {
        return super.east();
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos west(int i) {
        return super.west(i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos west() {
        return super.west();
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos south(int i) {
        return super.south(i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos south() {
        return super.south();
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos north(int i) {
        return super.north(i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos north() {
        return super.north();
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos down(int i) {
        return super.down(i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos down() {
        return super.down();
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos up(int i) {
        return super.up(i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos up() {
        return super.up();
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos multiply(int i) {
        return super.multiply(i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos subtract(Vec3i vec3i) {
        return super.subtract(vec3i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos add(Vec3i vec3i) {
        return super.add(vec3i);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public BlockPos add(int i, int j, int k) {
        return super.add(i, j, k);
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public int compareTo(Vec3i object) {
        return super.compareTo(object);
    }
}
