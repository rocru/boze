package dev.boze.client.systems.modules.combat.automine;

import dev.boze.client.enums.*;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.utils.BlockMiningUtils;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.trackers.TickRateTracker;
import dev.boze.client.utils.world.BlockBreakingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Miner implements IMinecraft, SettingsGroup {
    public final EnumSetting<AnticheatMode> field187 = new EnumSetting<AnticheatMode>(
            "Mode", AnticheatMode.Grim, "The anti-cheat mode to use\n - Grim: For 2b2t.org, 2bpvp.com, etc.\n - NCP: For crystalpvp.cc, 6b6t.org, etc."
    );
    public final BooleanSetting field188 = new BooleanSetting(
            "AllowAbort", true, "Allow aborting current mine to mine another block\nThis doesn't work on some servers\n", this::lambda$new$0
    );
    public final MinMaxSetting field194 = new MinMaxSetting(
            "Duration",
            1.0,
            0.7,
            1.0,
            0.05,
            "Mining duration\nLower values are faster but may cause desync on some servers\n0.7 works on 2b2t, but is slightly inconsistent"
    );
    public final BooleanSetting field197 = new BooleanSetting("DoubleMine", false, "Allows you to mine two blocks at once");
    public final List<TaskLogger> field201 = new ArrayList();
    public final List<TaskLogger> field204 = new ArrayList();
    final BooleanSetting field185 = new BooleanSetting("MultiTask", false, "Whether or not to multi-task");
    final EnumSetting<AutoMineItemResetMode> field186 = new EnumSetting<AutoMineItemResetMode>(
            "ItemReset",
            AutoMineItemResetMode.Delay,
            "Reset mining if using an item\n - Off: Don't reset\n - On: Reset\n - Delay: Don't reset, but delay swap until done using item\n"
    );
    final BooleanSetting field189 = new BooleanSetting("Rotate", true, "Whether or not to rotate to the block when mining", this::lambda$new$1);
    final BooleanSetting field190 = new BooleanSetting("Swing", true, "Swing arm when mining", this::lambda$new$2);
    final BooleanSetting field191 = new BooleanSetting("StrictDirection", true, "Only mine blocks from sides you could in vanilla");
    final BooleanSetting field192 = new BooleanSetting("Strict", true, "Packet mining bypass for strict servers");
    final MinMaxSetting field193 = new MinMaxSetting("Range", 4.5, 0.0, 6.0, 0.1, "The maximum range to mine blocks at");
    final EnumSetting<AutoMineTpsSync> field195 = new EnumSetting<AutoMineTpsSync>(
            "TPSSync", AutoMineTpsSync.Off, "Sync mining time to server tick rate", this.field194
    );
    final BooleanSetting field196 = new BooleanSetting(
            "IgnoreAir",
            false,
            "Vanilla mining is slower when you're not standing on ground\nThis setting allows you to mine at full speed even when not standing on ground\nThis won't work on all servers\n",
            this.field194
    );
    private final EnumSetting<AutoMineSwapbackMode> field198 = new EnumSetting<AutoMineSwapbackMode>(
            "Await",
            AutoMineSwapbackMode.Dynamic,
            "Wait for blocks to break before swapping back\n - Off: Don't wait\n - Dynamic: Wait when uncertain\n - Always: Always wait\n"
    );
    private final SettingBlock field199 = new SettingBlock(
            "Miner",
            "Miner settings",
            this.field185,
            this.field186,
            this.field187,
            this.field188,
            this.field189,
            this.field190,
            this.field191,
            this.field192,
            this.field193,
            this.field194,
            this.field195,
            this.field196,
            this.field197,
            this.field198
    );
    private final AutoMine field200;
    private int field202 = 0;
    private boolean field203;

    public Miner(AutoMine var1) {
        this.field200 = var1;
    }

    static void method1800(String var0) {
        if (AutoMine.field2518 && mc.player != null) {
            System.out.println("[AutoMine.Miner @" + mc.player.age + "] " + var0);
        }
    }

    @Override
    public Setting<?>[] get() {
        return this.field199.method472();
    }

    public void method99(BlockDirectionInfo var1) {
        method1800("Adding task at " + var1.field2523.toShortString());
        if (this.method2101(var1.field2523)) {
            method1800("Task already active");
        } else {
            TaskLogger var5 = new TaskLogger(this, var1);
            var5.init();
            this.field201.add(var5);
        }
    }

    public void method2142() {
        if (this.field186.getValue() == AutoMineItemResetMode.On && mc.player.isUsingItem()) {
            for (TaskLogger var10 : this.field201) {
                var10.field2533 = 0.0F;
                var10.field2534 = -1;
            }
        } else {
            ArrayList var4 = new ArrayList();

            for (TaskLogger var6 : this.field201) {
                if (var6.field2533 == 0.0F) {
                    var6.start();
                    AutoMineSuccessMode var7 = this.method100(var6);
                    if (var7 == AutoMineSuccessMode.Instant) {
                        var6.field2533 = 1.0F;
                        this.field204.add(var6);
                        var6.finish();
                        this.method103(var6);
                    } else if (var7 == AutoMineSuccessMode.Normal) {
                        this.method102(var6);
                    } else if (var7 == AutoMineSuccessMode.Fail) {
                        var4.add(var6);
                        var6.fail();
                    }
                } else if (var6.field2533 < 1.0F) {
                    if (this.field200.rangeAbort.getValue() && !BlockUtil.method2102(var6.field2532.field2523)) {
                        var4.add(var6);
                        this.method101(var6);
                        var6.pause();
                    }

                    this.method102(var6);
                } else {
                    if (this.field186.getValue() == AutoMineItemResetMode.Delay && mc.player.isUsingItem()) {
                        return;
                    }

                    this.field204.add(var6);
                    var6.complete();
                    this.method103(var6);
                }
            }

            this.field201.removeAll(this.field204);
            this.field201.removeAll(var4);
            if (this.field201.isEmpty() && this.field204.isEmpty()) {
                if (this.field203) {
                    method1800("Dynamic await disabled");
                }

                this.field203 = false;
            } else if (this.field201.size() > 1) {
                if (!this.field203) {
                    method1800("Dynamic await enabled");
                }

                this.field203 = true;
            }

            if (!this.field204.isEmpty()) {
                boolean var9 = true;

                for (TaskLogger var12 : this.field204) {
                    if (!this.method104(var12)) {
                        var9 = false;
                    }

                    if (mc.world.getBlockState(var12.field2532.field2523).isAir() && this.field200.render.field257.getValue()) {
                        this.field200.render.field268.putIfAbsent(var12.field2532.field2523, System.currentTimeMillis());
                    }
                }

                if (var9 && (!this.method2115() || this.field201.isEmpty())) {
                    this.field204.clear();
                    short var10000 = 6128;
                    InventoryUtil.method396(this.field200);
                    method1800("All tasks completed, swapping back");
                    this.method1416();
                }
            }
        }
    }

    public boolean method2114() {
        if (!this.field204.isEmpty()) {
            return false;
        } else if (!this.field201.isEmpty()) {
            return this.field197.getValue() && this.field201.size() == 1 && this.field201.get(0).field2533 <= 0.5F;
        } else {
            return true;
        }
    }

    public boolean method2101(BlockPos var1) {
        for (TaskLogger var6 : this.field201) {
            if (var6.field2532.field2523.equals(var1)) {
                return true;
            }
        }

        for (TaskLogger var8 : this.field204) {
            if (var8.field2532.field2523.equals(var1)) {
                return true;
            }
        }

        return false;
    }

    private AutoMineSuccessMode method100(TaskLogger var1) {
        BlockDirectionInfo var5 = var1.field2532;
        if (!this.field185.getValue() && mc.player.isUsingItem()) {
            return AutoMineSuccessMode.Fail;
        } else if (!BlockUtil.method2101(var5.field2523)) {
            return AutoMineSuccessMode.Fail;
        } else if (this.method2102(var5.field2523)) {
            var1.field2533 = 1.0F;
            int var6 = BlockMiningUtils.method590(var5.field2523, this.field200.swapMode.getValue());
            if (var6 != -1
                    && (var6 != mc.player.getInventory().selectedSlot || var6 != ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getLastSelectedSlot())
            ) {
                InventoryUtil.method533(this.field200, 125, this.field200.swapMode.getValue(), var6);
                if (this.field200.swapMode.getValue().swapBack) {
                    var1.field2534 = this.field200.swapDelay.getValue();
                }
            }

            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, var5.field2523, var5.field2524));
            return AutoMineSuccessMode.Instant;
        } else {
            if (this.field192.getValue()) {
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, var5.field2523, var5.field2524));
            }

            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, var5.field2523, var5.field2524));
            if (this.field190.getValue() || this.field187.getValue() == AnticheatMode.Grim) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }

            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, var5.field2523, var5.field2524));
            return AutoMineSuccessMode.Normal;
        }
    }

    public void method101(TaskLogger var1) {
        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, var1.field2532.field2523, var1.field2532.field2524));
    }

    private void method102(TaskLogger var1) {
        BlockDirectionInfo var5 = var1.field2532;
        BlockState var6 = mc.world.getBlockState(var5.field2523);
        if (var6.isAir()) {
            var1.field2533 = 1.0F;
        } else {
            int var7 = BlockMiningUtils.method591(var6, this.field200.swapMode.getValue());
            float var8 = BlockBreakingUtil.method507(var5.field2523, mc.player.getInventory().getStack(var7), this.field196.getValue());
            if (this.field195.getValue() == AutoMineTpsSync.Avg) {
                var8 *= TickRateTracker.getAverageTickRate() / 20.0F;
            } else if (this.field195.getValue() == AutoMineTpsSync.Min) {
                var8 *= TickRateTracker.getMinTickRate() / 20.0F;
            } else if (this.field195.getValue() == AutoMineTpsSync.Last) {
                var8 *= TickRateTracker.getLastTickRate() / 20.0F;
            }

            var1.field2533 = var1.field2533 + (float) ((double) var8 * (1.0 / this.field194.getValue()));
        }
    }

    private void method103(TaskLogger var1) {
        BlockDirectionInfo var5 = var1.field2532;
        int var6 = BlockMiningUtils.method590(var5.field2523, this.field200.swapMode.getValue());
        if (var6 != -1
                && (var6 != mc.player.getInventory().selectedSlot || var6 != ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getLastSelectedSlot())) {
            if (InventoryUtil.method532() != AutoMine.INSTANCE) {
                InventoryUtil.method533(this.field200, 125, this.field200.swapMode.getValue(), var6);
                var1.swap();
            }

            if (this.field200.swapMode.getValue().swapBack) {
                var1.field2534 = this.field200.swapDelay.getValue();
            }
        }

        if (this.field192.getValue()) {
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, var5.field2523, var5.field2524));
        }

        var1.field2535.reset();
    }

    private boolean method104(TaskLogger var1) {
        if (this.method2115() && !mc.world.getBlockState(var1.field2532.field2523).isAir() && !var1.field2535.hasElapsed(1500.0)) {
            return false;
        } else if (var1.field2534 != -1) {
            if (var1.field2534 > 0) {
                var1.field2534--;
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean method2115() {
        if (this.field198.getValue() == AutoMineSwapbackMode.Always) {
            return true;
        } else {
            return this.field198.getValue() == AutoMineSwapbackMode.Dynamic && this.field203;
        }
    }

    private boolean method2102(BlockPos var1) {
        if (this.method106(var1)) {
            if (this.field202 < 2) {
                this.field202++;
                return true;
            } else {
                this.field202 = 0;
                this.field200.field2519 = null;
                this.field200.field2520 = false;
                return false;
            }
        } else {
            return false;
        }
    }

    public void method1416() {
        this.field202 = 0;
    }

    private boolean method106(BlockPos var1) {
        return this.field200.instantRemine.getValue() && var1.equals(this.field200.field2519) && this.field200.field2520;
    }

    public Vec3d method1954() {
        if (!this.field201.isEmpty()) {
            TaskLogger var4 = this.field201.get(0);
            if (this.field189.getValue() && this.field187.getValue() == AnticheatMode.NCP && var4.field2533 > 0.9F) {
                return var4.field2532.field2525;
            }
        }

        return null;
    }

    private boolean lambda$new$2() {
        return this.field187.getValue() == AnticheatMode.NCP;
    }

    private boolean lambda$new$1() {
        return this.field187.getValue() == AnticheatMode.NCP;
    }

    private boolean lambda$new$0() {
        return this.field187.getValue() == AnticheatMode.NCP;
    }
}
