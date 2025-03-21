package mapped;

import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.mixininterfaces.IParticleManager;
import dev.boze.client.mixininterfaces.ISoundSystem;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.NetworkHandlerWrapper;
import net.minecraft.SharedConstants;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Class5918 implements IMinecraft {
    private static final List<Input> field9;

    public Class5918() {
        super();
    }

    public static Pair<ClientPlayerEntity, ArrayList<Vec3d>> method38(final int count, final PlayerEntity baseEntity) {
        final Vec3d velocity = new Vec3d(baseEntity.getVelocity().x, baseEntity.getVelocity().y, baseEntity.getVelocity().z);
        final Vec3d velocity2 = (Class5918.mc.player != null) ? new Vec3d(Class5918.mc.player.getVelocity().x, Class5918.mc.player.getVelocity().y, Class5918.mc.player.getVelocity().z) : Vec3d.ZERO;
        final boolean isDevelopment = SharedConstants.isDevelopment;
        SharedConstants.isDevelopment = true;
        final ISoundSystem soundSystem = (ISoundSystem) Class5918.mc.getSoundManager().soundSystem;
        final boolean paused = soundSystem.boze$isPaused();
        soundSystem.boze$setPaused(true);
        final Class3079 class3079 = new Class3079(Class5918.mc, Class5918.mc.world, NetworkHandlerWrapper.method739(), new StatHandler(), new ClientRecipeBook(), false, false);
        SharedConstants.isDevelopment = isDevelopment;
        class3079.input = new Class3078(method39(baseEntity));
        class3079.init();
        class3079.copyPositionAndRotation(baseEntity);
        class3079.copyFrom(baseEntity);
        if (class3079 == Class5918.mc.player && GhostRotations.INSTANCE.field760 != null) {
            class3079.setYaw(GhostRotations.INSTANCE.field760.method1384());
            class3079.setPitch(GhostRotations.INSTANCE.field760.method1385());
        }
        class3079.setOnGround(baseEntity.isOnGround());
        if (baseEntity == Class5918.mc.player) {
            class3079.setVelocity(baseEntity.getVelocity());
        } else {
            class3079.setVelocity(Vec3d.ZERO);
        }
        class3079.setPose(baseEntity.getPose());
        class3079.jumpingCooldown = baseEntity.jumpingCooldown;
        class3079.submergedInWater = baseEntity.submergedInWater;
        class3079.touchingWater = baseEntity.touchingWater;
        class3079.setSwimming(baseEntity.isSwimming());
        if (baseEntity == Class5918.mc.player) {
            class3079.autoJumpEnabled = Class5918.mc.player.autoJumpEnabled;
            class3079.ticksToNextAutojump = Class5918.mc.player.ticksToNextAutojump;
        } else {
            class3079.autoJumpEnabled = false;
        }
        final ArrayList list = new ArrayList();
        final boolean paused2 = ((IParticleManager) Class5918.mc.particleManager).boze$isPaused();
        ((IParticleManager) Class5918.mc.particleManager).boze$setPaused(true);
        for (int i = 0; i < count; ++i) {
            class3079.resetPosition();
            ++class3079.age;
            ((IClientPlayerEntity) class3079).boze_tick();
            list.add(class3079.getPos());
        }
        ((IParticleManager) Class5918.mc.particleManager).boze$setPaused(paused2);
        baseEntity.setVelocity(velocity);
        if (Class5918.mc.player != null && baseEntity.equals(Class5918.mc.player)) {
            Class5918.mc.player.setVelocity(velocity2);
        }
        soundSystem.boze$setPaused(paused);
        return (Pair<ClientPlayerEntity, ArrayList<Vec3d>>) new Pair(class3079, list);
    }

    private static Input method39(final PlayerEntity playerEntity) {
        if (playerEntity.equals(Class5918.mc.player)) {
            return Class5918.mc.player.input;
        }
        final Vec3d subtract = playerEntity.getPos().subtract(new Vec3d(playerEntity.prevX, playerEntity.prevY, playerEntity.prevZ));
        Pair pair = null;
        final Iterator<Input> iterator = Class5918.field9.iterator();
        while (iterator.hasNext()) {
            final Input method42 = method42(iterator.next(), !playerEntity.isOnGround(), playerEntity.isSneaking());
            final boolean b = method42.movementForward == 0.0f && method42.movementSideways == 0.0f;
            if (subtract.horizontalLengthSquared() > 0.0 && b) {
                continue;
            }
            Vec3d vec3d;
            if (b) {
                vec3d = new Vec3d(0.0, 0.0, 0.0);
            } else {
                final Vec2f movementInput = method42.getMovementInput();
                final Vec3d method43 = method40(new Vec3d(movementInput.x, 0.0, movementInput.y), 1.0f, playerEntity.getYaw());
                vec3d = new Vec3d(method43.getX(), 0.0, method43.getZ());
            }
            final double distanceTo = subtract.distanceTo(vec3d);
            if (pair != null && (double) pair.getRight() <= distanceTo) {
                continue;
            }
            pair = new Pair(method42, distanceTo);
        }
        return (Input) pair.getLeft();
    }

    private static Vec3d method40(final Vec3d vec3d, final float n, final float n2) {
        final double lengthSquared = vec3d.lengthSquared();
        if (lengthSquared < 1.0E-7) {
            return Vec3d.ZERO;
        }
        final Vec3d multiply = ((lengthSquared > 1.0) ? vec3d.normalize() : vec3d).multiply(n);
        final float sin = MathHelper.sin(n2 * 0.017453292f);
        final float cos = MathHelper.cos(n2 * 0.017453292f);
        return new Vec3d(multiply.x * cos - multiply.z * sin, multiply.y, multiply.z * cos + multiply.x * sin);
    }

    private static Input method41(final float movementForward, final float movementSideways) {
        final Input input = new Input();
        input.movementForward = movementForward;
        input.movementSideways = movementSideways;
        input.pressingForward = (movementForward > 0.0f);
        input.pressingBack = (movementForward < 0.0f);
        input.pressingLeft = (movementSideways > 0.0f);
        input.pressingRight = (movementSideways < 0.0f);
        return input;
    }

    private static Input method42(final Input input, final boolean jumping, final boolean sneaking) {
        final Input input2 = new Input();
        input2.movementForward = input.movementForward;
        input2.movementSideways = input.movementSideways;
        input2.pressingForward = input.pressingForward;
        input2.pressingBack = input.pressingBack;
        input2.pressingLeft = input.pressingLeft;
        input2.pressingRight = input.pressingRight;
        input2.jumping = jumping;
        input2.sneaking = sneaking;
        return input2;
    }

    public static Pair<ClientPlayerEntity, ArrayList<Vec3d>> method43(final int count, final ClientPlayerEntity baseEntity, final Vec3d startPos) {
        baseEntity.setPosition(startPos.x, startPos.y, startPos.z);
        final ArrayList list = new ArrayList();
        final boolean paused = ((IParticleManager) Class5918.mc.particleManager).boze$isPaused();
        ((IParticleManager) Class5918.mc.particleManager).boze$setPaused(true);
        for (int i = 0; i < count; ++i) {
            baseEntity.resetPosition();
            ++baseEntity.age;
            ((IClientPlayerEntity) baseEntity).boze_tick();
            list.add(baseEntity.getPos());
        }
        ((IParticleManager) Class5918.mc.particleManager).boze$setPaused(paused);
        return (Pair<ClientPlayerEntity, ArrayList<Vec3d>>) new Pair(baseEntity, list);
    }

    static {
        field9 = new ArrayList<Input>();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                Class5918.field9.add(method41((float) i, (float) j));
            }
        }
    }
}
