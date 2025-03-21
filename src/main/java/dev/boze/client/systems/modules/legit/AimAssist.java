package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.AimAssistAimPoint;
import dev.boze.client.enums.AimAssistGunPoint;
import dev.boze.client.enums.AimAssistPriority;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class1202;
import mapped.Class5917;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

public class AimAssist extends Module {
    public static final AimAssist INSTANCE = new AimAssist();
    private final ItemSetting field2676 = new ItemSetting("Gun", "Gun item");
    private final EnumSetting<AimAssistAimPoint> field2677 = new EnumSetting<AimAssistAimPoint>("AimPoint", AimAssistAimPoint.MinAngle, "Aim point to use");
    private final BooleanSetting field2678 = new BooleanSetting(
            "UseGhostRot",
            true,
            "Use ghost rotation algorithm\nThis will use the PointScale, Tracking, and Noise options from GhostRotations, but Min/Max Speed from here"
    );
    private final BooleanSetting field2679 = new BooleanSetting(
            "StopOverTarget",
            false,
            "Stop assisting when looking at target\nNote: If you are using AimAssist as an AimBot (high speed), having this on will make it look unnatural"
    );
    private final BooleanSetting field2680 = new BooleanSetting("OnlyWeapon", false, "Only assist when holding weapon");
    private final BooleanSetting field2681 = new BooleanSetting("OnlyWhenClicking", true, "Only assist when clicking attack key");
    private final IntSetting field2682 = new IntSetting("Ticks", 5, 1, 40, 1, "Ticks to assist for after clicking", this.field2681::getValue);
    private final EnumSetting<AimAssistGunPoint> field2683 = new EnumSetting<AimAssistGunPoint>(
            "GunMode",
            AimAssistGunPoint.Off,
            "Aim assist for gun mini-games\n - Off: Disable gun mode\n - LeftClick: Enable gun mode, assume left click for OnlyWhenClicking\n - RightClick: Enable gun mode, assume right click for OnlyWhenClicking\nSet item for OnlyWeapon: .set aimassist gun <item name>\n"
    );
    private final MinMaxSetting field2684 = new MinMaxSetting("Range", 3.0, 1.0, 7.0, 0.1, "Range to target within", this::lambda$new$0);
    private final MinMaxSetting field2685 = new MinMaxSetting("GunRange", 100.0, 1.0, 300.0, 1.0, "Range to target within", this::lambda$new$1);
    private final BooleanSetting field2686 = new BooleanSetting("ThroughWalls", false, "Target through walls");
    private final MinMaxSetting field2687 = new MinMaxSetting("BoxScale", 0.8, 0.1, 1.0, 0.1, "Scale of the box to target within");
    private final MinMaxDoubleSetting field2688 = new MinMaxDoubleSetting("Speed", new double[]{1.0, 2.0}, 0.1, 10.0, 0.1, "Aim assist speed");
    private final MinMaxDoubleSetting field2689 = new MinMaxDoubleSetting(
            "Delta",
            new double[]{0.0, 5.0},
            0.0,
            10.0,
            0.1,
            "Mouse delta change per poll limits\nWon't assist if delta below min\nWill limit assists to max\nMin: 0 is recommended to avoid fast jitters\n"
    );
    private final BooleanSetting field2690 = new BooleanSetting("DontResist", false, "Don't resist mouse movement in opposite directions");
    private final BooleanSetting field2691 = new BooleanSetting("Vertical", true, "Assist vertical rotation\nThis may increase your chance of getting flagged");
    private final IntSetting field2692 = new IntSetting("FOV", 180, 1, 180, 1, "Maximum FOV to target within");
    private final EnumSetting<AimAssistPriority> field2693 = new EnumSetting<AimAssistPriority>(
            "Priority", AimAssistPriority.Distance, "The priority to target enemies with"
    );
    private final BooleanSetting field2694 = new BooleanSetting("Sticky", false, "Stick to the same target when possible");
    private final SettingCategory field2695 = new SettingCategory("Targets", "Entities to target");
    private final BooleanSetting field2696 = new BooleanSetting("Invisible", false, "Target invisible entities", this.field2695);
    private final BooleanSetting field2697 = new BooleanSetting("Players", true, "Target players", this.field2695);
    private final BooleanSetting field2698 = new BooleanSetting("Friends", false, "Target friends", this.field2695);
    private final BooleanSetting field2699 = new BooleanSetting("Animals", false, "Target animals", this.field2695);
    private final BooleanSetting field2700 = new BooleanSetting("Monsters", false, "Target monsters", this.field2695);
    private Entity field2701;
    private final Comparator<Entity> field2702 = Comparator.comparing(this::lambda$new$2);
    private final Timer field2703 = new Timer();

