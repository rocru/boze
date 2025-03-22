package dev.boze.client.systems.modules.combat.automine;

import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;

public class AntiRegear implements IMinecraft, SettingsGroup {
    public final BooleanSetting field131 = new BooleanSetting("AntiRegear", false, "Whether or not to enable anti regear", AntiRegear::lambda$new$0);
    public final BindSetting field132 = new BindSetting("Bind", Bind.create(), "The bind to toggle anti regear (optional)", this.field131);
    private final MinMaxSetting field133 = new MinMaxSetting("Range", 4.5, 1.0, 6.0, 0.1, "The range to check for shulkers within");
    private final SettingBlock field134;
    private final HashSet<BlockPos> field136 = new HashSet();
    public BlockLocationInfo field135 = null;

    public AntiRegear(AutoMine var1) {
        this.field134 = new SettingBlock("AntiRegear", "Anti regear settings", this.field131, this.field132, this.field133);
        this.field134.setVisibility(var1.advanced::getValue);
    }

    private static boolean lambda$new$0() {
        return AutoMine.INSTANCE.advanced.getValue();
    }

    @Override
    public Setting<?>[] get() {
        return this.field134.method472();
    }

    public void method2142() {
        this.field135 = null;
    }

    public BlockDirectionInfo method1462() {
        if (this.field135 != null) {
            if (mc.world.getBlockState(this.field135.method1471()).getBlock() instanceof ShulkerBoxBlock) {
                return this.field135.method1467();
            }

            this.field135 = null;
        }

        int var4 = (int) Math.ceil(this.field133.getValue()) + 1;
        BlockPos var5 = BlockPos.ofFloored(mc.player.getPos());
        Vec3d var6 = mc.player.getEyePos();
        double var7 = this.field133.getValue() * this.field133.getValue();

        for (int var9 = -var4; var9 <= var4; var9++) {
            for (int var10 = -var4; var10 <= var4; var10++) {
                for (int var11 = -var4; var11 <= var4; var11++) {
                    BlockPos var12 = var5.add(var9, var11, var10);
                    if (!this.field136.contains(var12)
                            && mc.world.getBlockState(var12).getBlock() instanceof ShulkerBoxBlock
                            && !(var6.squaredDistanceTo((double) var12.getX() + 0.5, (double) var12.getY() + 1.0, (double) var12.getZ() + 0.5) > var7)) {
                        this.field135 = new BlockLocationInfo(var12, false);
                        break;
                    }
                }
            }
        }

        return this.field135 == null ? null : this.field135.method1467();
    }

    public void method1801(BlockPos var1) {
        if (this.field136.size() > 100) {
            this.field136.clear();
        }

        this.field136.add(var1);
    }
}
