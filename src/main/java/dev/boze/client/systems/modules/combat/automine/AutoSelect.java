package dev.boze.client.systems.modules.combat.automine;

import dev.boze.client.enums.*;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RaycastUtil;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class5924;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class AutoSelect implements IMinecraft, SettingsGroup {
    public final BooleanSetting field60 = new BooleanSetting("AutoSelect", false, "Automatically selects blocks to mine");
    public final BindSetting field63 = new BindSetting("Bind", Bind.create(), "The bind to toggle auto select (optional)", this.field60);
    public final BooleanSetting field68 = new BooleanSetting(
            "CrystalBomber",
            false,
            "Bomb players with crystals from above\nThis will only work when:\n - AutoCrystal is enabled\n - Target is surrounded, regardless of OnlySurrounded\n"
    );
    public final BindSetting field69 = new BindSetting("Bind", Bind.create(), "The bind to toggle crystal bomber (optional)", this.field68);
    public final BooleanSetting field70 = new BooleanSetting("AirPlace", false, "Air place obsidian", this.field68);
    public final EnumSetting<AutoSelectPriority> field71 = new EnumSetting<AutoSelectPriority>(
            "Priority",
            AutoSelectPriority.SurroundPP,
            "What to prioritise when both surround is minable and head is bombable\n - Bomber: Always bomb\n - SurroundPP: Only mine out pre-placeable Surrounds, bomb otherwise\n - Surround: Always mine surround\n",
            this.field68
    );
    public final BooleanSetting field76 = new BooleanSetting(
            "PrePlace", false, "This will tell AutoCrystal to place a crystal before mining the block\nThis is useful for mining out players' surrounds"
    );
    final BooleanSetting field67 = new BooleanSetting(
            "AntiBurrow", true, "Auto select burrowed players' burrows\nIf they're burrowed in Bedrock, mines the block underneath if possible"
    );
    private final EnumSetting<AutoSelectMode> field61 = new EnumSetting<AutoSelectMode>(
            "Mode",
            AutoSelectMode.Smart,
            "Automatically selects blocks to mine\n - Smart: Selects a block when no good block is selected\n - Always: Always selects a block\n - Bind: Selects a block when you press the select bind (SelectBind)\n",
            this.field60
    );
    private final BooleanSetting field62 = new BooleanSetting(
            "Sticky",
            false,
            "Special option for AutoSelect Always with DoubleMine\nOnly select twice, then keep last mined position until player moves\n",
            this::lambda$new$0,
            this.field60
    );
    public final BindSetting field64 = new BindSetting("SelectBind", Bind.create(), "The key to press to auto select a block", this::lambda$new$1);
    private final EnumSetting<AutoSelectSortingMode> field65 = new EnumSetting<AutoSelectSortingMode>(
            "Sorting", AutoSelectSortingMode.Distance, "How to sort the players"
    );
    private final BooleanSetting field66 = new BooleanSetting("OnlySurrounded", false, "Only auto selects blocks around players that are completely surrounded");
    private final EnumSetting<AutoSelectSafetyMode> field72 = new EnumSetting<AutoSelectSafetyMode>(
            "Safety",
            AutoSelectSafetyMode.Custom,
            "Which safety settings to use for bombing\n - AutoCrystal: Use AutoCrystal's safety settings\n - Custom: Use custom safety settings\n",
            this.field68
    );
    private final EnumSetting<AutoCrystalMaxDamage> field73 = new EnumSetting<AutoCrystalMaxDamage>(
            "Mode",
            AutoCrystalMaxDamage.Balance,
            "Safety mode for placing and breaking crystals\n - MaxSelfDmg: Don't place/break if self damage is above max self damage\n - Balance: Don't place/break if self damage is above target damage * balance\n - Combined: Use both\n",
            this::lambda$new$2,
            this.field68
    );
    private final FloatSetting field74 = new FloatSetting(
            "MaxSelfDmg", 12.0F, 0.0F, 20.0F, 0.1F, "Maximum amount of self damage for placing/breaking crystals", this::lambda$new$3, this.field68
    );
    private final FloatSetting field75 = new FloatSetting(
            "Balance", 1.0F, 0.0F, 1.0F, 0.05F, "Balance between self and target damage", this::lambda$new$4, this.field68
    );
    private final BooleanSetting field77 = new BooleanSetting("AvoidShared", false, "Don't auto select blocks besides you");
    private final SettingBlock field78 = new SettingBlock(
            "AutoSelect",
            "AutoSelect settings",
            this.field60,
            this.field61,
            this.field62,
            this.field63,
            this.field64,
            this.field65,
            this.field66,
            this.field67,
            this.field76,
            this.field68,
            this.field69,
            this.field70,
            this.field71,
            this.field72,
            this.field73,
            this.field74,
            this.field75,
            this.field77
    );
    private final ArrayList<BlockPos> field86 = new ArrayList();
    public BlockLocationInfo field79 = null;
    public BlockLocationInfo field80 = null;
    public int field83 = -1;
    public int field84 = 0;
    public boolean field85 = false;
    private BlockLocationInfo field81 = null;
    private BlockPos field82 = null;

    private static void method1800(String var0) {
        if (AutoMine.field2518 && mc.player != null) {
            System.out.println("[AutoMine.AutoSelect @" + mc.player.age + "] " + var0);
        }
    }

    private static boolean lambda$getPlayers$9(AbstractClientPlayerEntity var0) {
        return mc.world.getBlockState(BlockPos.ofFloored(var0.getPos())).getBlock() != Blocks.BEDROCK;
    }

    private static boolean lambda$getPlayers$8(AbstractClientPlayerEntity var0) {
        return var0 != mc.player
                && !Friends.method2055(var0)
                && !(var0 instanceof FakePlayerEntity)
                && (double) var0.distanceTo(mc.player) <= AutoMine.INSTANCE.miner.field193.getValue() + 2.5;
    }

    private static boolean lambda$canBomb$7(EndCrystalEntity var0) {
        return true;
    }

    private static boolean lambda$updateBomber$5(EndCrystalEntity var0) {
        return true;
    }

    private AutoCrystalMaxDamage method54() {
        return this.field72.getValue() == AutoSelectSafetyMode.Custom ? this.field73.getValue() : AutoCrystal.INSTANCE.field1042.field115.getValue();
    }

    private float method1384() {
        return this.field72.getValue() == AutoSelectSafetyMode.Custom ? this.field74.getValue() : AutoCrystal.INSTANCE.field1042.field116.getValue();
    }

    private float method1385() {
        return this.field72.getValue() == AutoSelectSafetyMode.Custom ? this.field75.getValue() : AutoCrystal.INSTANCE.field1042.field117.getValue();
    }

    @Override
    public Setting<?>[] get() {
        return this.field78.method472();
    }

    public void method2142() {
        this.field82 = null;
        this.field83 = -1;
        this.field84 = 0;
        this.method1416();
    }

    public int method2010() {
        if (this.field60.getValue() && this.method2114()) {
            if (this.field82 != null) {
                Entity var4 = mc.world.getEntityById(this.field83);
                if (var4 == null) {
                    this.method2142();
                } else if (!BlockPos.ofFloored(var4.getPos()).equals(this.field82)) {
                    this.method2142();
                }
            }

            if (this.field82 == null) {
                this.method1416();

                for (AbstractClientPlayerEntity var5 : this.method1144()) {
                    if (this.method1617(var5)) {
                        this.field82 = BlockPos.ofFloored(var5.getPos());
                        this.field83 = var5.getId();
                        this.field84 = 0;
                    }
                }
            }

            if (this.field82 == null) {
                method1800("No target found");
                return 0;
            } else {
                Entity var9 = mc.world.getEntityById(this.field83);
                if (var9 == null) {
                    return 0;
                } else if (!this.method1617((PlayerEntity) var9)) {
                    method1800("Can't bomb target");
                    this.method1416();
                    return 0;
                } else if (this.field79 == null && this.field61.getValue() == AutoSelectMode.Bind && !this.field85) {
                    return 0;
                } else {
                    BlockPos var10 = this.field82.up(2);
                    this.field79 = new BlockLocationInfo(var10, false);
                    this.field80 = null;
                    if (BlockUtil.method2101(this.field79.field2528)) {
                        Box var6 = new Box(var10.up());
                        boolean var7 = mc.world.getEntitiesByType(TypeFilter.instanceOf(EndCrystalEntity.class), var6, AutoSelect::lambda$updateBomber$5).isEmpty();
                        if (var7) {
                            method1800("No crystals");
                            return 1;
                        } else {
                            return 2;
                        }
                    } else {
                        method1800("Can't break pos");
                        return 1;
                    }
                }
            }
        } else {
            method1800("Can't bomb");
            this.method1416();
            return 0;
        }
    }

    public BlockDirectionInfo method1462() {
        return this.field79 != null ? this.field79.method1469(AutoMineMode.Bomber, this::lambda$getBomberTask$6) : null;
    }

    public void method1416() {
        this.field79 = null;
    }

    public BlockDirectionInfo method60() {
        if (!this.field60.getValue()) {
            this.method2142();
            this.field80 = null;
            return null;
        } else {
            if (this.field85 && this.field61.getValue() == AutoSelectMode.Bind) {
                this.method1198();
            }

            if (this.field80 == null) {
                if (this.field61.getValue() != AutoSelectMode.Always && this.field61.getValue() != AutoSelectMode.Smart) {
                    return null;
                }

                this.method1198();
                if (this.field80 == null) {
                    return null;
                }
            } else if (this.field61.getValue() == AutoSelectMode.Always
                    && AutoMine.INSTANCE.miner.field197.getValue()
                    && (!this.field62.getValue() || this.field84 < 2)) {
                this.method67(true);
            }

            boolean var4 = this.field80 != null && this.field80.field2528 != null && BlockUtil.method2101(this.field80.field2528);
            boolean var5 = this.method62(false);
            if (!var4 || var5) {
                if (this.field61.getValue() != AutoSelectMode.Always || this.field62.getValue() && this.field84 >= 2) {
                    if (this.field61.getValue() != AutoSelectMode.Smart
                            && (this.field61.getValue() != AutoSelectMode.Always || !this.field62.getValue() || this.field84 <= 1)
                            || !var5) {
                        return null;
                    }

                    this.method1198();
                } else {
                    this.method1198();
                }
            }

            if (this.field80 != null && this.field80.field2528 != null) {
                this.field86.add(this.field80.field2528);
                return this.field80.method1468(this.field86::contains);
            } else {
                return null;
            }
        }
    }

    public BlockDirectionInfo method61() {
        if (this.field60.getValue() && this.field67.getValue()) {
            if (this.field85 && this.field61.getValue() == AutoSelectMode.Bind) {
                this.method1904();
            } else if (this.field61.getValue() != AutoSelectMode.Bind) {
                this.method1904();
            }

            if (this.field81 != null && this.field81.field2528 != null) {
                this.field86.add(this.field81.field2528);
                return this.field81.method1468(this.field86::contains);
            } else {
                return null;
            }
        } else {
            if (!this.field60.getValue()) {
                this.method2142();
            }

            this.field81 = null;
            return null;
        }
    }

    private boolean method62(boolean var1) {
        if (this.field82 != null && this.field83 != -1) {
            Entity var5 = mc.world.getEntityById(this.field83);
            if (var5 == null) {
                this.method2142();
                this.field81 = null;
                this.field80 = null;
                this.field86.clear();
                return true;
            } else {
                BlockPos var6 = BlockPos.ofFloored(var5.getPos());
                boolean var7 = !var6.equals(this.field82);
                if (var7) {
                    if (var1) {
                        this.field81 = null;
                    } else {
                        this.field80 = null;
                    }

                    this.method2142();
                    this.field86.clear();
                }

                return var7;
            }
        } else {
            return false;
        }
    }

    public boolean method2114() {
        return this.field68.getValue() && AutoCrystal.INSTANCE.isEnabled();
    }

    private boolean method1617(PlayerEntity var1) {
        if (!this.method2114()) {
            return false;
        } else if (!mc.world.isSpaceEmpty(var1.getBoundingBox())) {
            return false;
        } else {
            BlockPos var5 = BlockPos.ofFloored(var1.getX(), var1.getY(), var1.getZ());
            BlockPos var6 = var5.up(2);
            if (!AutoCrystal.INSTANCE.method2101(var6)) {
                Box var7 = new Box(var6.up());
                if (mc.world.getEntitiesByType(TypeFilter.instanceOf(EndCrystalEntity.class), var7, AutoSelect::lambda$canBomb$7).isEmpty()) {
                    return false;
                }
            }

            Vec3d var9 = new Vec3d((double) var6.getX() + 0.5, (double) var6.getY() + 1.0, (double) var6.getZ() + 0.5);
            RaycastUtil.field1325 = var6;
            RaycastUtil.field1326 = var5;
            boolean var8 = this.method64(var9, var1);
            RaycastUtil.field1325 = null;
            RaycastUtil.field1326 = null;
            return var8;
        }
    }

    public boolean method64(Vec3d crystalPos, LivingEntity target) {
        if (target == null) {
            return false;
        } else {
            AutoCrystal var6 = AutoCrystal.INSTANCE;
            double var7 = var6.field1047.method5665(mc.player, AutoCrystalAction.Break, crystalPos, null, true);
            if (var7 + 2.0 >= (double) (mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
                return false;
            } else if (this.method54() != AutoCrystalMaxDamage.Balance && var7 > (double) this.method1384()) {
                return false;
            } else {
                double var9 = var6.field1047.method5665(target, AutoCrystalAction.Break, crystalPos, null, true);
                return this.method65(var7, var9);
            }
        }
    }

    private boolean method65(double var1, double var3) {
        return this.method54() == AutoCrystalMaxDamage.MaxSelfDmg || var1 <= var3 * (double) this.method1385();
    }

    private void method1198() {
        this.method67(false);
    }

    private void method67(boolean var1) {
        BlockPos var5 = BlockPos.ofFloored(mc.player.getPos());
        HashSet var6 = new HashSet(Arrays.asList(var5.north(), var5.south(), var5.east(), var5.west()));

        for (AbstractClientPlayerEntity var8 : this.method1144()) {
            if (!this.field66.getValue() || Class5924.method77(false, var8)) {
                List<BlockPos> var9 = Class5924.method348(var8.getPos());
                BlockLocationInfo var10 = null;
                double var11 = Double.MAX_VALUE;

                for (BlockPos var14 : var9) {
                    if (this.method70(var14, var6) && (!var1 || this.field80 == null || !var14.equals(this.field80.field2528))) {
                        BlockPos var15 = var14.down();
                        Block var16 = mc.world.getBlockState(var15).getBlock();
                        if (var16 == Blocks.OBSIDIAN || var16 == Blocks.BEDROCK) {
                            Direction var17 = Direction.fromVector(var14.getX() - var5.getX(), var14.getY() - var5.getY(), var14.getZ() - var5.getZ());
                            if (var17 != null) {
                                BlockPos var18 = var14.offset(var17);
                                if (mc.world.isAir(var18)) {
                                    BlockPos var19 = var18.down();
                                    Block var20 = mc.world.getBlockState(var19).getBlock();
                                    if (var20 == Blocks.OBSIDIAN || var20 == Blocks.BEDROCK) {
                                        double var21 = EntityUtil.method2144(mc.player)
                                                .squaredDistanceTo((double) var14.getX() + 0.5, (double) var14.getY() + 0.5, (double) var14.getZ() + 0.5);
                                        if (var21 < var11) {
                                            var10 = new BlockLocationInfo(var14, false);
                                            var11 = var21;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (var10 != null) {
                    this.field80 = var10;
                    this.field82 = BlockPos.ofFloored(var8.getPos());
                    this.field83 = var8.getId();
                    return;
                }

                if (!this.method1617(var8) || this.field71.getValue() != AutoSelectPriority.SurroundPP) {
                    for (BlockPos var25 : var9) {
                        if (this.method70(var25, var6) && (!var1 || this.field80 == null || !var25.equals(this.field80.field2528))) {
                            BlockPos var27 = var25.down();
                            Block var29 = mc.world.getBlockState(var27).getBlock();
                            if (var29 == Blocks.OBSIDIAN || var29 == Blocks.BEDROCK) {
                                double var30 = EntityUtil.method2144(mc.player)
                                        .squaredDistanceTo((double) var25.getX() + 0.5, (double) var25.getY() + 0.5, (double) var25.getZ() + 0.5);
                                if (var30 < var11) {
                                    var10 = new BlockLocationInfo(var25, false);
                                    var11 = var30;
                                }
                            }
                        }
                    }

                    if (var10 != null) {
                        this.field80 = var10;
                        this.field82 = BlockPos.ofFloored(var8.getPos());
                        this.field83 = var8.getId();
                        return;
                    }

                    for (BlockPos var26 : var9) {
                        if (this.method70(var26, var6) && (!var1 || this.field80 == null || !var26.equals(this.field80.field2528))) {
                            double var28 = EntityUtil.method2144(mc.player)
                                    .squaredDistanceTo((double) var26.getX() + 0.5, (double) var26.getY() + 0.5, (double) var26.getZ() + 0.5);
                            if (var28 < var11) {
                                var10 = new BlockLocationInfo(var26, false);
                                var11 = var28;
                            }
                        }
                    }

                    if (var10 != null) {
                        this.field80 = var10;
                        this.field82 = BlockPos.ofFloored(var8.getPos());
                        this.field83 = var8.getId();
                        return;
                    }
                }
            }
        }
    }

    private void method1904() {
        BlockPos var4 = BlockPos.ofFloored(mc.player.getPos());
        HashSet var5 = new HashSet(Arrays.asList(var4.north(), var4.south(), var4.east(), var4.west()));

        for (AbstractClientPlayerEntity var7 : this.method1144()) {
            Box var8 = var7.getBoundingBox().shrink(0.05, 0.05, 0.05);
            HashSet<BlockPos> var9 = new HashSet();
            var9.add(BlockPos.ofFloored(var8.minX, var8.minY, var8.minZ));
            var9.add(BlockPos.ofFloored(var8.minX, var8.minY, var8.maxZ));
            var9.add(BlockPos.ofFloored(var8.maxX, var8.minY, var8.minZ));
            var9.add(BlockPos.ofFloored(var8.maxX, var8.minY, var8.maxZ));

            for (BlockPos var11 : var9) {
                if (!AutoMine.INSTANCE.miner.method2101(var11) && !AutoMine.INSTANCE.queue.method2101(var11)) {
                    BlockState var12 = mc.world.getBlockState(var11);
                    if ((var12.getBlock() == Blocks.OBSIDIAN || var12.getBlock() == Blocks.ENDER_CHEST) && this.method70(var11, var5)) {
                        this.field82 = BlockPos.ofFloored(var7.getPos());
                        this.field83 = var7.getId();
                        this.field81 = new BlockLocationInfo(var11, true);
                        return;
                    }
                }
            }

            for (BlockPos var14 : var9) {
                if (!AutoMine.INSTANCE.miner.method2101(var14) && !AutoMine.INSTANCE.queue.method2101(var14)) {
                    BlockState var15 = mc.world.getBlockState(var14);
                    if ((var15.getBlock() == Blocks.OBSIDIAN || var15.getBlock() == Blocks.ENDER_CHEST) && this.method70(var14.down(), var5)) {
                        this.field82 = BlockPos.ofFloored(var7.getPos());
                        this.field83 = var7.getId();
                        this.field81 = new BlockLocationInfo(var14.down(), true);
                        return;
                    }
                }
            }
        }
    }

    private List<AbstractClientPlayerEntity> method1144() {
        return mc.world
                .getPlayers()
                .stream()
                .filter(AutoSelect::lambda$getPlayers$8)
                .filter(AutoSelect::lambda$getPlayers$9)
                .sorted(Comparator.comparingDouble(this::lambda$getPlayers$10))
                .toList();
    }

    private boolean method70(BlockPos var1, Set<BlockPos> var2) {
        return (!this.field77.getValue() || !var2.contains(var1)) && BlockUtil.method2101(var1);
    }

    private double lambda$getPlayers$10(AbstractClientPlayerEntity var1) {
        return this.field65.getValue() == AutoSelectSortingMode.Distance ? (double) var1.distanceTo(mc.player) : (double) var1.getHealth();
    }

    private Boolean lambda$getBomberTask$6(BlockPos var1) {
        return var1.equals(this.field79);
    }

    private boolean lambda$new$4() {
        return this.field72.getValue() == AutoSelectSafetyMode.Custom && this.field73.getValue() != AutoCrystalMaxDamage.MaxSelfDmg;
    }

    private boolean lambda$new$3() {
        return this.field72.getValue() == AutoSelectSafetyMode.Custom && this.field73.getValue() != AutoCrystalMaxDamage.Balance;
    }

    private boolean lambda$new$2() {
        return this.field72.getValue() == AutoSelectSafetyMode.Custom;
    }

    private boolean lambda$new$1() {
        return this.field61.getValue() == AutoSelectMode.Bind;
    }

    private boolean lambda$new$0() {
        return this.field61.getValue() == AutoSelectMode.Always && AutoMine.INSTANCE.miner.field197.getValue();
    }
}
