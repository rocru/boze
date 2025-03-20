package dev.boze.client.systems.modules.combat.aura;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.*;
import dev.boze.client.events.*;
import dev.boze.client.jumptable.eI;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.GhostModule;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.combat.Aura;
import dev.boze.client.systems.modules.legit.AntiBots;
import dev.boze.client.systems.modules.misc.AutoEat;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.click.ClickManager;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class1202;
import mapped.Class3089;
import mapped.Class5917;
import mapped.Class5924;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class AuraGhost extends GhostModule {
    private final Aura field2460;
    private final ClickManager field2461;
    public final BooleanSetting field2462 = new BooleanSetting("Render", true, "Render target");
    private final ColorSetting field2463 = new ColorSetting("Color", new BozeDrawColor(1691624484), "Color for fill", this.field2462);
    private final ColorSetting field2464 = new ColorSetting("Outline", new BozeDrawColor(-2874332), "Color for outline", this.field2462);
    private final BooleanSetting field2465 = new BooleanSetting("InInventory", false, "Track and attack players while in inventory");
    private final BooleanSetting field2466 = new BooleanSetting("WeaponOnly", false, "Only attack with weapons");
    private final BooleanSetting field2467 = new BooleanSetting("OnlyWhenHolding", false, "Only attack when holding left click");
    private final EnumSetting<CritMode> field2468 = new EnumSetting<CritMode>(
            "AwaitCrits",
            CritMode.Off,
            "Only attack when you can crit\n - Off: Don't wait for crits\n - Normal: Wait for crits when in air\n - Force: Always wait for crits\n"
    );
    private final MinMaxDoubleSetting field2469 = new MinMaxDoubleSetting("Reach", new double[]{2.95, 3.0}, 1.0, 6.0, 0.05, "Reach");
    private final MinMaxDoubleSetting field2470 = new MinMaxDoubleSetting("AimSpeed", new double[]{0.2, 0.4}, 0.1, 10.0, 0.1, "Aim speed");
    private final EnumSetting<AuraReference> field2471 = new EnumSetting<AuraReference>(
            "Reference", AuraReference.Client, "The frame of reference for rotation-related calculations"
    );
    private final IntSetting field2472 = new IntSetting("FOV", 180, 1, 180, 1, "The maximum FOV to target players within");
    private final EnumSetting<AimPoint> field2473 = new EnumSetting<AimPoint>("AimPoint", AimPoint.Clamped, "Aim point to use");
    private final BooleanSetting field2474 = new BooleanSetting("RayCast", true, "Check if current target can be seen");
    private final EnumSetting<TrackMode> field2475 = new EnumSetting<TrackMode>("Walls", TrackMode.Off, "Whether to track/attack through walls or not");
    private final BooleanSetting field2476 = new BooleanSetting("Avoid", false, "Avoid walls when rotating", this.field2475);
    private final BooleanSetting field2477 = new BooleanSetting("AlwaysHit", false, "(Blatant) Always hit the target");
    private final EnumSetting<ClickMethod> field2478 = new EnumSetting<ClickMethod>("Clicking", ClickMethod.Normal, "The clicking method to use");
    private final IntArraySetting field2479 = new IntArraySetting("CPS", new int[]{8, 12}, 1, 20, 1, "The CPS to click at", this.field2478);
    private final FloatSetting field2480 = new FloatSetting(
            "CooldownOffset", 0.0F, -2.5F, 2.5F, 0.05F, "The offset for vanilla clicking", this::lambda$new$0, this.field2478
    );
    private final BooleanSetting field2481 = new BooleanSetting("ModelDelay", false, "Model attack delays based on human response times", this.field2478);
    private final BooleanSetting field2482 = new BooleanSetting("MissSwing", false, "Swing at the air if target missed", this.field2478);
    private final EnumSetting<Targeting> field2483 = new EnumSetting<Targeting>("Targeting", Targeting.Single, "The targeting method to use");
    private final EnumSetting<AttackPriority> field2484 = new EnumSetting<AttackPriority>(
            "Priority", AttackPriority.Distance, "The priority to target enemies with", this.field2483
    );
    private final BooleanSetting field2485 = new BooleanSetting("Sticky", false, "Stick to the same target when possible", this.field2483);
    private final EnumSetting<AuraAntiBlock> field2486 = new EnumSetting<AuraAntiBlock>("AntiBlock", AuraAntiBlock.Off, "Anti block");
    private final BooleanSetting field2487 = new BooleanSetting("AttackBlocking", true, "Attack players that are blocking", this.field2486);
    private final BooleanSetting field2488 = new BooleanSetting("DisableOnDeath", true, "Disable when you die");
    private final BooleanSetting field2489 = new BooleanSetting("DisableOnTP", true, "Disable when you get teleported");
    private final SettingCategory field2490 = new SettingCategory("Targets", "Entities to target");
    private final BooleanSetting field2491 = new BooleanSetting("Players", true, "Target players", this.field2490);
    private final BooleanSetting field2492 = new BooleanSetting("Friends", false, "Target friends", this.field2490);
    private final BooleanSetting field2493 = new BooleanSetting("Animals", false, "Target animals", this.field2490);
    private final BooleanSetting field2494 = new BooleanSetting("Monsters", false, "Target monsters", this.field2490);
    private final BooleanSetting field2495 = new BooleanSetting("Fireballs", false, "Target fireballs", this.field2490);
    private CopyOnWriteArrayList<Pair<Entity, Vec3d>> field2496 = new CopyOnWriteArrayList();
    private boolean field2497 = false;
    private Entity field2498 = null;
    private final Timer field2499 = new Timer();
    private final Comparator<Pair<Entity, Vec3d>> field2500 = Comparator.comparing(this::lambda$new$1);

    public AuraGhost(Aura aura) {
        this.field2460 = aura;
        this.field2461 = new ClickManager(this.field2478, this.field2479, this.field2480);
    }

    public void method1415() {
        this.field2461.method2172();
    }

    public void method1416() {
        this.field2496 = new CopyOnWriteArrayList();
        this.field2498 = null;
    }

    @Override
    public InteractionMode getInteractionMode() {
        return Options.INSTANCE.method1971() ? InteractionMode.Ghost : this.field2460.mode.getValue();
    }

    public void method1417(final eJ event) {
        if (event.method1101()) {
            return;
        }
        if (AutoEat.method1663()) {
            return;
        }
        if (this.field2466.getValue() && !(AuraGhost.mc.player.getMainHandStack().getItem() instanceof SwordItem) && !(AuraGhost.mc.player.getMainHandStack().getItem() instanceof AxeItem)) {
            this.method1416();
            return;
        }
        if (this.field2467.getValue() && !AuraGhost.mc.options.attackKey.isPressed()) {
            this.method1416();
            return;
        }
        final ArrayList<Pair<Entity, Vec3d>> list = new ArrayList(this.field2496);
        this.field2496.clear();
        final RotationHelper rotationHelper = (GhostRotations.INSTANCE.field760 == null) ? new RotationHelper((Entity)AuraGhost.mc.player) : GhostRotations.INSTANCE.field760;
        for (Entity entity : AuraGhost.mc.world.getEntities()) {
            if (!this.method1426(entity)) {
                continue;
            }
            final Box expand = entity.getBoundingBox().expand((double) entity.getTargetingMargin());
            Vec3d to = expand.getCenter();
            switch (this.field2473.getValue().ordinal()) {
                case 0:
                    to = Class5917.method136(expand, rotationHelper.method1954(), AuraGhost.mc.player.getEyePos());
                    break;
                case 1:
                    to = Class5917.method123(expand, AuraGhost.mc.player.getEyePos());
                    break;
                case 2:
                    to = Class5917.method34(expand);
                    break;
            }
            if (to.squaredDistanceTo(AuraGhost.mc.player.getEyePos()) > this.field2469.method1295() * this.field2469.method1295()) {
                continue;
            }
            if (Class1202.method2391(AuraGhost.mc.player.getEyePos(), to).method605(this.method1427()) > this.field2472.getValue() * 1.417) {
                continue;
            }
            Vec3d vec3d;
            if (expand.contains(AuraGhost.mc.player.getEyePos()) && Class5924.method2116()) {
                vec3d = AuraGhost.mc.player.getEyePos().add(rotationHelper.method1954().multiply(0.01));
            } else {
                vec3d = this.method1424(expand, entity, rotationHelper);
            }
            if (vec3d.squaredDistanceTo(AuraGhost.mc.player.getEyePos()) > this.field2469.method1295() * this.field2469.method1295()) {
                vec3d = to;
            }
            if (this.field2475.getValue() != TrackMode.Attack && (this.field2475.getValue() == TrackMode.Off || !list.stream().noneMatch(arg_0 -> AuraGhost.lambda$onRotate$2(entity, arg_0))) && !Class5924.method117(AuraGhost.mc.player.getEyePos(), vec3d)) {
                continue;
            }
            this.field2496.add((Pair<Entity, Vec3d>) new Pair((Object) entity, (Object) vec3d));
        }
        if (!this.field2496.isEmpty()) {
            this.field2496.sort(Comparator.comparing(AuraGhost::lambda$onRotate$3).thenComparing(this::lambda$onRotate$4).thenComparing(this::lambda$onRotate$5).thenComparing(this.field2500));
            if (this.method1430(event, rotationHelper)) {
                return;
            }
            final Pair pair = this.field2496.get(0);
            final RotationHelper method2391 = Class1202.method2391(AuraGhost.mc.player.getEyePos(), (Vec3d)pair.getRight());
            RotationHelper method2392 = rotationHelper.method603(method2391, this.field2470.getValue());
            final HitResult method2393 = Class5924.method73(this.field2469.method1295(), method2392, true);
            if (this.field2477.getValue() && !(method2393 instanceof EntityHitResult) && ((Vec3d)pair.getRight()).squaredDistanceTo(AuraGhost.mc.player.getEyePos()) <= this.field2469.method1295() * this.field2469.method1295()) {
                method2392 = method2391;
            }
            event.method1099(method2392.method600(this::lambda$onRotate$6));
        }
        else {
            this.method1416();
            this.method1430(event, rotationHelper);
        }
    }

    public void method1418(OpenScreenEvent event) {
        if (event.screen instanceof DeathScreen && this.field2488.getValue()) {
            this.field2460.setEnabled(false);
        }
    }

    public void method1419(PacketBundleEvent event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket && this.field2489.getValue() && MinecraftUtils.isClientActive()) {
            this.field2460.setEnabled(false);
        }
    }

    public void method1420(PreTickEvent event) {
        if (this.field2497) {
            this.field2461.method2172();
        }

        this.field2497 = true;
    }

    public void method1421(RotationEvent event) {
        this.field2497 = false;
        if (this.field2465.getValue() || !(mc.currentScreen instanceof HandledScreen)) {
            if (!this.field2466.getValue()
                    || mc.player.getMainHandStack().getItem() instanceof SwordItem
                    || mc.player.getMainHandStack().getItem() instanceof AxeItem) {
                if (!this.field2467.getValue() || mc.options.attackKey.isPressed()) {
                    ArrayList<Pair<Entity, Vec3d>> var5 = new ArrayList<>();
                    this.field2496.stream().filter(this::lambda$onInteract$7).forEach(arg_0 -> this.lambda$onInteract$8(var5, arg_0));
                    if (!var5.isEmpty() && !event.method2114()) {
                        int var6 = this.field2461.method2171();
                        if (this.field2486.getValue() == AuraAntiBlock.Always
                                && var6 == 0
                                && mc.player.getMainHandStack().getItem() instanceof AxeItem
                                && var5.stream().anyMatch(AuraGhost::lambda$onInteract$9)) {
                            var6 = 1;
                        }

                        if (!mc.player.isUsingItem() && !this.field2468.getValue().method2114()) {
                            boolean var7 = false;

                            for (Pair var9 : var5) {
                                Entity var10 = (Entity) var9.getLeft();
                                Vec3d var11 = (Vec3d) var9.getRight();
                                this.method1423(var10, var6, var11);
                                event.method2142();
                                var7 = true;
                                if (this.field2483.getValue() == Targeting.Single) {
                                    break;
                                }
                            }

                            if (!var7 && this.field2482.getValue()) {
                                HitResult var12 = Class5924.method73(
                                        this.field2469.method1295(),
                                        GhostRotations.INSTANCE.field760 != null ? GhostRotations.INSTANCE.field760 : new RotationHelper(mc.player),
                                        false
                                );
                                if (var12 instanceof Class3089) {
                                    this.method1423(null, var6, null);
                                    event.method2142();
                                }
                            }

                            this.field2469.method1296();
                        }
                    } else {
                        this.field2461.method2172();
                    }
                }
            }
        }
    }

    public void method1422(Render3DEvent event) {
        if (!this.field2499.hasElapsed(2500.0) && GhostRotations.INSTANCE.field760 != null && this.field2498 != null) {
            double var4 = MathHelper.lerp(event.field1951, this.field2498.lastRenderX, this.field2498.getX()) - this.field2498.getX();
            double var6 = MathHelper.lerp(event.field1951, this.field2498.lastRenderY, this.field2498.getY()) - this.field2498.getY();
            double var8 = MathHelper.lerp(event.field1951, this.field2498.lastRenderZ, this.field2498.getZ()) - this.field2498.getZ();
            Box var10 = this.field2498.getBoundingBox();
            if (var10 != null) {
                event.field1950
                        .method1271(
                                var4 + var10.minX,
                                var6 + var10.minY,
                                var8 + var10.minZ,
                                var4 + var10.maxX,
                                var6 + var10.maxY,
                                var8 + var10.maxZ,
                                this.field2463.getValue(),
                                this.field2464.getValue(),
                                ShapeMode.Full,
                                0
                        );
            }
        }
    }

    private void method1423(Entity var1, int var2, Vec3d var3) {
        for (int var7 = 0; var7 < var2; var7++) {
            Class5924.method72(var1, var3);
        }

        if (var1 != null) {
            this.field2498 = var1;
            this.field2499.reset();
        }
    }

    private Vec3d method1424(Box box, Entity entity, RotationHelper rotationHelper) {
        double d;
        Vec3d vec3d;
        Vec3d vec3d2 = box.getCenter();
        switch (this.field2473.getValue().ordinal()) {
            case 0: {
                vec3d2 = Class5917.method136(box, rotationHelper.method1954(), AuraGhost.mc.player.getEyePos());
                break;
            }
            case 1: {
                vec3d2 = Class5917.method123(box, AuraGhost.mc.player.getEyePos());
                break;
            }
            case 2: {
                vec3d2 = Class5917.method34(box);
            }
        }
        boolean bl = Class5924.method117(AuraGhost.mc.player.getEyePos(), vec3d2);
        int n = 0;
        if (this.field2476.getValue() && !bl) {
            vec3d = null;
            double d2 = Double.POSITIVE_INFINITY;
            for (double d3 = 0.0; d3 <= 1.0 && n++ < 1000; d3 += GhostRotations.INSTANCE.field746.getValue().doubleValue()) {
                for (double d4 = 0.0; d4 <= 1.0 && n++ < 1000; d4 += GhostRotations.INSTANCE.field746.getValue().doubleValue()) {
                    for (d = 0.0; d <= 1.0 && n++ < 1000; d += GhostRotations.INSTANCE.field746.getValue().doubleValue()) {
                        double d5;
                        Vec3d vec3d3 = new Vec3d(box.minX + (box.maxX - box.minX) * d3, box.minY + (box.maxY - box.minY) * d4, box.minZ + (box.maxZ - box.minZ) * d);
                        if (!Class5924.method117(AuraGhost.mc.player.getEyePos(), vec3d3) || !((d5 = AuraGhost.mc.player.getEyePos().squaredDistanceTo(vec3d3)) < d2)) continue;
                        vec3d = vec3d3;
                        d2 = d5;
                    }
                }
            }
            if (vec3d != null) {
                vec3d2 = vec3d;
                bl = true;
            }
        }
        vec3d = new Vec3d(vec3d2.x, vec3d2.y, vec3d2.z);
        Vec3d vec3d4 = box.getCenter();
        double d6 = 1.0 - Class5917.method32(AuraGhost.mc.player.getEyePos().distanceTo(vec3d) / Math.max(this.field2469.method1295(), this.field2469.method1295() + GhostRotations.INSTANCE.field749.getValue()), 1.0 - GhostRotations.INSTANCE.field748.getValue());
        vec3d = vec3d.add((vec3d4.x - vec3d.x) * d6, (vec3d4.y - vec3d.y) * (GhostRotations.INSTANCE.field758.getValue() ? 1.0 - d6 : 1.0) * GhostRotations.INSTANCE.field757.getValue(), (vec3d4.z - vec3d.z) * d6);
        Vec3d vec3d5 = new Vec3d(AuraGhost.mc.player.prevX - AuraGhost.mc.player.getX(), AuraGhost.mc.player.prevY - AuraGhost.mc.player.getY(), AuraGhost.mc.player.prevZ - AuraGhost.mc.player.getZ());
        Vec3d vec3d6 = vec3d5.subtract(entity.prevX - entity.getX(), entity.prevY - entity.getY(), entity.prevZ - entity.getZ());
        if (vec3d6.lengthSquared() > 0.0) {
            double d7 = GhostRotations.INSTANCE.field752.getValue() != false ? vec3d6.horizontalLength() : 1.0;
            vec3d = vec3d.add(Math.sin((double)System.currentTimeMillis() * GhostRotations.INSTANCE.field755.getValue() * 0.01) * d7, Math.cos((double)System.currentTimeMillis() * GhostRotations.INSTANCE.field756.getValue() * 0.01) * (vec3d6.y + d7 * GhostRotations.INSTANCE.field753.getValue()), Math.sin((double)System.currentTimeMillis() * GhostRotations.INSTANCE.field755.getValue() * 0.01) * d7);
        }
        vec3d = vec3d.subtract(vec3d6.multiply(GhostRotations.INSTANCE.field751.getValue()));
        RotationHelper rotationHelper2 = GhostRotations.INSTANCE.field760 != null ? GhostRotations.INSTANCE.field760 : new RotationHelper((Entity)AuraGhost.mc.player);
        d = (double)rotationHelper2.method605(Class1202.method2391(AuraGhost.mc.player.getEyePos(), vec3d)) / 255.0;
        vec3d = vec3d.add(0.0, -d * box.getLengthY() * (GhostRotations.INSTANCE.field750.getValue() - 1.0), 0.0);
        while (bl && !Class5924.method117(AuraGhost.mc.player.getEyePos(), vec3d) && this.field2476.getValue() && n++ < 1000) {
            vec3d = new Vec3d(Class5917.method35(vec3d.x, vec3d2.x, GhostRotations.INSTANCE.field746.getValue()), Class5917.method35(vec3d.y, vec3d2.y, GhostRotations.INSTANCE.field746.getValue()), Class5917.method35(vec3d.z, vec3d2.z, GhostRotations.INSTANCE.field746.getValue()));
        }
        double d8 = AuraGhost.mc.player.getEyePos().squaredDistanceTo(vec3d2);
        if (d8 <= this.field2469.method1295() * this.field2469.method1295() && bl) {
            while (AuraGhost.mc.player.getEyePos().squaredDistanceTo(vec3d) > this.field2469.method1295() * this.field2469.method1295() && n++ < 1000) {
                vec3d = new Vec3d(Class5917.method35(vec3d.x, vec3d2.x, GhostRotations.INSTANCE.field746.getValue()), Class5917.method35(vec3d.y, vec3d2.y, GhostRotations.INSTANCE.field746.getValue()), Class5917.method35(vec3d.z, vec3d2.z, GhostRotations.INSTANCE.field746.getValue()));
            }
            HitResult hitResult = Class5924.method73(this.field2469.method1295(), Class1202.method2391(AuraGhost.mc.player.getEyePos(), vec3d), true);
            if (hitResult == null || !(hitResult instanceof EntityHitResult) || ((EntityHitResult)hitResult).getEntity() == null) {
                vec3d = vec3d2;
            }
        } else if (this.field2469.method1295() > this.field2469.method1295()) {
            d8 = (Math.sqrt(d8) - this.field2469.method1294()) / (this.field2469.method1294() - this.field2469.method1293());
            vec3d = vec3d.add((vec3d2.x - vec3d.x) * (1.0 - d8), (vec3d2.y - vec3d.y) * (1.0 - d8), (vec3d2.z - vec3d.z) * (1.0 - d8));
        }
        vec3d2 = Class5917.method33(vec3d, box);
        return vec3d2;
    }

    private boolean method1425(Entity var1) {
        if (!(mc.player.getMainHandStack().getItem() instanceof AxeItem)) {
            return this.field2487.getValue()
                    || !(var1 instanceof LivingEntity)
                    || !((LivingEntity) var1).isBlocking()
                    || !((LivingEntity) var1).blockedByShield(var1.getDamageSources().playerAttack(mc.player));
        } else return this.field2486.getValue() != AuraAntiBlock.On
                || !(var1 instanceof PlayerEntity var5)
                || !(var5.getOffHandStack().getItem() instanceof ShieldItem)
                || (var5.isBlocking() && var5.blockedByShield(var1.getDamageSources().playerAttack(mc.player)));
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private boolean method1426(Entity var1) {
        if (var1.isSpectator()) {
            return false;
        } else if (var1 instanceof PlayerEntity) {
            if (var1 == mc.player) {
                return false;
            } else if (var1 instanceof FakePlayerEntity) {
                return false;
            } else if (Friends.method2055(var1)) {
                return this.field2492.getValue();
            } else {
                return !AntiBots.method2055(var1) && this.field2491.getValue();
            }
        } else if (var1 instanceof FireballEntity) {
            return this.field2495.getValue();
        } else {
            switch (eI.field2096[var1.getType().getSpawnGroup().ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return this.field2493.getValue();
                case 5:
                    return this.field2494.getValue();
                default:
                    return false;
            }
        }
    }

    private RotationHelper method1427() {
        return this.field2471.getValue() == AuraReference.Server && GhostRotations.INSTANCE.field760 != null
                ? GhostRotations.INSTANCE.field760
                : new RotationHelper(mc.player);
    }

    private boolean method1428(Entity var1) {
        return var1 instanceof PlayerEntity var5 && (var5.getInventory().getMainHandStack().getItem() == Items.SHIELD || var5.getInventory().offHand.get(0).getItem() == Items.SHIELD);
    }

    private boolean method1429(Predicate<LivingEntity> var1) {
        if (this.field2483.getValue() == Targeting.Single) {
            Optional<Entity> var5 = this.field2496.stream().findFirst().map(AuraGhost::lambda$allAttackedLivingEntities$10);
            if (var5.isPresent() && var5.get() instanceof LivingEntity var6) {
                return var1.test(var6);
            } else {
                return false;
            }
        } else {
            return this.field2496.stream().allMatch(var0 -> ((Pair<Entity, Vec3d>) null).getLeft() instanceof LivingEntity && ((Predicate<Entity>) var0).test(((Pair<Entity, Vec3d>) null).getLeft()));
        }
    }

    public boolean method1430(eJ event, RotationHelper currentRot) {
        if (!this.field2465.getValue() && mc.currentScreen instanceof HandledScreen) {
            if (!event.method1101() && GhostRotations.INSTANCE.field760 != null) {
                event.method1099(currentRot);
            }

            return true;
        } else {
            return false;
        }
    }

    private static Entity lambda$allAttackedLivingEntities$10(Pair<Entity, Vec3d> var0) {
        return var0.getLeft();
    }

    private static boolean lambda$onInteract$9(Pair<Entity, Vec3d> var0) {
        return var0.getLeft() instanceof LivingEntity
                && ((LivingEntity) var0.getLeft()).isBlocking()
                && ((LivingEntity) var0.getLeft()).blockedByShield(var0.getLeft().getDamageSources().playerAttack(mc.player));
    }

    private void lambda$onInteract$8(ArrayList<Pair<Entity, Vec3d>> var1, Pair<Entity, Vec3d> var2) {
        Entity var6 = var2.getLeft();
        Vec3d var7 = var2.getRight();
        double var8 = var7.squaredDistanceTo(mc.player.getEyePos());
        if (this.field2474.getValue()) {
            if (GhostRotations.INSTANCE.field760 == null) {
                return;
            }

            HitResult var10 = Class5924.method73(
                    this.field2469.method1295(),
                    this.field2483.getValue() == Targeting.Single
                            ? (
                            this.field2481.getValue()
                                    ? new RotationHelper(((ClientPlayerEntityAccessor) mc.player).getLastYaw(), ((ClientPlayerEntityAccessor) mc.player).getLastPitch())
                                    : GhostRotations.INSTANCE.field760
                    )
                            : Class1202.method2391(mc.player.getEyePos(), var7),
                    this.field2475.getValue() == TrackMode.Attack
            );
            if (!(var10 instanceof EntityHitResult) || ((EntityHitResult) var10).getEntity() == null) {
                return;
            }

            var6 = ((EntityHitResult) var10).getEntity();
        } else if (var8 > this.field2469.method1295() * this.field2469.method1295()) {
            return;
        }

        var1.add(new Pair<>(var6, var7));
    }

    private boolean lambda$onInteract$7(Pair<Entity, Vec3d> var1) {
        return this.method1425(var1.getLeft());
    }

    private Boolean lambda$onRotate$6(RotationHelper var1) {
        return Class5924.method73(this.field2469.method1295(), var1, this.field2475.getValue() == TrackMode.Attack) instanceof EntityHitResult;
    }

    private Boolean lambda$onRotate$5(Pair<Entity, Vec3d> var1) {
        return mc.player.getEyePos().squaredDistanceTo(var1.getRight()) > this.field2469.method1295() * this.field2469.method1295();
    }

    private Boolean lambda$onRotate$4(Pair<Entity, Vec3d> var1) {
        return !this.method1425(var1.getLeft());
    }

    private static Boolean lambda$onRotate$3(Pair<Entity, Vec3d> var0) {
        return !(var0.getLeft() instanceof LivingEntity) || ((LivingEntity) var0.getLeft()).isDead();
    }

    private static boolean lambda$onRotate$2(Entity var0, Pair<Entity, Vec3d> var1) {
        return var1.getLeft() == var0;
    }

    private Double lambda$new$1(Pair<?, ?> pair) {
        if (this.field2485.getValue() && pair.getLeft() == this.field2498) {
            return 0.0;
        }

        AttackPriority priority = this.field2484.getValue();
        switch (priority.ordinal()) {
            case 0 -> {
                Vec3d eyePos = AuraGhost.mc.player.getEyePos();
                Entity entity = (Entity) pair.getLeft();
                Box box = entity.getBoundingBox();
                double margin = entity.getTargetingMargin();
                Box expandedBox = box.expand(margin);
                Vec3d targetPos = Class5917.method34(expandedBox);
                return eyePos.squaredDistanceTo(targetPos);
            }
            case 1 -> {
                Object obj = pair.getLeft();
                if (obj instanceof LivingEntity living) {
                    return (double) living.getHealth();
                }
                return 0.0;
            }
            case 2 -> {
                Vec3d eyePos = AuraGhost.mc.player.getEyePos();
                Entity entity = (Entity) pair.getLeft();
                Box box = entity.getBoundingBox();
                Vec3d targetPos = Class5917.method34(box);
                RotationHelper computedRotation = Class1202.method2391(eyePos, targetPos);
                return (double) computedRotation.method605(this.method1427());
            }
            default -> {
                return 0.0;
            }
        }
    }

    private boolean lambda$new$0() {
        return this.field2478.getValue() == ClickMethod.Vanilla;
    }
}
