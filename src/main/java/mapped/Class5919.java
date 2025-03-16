package mapped;

import dev.boze.client.Boze;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.CrossbowItemAccessor;
import dev.boze.client.mixin.PersistentProjectileEntityAccessor;
import dev.boze.client.mixininterfaces.IVec3d;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Class5919 implements IMinecraft {
   private static final Mutable field10 = new Mutable();
   private static final Vec3d field11 = new Vec3d(0.0, 0.0, 0.0);
   private static final Vec3d field12 = new Vec3d(0.0, 0.0, 0.0);
   public final Vector3d field13 = new Vector3d();
   private final Vector3d field14 = new Vector3d();
   private double field15;
   private double field16;
   private double field17;

   public boolean method44(Entity user, ItemStack itemStack, double simulated, double tickDelta) {
      Item var10 = var44.getItem();
      if (var10 instanceof BowItem) {
         double var11 = (double)BowItem.getPullProgress(mc.player.getItemUseTime());
         if (var11 <= 0.0) {
            return false;
         }

         this.method45(var43, 0.0, var11 * 3.0, var45, 0.05F, 0.6, var46);
      } else if (var10 instanceof CrossbowItem) {
         if (!CrossbowItem.isCharged(var44)) {
            return false;
         }

         this.method45(
            var43,
            0.0,
            (double)CrossbowItemAccessor.getSpeed((ChargedProjectilesComponent)var44.get(DataComponentTypes.CHARGED_PROJECTILES)),
            var45,
            0.05F,
            0.6,
            var46
         );
      } else if (var10 instanceof FishingRodItem) {
         this.method48(var43, var46);
      } else if (var10 instanceof TridentItem) {
         this.method45(var43, 0.0, 2.5, var45, 0.05F, 0.99, var46);
      } else if (var10 instanceof SnowballItem || var10 instanceof EggItem || var10 instanceof EnderPearlItem) {
         this.method45(var43, 0.0, 1.5, var45, 0.03, 0.8, var46);
      } else if (var10 instanceof ExperienceBottleItem) {
         this.method45(var43, -20.0, 0.7, var45, 0.07, 0.8, var46);
      } else {
         if (!(var10 instanceof ThrowablePotionItem)) {
            return false;
         }

         this.method45(var43, -20.0, 0.5, var45, 0.05, 0.8, var46);
      }

      return true;
   }

   public void method45(Entity user, double roll, double speed, double simulated, double gravity, double waterDrag, double tickDelta) {
      Class3062.method5990(this.field13, var47, var53).add(0.0, (double)var47.getEyeHeight(var47.getPose()), 0.0);
      double var17;
      double var19;
      if (var47 instanceof ClientPlayerEntity) {
         var17 = MathHelper.lerp(var53, (double) Boze.prevLastYaw, (double)((ClientPlayerEntityAccessor)var47).getLastYaw());
         var19 = MathHelper.lerp(var53, (double) Boze.prevLastPitch, (double)((ClientPlayerEntityAccessor)var47).getLastPitch());
      } else {
         var17 = MathHelper.lerp(var53, (double)var47.prevYaw, (double)var47.getYaw());
         var19 = MathHelper.lerp(var53, (double)var47.prevPitch, (double)var47.getPitch());
      }

      double var21;
      double var23;
      double var25;
      if (var50 == 0.0) {
         var21 = -Math.sin(var17 * 0.017453292) * Math.cos(var19 * 0.017453292);
         var23 = -Math.sin((var19 + var48) * 0.017453292);
         var25 = Math.cos(var17 * 0.017453292) * Math.cos(var19 * 0.017453292);
      } else {
         Vec3d var27 = var47.getOppositeRotationVector(1.0F);
         Quaterniond var28 = new Quaterniond().setAngleAxis(var50, var27.x, var27.y, var27.z);
         Vec3d var29 = var47.getRotationVec(1.0F);
         Vector3d var30 = new Vector3d(var29.x, var29.y, var29.z);
         var30.rotate(var28);
         var21 = var30.x;
         var23 = var30.y;
         var25 = var30.z;
      }

      this.field14.set(var21, var23, var25).normalize().mul(var49);
      Vec3d var31 = var47.getVelocity();
      this.field14.add(var31.x, var47.isOnGround() ? 0.0 : var31.y, var31.z);
      this.field15 = var51;
      this.field16 = 0.99;
      this.field17 = var52;
   }

   public boolean method46(Entity entity, double tickDelta) {
      if (var54 instanceof PersistentProjectileEntity && ((PersistentProjectileEntityAccessor)var54).getInGround()) {
         return false;
      } else {
         if (var54 instanceof ArrowEntity var7) {
            this.method47(var54, var7.getVelocity().length(), 0.05F, 0.6, var55);
         } else if (var54 instanceof EnderPearlEntity || var54 instanceof SnowballEntity || var54 instanceof EggEntity) {
            this.method47(var54, 1.5, 0.03, 0.8, var55);
         } else if (var54 instanceof TridentEntity) {
            this.method47(var54, 2.5, 0.05F, 0.99, var55);
         } else if (var54 instanceof ExperienceBottleEntity) {
            this.method47(var54, 0.7, 0.07, 0.8, var55);
         } else if (var54 instanceof ThrownEntity) {
            this.method47(var54, 0.5, 0.05, 0.8, var55);
         } else {
            if (!(var54 instanceof WitherSkullEntity) && !(var54 instanceof FireballEntity) && !(var54 instanceof DragonFireballEntity)) {
               return false;
            }

            this.method47(var54, 0.95, 0.0, 0.8, var55);
         }

         return true;
      }
   }

   public void method47(Entity entity, double speed, double gravity, double waterDrag, double tickDelta) {
      Class3062.method5990(this.field13, var56, var60);
      this.field14.set(var56.getVelocity().x, var56.getVelocity().y, var56.getVelocity().z).normalize().mul(var57);
      Vec3d var12 = var56.getVelocity();
      this.field14.add(var12.x, var56.isOnGround() ? 0.0 : var12.y, var12.z);
      this.field15 = var58;
      this.field16 = 0.99;
      this.field17 = var59;
   }

   public void method48(Entity user, double tickDelta) {
      double var4 = MathHelper.lerp(var62, (double)var61.prevYaw, (double)var61.getYaw());
      double var6 = MathHelper.lerp(var62, (double)var61.prevPitch, (double)var61.getPitch());
      double var8 = Math.cos(-var4 * (float) (Math.PI / 180.0) - (float) Math.PI);
      double var10 = Math.sin(-var4 * (float) (Math.PI / 180.0) - (float) Math.PI);
      double var12 = -Math.cos(-var6 * (float) (Math.PI / 180.0));
      double var14 = Math.sin(-var6 * (float) (Math.PI / 180.0));
      Class3062.method5990(this.field13, var61, var62).sub(var10 * 0.3, 0.0, var8 * 0.3).add(0.0, (double)var61.getEyeHeight(var61.getPose()), 0.0);
      this.field14.set(-var10, MathHelper.clamp(-(var14 / var12), -5.0, 5.0), -var8);
      double var16 = this.field14.length();
      this.field14.mul(0.6 / var16 + 0.5, 0.6 / var16 + 0.5, 0.6 / var16 + 0.5);
      this.field15 = 0.03;
      this.field16 = 0.92;
      this.field17 = 0.0;
   }

   public HitResult method49() {
      ((IVec3d)field12).boze$set(this.field13);
      this.field13.add(this.field14);
      this.field14.mul(this.method2114() ? this.field17 : this.field16);
      this.field14.sub(0.0, this.field15, 0.0);
      if (this.field13.y < (double)mc.world.getBottomY()) {
         return Class3089.field215;
      } else {
         int var4 = (int)(this.field13.x / 16.0);
         int var5 = (int)(this.field13.z / 16.0);
         if (!mc.world.getChunkManager().isChunkLoaded(var4, var5)) {
            return Class3089.field215;
         } else {
            ((IVec3d)field11).boze$set(this.field13);
            HitResult var6 = this.method51();
            return var6.getType() == Type.MISS ? null : var6;
         }
      }
   }

   private boolean method2114() {
      field10.set(this.field13.x, this.field13.y, this.field13.z);
      FluidState var4 = mc.world.getFluidState(field10);
      return var4.getFluid() != Fluids.WATER && var4.getFluid() != Fluids.FLOWING_WATER
         ? false
         : this.field13.y - (double)((int)this.field13.y) <= (double)var4.getHeight();
   }

   private HitResult method51() {
      Vec3d var4 = field12;
      Object var5 = mc.world
         .raycast(new RaycastContext(var4, field11, ShapeType.COLLIDER, this.field17 == 0.0 ? FluidHandling.ANY : FluidHandling.NONE, mc.player));
      if (var5.getType() != Type.MISS) {
         var4 = var5.getPos();
      }

      EntityHitResult var6 = ProjectileUtil.getEntityCollision(
         mc.world,
         mc.player,
         var4,
         field11,
         new Box(this.field13.x, this.field13.y, this.field13.z, this.field13.x, this.field13.y, this.field13.z).stretch(mc.player.getVelocity()).expand(1.0),
         Class5919::lambda$getCollision$0
      );
      if (var6 != null) {
         var5 = var6;
      }

      return (HitResult)var5;
   }

   private static boolean lambda$getCollision$0(Entity var0) {
      return !var0.isSpectator() && var0.isAlive() && var0.canHit();
   }
}
