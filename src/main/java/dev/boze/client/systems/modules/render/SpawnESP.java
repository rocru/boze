package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.renderer.DrawMode;
import dev.boze.client.renderer.Mesh;
import dev.boze.client.renderer.Mesh.Attrib;
import dev.boze.client.renderer.ShaderMesh;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.spawnesp.BlockRenderer;
import mapped.Class3064;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.LightType;

import java.util.ArrayList;
import java.util.List;

public class SpawnESP extends Module {
    public static final SpawnESP INSTANCE = new SpawnESP();
    private final IntSetting field767 = new IntSetting("Range", 4, 3, 20, 1, "Range for renders");
    public final ColorSetting field768 = new ColorSetting("Current", new BozeDrawColor(-65536), "Color for blocks where mobs can currently spawn");
    public final ColorSetting field769 = new ColorSetting("Later", new BozeDrawColor(-256), "Color for blocks where mobs might spawn later (i.e. at night)");
    private final Class3064<BlockRenderer> field770 = new Class3064<BlockRenderer>(this::lambda$new$0);
    private final List<BlockRenderer> field771 = new ArrayList();
    private final Mesh field772 = new ShaderMesh(ShaderRegistry.field2257, DrawMode.Lines, Attrib.Vec3, Attrib.Color);

    private SpawnESP() {
        super("SpawnESP", "Shows where mobs can spawn", Category.Render);
    }

    @EventHandler
    private void method2041(MovementEvent var1) {
        for (BlockRenderer var6 : this.field771) {
            this.field770.method5994(var6);
        }

        this.field771.clear();
        BlockPos var10 = mc.player.getBlockPos();

        for (int var11 = -this.field767.getValue(); var11 <= this.field767.getValue(); var11++) {
            for (int var7 = -this.field767.getValue(); var7 <= this.field767.getValue(); var7++) {
                for (int var8 = -this.field767.getValue(); var8 <= this.field767.getValue(); var8++) {
                    BlockPos var9 = var10.add(var11, var7, var8);
                    switch (method359(var9, mc.world.getBlockState(var9))) {
                        case 1:
                            this.field771.add(this.field770.method5993().method2028(var9, true));
                            break;
                        case 2:
                            this.field771.add(this.field770.method5993().method2028(var9, false));
                    }
                }
            }
        }
    }

    @EventHandler
    private void method2071(Render3DEvent var1) {
        if (!this.field771.isEmpty()) {
            for (BlockRenderer var6 : this.field771) {
                var6.method2029(var1);
            }
        }
    }

    private static int method359(BlockPos var0, BlockState var1) {
        if (!(var1.getBlock() instanceof AirBlock)) {
            return 0;
        } else {
            BlockPos var5 = var0.down();
            BlockState var6 = mc.world.getBlockState(var5);
            if (var6.getBlock() == Blocks.BEDROCK) {
                return 0;
            } else {
                if (!method360(var6)) {
                    if (var6.getCollisionShape(mc.world, var5) != VoxelShapes.fullCube()) {
                        return 0;
                    }

                    if (var6.isTransparent(mc.world, var5)) {
                        return 0;
                    }
                }

                if (mc.world.getLightLevel(LightType.BLOCK, var0) > 0) {
                    return 0;
                } else {
                    return mc.world.getLightLevel(LightType.SKY, var0) > 0 ? 1 : 2;
                }
            }
        }
    }

    private static boolean method360(BlockState var0) {
        return var0.getBlock() instanceof SlabBlock && var0.get(SlabBlock.TYPE) == SlabType.TOP || var0.getBlock() instanceof StairsBlock && var0.get(StairsBlock.HALF) == BlockHalf.TOP;
    }

    private BlockRenderer lambda$new$0() {
        return new BlockRenderer(this);
    }
}