    public AimAssist() {
        super("AimAssist", "Assists your aim", Category.Legit);
    }

    @EventHandler(
            priority = 44
    )
    public void method1572(MouseUpdateEvent event) {
        if (MinecraftUtils.isClientActive() && !event.method1022()) {
            if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
                if (this.field2680.getValue()) {
                    if (this.field2683.getValue() != AimAssistGunPoint.Off) {
                        if (mc.player.getMainHandStack().getItem() != this.field2676.method447()) {
                            return;
                        }
                    } else if (!(mc.player.getMainHandStack().getItem() instanceof SwordItem)
                            && !(mc.player.getMainHandStack().getItem() instanceof AxeItem)
                            && !(mc.player.getMainHandStack().getItem() instanceof TridentItem)) {
                        return;
                    }
                }

                if (this.field2683.getValue() == AimAssistGunPoint.RightClick) {
                    if (mc.options.useKey.isPressed()) {
                        this.field2703.reset();
                    }
                } else if (mc.options.attackKey.isPressed()) {
                    this.field2703.reset();
                }

                if (!this.field2681.getValue() || !this.field2703.hasElapsed(this.field2682.getValue() * 50)) {
                    RotationHelper var5 = new RotationHelper(mc.player);
                    Entity var6 = this.method1574();
                    if (var6 != null) {
                        if (!this.field2679.getValue() || !(mc.crosshairTarget instanceof EntityHitResult var7) || var7.getEntity() != var6) {
                            Box var18 = var6.getBoundingBox().expand(var6.getTargetingMargin()).contract(1.0 - this.field2687.getValue());
                            Vec3d var19 = this.method1573(var18, var6, var5);
                            RotationHelper var9 = Class1202.method2391(mc.player.getEyePos(), var19);
                            if (!((double) var9.method605(var5) > (double) this.field2692.getValue().intValue() * 1.417)) {
                                RotationHelper var10 = var5.method603(var9, this.field2688.getValue()).method1600();
                                RotationHelper var11 = var10.method606(var5);
                                Pair[] var12 = RotationHelper.method614(var11);
                                Pair var13 = var12[0];

                                for (Pair var17 : var12) {
                                    if (Class5924.method73(
                                            this.field2683.getValue() != AimAssistGunPoint.Off ? this.field2685.getValue() : this.field2684.getValue(),
                                            RotationHelper.method613(var5, var17),
                                            false
                                    )
                                            != null) {
                                        var13 = var17;
                                        break;
                                    }
                                }

                                double var20 = MathHelper.clamp((Double) var13.getLeft(), -this.field2689.method1294() * 10.0, this.field2689.method1294() * 10.0);
                                if ((!this.field2690.getValue() || event.deltaX * var20 >= 0.0) && Math.abs(var20) > this.field2689.method1293() * 10.0) {
                                    event.deltaX += var20;
                                    event.method1021(true);
                                }

                                if (this.field2691.getValue()) {
                                    double var21 = MathHelper.clamp((Double) var13.getRight(), -this.field2689.method1294() * 10.0, this.field2689.method1294() * 10.0);
                                    if ((!this.field2690.getValue() || event.deltaY * var21 >= 0.0) && Math.abs(var21) > this.field2689.method1293() * 10.0) {
                                        event.deltaY += var21;
                                        event.method1021(true);
                                    }
                                }

                                this.field2701 = var6;
                            }
                        }
                    }
                }
            }
        }
    }

    private Vec3d method1573(final Box box, final Entity entity, final RotationHelper rotationHelper) {
        Vec3d vec3d = box.getCenter();
        final double doubleValue = (this.field2683.getValue() != AimAssistGunPoint.Off) ? this.field2685.getValue() : this.field2684.getValue();
        switch (this.field2677.getValue().ordinal()) {
            case 0:
                vec3d = Class5917.method136(box, rotationHelper.method1954(), AimAssist.mc.player.getEyePos());
                break;
            case 1:
                vec3d = Class5917.method123(box, AimAssist.mc.player.getEyePos());
                break;
            case 2:
                vec3d = Class5917.method34(box);
                break;
            case 4: {
                final Vec3d eyePos = entity.getEyePos();
                vec3d = Class5917.method123(new Box(eyePos.x - 0.25, eyePos.y - 0.25, eyePos.z - 0.25, eyePos.x + 0.25, eyePos.y, eyePos.z + 0.25), AimAssist.mc.player.getEyePos());
                break;
            }
        }
        if (!this.field2678.getValue()) {
            return vec3d;
        }
        final boolean method117 = Class5924.method117(AimAssist.mc.player.getEyePos(), vec3d);
        final Vec3d vec3d2 = new Vec3d(vec3d.x, vec3d.y, vec3d.z);
        final Vec3d center = box.getCenter();
        final double n = 1.0 - Class5917.method32(AimAssist.mc.player.getEyePos().distanceTo(vec3d2) / Math.max((this.field2683.getValue() != AimAssistGunPoint.Off) ? this.field2685.getValue() : this.field2684.getValue(), ((this.field2683.getValue() != AimAssistGunPoint.Off) ? this.field2685.getValue() : this.field2684.getValue()) + GhostRotations.INSTANCE.field749.getValue()), 1.0 - GhostRotations.INSTANCE.field748.getValue());
        Vec3d vec3d3 = vec3d2.add((center.x - vec3d2.x) * n, (center.y - vec3d2.y) * (GhostRotations.INSTANCE.field758.getValue() ? (1.0 - n) : 1.0) * GhostRotations.INSTANCE.field757.getValue(), (center.z - vec3d2.z) * n);
        final Vec3d subtract = new Vec3d(AimAssist.mc.player.prevX - AimAssist.mc.player.getX(), AimAssist.mc.player.prevY - AimAssist.mc.player.getY(), AimAssist.mc.player.prevZ - AimAssist.mc.player.getZ()).subtract(entity.prevX - entity.getX(), entity.prevY - entity.getY(), entity.prevZ - entity.getZ());
        if (subtract.lengthSquared() > 0.0) {
            final double n2 = GhostRotations.INSTANCE.field752.getValue() ? subtract.horizontalLength() : 1.0;
            vec3d3 = vec3d3.add(Math.sin(System.currentTimeMillis() * GhostRotations.INSTANCE.field755.getValue() * 0.01) * n2, Math.cos(System.currentTimeMillis() * GhostRotations.INSTANCE.field756.getValue() * 0.01) * (subtract.y + n2 * GhostRotations.INSTANCE.field753.getValue()), Math.sin(System.currentTimeMillis() * GhostRotations.INSTANCE.field755.getValue() * 0.01) * n2);
        }
        final Vec3d subtract2 = vec3d3.subtract(subtract.multiply(GhostRotations.INSTANCE.field751.getValue()));
        Vec3d add = subtract2.add(0.0, -(((GhostRotations.INSTANCE.field760 != null) ? GhostRotations.INSTANCE.field760 : new RotationHelper(AimAssist.mc.player)).method605(Class1202.method2391(AimAssist.mc.player.getEyePos(), subtract2)) / 255.0) * box.getLengthY() * (GhostRotations.INSTANCE.field750.getValue() - 1.0), 0.0);
        if (AimAssist.mc.player.getEyePos().squaredDistanceTo(vec3d) <= doubleValue * doubleValue && method117) {
            while (AimAssist.mc.player.getEyePos().squaredDistanceTo(add) > doubleValue * doubleValue) {
                add = new Vec3d(Class5917.method35(add.x, vec3d.x, GhostRotations.INSTANCE.field746.getValue()), Class5917.method35(add.y, vec3d.y, GhostRotations.INSTANCE.field746.getValue()), Class5917.method35(add.z, vec3d.z, GhostRotations.INSTANCE.field746.getValue()));
            }
            final HitResult method118 = Class5924.method73(doubleValue, Class1202.method2391(AimAssist.mc.player.getEyePos(), add), true);
            if (method118 == null || !(method118 instanceof EntityHitResult) || ((EntityHitResult) method118).getEntity() == null) {
                add = vec3d;
            }
        }
        return Class5917.method33(add, box);
    }

    private Entity method1574() {
        ArrayList var4 = new ArrayList();

        for (Entity var6 : mc.world.getEntities()) {
            if (var6 instanceof LivingEntity
                    && this.method1576(var6)
                    && !(
                    (double) var6.distanceTo(mc.player)
                            > (this.field2683.getValue() != AimAssistGunPoint.Off ? this.field2685.getValue() : this.field2684.getValue() + 1.0)
            )
                    && !((LivingEntity) var6).isDead()
                    && !(((LivingEntity) var6).getHealth() + ((LivingEntity) var6).getAbsorptionAmount() <= 0.0F)
                    && (this.field2686.getValue() || mc.player.canSee(var6))) {
                var4.add(var6);
            }
        }

        return (Entity) var4.stream().min(this.field2702).orElse(null);
    }

    private RotationHelper method1575() {
        return new RotationHelper(mc.player);
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private boolean method1576(Entity var1) {
        if ((var1.isInvisible() || var1.isInvisibleTo(mc.player)) && !this.field2696.getValue()) {
            return false;
        } else if (var1 instanceof PlayerEntity) {
            if (var1 == mc.player) {
                return false;
            } else if (var1 instanceof FakePlayerEntity) {
                return false;
            } else if (Friends.method2055(var1)) {
                return this.field2698.getValue();
            } else {
                return !AntiBots.method2055(var1) && this.field2697.getValue();
            }
        } else {
            return switch (var1.getType().getSpawnGroup()) {
                case SpawnGroup.CREATURE, SpawnGroup.AMBIENT, SpawnGroup.WATER_CREATURE, SpawnGroup.WATER_AMBIENT ->
                        this.field2699.getValue();
                case SpawnGroup.MONSTER -> this.field2700.getValue();
                default -> false;
            };
        }
    }

    private Double lambda$new$2(final Entity entity) {
        if (this.field2694.getValue() && entity == this.field2701) {
            return 0.0;
        }
        return switch (this.field2693.getValue().ordinal()) {
            case 0 ->
                    AimAssist.mc.player.getEyePos().squaredDistanceTo(Class5917.method34(entity.getBoundingBox().expand(entity.getTargetingMargin())));
            case 2 -> {
                if (entity instanceof final LivingEntity livingEntity) {
                    yield (double) livingEntity.getHealth();
                }
                yield 0.0;
            }
            case 1 ->
                    (double) Class1202.method2391(AimAssist.mc.player.getEyePos(), Class5917.method34(entity.getBoundingBox())).method605(this.method1575());
            default -> 0.0;
        };
    }

    private boolean lambda$new$1() {
        return this.field2683.getValue() != AimAssistGunPoint.Off;
    }

    private boolean lambda$new$0() {
        return this.field2683.getValue() == AimAssistGunPoint.Off;
    }
}
