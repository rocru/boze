package mapped;

import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.mixininterfaces.IParticleManager;
import dev.boze.client.mixininterfaces.ISoundSystem;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.NetworkHandlerWrapper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.SharedConstants;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Class5918 implements IMinecraft {
   private static final List<Input> field9 = new ArrayList();

   public static Pair<ClientPlayerEntity, ArrayList<Vec3d>> method38(int count, PlayerEntity baseEntity) {
      Vec3d var5 = new Vec3d(var39.getVelocity().x, var39.getVelocity().y, var39.getVelocity().z);
      Vec3d var6 = mc.player != null ? new Vec3d(mc.player.getVelocity().x, mc.player.getVelocity().y, mc.player.getVelocity().z) : Vec3d.ZERO;
      boolean var7 = SharedConstants.isDevelopment;
      SharedConstants.isDevelopment = true;
      ISoundSystem var8 = (ISoundSystem)mc.getSoundManager().soundSystem;
      boolean var9 = var8.boze$isPaused();
      var8.boze$setPaused(true);
      Class3079 var10 = new Class3079(mc, mc.world, NetworkHandlerWrapper.method739(), new StatHandler(), new ClientRecipeBook(), false, false);
      SharedConstants.isDevelopment = var7;
      Input var11 = method39(var39);
      var10.input = new Class3078(var11);
      var10.init();
      var10.copyPositionAndRotation(var39);
      var10.copyFrom(var39);
      if (var10 == mc.player && GhostRotations.INSTANCE.field760 != null) {
         var10.setYaw(GhostRotations.INSTANCE.field760.method1384());
         var10.setPitch(GhostRotations.INSTANCE.field760.method1385());
      }

      var10.setOnGround(var39.isOnGround());
      if (var39 == mc.player) {
         var10.setVelocity(var39.getVelocity());
      } else {
         var10.setVelocity(Vec3d.ZERO);
      }

      var10.setPose(var39.getPose());
      var10.jumpingCooldown = var39.jumpingCooldown;
      var10.submergedInWater = var39.submergedInWater;
      var10.touchingWater = var39.touchingWater;
      var10.setSwimming(var39.isSwimming());
      if (var39 == mc.player) {
         var10.autoJumpEnabled = mc.player.autoJumpEnabled;
         var10.ticksToNextAutojump = mc.player.ticksToNextAutojump;
      } else {
         var10.autoJumpEnabled = false;
      }

      ArrayList var12 = new ArrayList();
      boolean var13 = ((IParticleManager)mc.particleManager).boze$isPaused();
      ((IParticleManager)mc.particleManager).boze$setPaused(true);

      for (int var14 = 0; var14 < var38; var14++) {
         var10.resetPosition();
         var10.age++;
         ((IClientPlayerEntity)var10).boze_tick();
         var12.add(var10.getPos());
      }

      ((IParticleManager)mc.particleManager).boze$setPaused(var13);
      var39.setVelocity(var5);
      if (mc.player != null && var39.equals(mc.player)) {
         mc.player.setVelocity(var6);
      }

      var8.boze$setPaused(var9);
      return new Pair(var10, var12);
   }

   private static Input method39(PlayerEntity var0) {
      if (var0.equals(mc.player)) {
         return mc.player.input;
      } else {
         Vec3d var4 = new Vec3d(var0.prevX, var0.prevY, var0.prevZ);
         Vec3d var5 = var0.getPos().subtract(var4);
         Pair var6 = null;

         for (Input var8 : field9) {
            Input var9 = method42(var8, !var0.isOnGround(), var0.isSneaking());
            boolean var10 = var9.movementForward == 0.0F && var9.movementSideways == 0.0F;
            if (!(var5.horizontalLengthSquared() > 0.0) || !var10) {
               Vec3d var11;
               if (var10) {
                  var11 = new Vec3d(0.0, 0.0, 0.0);
               } else {
                  Vec2f var12 = var9.getMovementInput();
                  Vec3d var13 = method40(new Vec3d((double)var12.x, 0.0, (double)var12.y), 1.0F, var0.getYaw());
                  var11 = new Vec3d(var13.getX(), 0.0, var13.getZ());
               }

               double var14 = var5.distanceTo(var11);
               if (var6 == null || (Double)var6.getRight() > var14) {
                  var6 = new Pair(var9, var14);
               }
            }
         }

         return (Input)var6.getLeft();
      }
   }

   private static Vec3d method40(Vec3d var0, float var1, float var2) {
      double var6 = var0.lengthSquared();
      if (var6 < 1.0E-7) {
         return Vec3d.ZERO;
      } else {
         Vec3d var8 = (var6 > 1.0 ? var0.normalize() : var0).multiply((double)var1);
         float var9 = MathHelper.sin(var2 * (float) (Math.PI / 180.0));
         float var10 = MathHelper.cos(var2 * (float) (Math.PI / 180.0));
         return new Vec3d(var8.x * (double)var10 - var8.z * (double)var9, var8.y, var8.z * (double)var10 + var8.x * (double)var9);
      }
   }

   private static Input method41(float var0, float var1) {
      Input var4 = new Input();
      var4.movementForward = var0;
      var4.movementSideways = var1;
      var4.pressingForward = var0 > 0.0F;
      var4.pressingBack = var0 < 0.0F;
      var4.pressingLeft = var1 > 0.0F;
      var4.pressingRight = var1 < 0.0F;
      return var4;
   }

   private static Input method42(Input var0, boolean var1, boolean var2) {
      Input var3 = new Input();
      var3.movementForward = var0.movementForward;
      var3.movementSideways = var0.movementSideways;
      var3.pressingForward = var0.pressingForward;
      var3.pressingBack = var0.pressingBack;
      var3.pressingLeft = var0.pressingLeft;
      var3.pressingRight = var0.pressingRight;
      var3.jumping = var1;
      var3.sneaking = var2;
      return var3;
   }

   public static Pair<ClientPlayerEntity, ArrayList<Vec3d>> method43(int count, ClientPlayerEntity baseEntity, Vec3d startPos) {
      var41.setPosition(var42.x, var42.y, var42.z);
      ArrayList var6 = new ArrayList();
      boolean var7 = ((IParticleManager)mc.particleManager).boze$isPaused();
      ((IParticleManager)mc.particleManager).boze$setPaused(true);

      for (int var8 = 0; var8 < var40; var8++) {
         var41.resetPosition();
         var41.age++;
         ((IClientPlayerEntity)var41).boze_tick();
         var6.add(var41.getPos());
      }

      ((IParticleManager)mc.particleManager).boze$setPaused(var7);
      return new Pair(var41, var6);
   }

   static {
      for (int var0 = -1; var0 <= 1; var0++) {
         for (int var1 = -1; var1 <= 1; var1++) {
            field9.add(method41((float)var0, (float)var1));
         }
      }
   }
}
