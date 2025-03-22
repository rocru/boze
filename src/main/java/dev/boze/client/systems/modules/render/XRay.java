package dev.boze.client.systems.modules.render;

import dev.boze.client.events.AmbientOcclusionEvent;
import dev.boze.client.events.OcclusionEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.StringModeSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.List;

public class XRay extends Module {
    public static final XRay INSTANCE = new XRay();
    private static final ThreadLocal<Mutable> field3869 = ThreadLocal.withInitial(Mutable::new);
    public final StringModeSetting field3867 = new StringModeSetting("Blocks", "Blocks to keep opaque");
    private final BooleanSetting field3868 = new BooleanSetting("Inverse", false, "Inverse block selection");

    public XRay() {
        super(
                "XRay",
                "See through blocks\nUse '.set xray blocks add <block name>' to add blocks to xray\nUse '.set xray blocks del <block name>' to remove blocks from xray\nUse '.set xray blocks list' to list all blocks in the xray",
                Category.Render
        );
        this.field3867.method401(XRay::lambda$new$0);
    }

    public static boolean method2087(BlockPos blockPos) {
        for (Direction var7 : Direction.values()) {
            if (!mc.world.getBlockState(field3869.get().set(blockPos, var7)).isOpaque()) {
                return true;
            }
        }

        return true;
    }

    private static void lambda$new$0(List var0) {
        if (MinecraftUtils.isClientActive()) {
            mc.worldRenderer.reload();
        }
    }

    @EventHandler
    private void method2084(OcclusionEvent var1) {
        var1.method1020();
    }

    @EventHandler
    private void method2085(AmbientOcclusionEvent var1) {
        var1.field1900 = 1.0F;
    }

    @Override
    public void onEnable() {
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    @Override
    public void onDisable() {
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    public boolean method2086(BlockState state, BlockView view, BlockPos pos, Direction facing, boolean returns) {
        if (!returns && this.method2088(state.getBlock())) {
            BlockPos var9 = pos.offset(facing);
            BlockState var10 = view.getBlockState(var9);
            return var10.getCullingFace(view, var9, facing.getOpposite()) != VoxelShapes.fullCube() || var10.getBlock() != state.getBlock() || method2087(var9);
        } else {
            return returns;
        }
    }

    public boolean method2088(Block block) {
        return this.field3868.getValue() != this.field3867.method2032().contains(block);
    }
}
