package dev.boze.client.systems.modules.combat.autocrystal.setting;

import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.AutoCrystalAction;
import dev.boze.client.enums.AutoCrystalMaxDamage;
import dev.boze.client.enums.AutoMineSwapMode;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.systems.modules.misc.FastUse;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.trackers.TargetTracker;
import mapped.Class2923;
import mapped.Class3087;
import mapped.Class5913;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map.Entry;

public class AutoCrystalPlace implements IMinecraft, SettingsGroup {
    private final SettingCategory field137 = new SettingCategory("Place", "Place settings\n");
    public final FloatSetting field138 = new FloatSetting(
            "Range", 4.5F, 1.0F, 6.0F, 0.1F, "Range for placing\n4.5 is vanilla\n5-6 may work on some servers\n", this.field137
    );
    public final FloatSetting field139 = new FloatSetting("WallsRange", 3.0F, 0.0F, 6.0F, 0.1F, "Place range for placing through walls", this.field137);
    final FloatSetting field140 = new FloatSetting("Delay", 0.0F, 0.0F, 10.0F, 0.05F, "Place delay (in ticks)", this.field137);
    final BooleanSetting field141 = new BooleanSetting("Await", true, "Await\nImproves consistency, but reduces speed\n", this.field137);
    private final Setting<?>[] field142;
    private final AutoCrystal field143;
    private final Timer field144 = new Timer();
    boolean field145;

    public AutoCrystalPlace(AutoCrystal var1) {
        this.field143 = var1;
        this.field139.setVisibility(() -> $lambda$new$0(var1));
        this.field142 = new Setting[]{this.field137, this.field138, this.field139, this.field140, this.field141};
    }

    private static void method1800(String var0) {
        if (AutoCrystal.field1038 && mc.player != null) {
            System.out.println("[AutoCrystal.Place @" + mc.player.age + "] " + var0);
        }
    }

    private static boolean $lambda$new$0(AutoCrystal var0) {
        return var0.field1041.field205.getValue() == AnticheatMode.NCP;
    }

    @Override
    public Setting<?>[] get() {
        return this.field142;
    }

