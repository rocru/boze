package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.AutoWalkMode;
import dev.boze.client.enums.SpeedMode;
import dev.boze.client.events.*;
import dev.boze.client.mixin.LivingEntityAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.fakeplayer.FakeClientPlayerEntity;
import dev.boze.client.utils.player.RotationHandler;
import mapped.Class3076;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.CobwebBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Speed extends Module {
    public static final Speed INSTANCE = new Speed();
    private final EnumSetting<SpeedMode> field715 = new EnumSetting<SpeedMode>(
            "Mode",
            SpeedMode.Strafe,
            "Mode for speed\n - Vanilla: Vanilla speed\n - Strafe: Generic strafe, optimized for NCP\n - Grim: Strafe from Grim\n - BHop: Generic BHop, customizable speed\n - OnGround: OnGround (packet) speed\n"
    );
    private final MinMaxSetting field716 = new MinMaxSetting("EntityBoost", 0.0, 0.0, 0.5, 0.05, "Boost when passing by entities", this::lambda$new$0);
    private final FloatSetting field717 = new FloatSetting("Speed", 1.0F, 0.1F, 50.0F, 0.02F, "Speed multiplier", this::lambda$new$1);
    private final MinMaxSetting field718 = new MinMaxSetting("AccelSpeed", 1.0, 0.05, 1.0, 0.05, "Acceleration speed", this::lambda$new$2);
    private final BooleanSetting field719 = new BooleanSetting("Strict", true, "Makes speed work better on stricter servers", this::lambda$new$3);
    private final FloatSetting field720 = new FloatSetting("HopReduction", 0.0F, 0.0F, 1.0F, 0.05F, "Hop reduction", this::lambda$new$4);
    private final IntSetting field721 = new IntSetting("HopPulse", 0, 0, 5, 1, "Hop pulse", this::lambda$new$5);
    private final BooleanSetting field722 = new BooleanSetting("UseTimer", true, "Use timer to go slightly faster", this::lambda$new$6);
    private final BooleanSetting field723 = new BooleanSetting("CrystalBoost", false, "Use crystal explosions to go faster momentarily", this::lambda$new$7);
    private final MinMaxSetting field724 = new MinMaxSetting("Factor", 1.0, 0.1, 2.0, 0.05, "Boost factor", this.field723);
    private final BooleanSetting field725 = new BooleanSetting("NoJump", false, "Don't jump", this::lambda$new$8);
    private final BooleanSetting field726 = new BooleanSetting("WhileSneaking", false, "Speed while sneaking", this::lambda$new$9);
    private final BooleanSetting field727 = new BooleanSetting("InLiquid", true, "Speed in liquids");
    private final BooleanSetting field728 = new BooleanSetting("InWebs", true, "Speed in webs");
    private final BooleanSetting field729 = new BooleanSetting("AvoidUnloaded", true, "Avoid flying into unloaded chunks", this::lambda$new$10);
    private final IntSetting field730 = new IntSetting(
            "Ticks", 5, 1, 20, 1, "Amount of ticks to simulate\nHigher value = less chance of rubber-banding", this::lambda$new$11, this.field729
    );
    private double field731 = 0.0;
    private double field732 = 0.0;
    private boolean field733 = false;
    private int field734 = 4;
    private int field735;
    private int field736 = 0;
    private int field737 = 0;
    private final Timer field738 = new Timer();
    private double field739 = 0.0;
    private final Timer field740 = new Timer();
    private final Timer field741 = new Timer();
    public static boolean field742 = false;
    private float field743;
    private float aa;
    private float ab;
    private boolean ac = false;
    private double ad;

    public Speed() {
        super("Speed", "Makes you move faster", Category.Movement);
    }

    private boolean method1971() {
        Box var4 = mc.player.getBoundingBox().withMinY(this.ad + 2.0).withMaxY(this.ad + 2.21);
        Box var5 = mc.player.getBoundingBox().withMinY(this.ad).withMaxY(this.ad + 1.8);
        return !mc.world.isSpaceEmpty(var4) && mc.world.isSpaceEmpty(var5);
    }

    @EventHandler(
            priority = 25
    )
    public void method1871(PlayerGrimV3BypassEvent event) {
        if (this.field715.getValue() == SpeedMode.Grim
                || (this.field715.getValue() == SpeedMode.Strafe || this.field715.getValue() == SpeedMode.BHop)
                && this.method1971()
                && (this.field743 != 0.0F || this.aa != 0.0F)) {
            if (!mc.player.isSneaking()) {
                if (event.method1022()) {
                    this.ab = -420.0F;
                } else {
                    this.ab = mc.player.getYaw();
                    if (this.field743 == 0.0F && this.aa == 0.0F) {
                        this.ab = -420.0F;
                    } else {
                        if (this.field743 != 0.0F) {
                            if (this.aa >= 1.0F) {
                                this.ab = this.ab + (float) (this.field743 > 0.0F ? -45 : 45);
                            } else if (this.aa <= -1.0F) {
                                this.ab = this.ab + (float) (this.field743 > 0.0F ? 45 : -45);
                            }

                            if (this.field743 < 0.0F) {
                                this.ab += 180.0F;
                            }
                        } else if (this.aa != 0.0F) {
                            if (this.aa >= 1.0F) {
                                this.ab += -90.0F;
                            } else if (this.aa <= 1.0F) {
                                this.ab += 90.0F;
                            }
                        }

                        this.ab %= 360.0F;

                        try {
                            mc.player.setSprinting(true);
                        } catch (Exception var6) {
                        }

                        event.yaw = this.ab;
                        event.method1021(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void method2041(MovementEvent event) {
        if (this.field715.getValue() == SpeedMode.Grim && this.field716.getValue() > 0.0) {
            for (Entity var6 : mc.world.getEntities()) {
                if (var6 instanceof LivingEntity
                        && var6 != mc.player
                        && !(var6 instanceof ArmorStandEntity)
                        && !(var6 instanceof FakePlayerEntity)
                        && !(var6 instanceof FakeClientPlayerEntity)
                        && (double) var6.distanceTo(mc.player) <= 1.5) {
                    mc.player
                            .setVelocity(
                                    mc.player.getVelocity().x * (1.0 + this.field716.getValue()),
                                    mc.player.getVelocity().y,
                                    mc.player.getVelocity().z * (1.0 + this.field716.getValue())
                            );
                    return;
                }
            }
        }
    }

    @EventHandler
    private void method1810(PostTickEvent var1) {
        if (MinecraftUtils.isClientActive()) {
            if ((this.field715.getValue() == SpeedMode.Strafe || this.field715.getValue() == SpeedMode.BHop) && this.method1971()) {
                ((LivingEntityAccessor) mc.player).setJumpCooldown(0);
                this.field741.reset();
                this.ac = true;
            }
        }
    }

    @EventHandler(
            priority = 25
    )
    public void method1872(TickInputPostEvent event) {
        if (!RotationHandler.field1546.method2114()) {
            if (!mc.player.isSneaking()) {
                if (this.field715.getValue() == SpeedMode.Grim
                        || (this.field715.getValue() == SpeedMode.Strafe || this.field715.getValue() == SpeedMode.BHop)
                        && this.method1971()
                        && (event.field1954 != 0.0F || event.field1953 != 0.0F)) {
                    this.field743 = event.field1954;
                    this.aa = event.field1953;
                    if (event.field1954 != 0.0F || event.field1953 != 0.0F) {
                        event.field1954 = 1.0F;
                        if (!this.field725.getValue() || this.field715.getValue() != SpeedMode.Grim) {
                            event.field1955 = true;
                        }
                    }

                    event.field1953 = 0.0F;
                }
            }
        }
    }

    @EventHandler(priority = 40)
    public void method1893(final PlayerMoveEvent event) {
        if (event.field1892) {
            return;
        }
        if (!this.field726.getValue() && Speed.mc.player.isSneaking()) {
            return;
        }
        if (!this.field727.getValue() && Class5924.method91(FluidBlock.class)) {
            return;
        }
        if (!this.field728.getValue() && Class5924.method91(CobwebBlock.class)) {
            return;
        }
        if (!this.field738.hasElapsed(350.0)) {
            return;
        }
        if (this.field715.getValue() == SpeedMode.Grim) {
            return;
        }
        if (this.ac) {
            if (!Speed.mc.player.isOnGround()) {
                return;
            }
            this.ac = false;
        }
        final double x = event.vec3.x;
        double n = event.vec3.y;
        final double z = event.vec3.z;
        final float n2 = (AutoWalk.INSTANCE.isEnabled() && AutoWalk.INSTANCE.field3148.getValue() != AutoWalkMode.Baritone) ? (AutoWalk.INSTANCE.field3147.getValue() ? -1.0f : 1.0f) : Speed.mc.player.input.movementForward;
        switch (this.field715.getValue().ordinal()) {
            case 0: {
                final Vec3d method95 = Class5924.method95(this.field717.getValue() * 10.0f);
                double x2 = method95.getX();
                double z2 = method95.getZ();
                if (Speed.mc.player.hasStatusEffect(StatusEffects.SPEED)) {
                    final double n3 = (Speed.mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier() + 1) * 0.205;
                    x2 += x2 * n3;
                    z2 += z2 * n3;
                }
                if (this.field729.getValue() && !Speed.mc.world.getChunkManager().isChunkLoaded(ChunkSectionPos.getSectionCoord(Speed.mc.player.getX() + x2 * this.field730.getValue()), ChunkSectionPos.getSectionCoord(Speed.mc.player.getZ() + z2 * this.field730.getValue()))) {
                    x2 = 0.0;
                    z2 = 0.0;
                }
                final Vec3d velocity = Speed.mc.player.getVelocity();
                if (this.field718.getValue() < 1.0 && velocity.subtract(x2, n, z2).length() > 0.5) {
                    x2 = velocity.x + (x2 - velocity.x) * this.field718.getValue();
                    z2 = velocity.z + (z2 - velocity.z) * this.field718.getValue();
                }
                event.vec3 = new Vec3d(x2, n, z2);
                event.field1892 = true;
                if (this.field718.getValue() < 1.0) {
                    Speed.mc.player.setVelocity(event.vec3);
                }
                break;
            }
            case 1: {
                if (Speed.mc.player.isOnGround()) {
                    this.ad = Speed.mc.player.getY();
                }
                if (this.method1971()) {
                    this.field734 = 4;
                    this.field731 = Class5924.method2091();
                    this.field732 = 0.0;
                    return;
                }
                double x3;
                double z3;
                if (this.field719.getValue()) {
                    ++this.field735;
                    this.field735 %= 5;
                    if (this.field735 != 0) {
                        Class3076.method6025(this);
                    }
                    else if (Class5924.method2116()) {
                        if (this.field722.getValue()) {
                            Class3076.method6024(this, 10, 1.44f);
                        }
                        Speed.mc.player.setVelocity(Speed.mc.player.getVelocity().x * 1.0199999809265137, Speed.mc.player.getVelocity().y, Speed.mc.player.getVelocity().z * 1.0199999809265137);
                    }
                    if (Speed.mc.player.isOnGround() && Class5924.method2116()) {
                        this.field734 = 2;
                    }
                    if (this.method355(Speed.mc.player.getY() - (int)Speed.mc.player.getY()) == this.method355(0.138)) {
                        Speed.mc.player.setVelocity(Speed.mc.player.getVelocity().x, Speed.mc.player.getVelocity().y - 0.08, Speed.mc.player.getVelocity().z);
                        n -= 0.09316090325960147;
                        Speed.mc.player.setPos(Speed.mc.player.getX(), Speed.mc.player.getY() - 0.09316090325960147, Speed.mc.player.getZ());
                    }
                    if (this.field734 == 1 && (n2 != 0.0f || Speed.mc.player.input.movementSideways != 0.0f)) {
                        this.field734 = 2;
                        this.field732 = 1.38 * Class5924.method2091() - 0.01;
                    }
                    else if (this.field734 == 2) {
                        this.field734 = 3;
                        n = this.method1389(0.399399995803833);
                        if (!this.method1972()) {
                            n = 0.2;
                        }
                        Speed.mc.player.setVelocity(Speed.mc.player.getVelocity().x, n, Speed.mc.player.getVelocity().z);
                        this.field732 *= 2.149;
                    }
                    else if (this.field734 == 3) {
                        this.field734 = 4;
                        this.field732 -= 0.66 * (this.field732 - Class5924.method2091());
                    }
                    else {
                        if (!Speed.mc.world.isSpaceEmpty(Speed.mc.player.getBoundingBox().offset(0.0, Speed.mc.player.getVelocity().y, 0.0)) || Speed.mc.player.verticalCollision) {
                            this.field734 = 1;
                        }
                        this.field732 -= this.field732 / 159.0;
                    }
                    this.field732 = Math.max(this.field732, Class5924.method2091());
                    if (this.field739 > 0.0 && this.field723.getValue() && !this.field740.hasElapsed(75.0)) {
                        this.field732 = Math.max(this.field732, this.field739);
                    }
                    else if (this.field719.getValue()) {
                        this.field732 = Math.min(this.field732, (this.field736 > 25) ? 0.449 : 0.433);
                    }
                    float n4 = n2;
                    float movementSideways = Speed.mc.player.input.movementSideways;
                    float yaw = Speed.mc.player.getYaw();
                    ++this.field736;
                    if (this.field736 > 50) {
                        this.field736 = 0;
                    }
                    if (n4 != 0.0f || movementSideways != 0.0f) {
                        if (n4 != 0.0f) {
                            if (movementSideways >= 1.0f) {
                                yaw += ((n4 > 0.0f) ? -45 : 45);
                                movementSideways = 0.0f;
                            }
                            else if (movementSideways <= -1.0f) {
                                yaw += ((n4 > 0.0f) ? 45 : -45);
                                movementSideways = 0.0f;
                            }
                            if (n4 > 0.0f) {
                                n4 = 1.0f;
                            }
                            else if (n4 < 0.0f) {
                                n4 = -1.0f;
                            }
                        }
                    }
                    final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                    final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                    x3 = n4 * this.field732 * cos + movementSideways * this.field732 * sin;
                    z3 = n4 * this.field732 * sin - movementSideways * this.field732 * cos;
                    if (n4 == 0.0f && movementSideways == 0.0f) {
                        x3 = 0.0;
                        z3 = 0.0;
                    }
                }
                else {
                    if (this.field734 != 1 || n2 == 0.0f || Speed.mc.player.input.movementSideways == 0.0f) {
                        if (this.field734 == 2 && (n2 != 0.0f || Speed.mc.player.input.movementSideways != 0.0f)) {
                            double method96 = this.method1389(0.3999);
                            if (!this.method1972()) {
                                method96 = 0.2;
                            }
                            Speed.mc.player.setVelocity(new Vec3d(Speed.mc.player.getVelocity().x, method96, Speed.mc.player.getVelocity().z));
                            n = method96;
                            this.field731 *= (this.field733 ? (1.6835 * this.field717.getValue()) : (1.395 * this.field717.getValue()));
                        }
                        else if (this.field734 == 3) {
                            this.field731 = this.field732 - 0.66 * (this.field732 - Class5924.method2091());
                            this.field733 = !this.field733;
                        }
                        else {
                            if ((!Speed.mc.world.isSpaceEmpty(Speed.mc.player.getBoundingBox().offset(0.0, Speed.mc.player.getVelocity().y, 0.0)) || Speed.mc.player.verticalCollision) && this.field734 > 0) {
                                this.field734 = ((n2 != 0.0f || Speed.mc.player.input.movementSideways != 0.0f) ? 1 : 0);
                            }
                            this.field731 = this.field732 - this.field732 / 159.0;
                        }
                    }
                    else {
                        this.field731 = 1.35 * Class5924.method2091() - 0.01;
                    }
                    this.field731 = Math.max(this.field731, Class5924.method2091());
                    if (this.field739 > 0.0 && this.field723.getValue() && !this.field740.hasElapsed(75.0)) {
                        this.field731 = Math.max(this.field731, this.field739);
                    }
                    else if (this.field719.getValue()) {
                        this.field731 = Math.min(this.field731, (this.field736 > 25) ? 0.449 : 0.433);
                    }
                    final Vec3d method97 = Class5924.method93(this.field731);
                    x3 = method97.x;
                    z3 = method97.z;
                    if (n2 != 0.0f || Speed.mc.player.input.movementSideways != 0.0f) {
                        ++this.field734;
                    }
                }
                event.vec3 = new Vec3d(x3, n, z3);
                event.field1892 = true;
                break;
            }
            case 3: {
                if (Speed.mc.player.isOnGround()) {
                    this.ad = Speed.mc.player.getY();
                }
                if (this.method1971()) {
                    this.field734 = 4;
                    this.field731 = Class5924.method2091();
                    this.field732 = 0.0;
                    return;
                }
                switch (this.field734) {
                    case 0:
                        if (Class5924.method2116()) {
                            ++this.field734;
                            this.field731 = 1.18 * Class5924.method2091() - 0.01;
                        }
                    case 1:
                        if (!Class5924.method2116()) {
                            break;
                        }
                        if (!Speed.mc.player.isOnGround()) {
                            break;
                        }
                        n = this.method1389(0.40123128);
                        if (!this.method1972()) {
                            n = 0.2;
                        }
                        Speed.mc.player.setVelocity(new Vec3d(Speed.mc.player.getVelocity().x, n, Speed.mc.player.getVelocity().z));
                        this.field731 *= this.field717.getValue() * 1.6;
                        if (this.field721.getValue() > 0) {
                            if (this.field737 % this.field721.getValue() != 0) {
                                this.field731 *= 1.0 - this.field720.getValue();
                            }
                            ++this.field737;
                        }
                        ++this.field734;
                        break;
                    case 2:
                        this.field731 = this.field732 - 0.76 * (this.field732 - Class5924.method2091());
                        ++this.field734;
                        break;
                    case 3:
                    case 4:
                        if (!Speed.mc.world.isSpaceEmpty(Speed.mc.player.getBoundingBox().offset(0.0, Speed.mc.player.getVelocity().y, 0.0)) || (Speed.mc.player.verticalCollision && this.field734 > 0)) {
                            this.field734 = 0;
                        }
                        this.field731 = this.field732 - this.field732 / 159.0;
                        break;
                }
                this.field731 = Math.max(this.field731, Class5924.method2091());
                if (this.field739 > 0.0 && this.field723.getValue() && !this.field740.hasElapsed(75.0)) {
                    this.field731 = Math.max(this.field731, this.field739);
                }
                else if (this.field719.getValue()) {
                    this.field731 = Math.min(this.field731, (this.field736 > 25) ? 0.449 : 0.433);
                }
                final Vec3d method98 = Class5924.method93(this.field731);
                event.vec3 = new Vec3d(method98.x, n, method98.z);
                event.field1892 = true;
                break;
            }
            case 4: {
                if (!Speed.mc.player.isOnGround() && this.field734 != 3) {
                    return;
                }
                if (Speed.mc.player.forwardSpeed != 0.0f) {
                    if (this.field734 == 2) {
                        Speed.mc.player.setVelocity(Speed.mc.player.getVelocity().x, -0.5, Speed.mc.player.getVelocity().z);
                        n = (this.method1972() ? 0.2 : 0.4);
                        this.field731 *= 2.149;
                        this.field734 = 3;
                    }
                    else if (this.field734 == 3) {
                        this.field731 = this.field732 - 0.66 * (this.field732 - Class5924.method2091());
                        this.field734 = 2;
                    }
                    else if (this.method1972() || Speed.mc.player.verticalCollision) {
                        this.field734 = 1;
                    }
                }
                this.field731 = Math.min(Math.max(this.field731, Class5924.method2091()), this.field717.getValue());
                float n5 = n2;
                float movementSideways2 = Speed.mc.player.input.movementSideways;
                if (n5 != 0.0 && movementSideways2 != 0.0) {
                    n5 *= (float)Math.sin(0.7853981633974483);
                    movementSideways2 *= (float)Math.cos(0.7853981633974483);
                }
                event.vec3 = new Vec3d(n5 * this.field731 * -Math.sin(Math.toRadians(Speed.mc.player.getYaw())) + movementSideways2 * this.field731 * Math.cos(Math.toRadians(Speed.mc.player.getYaw())), n, n5 * this.field731 * Math.cos(Math.toRadians(Speed.mc.player.getYaw())) - movementSideways2 * this.field731 * -Math.sin(Math.toRadians(Speed.mc.player.getYaw())));
                event.field1892 = true;
                break;
            }
        }
    }

    public void method1710(ExplosionS2CPacket velocity) {
        if (this.field719.getValue()) {
            double var5 = mc.player.getX() - mc.player.prevX;
            double var7 = mc.player.getZ() - mc.player.prevZ;
            if (velocity.getPlayerVelocityX() > 0.0F && var5 > 0.0 && velocity.getPlayerVelocityZ() > 0.0F && var7 > 0.0
                    || (double) velocity.getPlayerVelocityX() < 0.0 && var5 < 0.0 && (double) velocity.getPlayerVelocityZ() < 0.0 && var7 < 0.0
                    || (double) velocity.getPlayerVelocityX() > 0.0 && var5 > 0.0 && (double) velocity.getPlayerVelocityZ() < 0.0 && var7 < 0.0
                    || (double) velocity.getPlayerVelocityX() < 0.0 && var5 < 0.0 && (double) velocity.getPlayerVelocityZ() > 0.0 && var7 > 0.0) {
                this.field739 = Math.sqrt(
                        velocity.getPlayerVelocityX() * velocity.getPlayerVelocityX() + velocity.getPlayerVelocityZ() * velocity.getPlayerVelocityZ()
                )
                        * this.field724.getValue();
                this.field740.reset();
            }
        } else {
            this.field739 = Math.sqrt(
                    velocity.getPlayerVelocityX() * velocity.getPlayerVelocityX() + velocity.getPlayerVelocityZ() * velocity.getPlayerVelocityZ()
            )
                    * this.field724.getValue();
            this.field740.reset();
        }

        field742 = true;
    }

    @Override
    public String method1322() {
        return this.field715.getValue().name();
    }

    private double method1389(double var1) {
        StatusEffectInstance var6 = mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST) ? mc.player.getStatusEffect(StatusEffects.JUMP_BOOST) : null;
        if (var6 != null) {
            var1 += (float) (var6.getAmplifier() + 1) * 0.1F;
        }

        return var1;
    }

    private boolean method1972() {
        return !mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0.0, 0.21, 0.0)).iterator().hasNext();
    }

    private double method355(double var1) {
        BigDecimal var3 = new BigDecimal(var1);
        var3 = var3.setScale(3, RoundingMode.HALF_UP);
        return var3.doubleValue();
    }

    @EventHandler(
            priority = 25
    )
    public void method1633(MovementEvent event) {
        if (!Class5924.method2116()) {
            this.field731 = 0.0;
            if (this.field715.getValue() != SpeedMode.Grim) {
                mc.player.setVelocity(0.0, mc.player.getVelocity().y, 0.0);
            }

            Class3076.method6025(this);
        } else if (this.field722.getValue()
                && (this.field715.getValue() != SpeedMode.Strafe || !this.field719.getValue())
                && this.field715.getValue() != SpeedMode.Grim) {
            Class3076.method6024(this, 69, 1.0875F);
        } else {
            Class3076.method6025(this);
        }

        double var5 = mc.player.getX() - mc.player.prevX;
        double var7 = mc.player.getZ() - mc.player.prevZ;
        field742 = false;
        this.field732 = Math.sqrt(var5 * var5 + var7 * var7);
        if (this.field715.getValue() == SpeedMode.Grim && this.ab != -420.0F && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
            event.method1074(new ActionWrapper(this.ab, mc.player.getPitch()));
        }
    }

    @EventHandler
    public void method2042(PacketBundleEvent event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket && MinecraftUtils.isClientActive() && !(mc.currentScreen instanceof DownloadingTerrainScreen)) {
            this.field738.reset();
            this.field734 = 4;
            this.field731 = Class5924.method2091();
            this.field732 = 0.0;
        } else if (event.packet instanceof ExplosionS2CPacket var5 && !field742) {
            this.method1710(var5);
        }
    }

    @Override
    public void onEnable() {
        if (!MinecraftUtils.isClientActive()) {
            this.setEnabled(false);
        } else {
            this.field734 = 4;
            this.field731 = Class5924.method2091();
            this.field732 = 0.0;
        }
    }

    @Override
    public void onDisable() {
        Class3076.method6025(this);
    }

    private boolean lambda$new$11() {
        return this.field715.getValue() == SpeedMode.Vanilla;
    }

    private boolean lambda$new$10() {
        return this.field715.getValue() == SpeedMode.Vanilla;
    }

    private boolean lambda$new$9() {
        return this.field715.getValue() != SpeedMode.Grim;
    }

    private boolean lambda$new$8() {
        return this.field715.getValue() == SpeedMode.Grim;
    }

    private boolean lambda$new$7() {
        return this.field715.getValue() != SpeedMode.Grim;
    }

    private boolean lambda$new$6() {
        return this.field715.getValue() != SpeedMode.Grim;
    }

    private boolean lambda$new$5() {
        return this.field715.getValue() == SpeedMode.BHop;
    }

    private boolean lambda$new$4() {
        return this.field715.getValue() == SpeedMode.BHop;
    }

    private boolean lambda$new$3() {
        return this.field715.getValue() == SpeedMode.Strafe;
    }

    private boolean lambda$new$2() {
        return this.field715.getValue() == SpeedMode.Vanilla;
    }

    private boolean lambda$new$1() {
        return this.field715.getValue() != SpeedMode.Grim;
    }

    private boolean lambda$new$0() {
        return this.field715.getValue() == SpeedMode.Grim;
    }
}
