package mapped;

import dev.boze.client.Boze;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.CrossbowItemAccessor;
import dev.boze.client.mixin.PersistentProjectileEntityAccessor;
import dev.boze.client.mixininterfaces.IVec3d;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Class5919 implements IMinecraft {
    private static final BlockPos.Mutable field10;
    private static final Vec3d field11;
    private static final Vec3d field12;

    static {
        field10 = new BlockPos.Mutable();
        field11 = new Vec3d(0.0, 0.0, 0.0);
        field12 = new Vec3d(0.0, 0.0, 0.0);
    }

    public final Vector3d field13;
    private final Vector3d field14;
    private double field15;
    private double field16;
    private double field17;

    public Class5919() {
        super();
        this.field13 = new Vector3d();
        this.field14 = new Vector3d();
    }

    private static boolean lambda$getCollision$0(final Entity entity) {
        return !entity.isSpectator() && entity.isAlive() && entity.canHit();
    }

    public boolean method44(final Entity user, final ItemStack itemStack, final double simulated, final double tickDelta) {
        final Item item = itemStack.getItem();
        if (item instanceof BowItem) {
            final double n = BowItem.getPullProgress(Class5919.mc.player.getItemUseTime());
            if (n <= 0.0) {
                return false;
            }
            this.method45(user, 0.0, n * 3.0, simulated, 0.05000000074505806, 0.6, tickDelta);
        } else if (item instanceof CrossbowItem) {
            if (!CrossbowItem.isCharged(itemStack)) {
                return false;
            }
            this.method45(user, 0.0, CrossbowItemAccessor.getSpeed(itemStack.get(DataComponentTypes.CHARGED_PROJECTILES)), simulated, 0.05000000074505806, 0.6, tickDelta);
        } else if (item instanceof FishingRodItem) {
            this.method48(user, tickDelta);
        } else if (item instanceof TridentItem) {
            this.method45(user, 0.0, 2.5, simulated, 0.05000000074505806, 0.99, tickDelta);
        } else if (item instanceof SnowballItem || item instanceof EggItem || item instanceof EnderPearlItem) {
            this.method45(user, 0.0, 1.5, simulated, 0.03, 0.8, tickDelta);
        } else if (item instanceof ExperienceBottleItem) {
            this.method45(user, -20.0, 0.7, simulated, 0.07, 0.8, tickDelta);
        } else {
            if (!(item instanceof ThrowablePotionItem)) {
                return false;
            }
            this.method45(user, -20.0, 0.5, simulated, 0.05, 0.8, tickDelta);
        }
        return true;
    }

    public void method45(final Entity user, final double roll, final double speed, final double simulated, final double gravity, final double waterDrag, final double tickDelta) {
        Class3062.method5990(this.field13, user, tickDelta).add(0.0, user.getEyeHeight(user.getPose()), 0.0);
        double n;
        double n2;
        if (user instanceof ClientPlayerEntity) {
            n = MathHelper.lerp(tickDelta, Boze.prevLastYaw, ((ClientPlayerEntityAccessor) user).getLastYaw());
            n2 = MathHelper.lerp(tickDelta, Boze.prevLastPitch, ((ClientPlayerEntityAccessor) user).getLastPitch());
        } else {
            n = MathHelper.lerp(tickDelta, user.prevYaw, user.getYaw());
            n2 = MathHelper.lerp(tickDelta, user.prevPitch, user.getPitch());
        }
        double x;
        double y;
        double z;
        if (simulated == 0.0) {
            x = -Math.sin(n * 0.017453292) * Math.cos(n2 * 0.017453292);
            y = -Math.sin((n2 + roll) * 0.017453292);
            z = Math.cos(n * 0.017453292) * Math.cos(n2 * 0.017453292);
        } else {
            final Vec3d oppositeRotationVector = user.getOppositeRotationVector(1.0f);
            final Quaterniond setAngleAxis = new Quaterniond().setAngleAxis(simulated, oppositeRotationVector.x, oppositeRotationVector.y, oppositeRotationVector.z);
            final Vec3d rotationVec = user.getRotationVec(1.0f);
            final Vector3d vector3d = new Vector3d(rotationVec.x, rotationVec.y, rotationVec.z);
            vector3d.rotate(setAngleAxis);
            x = vector3d.x;
            y = vector3d.y;
            z = vector3d.z;
        }
        this.field14.set(x, y, z).normalize().mul(speed);
        final Vec3d velocity = user.getVelocity();
        this.field14.add(velocity.x, user.isOnGround() ? 0.0 : velocity.y, velocity.z);
        this.field15 = gravity;
        this.field16 = 0.99;
        this.field17 = waterDrag;
    }

    public boolean method46(final Entity entity, final double tickDelta) {
        if (entity instanceof PersistentProjectileEntity && ((PersistentProjectileEntityAccessor) entity).getInGround()) {
            return false;
        }
        if (entity instanceof final ArrowEntity arrowEntity) {
            this.method47(entity, arrowEntity.getVelocity().length(), 0.05000000074505806, 0.6, tickDelta);
        } else if (entity instanceof EnderPearlEntity || entity instanceof SnowballEntity || entity instanceof EggEntity) {
            this.method47(entity, 1.5, 0.03, 0.8, tickDelta);
        } else if (entity instanceof TridentEntity) {
            this.method47(entity, 2.5, 0.05000000074505806, 0.99, tickDelta);
        } else if (entity instanceof ExperienceBottleEntity) {
            this.method47(entity, 0.7, 0.07, 0.8, tickDelta);
        } else if (entity instanceof ThrownEntity) {
            this.method47(entity, 0.5, 0.05, 0.8, tickDelta);
        } else {
            if (!(entity instanceof WitherSkullEntity) && !(entity instanceof FireballEntity) && !(entity instanceof DragonFireballEntity)) {
                return false;
            }
            this.method47(entity, 0.95, 0.0, 0.8, tickDelta);
        }
        return true;
    }

    public void method47(final Entity entity, final double speed, final double gravity, final double waterDrag, final double tickDelta) {
        Class3062.method5990(this.field13, entity, tickDelta);
        this.field14.set(entity.getVelocity().x, entity.getVelocity().y, entity.getVelocity().z).normalize().mul(speed);
        final Vec3d velocity = entity.getVelocity();
        this.field14.add(velocity.x, entity.isOnGround() ? 0.0 : velocity.y, velocity.z);
        this.field15 = gravity;
        this.field16 = 0.99;
        this.field17 = waterDrag;
    }

    public void method48(final Entity user, final double tickDelta) {
        final double lerp = MathHelper.lerp(tickDelta, user.prevYaw, user.getYaw());
        final double lerp2 = MathHelper.lerp(tickDelta, user.prevPitch, user.getPitch());
        final double cos = Math.cos(-lerp * 0.01745329238474369 - 3.1415927410125732);
        final double sin = Math.sin(-lerp * 0.01745329238474369 - 3.1415927410125732);
        final double n = -Math.cos(-lerp2 * 0.01745329238474369);
        final double sin2 = Math.sin(-lerp2 * 0.01745329238474369);
        Class3062.method5990(this.field13, user, tickDelta).sub(sin * 0.3, 0.0, cos * 0.3).add(0.0, user.getEyeHeight(user.getPose()), 0.0);
        this.field14.set(-sin, MathHelper.clamp(-(sin2 / n), -5.0, 5.0), -cos);
        final double length = this.field14.length();
        this.field14.mul(0.6 / length + 0.5, 0.6 / length + 0.5, 0.6 / length + 0.5);
        this.field15 = 0.03;
        this.field16 = 0.92;
        this.field17 = 0.0;
    }

    public HitResult method49() {
        ((IVec3d) Class5919.field12).boze$set(this.field13);
        this.field13.add(this.field14);
        this.field14.mul(this.method2114() ? this.field17 : this.field16);
        this.field14.sub(0.0, this.field15, 0.0);
        if (this.field13.y < Class5919.mc.world.getBottomY()) {
            return Class3089.field215;
        }
        if (!Class5919.mc.world.getChunkManager().isChunkLoaded((int) (this.field13.x / 16.0), (int) (this.field13.z / 16.0))) {
            return Class3089.field215;
        }
        ((IVec3d) Class5919.field11).boze$set(this.field13);
        final HitResult method51 = this.method51();
        return (method51.getType() == HitResult.Type.MISS) ? null : method51;
    }

    private boolean method2114() {
        Class5919.field10.set(this.field13.x, this.field13.y, this.field13.z);
        final FluidState fluidState = Class5919.mc.world.getFluidState(Class5919.field10);
        return (fluidState.getFluid() == Fluids.WATER || fluidState.getFluid() == Fluids.FLOWING_WATER) && this.field13.y - (int) this.field13.y <= fluidState.getHeight();
    }

    private HitResult method51() {
        Vec3d vec3d = Class5919.field12;
        Object raycast = Class5919.mc.world.raycast(new RaycastContext(vec3d, Class5919.field11, RaycastContext.ShapeType.COLLIDER, (this.field17 == 0.0) ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, Class5919.mc.player));
        if (((HitResult) raycast).getType() != HitResult.Type.MISS) {
            vec3d = ((HitResult) raycast).getPos();
        }
        final EntityHitResult entityCollision = ProjectileUtil.getEntityCollision(Class5919.mc.world, Class5919.mc.player, vec3d, Class5919.field11, new Box(this.field13.x, this.field13.y, this.field13.z, this.field13.x, this.field13.y, this.field13.z).stretch(Class5919.mc.player.getVelocity()).expand(1.0), Class5919::lambda$getCollision$0);
        if (entityCollision != null) {
            raycast = entityCollision;
        }
        return (HitResult) raycast;
    }
}
