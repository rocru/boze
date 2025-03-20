package dev.boze.client.systems.modules.combat.autocrystal.setting;

import dev.boze.client.enums.AutoCrystalMaxDamage;
import dev.boze.client.enums.AutoCrystalTargetingMode;
import dev.boze.client.enums.ServerType;
import dev.boze.client.mixininterfaces.ILivingEntity;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.IMinecraft;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import mapped.Class5924;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class AutoCrystalTargeting
        implements IMinecraft,
        SettingsGroup {
    private final EnumSetting<AutoCrystalTargetingMode> field107 = new EnumSetting<AutoCrystalTargetingMode>("Targeting", AutoCrystalTargetingMode.Optimal, "Algorithm for selecting targets\nOptimal is the best, but uses more CPU, proportional to the number of targets\n - Optimal: Selects the best target based on damage\n - Exposure: Selects the target most in the open (least cover)\n - Distance: Selects the closest target\n - Health: Selects the lowest health target");
    final FloatSetting field108 = new FloatSetting("MinDamage", 6.0f, 0.0f, 20.0f, 0.1f, "Minimum amount of damage for placing crystals", this.field107);
    final FloatSetting field109 = new FloatSetting("LethalHP", 4.0f, 0.0f, 20.0f, 0.1f, "Health at which to ignore min damage (face-place)", this.field107);
    final FloatSetting field110 = new FloatSetting("ArmorPct", 0.05f, 0.0f, 1.0f, 0.01f, "Armor piece percentage at which to ignore min damage\nA.k.a. ArmorBreaker\nSet to 0 to disable\n", this.field107);
    final FloatSetting field111 = new FloatSetting("TradeOff", 0.0f, 0.0f, 10.0f, 0.1f, "Max amount of damage to sacrifice to maintain max speed\nAt 0, higher damage placements are always prioritized\n0 is recommended if you don't care about CPS, but only DPS\nThis is not ping dependent\n");
    final BindSetting field112 = new BindSetting("Override", Bind.create(), "Bind to override ignore min damage", this.field107);
    final FloatSetting field113 = new FloatSetting("OverrideMinDmg", 0.1f, 0.0f, 4.0f, 0.1f, "Minimum amount of damage for ignore mode, when:\n - Health below LethalHP\n - Armor below ArmorPct\n - Override bind pressed\n", this.field107);
    final BooleanSetting field114 = new BooleanSetting("Economize", false, "Economize on crystals when face-placing", this.field107);
    public final EnumSetting<AutoCrystalMaxDamage> field115 = new EnumSetting<AutoCrystalMaxDamage>("Safety", AutoCrystalMaxDamage.Combined, "Safety mode for placing and breaking crystals\n - MaxSelfDmg: Don't place/break if self damage is above max self damage\n - Balance: Don't place/break if self damage is above target damage * balance\n - Combined: Use both\n");
    public final FloatSetting field116 = new FloatSetting("MaxSelfDmg", 12.0f, 0.0f, 20.0f, 0.1f, "Maximum amount of self damage for placing/breaking crystals", this::lambda$new$0, this.field115);
    public final FloatSetting field117 = new FloatSetting("Balance", 0.5f, 0.0f, 1.0f, 0.05f, "Balance between self and target damage", this::lambda$new$1, this.field115);
    public final BooleanSetting field118 = new BooleanSetting("Sacrifice", false, "Ignore safety and self-popping to pop/kill target\nSo if damage to target is above their health, it will place regardless of self damage", this.field115);
    private final Setting<?>[] field119 = new Setting[]{this.field107, this.field108, this.field109, this.field110, this.field111, this.field112, this.field113, this.field114, this.field115, this.field116, this.field117, this.field118};
    private final AutoCrystal field120;
    private List<LivingEntity> field121 = new ArrayList<LivingEntity>();

    private static void method1800(String string) {
        if (!AutoCrystal.field1038 || AutoCrystalTargeting.mc.player == null) {
            return;
        }
        System.out.println("[AutoCrystal.Targeting @" + AutoCrystalTargeting.mc.player.age + "] " + string);
    }

    AutoCrystalTargeting(AutoCrystal autoCrystal) {
        this.field120 = autoCrystal;
    }

    @Override
    public Setting<?>[] get() {
        return this.field119;
    }

    List<LivingEntity> method1144() {
        return this.field121;
    }

    void method2142() {
        ArrayList<LivingEntity> arrayList = new ArrayList<LivingEntity>();
        for (Entity entity : AutoCrystalTargeting.mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity) || !this.field120.field1043.method2055(entity) || entity.distanceTo((Entity)AutoCrystalTargeting.mc.player) > this.field120.targetRange.getValue() || ((LivingEntity)entity).isDead() || ((LivingEntity)entity).getHealth() + ((LivingEntity)entity).getAbsorptionAmount() <= 0.0f || System.currentTimeMillis() - ((ILivingEntity)entity).boze$getDamageSyncTime() < 500L) continue;
            arrayList.add((LivingEntity)entity);
        }
        this.field121 = switch (this.field107.getValue().ordinal()) {
            case 1 -> Collections.singletonList(arrayList.stream().max(Comparator.comparingDouble(this::method78)).orElse(null));
            case 2 -> Collections.singletonList(arrayList.stream().min(Comparator.comparing(AutoCrystalTargeting::lambda$updateTargets$2)).orElse(null));
            case 3 -> Collections.singletonList(arrayList.stream().min(Comparator.comparingDouble(AutoCrystalTargeting::lambda$updateTargets$3)).orElse(null));
            default -> arrayList.stream().sorted(Comparator.comparing(AutoCrystalTargeting::lambda$updateTargets$4)).collect(Collectors.toList());
        };
    }

    double method75(LivingEntity livingEntity) {
        double d = this.field108.getValue();
        if (this.field120.minDamage.getValue()) {
            d = Class5924.method77(true, (Entity)livingEntity) ? (double)this.field120.holeMinDamage.getValue() : (Class5924.method1934((Entity)livingEntity) ? (double)this.field120.stillMinDamage.getValue() : (double)this.field120.movingMinDamage.getValue());
        }
        return d;
    }

    double method76(LivingEntity livingEntity, double d) {
        if (this.method77(livingEntity, d)) {
            return this.field113.getValue();
        }
        return this.method75(livingEntity);
    }

    boolean method65(double d, double d2) {
        return this.field115.getValue() == AutoCrystalMaxDamage.MaxSelfDmg || d <= d2 * (double)this.field117.getValue();
    }

    boolean method77(LivingEntity livingEntity, double d) {
        if (livingEntity == null) {
            return false;
        }
        if (d >= 0.5 && !ServerType.method2114() && ((double)(livingEntity.getHealth() + livingEntity.getAbsorptionAmount()) - d <= 0.0 || livingEntity.getHealth() + livingEntity.getAbsorptionAmount() < this.field120.field1042.field109.getValue())) {
            return true;
        }
        if (this.field110.getValue() > 0.0f && livingEntity != null && livingEntity.getArmorItems() != null) {
            for (ItemStack itemStack : livingEntity.getArmorItems()) {
                int n = itemStack.getMaxDamage();
                int n2 = n - itemStack.getDamage();
                float f = (float)n2 / (float)n;
                if (!(f <= this.field110.getValue())) continue;
                return true;
            }
        }
        return this.field120.field1042.field112.getValue().isPressed();
    }

    private double method78(LivingEntity livingEntity) {
        if (Class5924.method77(true, (Entity)livingEntity)) {
            return 0.0;
        }
        return this.field120.targetRange.getValue() - livingEntity.distanceTo((Entity)AutoCrystalTargeting.mc.player);
    }

    private static Float lambda$updateTargets$4(LivingEntity livingEntity) {
        return Float.valueOf(livingEntity.distanceTo((Entity)AutoCrystalTargeting.mc.player));
    }

    private static double lambda$updateTargets$3(LivingEntity livingEntity) {
        return livingEntity.getHealth() + livingEntity.getAbsorptionAmount();
    }

    private static Float lambda$updateTargets$2(LivingEntity livingEntity) {
        return Float.valueOf(livingEntity.distanceTo((Entity)AutoCrystalTargeting.mc.player));
    }

    private boolean lambda$new$1() {
        return this.field115.getValue()!= AutoCrystalMaxDamage.MaxSelfDmg;
    }

    private boolean lambda$new$0() {
        return this.field115.getValue() != AutoCrystalMaxDamage.Balance;
    }
}
