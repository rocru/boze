package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.AimMode;
import dev.boze.client.enums.InteractionMode;
import dev.boze.client.enums.PriorityMode;
import dev.boze.client.enums.TargetAlgorithm;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.eJ;
import dev.boze.client.jumptable.mx;
import dev.boze.client.mixin.CrossbowItemAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.legit.AntiBots;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class1202;
import mapped.Class5918;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

public class BowAim extends Module {
    public static final BowAim INSTANCE = new BowAim();
    private final EnumSetting<InteractionMode> interactionMode = new EnumSetting<InteractionMode>(
            "Mode", InteractionMode.NCP, "Interaction mode", BowAim::lambda$new$0
    );
    private final EnumSetting<AimMode> aimMode = new EnumSetting<AimMode>("Aim", AimMode.Body, "Where to aim at");
    private final MinMaxDoubleSetting aimSpeed = new MinMaxDoubleSetting("AimSpeed", new double[]{1.0, 2.0}, 0.1, 10.0, 0.1, "Aim speed", this::lambda$new$1);
    private final EnumSetting<TargetAlgorithm> targetMode = new EnumSetting<TargetAlgorithm>("Target", TargetAlgorithm.Health, "Target algorithm");
    private final EnumSetting<PriorityMode> targetPriority = new EnumSetting<PriorityMode>("Priority", PriorityMode.Lowest, "Target priority");
    private final IntSetting fov = new IntSetting("FOV", 180, 1, 180, 1, "The maximum FOV to target within");
    private final IntSetting extrapolation = new IntSetting("Extrapolation", 0, 0, 20, 1, "Extrapolation ticks\nThis is in addition to flight ticks\n");
    private final SettingCategory targets = new SettingCategory("Targets", "Entities to target");
    private final BooleanSetting players = new BooleanSetting("Players", true, "Target players", this.targets);
    private final BooleanSetting friends = new BooleanSetting("Friends", false, "Target friends", this.targets);
    private final BooleanSetting animals = new BooleanSetting("Animals", false, "Target animals", this.targets);
    private final BooleanSetting monsters = new BooleanSetting("Monsters", false, "Target monsters", this.targets);
    private Entity field488 = null;
    private float[] field489 = null;

    public BowAim() {
        super("BowAim", "Aims your bow at enemies", Category.Combat);
        this.field435 = true;
    }

    private InteractionMode method260() {
        return Options.INSTANCE.method1971() ? InteractionMode.Ghost : this.interactionMode.getValue();
    }

    @Override
    public void onDisable() {
        this.field488 = null;
        this.field489 = null;
    }

    @Override
    public String method1322() {
        return this.field488 != null ? this.field488.getNameForScoreboard() : "";
    }

    @EventHandler(
            priority = 20
    )
    public void method1694(eJ event) {
        if (!event.method1101()) {
            if (this.method260() == InteractionMode.Ghost) {
                float[] var5 = this.method111();
                if (var5 != null) {
                    RotationHelper var6 = new RotationHelper(var5[0], var5[1]);
                    RotationHelper var7 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
                    RotationHelper var8 = var7.method603(var6, this.aimSpeed.getValue());
                    event.method1099(var8.method1600());
                }
            }
        }
    }

    @EventHandler(
            priority = 20
    )
    private void method1885(ACRotationEvent var1) {
        if (this.method260() != InteractionMode.Ghost) {
            if (!var1.method1018(this.method260().interactMode, true)) {
                float[] var5 = this.method111();
                if (var5 != null) {
                    var1.yaw = var5[0];
                    var1.pitch = var5[1];
                    var1.method1021(true);
                    this.field489 = var5;
                } else if (this.field489 != null) {
                    var1.yaw = this.field489[0];
                    var1.pitch = this.field489[1];
                    var1.method1021(true);
                    this.field489 = null;
                }
            }
        }
    }

    private float[] method111() {
        this.field488 = null;
        boolean var4 = mc.player.isUsingItem() && mc.player.getActiveItem().getItem() == Items.BOW;
        boolean var5 = mc.player.getMainHandStack().getItem() == Items.CROSSBOW && CrossbowItem.isCharged(mc.player.getMainHandStack());
        if (!var4 && !var5) {
            return null;
        } else {
            LivingEntity var6 = this.method265();
            if (var6 == null) {
                return null;
            } else {
                float var7 = 0.0F;
                if (var4) {
                    var7 = (float) (mc.player.getActiveItem().getMaxUseTime(mc.player) - mc.player.getItemUseTime());
                    if (FastProjectile.INSTANCE.isEnabled()) {
                        var7 = (float) ((double) var7 + FastProjectile.INSTANCE.factor.getValue() * 10.0);
                    }

                    var7 /= 20.0F;
                    var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;
                    if (var7 >= 1.0F) {
                        var7 = 1.0F;
                    }
                } else {
                    var7 = CrossbowItemAccessor.getSpeed(mc.player.getMainHandStack().get(DataComponentTypes.CHARGED_PROJECTILES));
                }

                double var8 = var7 * 3.0F;
                Vec3d var12 = var6.getPos();
                Vec3d var13 = mc.player.getPos();
                if (this.extrapolation.getValue() > 0 && var6 instanceof PlayerEntity var14) {
                    var12 = this.method262(var14, this.extrapolation.getValue());
                    var13 = this.method262(mc.player, this.extrapolation.getValue());
                }

                float var25 = (float) (-Math.toDegrees(this.method263(var6, var8, 0.05F, var12, var13)));
                if (Float.isNaN(var25)) {
                    return null;
                } else {
                    double var15 = var6.getX() - var6.prevX;
                    double var17 = var6.getZ() - var6.prevZ;
                    double var19 = mc.player.distanceTo(var6);
                    var19 -= var19 % 2.0;
                    var15 = var19 / 2.0 * var15 * (mc.player.isSprinting() ? 1.3 : 1.1);
                    var17 = var19 / 2.0 * var17 * (mc.player.isSprinting() ? 1.3 : 1.1);
                    float var21 = (float) Math.toDegrees(Math.atan2(var12.getZ() + var17 - var13.getZ(), var12.getX() + var15 - var13.getX())) - 90.0F;
                    this.field488 = var6;
                    return new float[]{var21, var25};
                }
            }
        }
    }

