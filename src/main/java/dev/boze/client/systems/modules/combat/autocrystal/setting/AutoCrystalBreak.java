package dev.boze.client.systems.modules.combat.autocrystal.setting;

import dev.boze.client.Boze;
import dev.boze.client.enums.*;
import dev.boze.client.mixin.PlayerInteractEntityC2SPacketAccessor;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.mixininterfaces.ILivingEntity;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.Timer;
import mapped.Class2923;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class AutoCrystalBreak implements IMinecraft, SettingsGroup {
    private final SettingCategory field177 = new SettingCategory("Break", "Break settings\nDelays, ranges, etc.\n");
    public final FloatSetting field178 = new FloatSetting("Range", 3.0F, 1.0F, 6.0F, 0.1F, "Break range for breaking visible crystals", this.field177);
    public final FloatSetting field179 = new FloatSetting("WallsRange", 3.0F, 0.0F, 6.0F, 0.1F, "Break range for breaking crystals through walls", this.field177);
    public final FloatSetting field180 = new FloatSetting("BreakDelay", 0.0F, 0.0F, 10.0F, 0.05F, "Break delay (in ticks)", this.field177);
    public final EnumSetting<InhibitMode> field181 = new EnumSetting<InhibitMode>(
            "Inhibit",
            InhibitMode.On,
            "Avoid unnecessary attacks\n - Off: Don't inhibit attacks, not recommended\n - On: Inhibit attacks\n - Strict: Inhibit attacks, and abandon failed attacks\n",
            this.field177
    );
    private final Setting<?>[] field182;
    private final AutoCrystal field183;
    public final Timer timer = new Timer();
    private boolean field184 = false;

    public AutoCrystalBreak(AutoCrystal var1) {
        this.field183 = var1;
        this.field178.setVisibility(() -> lambda$new$0(var1));
        this.field179.setVisibility(() -> lambda$new$1(var1));
        this.field182 = new Setting[]{this.field177, this.field178, this.field179, this.field180, this.field181};
    }

    @Override
    public Setting<?>[] get() {
        return this.field182;
    }

    public boolean method2114() {
        this.field183.autoCrystalTracker.method1416();
        Entity[] var4 = this.method95();
        if (var4 != null && var4[0] != null) {
            EndCrystalEntity var5 = (EndCrystalEntity) var4[0];
            if (((IEndCrystalEntity) var5).boze$getTicksExisted() >= this.field183.ticksExisted.getValue()) {
                this.field183.autoCrystalTracker.field1529 = var5;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void method2142() {
        float var4 = this.field180.getValue() * 50.0F;
        if (this.field183.autoCrystalTracker.field1529 != null
                && this.timer.hasElapsed(var4)
                && this.method93(this.field183.autoCrystalTracker.field1529, this.field183.autoCrystalTracker.field1530)) {
            this.timer.reset();
            this.field183.aa.method530(this.field183.autoCrystalTracker.field1529.getPos());
        }
    }

    public Vec3d method1954() {
        if (this.field183.autoCrystalTracker.field1529 != null) {
            if (this.field183.field1041.field205.getValue() == AnticheatMode.NCP) {
                return this.field183.autoCrystalTracker.field1529.getPos();
            } else {
                Vec3d var4 = this.field183.field1041.method115(this.field183.autoCrystalTracker.field1529.getPos());
                return var4;
            }
        } else {
            return null;
        }
    }

    public void method1416() {
        this.field184 = true;
    }

    boolean method93(EndCrystalEntity var1, Entity[] var2) {
        if (this.field184) {
            this.field184 = false;
            return false;
        } else if (this.method2116()) {
            this.method2115();
            return false;
        } else if (var1 == null) {
            return false;
        } else {
            BlockPos var6 = BlockPos.ofFloored(var1.getPos()).down();
            if (this.field183.field1042.field114.getValue()
                    && this.field183.autoCrystalTracker.field1534 != null
                    && var6.equals(this.field183.autoCrystalTracker.field1534.method6061())
                    && this.field183.autoCrystalTracker.field1536 < this.field183.autoCrystalTracker.field1537
                    && var1.age < 5) {
                return false;
            } else {
                boolean var7 = this.method2115();

                if (this.method2116()) {
                    return false;
                } else {
                    mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(var1, mc.player.isSneaking()));
                    mc.player.swingHand(Hand.MAIN_HAND);
                    this.field183.autoCrystalTracker.method681(var6, var1.getId());
                    if (var7) {
                        InventoryUtil.method396(this.field183);
                    }

                    if (this.field183.damageSync.getValue() && var2 != null) {
                        for (int var8 = 1; var8 < var2.length; var8++) {
                            if (var2[var8] != null && var2[var8] instanceof LivingEntity var9) {
                                ((ILivingEntity) var9).boze$setDamageSyncTime(System.currentTimeMillis());
                            }
                        }
                    }

                    return true;
                }
            }
        }
    }

    public boolean method94(Vec3d var1, int var2, Entity[] var3) {
        if (this.method2116()) {
            this.method2115();
            return false;
        } else {
            BlockPos var7 = BlockPos.ofFloored(var1).down();
            if (this.field183.field1042.field114.getValue()
                    && this.field183.autoCrystalTracker.field1534 != null
                    && var7.equals(this.field183.autoCrystalTracker.field1534.method6061())
                    && this.field183.autoCrystalTracker.field1536 < this.field183.autoCrystalTracker.field1537) {
                return false;
            } else {
                boolean var8 = this.method2115();

                if (this.method2116()) {
                    return false;
                } else {
                    try {
                        PlayerInteractEntityC2SPacket var9 = PlayerInteractEntityC2SPacket.attack(mc.player, false);
                        ((PlayerInteractEntityC2SPacketAccessor) var9).setEntityId(var2);
                        mc.player.networkHandler.sendPacket(var9);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        this.field183.autoCrystalTracker.method681(var7, var2);
                    } catch (Exception var12) {
                    }

                    if (var8) {
                        InventoryUtil.method396(this.field183);
                    }

                    if (this.field183.damageSync.getValue() && var3 != null) {
                        for (int var13 = 1; var13 < var3.length; var13++) {
                            if (var3[var13] != null && var3[var13] instanceof LivingEntity var10) {
                                ((ILivingEntity) var10).boze$setDamageSyncTime(System.currentTimeMillis());
                            }
                        }
                    }

                    return true;
                }
            }
        }
    }

    private Entity[] method95() {
        Entity[] var4 = new Entity[this.field183.damageSync.getValue() ? 5 : 1];
        LivingEntity[] var5 = null;
        if (this.field183.damageSync.getValue()) {
            var5 = new LivingEntity[4];
        }

        List<EndCrystalEntity> var6 = this.method1144();
        if (var6.isEmpty()) {
            return null;
        } else {
            AutoMine var7 = AutoMine.INSTANCE;
            if (var7.isEnabled() && var7.autoSelect.field60.getValue() && var7.autoSelect.field68.getValue() && var7.autoSelect.field79 != null) {
                BlockState var8 = mc.world.getBlockState(var7.autoSelect.field79.method1471());
                if (var8.getBlock() == Blocks.AIR) {
                    BlockPos var9 = var7.autoSelect.field79.method1471().up();
                    Box var10 = new Box(var9);
                    EndCrystalEntity var11 = mc.world
                            .getEntitiesByType(TypeFilter.instanceOf(EndCrystalEntity.class), var10, AutoCrystalBreak::lambda$findCrystalTarget$2)
                            .stream()
                            .findFirst()
                            .orElse(null);
                    Entity var12 = mc.world.getEntityById(var7.autoSelect.field83);
                    if (var11 != null && var12 != null && var12 instanceof PlayerEntity var13 && var7.autoSelect.method64(var11.getPos(), var13)) {
                        var4[0] = var11;
                        return var4;
                    }
                }
            }

            double var25 = 0.0;

            for (EndCrystalEntity var27 : var6) {
                BlockPos var28 = BlockPos.ofFloored(var27.getPos()).down();
                boolean var29 = this.field183.autoCrystalTracker.field1534 != null && var28.equals(this.field183.autoCrystalTracker.field1534.method6061());
                double var14 = this.field183.field1047.method5665(mc.player, AutoCrystalAction.Break, var27.getPos(), null, true);
                if (!(var14 + 2.0 >= (double) (mc.player.getHealth() + mc.player.getAbsorptionAmount()))
                        && (
                        this.field183.field1042.field115.getValue() == AutoCrystalMaxDamage.Balance
                                || !(var14 > (double) this.field183.field1042.field116.getValue().floatValue())
                                || var29
                )) {
                    double var16 = 0.0;
                    double var18 = 0.0;
                    int var20 = 0;

                    for (LivingEntity var22 : this.field183.field1042.method1144()) {
                        if (var20 > 3) {
                            break;
                        }

                        if (var22 != null) {
                            double var23 = this.field183.field1047.method5665(var22, AutoCrystalAction.Break, var27.getPos(), null, true);
                            var16 += var23;
                            var18 += this.field183.field1042.method76(var22, var23);
                            if (this.field183.damageSync.getValue()) {
                                var5[var20] = var22;
                            }

                            var20++;
                        }
                    }

                    if (var20 != 0 && this.field183.field1042.method65(var14, var16)) {
                        if (this.field183.damageSync.getValue()) {
                            while (var20 <= 3) {
                                var5[var20] = null;
                                var20++;
                            }
                        }

                        if ((var29 || !(var16 < var18 / (double) var20) && !(var16 < var14)) && (var16 > var25 || var25 == 0.0)) {
                            var25 = var16;
                            var4[0] = var27;
                            if (this.field183.damageSync.getValue()) {
                                System.arraycopy(var5, 0, var4, 1, 4);
                            }
                        }
                    }
                }
            }

            return var4;
        }
    }

    public List<EndCrystalEntity> method1144() {
        ArrayList var4 = new ArrayList();

        for (Entity var6 : mc.world.getEntities()) {
            if (var6 instanceof EndCrystalEntity && this.method96((EndCrystalEntity) var6)) {
                var4.add(var6);
            }
        }

        return var4;
    }

    private boolean method96(EndCrystalEntity var1) {
        if (!this.field183.field1041.method117(var1.getPos(), mc.player.getPos())) {
            return false;
        } else {
            if (this.field181.getValue() != InhibitMode.Off) {
                if (((IEndCrystalEntity) var1).boze$isAbandoned()) {
                    return false;
                }

                return ((IEndCrystalEntity) var1).boze$getHitsSinceLastAttack() <= this.field183.extraLimit.getValue()
                        || !((double) (System.currentTimeMillis() - ((IEndCrystalEntity) var1).boze$getLastAttackTime()) < Boze.getModules().field905.field1519);
            }

            return true;
        }
    }

    private boolean method2115() {
        if (this.field183.field1041.field214.getValue()
                && mc.player.hasStatusEffect(StatusEffects.WEAKNESS)
                && !(InventoryUtil.method1774().getItem() instanceof SwordItem)) {
            int var4 = InventoryHelper.method165(Class2923.field127, this.field183.field1041.field213.getValue());
            return InventoryUtil.method533(this.field183, 26, this.field183.field1041.field213.getValue(), var4);
        } else {
            return false;
        }
    }

    public boolean method2116() {
        return this.method2117()
                && !Boze.getModules()
                .field906
                .field1617
                .hasElapsed((float) this.field183.field1041.field215.getValue().intValue() * 50.0F * this.field183.autoCrystalTracker.field1525);
    }

    boolean method2117() {
        return this.field183.field1041.field205.getValue() == AnticheatMode.NCP && (this.field183.field1041.field213.getValue() == AutoMineSwapMode.Normal || this.field183.field1041.field213.getValue() == AutoMineSwapMode.Silent);
    }

    private static boolean lambda$findCrystalTarget$2(EndCrystalEntity var0) {
        return true;
    }

    private static boolean lambda$new$1(AutoCrystal var0) {
        return var0.field1041.field205.getValue() == AnticheatMode.NCP;
    }

    private static boolean lambda$new$0(AutoCrystal var0) {
        return var0.field1041.field205.getValue() == AnticheatMode.NCP;
    }
}
