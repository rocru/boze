package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import dev.boze.client.enums.HoleTPHoles;
import dev.boze.client.enums.HoleTPMode;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.HoleESP;
import dev.boze.client.systems.pathfinding.Path;
import dev.boze.client.systems.pathfinding.PathBuilder;
import dev.boze.client.systems.pathfinding.PathFinder;
import dev.boze.client.systems.pathfinding.PathRules;
import dev.boze.client.utils.Timer;
import mapped.Class3076;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HoleTP extends Module {
    public static final HoleTP INSTANCE = new HoleTP();
    private final EnumSetting<HoleTPMode> field445 = new EnumSetting<HoleTPMode>(
            "Mode", HoleTPMode.Path, "Mode for HoleTP\n - Normal: Normal hole tp\n - Path: Path-following hole tp\n - Baritone: Baritone with timer\n"
    );
    private final EnumSetting<HoleTPHoles> field446 = new EnumSetting<HoleTPHoles>("Holes", HoleTPHoles.All, "Holes to detect", this::lambda$new$0);
    private final IntSetting field447 = new IntSetting("Range", 10, 5, 25, 1, "Range for detecting holes", this::lambda$new$1);
    private final MinMaxSetting field448 = new MinMaxSetting("Speed", 0.3, 0.05, 3.0, 0.01, "Speed in blocks per tick", this::lambda$new$2);
    private final MinMaxSetting field449 = new MinMaxSetting("Timer", 2.0, 1.0, 10.0, 0.1, "Timer speed", this::lambda$new$3);
    private final MinMaxSetting field450 = new MinMaxSetting("Timeout", 5.0, 0.5, 20.0, 0.1, "Timeout for baritone in seconds", this::lambda$new$4);
    private final IntSetting field451 = new IntSetting("Height", 3, 1, 10, 1, "Max height for detecting holes", this::lambda$new$5);
    private final MinMaxSetting field452 = new MinMaxSetting("PullDown", 0.0, 0.0, 1.0, 0.01, "Pull down into holes", this::lambda$new$6);
    private final BooleanSetting field453 = new BooleanSetting("DisableSpeed", true, "Disable speed module after anchoring", this::lambda$new$7);
    private final BooleanSetting field454 = new BooleanSetting("LagBackDisable", true, "Disable on lag-back");
    private final PathRules field455 = new PathRules(true, true, true, false);
    private final Mutable field456 = new Mutable();
    private PathFinder field457 = null;
    private Path field458 = null;
    private boolean field459 = false;
    private final Timer field460 = new Timer();

    public HoleTP() {
        super("HoleTP", "Get into holes easier", Category.Movement);
    }

    @Override
    public void onDisable() {
        Class3076.method6025(this);
    }

    @EventHandler(
            priority = 600
    )
    public void method1893(PlayerMoveEvent event) {
        int var5 = (int) Math.floor(mc.player.getX());
        int var6 = (int) Math.floor(mc.player.getY());
        int var7 = (int) Math.floor(mc.player.getZ());
        Class3076.method6025(this);
        if (this.field445.getValue() == HoleTPMode.Baritone) {
            if (this.field459) {
                if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() == null) {
                    this.field459 = false;
                    this.setEnabled(false);
                } else if (this.field460.hasElapsed(this.field450.getValue() * 1000.0)) {
                    NotificationManager.method1151(new Notification(this.getName(), "Baritone timeout", Notifications.WARNING, NotificationPriority.Red));
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("stop");
                    this.setEnabled(false);
                } else {
                    Class3076.method6024(this, 15, this.field449.getValue().floatValue());
                }

                return;
            }

            BlockPos var8 = this.method242(var5, var6, var7);
            if (var8 != null) {
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(var8));
                this.field459 = true;
                this.field460.reset();
            } else {
                NotificationManager.method1151(new Notification(this.getName(), "No holes found", Notifications.WARNING, NotificationPriority.Red));
                this.setEnabled(false);
            }
        } else if (this.field445.getValue() == HoleTPMode.Path) {
            if (this.field457 != null) {
                this.field457.method2098();
                if (this.field457.method2118()) {
                    NotificationManager.method1151(new Notification(this.getName(), "Unable to find path", Notifications.WARNING, NotificationPriority.Red));
                    this.field457 = null;
                    this.setEnabled(false);
                    return;
                }

                if (!this.field457.method2117()) {
                    return;
                }

                this.field458 = PathBuilder.method616(this.field457.method2120(), this.field448.getValue(), this.field448.getValue());
                this.field457 = null;
            }

            if (this.field458 != null) {
                Vec3d var14 = this.field458.method2094();
                if (var14 == null) {
                    this.field458 = null;
                    this.setEnabled(false);
                    return;
                }

                event.vec3 = var14;
                mc.player.setVelocity(Vec3d.ZERO);
                event.field1892 = true;
                return;
            }

            BlockPos var13 = this.method242(var5, var6, var7);
            if (var13 != null) {
                this.field457 = new PathFinder(var13, this.field455);
            } else {
                NotificationManager.method1151(new Notification(this.getName(), "No holes found", Notifications.WARNING, NotificationPriority.Red));
                this.setEnabled(false);
            }
        } else {
            if (mc.player.getY() - (double) var6 == 0.0 && this.method244(var5, var6, var7)) {
                if (this.field453.getValue()) {
                    Speed.INSTANCE.setEnabled(false);
                }

                this.setEnabled(false);
                return;
            }

            boolean var15 = false;

            for (int var9 = 0; var9 < this.field451.getValue() && var6 > 0 && this.method2077(var5, var6, var7); var9++) {
                if (this.method244(var5, var6, var7)) {
                    var15 = true;
                    break;
                }

                var6--;
            }

            if (var15) {
                if (this.field453.getValue()) {
                    Speed.INSTANCE.setEnabled(false);
                }

                double var16 = MathHelper.clamp((double) var5 + 0.5 - mc.player.getX(), -0.0525, 0.0525);
                double var11 = MathHelper.clamp((double) var7 + 0.5 - mc.player.getZ(), -0.0525, 0.0525);
                event.field1892 = true;
                event.vec3 = new Vec3d(var16, event.vec3.y, var11);
                mc.player.setVelocity(mc.player.getVelocity().subtract(0.0, this.field452.getValue(), 0.0));
            }
        }
    }

    private BlockPos method242(int var1, int var2, int var3) {
        BlockPos var7 = null;
        double var8 = Double.MAX_VALUE;

        for (int var10 = var1 - this.field447.getValue(); var10 <= var1 + this.field447.getValue(); var10++) {
            for (int var11 = var3 - this.field447.getValue(); var11 <= var3 + this.field447.getValue(); var11++) {
                for (int var12 = Math.max(mc.world.getBottomY(), var2 - this.field447.getValue());
                     var12 <= var2 + this.field447.getValue() && var12 <= mc.world.getTopY();
                     var12++
                ) {
                    BlockPos var13 = new BlockPos(var10, var12, var11);
                    if ((
                            !mc.world.getBlockState(var13).blocksMovement()
                                    || !mc.world.getBlockState(var13.add(0, 1, 0)).blocksMovement()
                                    || !mc.world.getBlockState(var13.add(0, 2, 0)).blocksMovement()
                    )
                            && (HoleESP.method2101(var13) || this.field446.getValue() == HoleTPHoles.All && HoleESP.method2102(var13))) {
                        double var14 = mc.player.squaredDistanceTo((double) var13.getX() + 0.5, var13.getY(), (double) var13.getZ() + 0.5);
                        if (var14 < var8) {
                            var7 = var13;
                            var8 = var14;
                        }
                    }
                }
            }
        }

        return var7;
    }

    @EventHandler
    public void method2042(PacketBundleEvent event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket && this.field454.getValue()) {
            if (this.field458 != null) {
                this.field458 = null;
            }

            if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior() != null) {
                BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().forceCancel();
            }

            this.setEnabled(false);
            NotificationManager.method1151(new Notification(this.getName(), "HoleTP lagged back", Notifications.WARNING, NotificationPriority.Red));
        }
    }

    private boolean method244(int var1, int var2, int var3) {
        return this.method245(var1, var2 - 1, var3)
                && this.method245(var1 + 1, var2, var3)
                && this.method245(var1 - 1, var2, var3)
                && this.method245(var1, var2, var3 + 1)
                && this.method245(var1, var2, var3 - 1);
    }

    private boolean method245(int var1, int var2, int var3) {
        this.field456.set(var1, var2, var3);
        Block var7 = mc.world.getBlockState(this.field456).getBlock();
        return var7 == Blocks.BEDROCK || var7 == Blocks.OBSIDIAN || var7 == Blocks.CRYING_OBSIDIAN;
    }

    private boolean method2077(int var1, int var2, int var3) {
        this.field456.set(var1, var2, var3);
        return mc.world.getBlockState(this.field456).isAir();
    }

    private boolean lambda$new$7() {
        return this.field445.getValue() == HoleTPMode.Normal;
    }

    private boolean lambda$new$6() {
        return this.field445.getValue() == HoleTPMode.Normal;
    }

    private boolean lambda$new$5() {
        return this.field445.getValue() == HoleTPMode.Normal;
    }

    private boolean lambda$new$4() {
        return this.field445.getValue() == HoleTPMode.Baritone;
    }

    private boolean lambda$new$3() {
        return this.field445.getValue() == HoleTPMode.Baritone;
    }

    private boolean lambda$new$2() {
        return this.field445.getValue() == HoleTPMode.Path;
    }

    private boolean lambda$new$1() {
        return this.field445.getValue() != HoleTPMode.Normal;
    }

    private boolean lambda$new$0() {
        return this.field445.getValue() != HoleTPMode.Normal;
    }
}