    private Vec3d method262(PlayerEntity var1, int var2) {
        if (var1.prevX == var1.getX() && var1.prevY == var1.getY() && var1.prevZ == var1.getZ()) {
            return new Vec3d(var1.getX(), var1.getY(), var1.getZ());
        } else {
            Pair var6 = Class5918.method38(var2, var1);
            if (var6 != null) {
                Vec3d var7 = ((ClientPlayerEntity) var6.getLeft()).getPos();
                return new Vec3d(var7.x, var7.y, var7.z);
            } else {
                return var1.getPos();
            }
        }
    }

    private float method263(LivingEntity var1, double var2, double var4, Vec3d var6, Vec3d var7) {
        double var10 = var6.getY()
                + (double) (var1.getEyeHeight(var1.getPose()) / this.method1384())
                - (var7.getY() + (double) mc.player.getEyeHeight(mc.player.getPose()));
        double var12 = var6.getX() - var7.getX();
        double var14 = var6.getZ() - var7.getZ();
        double var16 = Math.sqrt(var12 * var12 + var14 * var14);
        return this.method264(var2, var4, var16, var10);
    }

    private float method1384() {
        if (this.aimMode.getValue() == AimMode.Feet) {
            return 10.0F;
        } else {
            return this.aimMode.getValue() == AimMode.Body ? 2.0F : 1.0F;
        }
    }

    private float method264(double var1, double var3, double var5, double var7) {
        double var9 = var3 * var5 * var5;
        var7 = 2.0 * var7 * var1 * var1;
        var7 = var3 * (var9 + var7);
        var7 = Math.sqrt(var1 * var1 * var1 * var1 - var7);
        var1 = var1 * var1 - var7;
        var7 = Math.atan2(var1 * var1 + var7, var3 * var5);
        var1 = Math.atan2(var1, var3 * var5);
        return (float) Math.min(var7, var1);
    }

    private LivingEntity method265() {
        ArrayList var4 = new ArrayList();

        for (Entity var6 : mc.world.getEntities()) {
            if (var6 instanceof LivingEntity
                    && this.method2055(var6)
                    && !((LivingEntity) var6).isDead()
                    && !(((LivingEntity) var6).getHealth() + ((LivingEntity) var6).getAbsorptionAmount() <= 0.0F)
                    && !(
                    (double) Class1202.method2391(mc.player.getEyePos(), var6.getEyePos()).method605(new RotationHelper(mc.player))
                            > (double) this.fov.getValue().intValue() * 1.417
            )) {
                var4.add(var6);
            }
        }

        if (this.targetPriority.getValue() == PriorityMode.Highest) {
            return this.targetMode.getValue() == TargetAlgorithm.Health
                    ? (LivingEntity) var4.stream().max(Comparator.comparing(BowAim::lambda$getTarget$2)).orElse(null)
                    : (LivingEntity) var4.stream().max(Comparator.comparing(BowAim::lambda$getTarget$3)).orElse(null);
        } else {
            return this.targetMode.getValue() == TargetAlgorithm.Health
                    ? (LivingEntity) var4.stream().min(Comparator.comparing(BowAim::lambda$getTarget$4)).orElse(null)
                    : (LivingEntity) var4.stream().min(Comparator.comparing(BowAim::lambda$getTarget$5)).orElse(null);
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private boolean method2055(Entity var1) {
        if (var1 instanceof PlayerEntity) {
            if (var1 == mc.player) {
                return false;
            } else if (var1 instanceof FakePlayerEntity) {
                return false;
            } else if (Friends.method2055(var1)) {
                return this.friends.getValue();
            } else {
                return (this.method260() != InteractionMode.Ghost || !AntiBots.method2055(var1)) && this.players.getValue();
            }
        } else {
            switch (var1.getType().getSpawnGroup()) {
                case SpawnGroup.CREATURE:
                case SpawnGroup.WATER_AMBIENT:
                case SpawnGroup.WATER_CREATURE:
                case SpawnGroup.AMBIENT:
                    return this.animals.getValue();
                case SpawnGroup.MONSTER:
                    return this.monsters.getValue();
                default:
                    return false;
            }
        }
    }

    private static Float lambda$getTarget$5(LivingEntity var0) {
        return var0.distanceTo(mc.player);
    }

    private static Float lambda$getTarget$4(LivingEntity var0) {
        return var0.getHealth() + var0.getAbsorptionAmount();
    }

    private static Float lambda$getTarget$3(LivingEntity var0) {
        return var0.distanceTo(mc.player);
    }

    private static Float lambda$getTarget$2(LivingEntity var0) {
        return var0.getHealth() + var0.getAbsorptionAmount();
    }

    private boolean lambda$new$1() {
        return Options.INSTANCE.method1971() || this.interactionMode.getValue() == InteractionMode.Ghost;
    }

    private static boolean lambda$new$0() {
        return !Options.INSTANCE.method1971();
    }
}