    public boolean method2114() {
        this.field143.autoCrystalTracker.method1198();
        if (Class2923.method2114()
                || mc.player.getInventory().getMainHandStack().getItem() instanceof EndCrystalItem
                || this.field143.field1041.field213.getValue() != AutoMineSwapMode.Off
                && InventoryHelper.method165(Class2923.field126, this.field143.field1041.field213.getValue()) != -1) {
            AutoMine var4 = AutoMine.INSTANCE;
            if (var4.isEnabled() && var4.autoSelect.field60.getValue()) {
                if (var4.autoSelect.field76.getValue() && var4.autoSelect.field80 != null) {
                    BlockState var8 = mc.world.getBlockState(var4.autoSelect.field80.method1471());
                    if (var8.getBlock() == Blocks.OBSIDIAN || var8.getBlock() == Blocks.ENDER_CHEST) {
                        BlockPos var10 = var4.autoSelect.field80.method1471().offset(var4.autoSelect.field80.method1470()).down();
                        Pair var7 = this.field143.field1045.method724(var10);
                        if (var7 != null && var7.getLeft() != null) {
                            this.field143.autoCrystalTracker.field1534 = (Class3087) var7.getLeft();
                            this.field143.autoCrystalTracker.field1535 = true;
                            return true;
                        }
                    }
                } else if (var4.autoSelect.field68.getValue() && var4.autoSelect.field79 != null) {
                    BlockState var5 = mc.world.getBlockState(var4.autoSelect.field79.method1471());
                    if (var5.getBlock() == Blocks.OBSIDIAN) {
                        Pair var6 = this.field143.field1045.method724(var4.autoSelect.field79.method1471());
                        if (var6 != null && var6.getLeft() != null) {
                            this.field143.autoCrystalTracker.field1534 = (Class3087) var6.getLeft();
                            this.field143.autoCrystalTracker.field1535 = true;
                            return true;
                        }
                    }
                }
            }

            Pair var9 = this.method88();
            if (!((HashMap) var9.getLeft()).isEmpty()) {
                method1800(((HashMap) var9.getLeft()).size() + " place positions candidates found");
                return this.method87((HashMap<Class3087, Double>) var9.getLeft(), (Boolean) var9.getRight());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Vec3d method1954() {
        return this.field143.autoCrystalTracker.field1534 != null ? this.field143.autoCrystalTracker.field1534.method6062().getPos() : null;
    }

    public void method2142() {
        if (this.field141.getValue()) {
            this.field145 = true;
        }
    }

    public void method1416() {
        this.field145 = true;
    }

    public void method1198() {
        if (this.field145) {
            this.field145 = false;
        } else {
            float var4 = this.field140.getValue() * 50.0F;
            if (this.field143.autoCrystalTracker.field1534 != null
                    && this.field144.hasElapsed(var4)
                    && this.method86(this.field143.autoCrystalTracker.field1534.method6062(), !this.field143.autoCrystalTracker.field1535)) {
                this.field144.reset();
            }
        }
    }

    public boolean method86(BlockHitResult result, boolean addToSelfPlace) {
        if (result == null) {
            return false;
        } else {
            boolean var6 = false;
            if (!Class2923.method2114() && InventoryUtil.method1774().getItem() != Items.END_CRYSTAL) {
                int var7 = InventoryHelper.method165(Class2923.field126, this.field143.field1041.field213.getValue());
                if (!InventoryUtil.method533(this.field143, 25, this.field143.field1041.field213.getValue(), var7)) {
                    return false;
                }

                var6 = true;
                if (this.field143.field1041.field216.getValue()
                        && (
                        this.field143.field1041.field213.getValue() == AutoMineSwapMode.Normal
                                || this.field143.field1041.field213.getValue() == AutoMineSwapMode.Silent
                )
                        && this.field143.field1041.field205.getValue() == AnticheatMode.Grim) {
                    Class5913.method16(Hand.MAIN_HAND);
                }
            }

            FastUse.field2960 = true;
            Class5913.method17(Class2923.method2114() ? Hand.OFF_HAND : Hand.MAIN_HAND, result);
            mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Class2923.method2114() ? Hand.OFF_HAND : Hand.MAIN_HAND));
            if (var6 && this.field143.field1041.field213.getValue() == AutoMineSwapMode.Alt) {
                InventoryUtil.method396(this.field143);
            }

            if (addToSelfPlace) {
                this.field143.autoCrystalTracker.method683(result.getBlockPos());
            }

            if (this.field143.field1040.field147.getValue()) {
                this.field143.field1040.field175.reset();
                this.field143.field1040.field171 = new Box(result.getBlockPos());
                this.field143.field1040.field172 = this.field143.autoCrystalTracker.field1536;
            }

            return true;
        }
    }

    private boolean method87(HashMap<Class3087, Double> var1, boolean var2) {
        Class3087 var6 = null;
        LivingEntity var7 = null;
        double var8 = 0.0;
        double var10 = this.field143.field1042.field108.getValue().floatValue();

        for (LivingEntity var13 : this.field143.field1042.method1144()) {
            if (var13 != null) {
                for (Entry var15 : var1.entrySet()) {
                    if (var15.getKey() != null && (!var2 || ((Class3087) var15.getKey()).method6063())) {
                        double var16 = 0.0;
                        Vec3d var18 = new Vec3d(
                                (double) ((Class3087) var15.getKey()).method6061().getX() + 0.5,
                                (double) ((Class3087) var15.getKey()).method6061().getY() + 1.0,
                                (double) ((Class3087) var15.getKey()).method6061().getZ() + 0.5
                        );
                        double var19 = this.field143.field1047.method5665(var13, AutoCrystalAction.Full, var18, ((Class3087) var15.getKey()).method6061(), true);
                        double var21 = this.field143.field1042.method76(var13, var19);
                        if (var19 > var16 && var19 >= var21) {
                            var16 = var19;
                        }

                        if (this.field143.field1042.method65((Double) var15.getValue(), var16)
                                && var16 > var8
                                && (
                                !this.field143.field1042.field118.getValue()
                                        || this.field143.field1042.field115.getValue() == AutoCrystalMaxDamage.Balance
                                        || !((Double) var15.getValue() > (double) this.field143.field1042.field116.getValue().floatValue())
                                        || !(var16 < (double) (var13.getHealth() + var13.getAbsorptionAmount()))
                        )) {
                            var8 = var16;
                            var6 = (Class3087) var15.getKey();
                            var7 = var13;
                            var10 = this.field143.field1042.method75(var13);
                        }
                    }
                }

                if (var2) {
                    for (Entry var28 : var1.entrySet()) {
                        if (var28.getKey() != null && !((Class3087) var28.getKey()).method6063()) {
                            double var29 = 0.0;
                            Vec3d var30 = new Vec3d(
                                    (double) ((Class3087) var28.getKey()).method6061().getX() + 0.5,
                                    (double) ((Class3087) var28.getKey()).method6061().getY() + 1.0,
                                    (double) ((Class3087) var28.getKey()).method6061().getZ() + 0.5
                            );
                            double var31 = this.field143.field1047.method5665(var13, AutoCrystalAction.Full, var30, ((Class3087) var28.getKey()).method6061(), true);
                            double var32 = this.field143.field1042.method76(var13, var31);
                            if (var31 > var29 && var31 >= var32) {
                                var29 = var31;
                            }

                            if (this.field143.field1042.method65((Double) var28.getValue(), var29)
                                    && var29 - (var6 != null && var6.method6063() ? (double) this.field143.field1042.field111.getValue().floatValue() : 0.0) > var8
                                    && (
                                    !this.field143.field1042.field118.getValue()
                                            || this.field143.field1042.field115.getValue() == AutoCrystalMaxDamage.Balance
                                            || !((Double) var28.getValue() > (double) this.field143.field1042.field116.getValue().floatValue())
                                            || !(var29 < (double) (var13.getHealth() + var13.getAbsorptionAmount()))
                            )) {
                                var8 = var29;
                                var6 = (Class3087) var28.getKey();
                                var7 = var13;
                                var10 = this.field143.field1042.method75(var13);
                            }
                        }
                    }
                }

                if (var7 != null && var6 != null) {
                    break;
                }
            }
        }

        if (var7 != null && var6 != null && var6.method6062() != null) {
            String var10000 = var6.method6061().toShortString();
            String var25 = var7.getName().getString();
            String var26 = var10000;
            method1800("Algorithm found best place position " + var26 + " targeting " + var25 + " dealing " + var8 + " hp");
            this.field143.autoCrystalTracker.method684(var6, var8, var10, var7);
            TargetTracker.method488(this.field143.autoCrystalTracker.field1538);
            return true;
        } else {
            method1800("Algorithm could not find valid place position");
            return false;
        }
    }

    private Pair<HashMap<Class3087, Double>, Boolean> method88() {
        HashMap var4 = new HashMap();
        if (this.field143.autoCrystalTracker.field1533 != null) {
            Pair var5 = this.field143.field1045.method724(this.field143.autoCrystalTracker.field1533);
            if (var5 != null && this.field143.field1045.method727((Double) var5.getRight())) {
                var4.put(var5.getLeft(), var5.getRight());
                return new Pair(var4, false);
            }
        }

        boolean var16 = false;
        BlockPos var6 = mc.player.getBlockPos();
        int var7 = (int) Math.ceil(this.field143.autoCrystalPlace.field138.getValue() + 1.0F);
        int var8 = (int) Math.ceil(this.field143.autoCrystalPlace.field138.getValue() + 1.0F);

        for (int var9 = var6.getX() - var7; var9 < var6.getX() + var7; var9++) {
            for (int var10 = var6.getY() - var8; var10 < var6.getY() + var8 + 1; var10++) {
                for (int var11 = var6.getZ() - var7; var11 < var6.getZ() + var7; var11++) {
                    Vec3d var12 = new Vec3d((double) var9 + 0.5, (double) var10 + 0.5, (double) var11 + 0.5);
                    boolean var13 = false;

                    for (LivingEntity var15 : this.field143.field1042.method1144()) {
                        if (var15 != null && var15.getPos().distanceTo(var12) <= (double) this.field143.crystalRange.getValue().floatValue() + 0.71) {
                            var13 = true;
                            break;
                        }
                    }

                    if (var13) {
                        BlockPos var17 = new BlockPos(var9, var10, var11);
                        Pair var18 = this.field143.field1045.method724(var17);
                        if (var18 != null && this.field143.field1045.method727((Double) var18.getRight())) {
                            var4.put(var18.getLeft(), var18.getRight());
                            if (((Class3087) var18.getLeft()).method6063()) {
                                var16 = true;
                            }
                        }
                    }
                }
            }
        }

        return new Pair(var4, var16);
    }
}
