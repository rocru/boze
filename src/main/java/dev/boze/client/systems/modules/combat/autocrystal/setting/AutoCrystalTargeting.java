package dev.boze.client.systems.modules.combat.autocrystal.setting;

import dev.boze.client.enums.AutoCrystalMaxDamage;
import dev.boze.client.enums.AutoCrystalTargetingMode;
import dev.boze.client.enums.ServerType;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.IMinecraft;
import mapped.Class5924;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AutoCrystalTargeting implements IMinecraft, SettingsGroup {
    private final EnumSetting<AutoCrystalTargetingMode> field107 = new EnumSetting<AutoCrystalTargetingMode>(
            "Targeting",
            AutoCrystalTargetingMode.Optimal,
            "Algorithm for selecting targets\nOptimal is the best, but uses more CPU, proportional to the number of targets\n - Optimal: Selects the best target based on damage\n - Exposure: Selects the target most in the open (least cover)\n - Distance: Selects the closest target\n - Health: Selects the lowest health target"
    );
    final FloatSetting field108 = new FloatSetting("MinDamage", 6.0F, 0.0F, 20.0F, 0.1F, "Minimum amount of damage for placing crystals", this.field107);
    final FloatSetting field109 = new FloatSetting("LethalHP", 4.0F, 0.0F, 20.0F, 0.1F, "Health at which to ignore min damage (face-place)", this.field107);
    final FloatSetting field110 = new FloatSetting(
            "ArmorPct", 0.05F, 0.0F, 1.0F, 0.01F, "Armor piece percentage at which to ignore min damage\nA.k.a. ArmorBreaker\nSet to 0 to disable\n", this.field107
    );
    final FloatSetting field111 = new FloatSetting(
            "TradeOff",
            0.0F,
            0.0F,
            10.0F,
            0.1F,
            "Max amount of damage to sacrifice to maintain max speed\nAt 0, higher damage placements are always prioritized\n0 is recommended if you don't care about CPS, but only DPS\nThis is not ping dependent\n"
    );
    final BindSetting field112 = new BindSetting("Override", Bind.create(), "Bind to override ignore min damage", this.field107);
    final FloatSetting field113 = new FloatSetting(
            "OverrideMinDmg",
            0.1F,
            0.0F,
            4.0F,
            0.1F,
            "Minimum amount of damage for ignore mode, when:\n - Health below LethalHP\n - Armor below ArmorPct\n - Override bind pressed\n",
            this.field107
    );
    final BooleanSetting field114 = new BooleanSetting("Economize", false, "Economize on crystals when face-placing", this.field107);
    public final EnumSetting<AutoCrystalMaxDamage> field115 = new EnumSetting<AutoCrystalMaxDamage>(
            "Safety",
            AutoCrystalMaxDamage.Combined,
            "Safety mode for placing and breaking crystals\n - MaxSelfDmg: Don't place/break if self damage is above max self damage\n - Balance: Don't place/break if self damage is above target damage * balance\n - Combined: Use both\n"
    );
    public final FloatSetting field116 = new FloatSetting(
            "MaxSelfDmg", 12.0F, 0.0F, 20.0F, 0.1F, "Maximum amount of self damage for placing/breaking crystals", this::lambda$new$0, this.field115
    );
    public final FloatSetting field117 = new FloatSetting(
            "Balance", 0.5F, 0.0F, 1.0F, 0.05F, "Balance between self and target damage", this::lambda$new$1, this.field115
    );
    public final BooleanSetting field118 = new BooleanSetting(
            "Sacrifice",
            false,
            "Ignore safety and self-popping to pop/kill target\nSo if damage to target is above their health, it will place regardless of self damage",
            this.field115
    );
    private final Setting<?>[] field119 = new Setting[]{
            this.field107,
            this.field108,
            this.field109,
            this.field110,
            this.field111,
            this.field112,
            this.field113,
            this.field114,
            this.field115,
            this.field116,
            this.field117,
            this.field118
    };
    private final AutoCrystal field120;
    private final List<LivingEntity> field121 = new ArrayList();

    private static void method1800(String var0) {
        if (AutoCrystal.field1038 && mc.player != null) {
            System.out.println("[AutoCrystal.Targeting @" + mc.player.age + "] " + var0);
        }
    }

    AutoCrystalTargeting(AutoCrystal var1) {
        this.field120 = var1;
    }

    @Override
    public Setting<?>[] get() {
        return this.field119;
    }

    List<LivingEntity> method1144() {
        return this.field121;
    }

    void method2142() {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
        //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
        //
        // Bytecode:
        // 000: new java/util/ArrayList
        // 003: dup
        // 004: invokespecial java/util/ArrayList.<init> ()V
        // 007: astore 4
        // 009: getstatic dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.mc Lnet/minecraft/client/MinecraftClient;
        // 00c: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
        // 00f: invokevirtual net/minecraft/client/world/ClientWorld.getEntities ()Ljava/lang/Iterable;
        // 012: invokeinterface java/lang/Iterable.iterator ()Ljava/util/Iterator; 1
        // 017: astore 5
        // 019: aload 5
        // 01b: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 020: ifeq 0ba
        // 023: aload 5
        // 025: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 02a: checkcast net/minecraft/entity/Entity
        // 02d: astore 6
        // 02f: aload 6
        // 031: instanceof net/minecraft/entity/LivingEntity
        // 034: ifne 03a
        // 037: goto 019
        // 03a: aload 0
        // 03b: getfield dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.field120 Ldev/boze/client/systems/modules/combat/AutoCrystal;
        // 03e: getfield dev/boze/client/systems/modules/combat/AutoCrystal.field1043 Ldev/boze/client/settings/TargetSetting;
        // 041: aload 6
        // 043: invokevirtual dev/boze/client/settings/TargetSetting.method2055 (Lnet/minecraft/entity/Entity;)Z
        // 046: ifne 04c
        // 049: goto 019
        // 04c: aload 6
        // 04e: getstatic dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.mc Lnet/minecraft/client/MinecraftClient;
        // 051: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
        // 054: invokevirtual net/minecraft/entity/Entity.distanceTo (Lnet/minecraft/entity/Entity;)F
        // 057: aload 0
        // 058: getfield dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.field120 Ldev/boze/client/systems/modules/combat/AutoCrystal;
        // 05b: getfield dev/boze/client/systems/modules/combat/AutoCrystal.targetRange Ldev/boze/client/settings/FloatSetting;
        // 05e: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
        // 061: invokevirtual java/lang/Float.floatValue ()F
        // 064: fcmpl
        // 065: ifle 06b
        // 068: goto 019
        // 06b: aload 6
        // 06d: checkcast net/minecraft/entity/LivingEntity
        // 070: invokevirtual net/minecraft/entity/LivingEntity.isDead ()Z
        // 073: ifeq 079
        // 076: goto 019
        // 079: aload 6
        // 07b: checkcast net/minecraft/entity/LivingEntity
        // 07e: invokevirtual net/minecraft/entity/LivingEntity.getHealth ()F
        // 081: aload 6
        // 083: checkcast net/minecraft/entity/LivingEntity
        // 086: invokevirtual net/minecraft/entity/LivingEntity.getAbsorptionAmount ()F
        // 089: fadd
        // 08a: fconst_0
        // 08b: fcmpg
        // 08c: ifgt 092
        // 08f: goto 019
        // 092: invokestatic java/lang/System.currentTimeMillis ()J
        // 095: aload 6
        // 097: checkcast dev/boze/client/mixininterfaces/ILivingEntity
        // 09a: invokeinterface dev/boze/client/mixininterfaces/ILivingEntity.getDamageSyncTime ()J 1
        // 09f: lsub
        // 0a0: ldc2_w 500
        // 0a3: lcmp
        // 0a4: ifge 0aa
        // 0a7: goto 019
        // 0aa: aload 4
        // 0ac: aload 6
        // 0ae: checkcast net/minecraft/entity/LivingEntity
        // 0b1: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 0b6: pop
        // 0b7: goto 019
        // 0ba: aload 0
        // 0bb: aload 0
        // 0bc: getfield dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.field107 Ldev/boze/client/settings/EnumSetting;
        // 0bf: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
        // 0c2: checkcast dev/boze/client/enums/AutoCrystalTargetingMode
        // 0c5: invokevirtual dev/boze/client/enums/AutoCrystalTargetingMode.ordinal ()I
        // 0c8: tableswitch 128 1 3 28 62 95
        // 0e4: aload 4
        // 0e6: invokeinterface java/util/List.stream ()Ljava/util/stream/Stream; 1
        // 0eb: aload 0
        // 0ec: invokedynamic applyAsDouble (Ldev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting;)Ljava/util/function/ToDoubleFunction; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)D, dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.method78 (Lnet/minecraft/entity/LivingEntity;)D, (Lnet/minecraft/entity/LivingEntity;)D ]
        // 0f1: invokestatic java/util/Comparator.comparingDouble (Ljava/util/function/ToDoubleFunction;)Ljava/util/Comparator;
        // 0f4: invokeinterface java/util/stream/Stream.max (Ljava/util/Comparator;)Ljava/util/Optional; 2
        // 0f9: aconst_null
        // 0fa: invokevirtual java/util/Optional.orElse (Ljava/lang/Object;)Ljava/lang/Object;
        // 0fd: checkcast net/minecraft/entity/LivingEntity
        // 100: invokestatic java/util/Collections.singletonList (Ljava/lang/Object;)Ljava/util/List;
        // 103: goto 167
        // 106: aload 4
        // 108: invokeinterface java/util/List.stream ()Ljava/util/stream/Stream; 1
        // 10d: invokedynamic apply ()Ljava/util/function/Function; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Ljava/lang/Object;, dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.lambda$updateTargets$2 (Lnet/minecraft/entity/LivingEntity;)Ljava/lang/Float;, (Lnet/minecraft/entity/LivingEntity;)Ljava/lang/Float; ]
        // 112: invokestatic java/util/Comparator.comparing (Ljava/util/function/Function;)Ljava/util/Comparator;
        // 115: invokeinterface java/util/stream/Stream.min (Ljava/util/Comparator;)Ljava/util/Optional; 2
        // 11a: aconst_null
        // 11b: invokevirtual java/util/Optional.orElse (Ljava/lang/Object;)Ljava/lang/Object;
        // 11e: checkcast net/minecraft/entity/LivingEntity
        // 121: invokestatic java/util/Collections.singletonList (Ljava/lang/Object;)Ljava/util/List;
        // 124: goto 167
        // 127: aload 4
        // 129: invokeinterface java/util/List.stream ()Ljava/util/stream/Stream; 1
        // 12e: invokedynamic applyAsDouble ()Ljava/util/function/ToDoubleFunction; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)D, dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.lambda$updateTargets$3 (Lnet/minecraft/entity/LivingEntity;)D, (Lnet/minecraft/entity/LivingEntity;)D ]
        // 133: invokestatic java/util/Comparator.comparingDouble (Ljava/util/function/ToDoubleFunction;)Ljava/util/Comparator;
        // 136: invokeinterface java/util/stream/Stream.min (Ljava/util/Comparator;)Ljava/util/Optional; 2
        // 13b: aconst_null
        // 13c: invokevirtual java/util/Optional.orElse (Ljava/lang/Object;)Ljava/lang/Object;
        // 13f: checkcast net/minecraft/entity/LivingEntity
        // 142: invokestatic java/util/Collections.singletonList (Ljava/lang/Object;)Ljava/util/List;
        // 145: goto 167
        // 148: aload 4
        // 14a: invokeinterface java/util/List.stream ()Ljava/util/stream/Stream; 1
        // 14f: invokedynamic apply ()Ljava/util/function/Function; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Ljava/lang/Object;, dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.lambda$updateTargets$4 (Lnet/minecraft/entity/LivingEntity;)Ljava/lang/Float;, (Lnet/minecraft/entity/LivingEntity;)Ljava/lang/Float; ]
        // 154: invokestatic java/util/Comparator.comparing (Ljava/util/function/Function;)Ljava/util/Comparator;
        // 157: invokeinterface java/util/stream/Stream.sorted (Ljava/util/Comparator;)Ljava/util/stream/Stream; 2
        // 15c: invokestatic java/util/stream/Collectors.toList ()Ljava/util/stream/Collector;
        // 15f: invokeinterface java/util/stream/Stream.collect (Ljava/util/stream/Collector;)Ljava/lang/Object; 2
        // 164: checkcast java/util/List
        // 167: putfield dev/boze/client/systems/modules/combat/AutoCrystal/setting/AutoCrystalTargeting.field121 Ljava/util/List;
        // 16a: return
    }

    double method75(LivingEntity var1) {
        double var5 = this.field108.getValue().floatValue();
        if (this.field120.minDamage.getValue()) {
            if (Class5924.method77(true, var1)) {
                var5 = this.field120.holeMinDamage.getValue().floatValue();
            } else if (Class5924.method1934(var1)) {
                var5 = this.field120.stillMinDamage.getValue().floatValue();
            } else {
                var5 = this.field120.movingMinDamage.getValue().floatValue();
            }
        }

        return var5;
    }

    double method76(LivingEntity var1, double var2) {
        return this.method77(var1, var2) ? (double) this.field113.getValue().floatValue() : this.method75(var1);
    }

    boolean method65(double var1, double var3) {
        return this.field115.getValue() == AutoCrystalMaxDamage.MaxSelfDmg || var1 <= var3 * (double) this.field117.getValue().floatValue();
    }

    boolean method77(LivingEntity var1, double var2) {
        if (var1 == null) {
            return false;
        } else if (!(var2 >= 0.5)
                || ServerType.method2114()
                || !((double) (var1.getHealth() + var1.getAbsorptionAmount()) - var2 <= 0.0)
                && !(var1.getHealth() + var1.getAbsorptionAmount() < this.field120.field1042.field109.getValue())) {
            if (this.field110.getValue() > 0.0F && var1 != null && var1.getArmorItems() != null) {
                for (ItemStack var8 : var1.getArmorItems()) {
                    int var9 = var8.getMaxDamage();
                    int var10 = var9 - var8.getDamage();
                    float var11 = (float) var10 / (float) var9;
                    if (var11 <= this.field110.getValue()) {
                        return true;
                    }
                }
            }

            return this.field120.field1042.field112.getValue().isPressed();
        } else {
            return true;
        }
    }

    private double method78(LivingEntity var1) {
        return Class5924.method77(true, var1) ? 0.0 : (double) (this.field120.targetRange.getValue() - var1.distanceTo(mc.player));
    }

    private static Float lambda$updateTargets$4(LivingEntity var0) {
        return var0.distanceTo(mc.player);
    }

    private static double lambda$updateTargets$3(LivingEntity var0) {
        return var0.getHealth() + var0.getAbsorptionAmount();
    }

    private static Float lambda$updateTargets$2(LivingEntity var0) {
        return var0.distanceTo(mc.player);
    }

    private boolean lambda$new$1() {
        return this.field115.getValue() != AutoCrystalMaxDamage.MaxSelfDmg;
    }

    private boolean lambda$new$0() {
        return this.field115.getValue() != AutoCrystalMaxDamage.Balance;
    }
}
