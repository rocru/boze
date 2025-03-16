package dev.boze.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.enums.CrystalOptimizerMode;
import dev.boze.client.events.CrosshairEvent;
import dev.boze.client.events.GetFovEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.renderer.RenderManager;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.legit.CrystalOptimizer;
import dev.boze.client.systems.modules.render.AspectRatio;
import dev.boze.client.systems.modules.render.Chams;
import dev.boze.client.systems.modules.render.FreeCam;
import dev.boze.client.systems.modules.render.NoRender;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.player.RotationHelper;
import java.util.function.Predicate;
import mapped.Class27;
import mapped.Class3094;
import mapped.Class5922;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({GameRenderer.class})
public abstract class GameRendererMixin {
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   @Final
   public HeldItemRenderer firstPersonRenderer;
   @Shadow
   @Final
   private BufferBuilderStorage buffers;
   @Shadow
   private float zoom;
   @Shadow
   private float zoomX;
   @Shadow
   private float zoomY;
   @Shadow
   @Final
   private Camera camera;
   @Unique
   private Renderer3D renderer;
   @Unique
   private MatrixStack matrices = new MatrixStack();

   @Shadow
   public abstract void reset();

   @Shadow
   public abstract float getFarPlaneDistance();

   @Shadow
   protected abstract void tiltViewWhenHurt(MatrixStack var1, float var2);

   @Shadow
   protected abstract void bobView(MatrixStack var1, float var2);

   @ModifyReturnValue(
      method = {"getFov"},
      at = {@At("RETURN")}
   )
   private double boze$getFov$return(double var1) {
      GetFovEvent var3 = (GetFovEvent)Class27.EVENT_BUS.post(GetFovEvent.method1063(var1));
      return var3.field1922;
   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onTiltViewWhenHurt(MatrixStack var1, float var2, CallbackInfo var3) {
      if (NoRender.method1994()) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"showFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onShowFloatingItem(ItemStack var1, CallbackInfo var2) {
      if (var1.getItem() == Items.TOTEM_OF_UNDYING && NoRender.method1997()) {
         var2.cancel();
      }
   }

   @Redirect(
      method = {"renderWorld"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/GameRenderer;getBasicProjectionMatrix(D)Lorg/joml/Matrix4f;",
         ordinal = 0
      )
   )
   private Matrix4f onGetBasicProjectionMatrix(GameRenderer var1, double var2) {
      if (AspectRatio.INSTANCE.isEnabled()) {
         MatrixStack var6 = new MatrixStack();
         var6.peek().getPositionMatrix().identity();
         if (this.zoom != 1.0F) {
            var6.translate(this.zoomX, -this.zoomY, 0.0F);
            var6.scale(this.zoom, this.zoom, 1.0F);
         }

         var6.peek()
            .getPositionMatrix()
            .mul(
               new Matrix4f()
                  .setPerspective(
                     (float)(var2 * (float) (Math.PI / 180.0)) * AspectRatio.INSTANCE.field3383.method423(),
                     (float)this.client.getWindow().getFramebufferWidth()
                        / (float)this.client.getWindow().getFramebufferHeight()
                        * AspectRatio.INSTANCE.field3380.method423(),
                     AspectRatio.INSTANCE.field3381.method423(),
                     this.getFarPlaneDistance() * AspectRatio.INSTANCE.field3382.method423()
                  )
            );
         return var6.peek().getPositionMatrix();
      } else {
         return var1.getBasicProjectionMatrix(var2);
      }
   }

   @Inject(
      method = {"renderWorld"},
      at = {@At(
         value = "INVOKE_STRING",
         target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
         args = {"ldc=hand"}
      )},
      locals = LocalCapture.CAPTURE_FAILEXCEPTION
   )
   private void onRenderWorld(
      RenderTickCounter var1, CallbackInfo var2, @Local(ordinal = 1) Matrix4f var3, @Local(ordinal = 1) float var4, @Local MatrixStack var5
   ) {
      if (MinecraftUtils.isClientActive()) {
         if (RenderSystem.isOnRenderThread()) {
            if (!ShaderRegistry.field2273) {
               ShaderRegistry.method1315();
            }

            Class3094.field220 = true;
            if (this.renderer == null) {
               this.renderer = new Renderer3D(false, true);
            }

            if (this.matrices == null) {
               this.matrices = new MatrixStack();
            }

            Render3DEvent var6 = Render3DEvent.method1094(
               var5, this.camera, this.renderer, var4, this.camera.getPos().x, this.camera.getPos().y, this.camera.getPos().z
            );
            RotationHelper.updateRotation();
            Class5922.method58(var3);
            RenderSystem.getModelViewStack().pushMatrix().mul(var3);
            this.matrices.push();
            this.tiltViewWhenHurt(this.matrices, this.camera.getLastTickDelta());
            if ((Boolean)this.client.options.getBobView().getValue()) {
               this.bobView(this.matrices, this.camera.getLastTickDelta());
            }

            RenderSystem.getModelViewStack().mul(this.matrices.peek().getPositionMatrix().invert());
            this.matrices.pop();
            RenderSystem.applyModelViewMatrix();
            this.renderer.method1217();
            Class27.EVENT_BUS.post(var6);
            this.renderer.method1219(var5);
            RenderSystem.getModelViewStack().popMatrix();
            RenderSystem.applyModelViewMatrix();
            Class3094.field220 = false;
         }
      }
   }

   @Redirect(
      method = {"renderHand"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"
      )
   )
   private void onRenderHandRenderItem(HeldItemRenderer var1, float var2, MatrixStack var3, Immediate var4, ClientPlayerEntity var5, int var6) {
      if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.al.method419()) {
         if (Chams.INSTANCE.am.method419()) {
            var3.push();
            var1.renderItem(var2, var3, var4, var5, var6);
            var3.pop();
         }

         ChamsShaderRenderer.method1310(
            GameRendererMixin::lambda$onRenderHandRenderItem$0,
            Chams.INSTANCE.method1921(),
            Chams.INSTANCE.aq.method419(),
            Chams.INSTANCE.an,
            Chams.INSTANCE.ao,
            Chams.INSTANCE.au.method434(),
            Chams.INSTANCE.av.method423(),
            Chams.INSTANCE.as.method423(),
            Chams.INSTANCE.at.method423(),
            Chams.INSTANCE.ar.method434(),
            Chams.INSTANCE.aL
         );
      } else {
         var1.renderItem(var2, var3, var4, var5, var6);
      }
   }

   @Redirect(
      method = {"renderWorld"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F",
         ordinal = 0
      ),
      require = 0
   )
   private float onLerp(float var1, float var2, float var3) {
      return NoRender.method1982() ? 0.0F : MathHelper.lerp(var1, var2, var3);
   }

   @Inject(
      method = {"renderWorld"},
      at = {@At("TAIL")}
   )
   public void onRenderWorldTail(CallbackInfo ci) {
      RenderManager.field1618.renderGUI();
   }

   @Inject(
      method = {"findCrosshairTarget"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onFindCrosshairTarget(
      Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta, CallbackInfoReturnable<HitResult> cir
   ) {
      CrosshairEvent var8 = (CrosshairEvent)Class27.EVENT_BUS.post(CrosshairEvent.method1023());
      if (var8.method1022()) {
         double var9 = Math.max(blockInteractionRange, entityInteractionRange);
         Vec3d var11 = camera.getCameraPosVec(tickDelta);
         HitResult var12 = camera.raycast(var9, tickDelta, false);
         cir.setReturnValue(GameRenderer.ensureTargetInRange(var12, var11, blockInteractionRange));
      }
   }

   @Redirect(
      method = {"findCrosshairTarget"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
      )
   )
   public EntityHitResult onRaycastEntity(Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double d) {
      EntityHitResult var10 = ProjectileUtil.raycast(entity, min, max, box, GameRendererMixin::lambda$onRaycastEntity$1, d);
      return var10 != null
            && CrystalOptimizer.INSTANCE.isEnabled()
            && CrystalOptimizer.INSTANCE.field2789.method461() == CrystalOptimizerMode.EntityTrace
            && var10.getEntity() instanceof EndCrystalEntity
            && System.currentTimeMillis() - ((IEndCrystalEntity)var10.getEntity()).getLastAttackTime() < 1000L
         ? null
         : var10;
   }

   @Inject(
      method = {"renderHand"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderHand(Camera var1, float var2, Matrix4f var3, CallbackInfo var4) {
      if (FreeCam.INSTANCE.isEnabled()) {
         var4.cancel();
      }
   }

   @Inject(
      method = {"findCrosshairTarget"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onUpdateTargetedEntity(
      Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta, CallbackInfoReturnable<HitResult> cir
   ) {
      if (FreeCam.INSTANCE.isEnabled() && FreeCam.INSTANCE.method1941()) {
         if (!MinecraftUtils.isClientActive()) {
            return;
         }

         double var10 = Math.max(blockInteractionRange, entityInteractionRange);
         double var12 = MathHelper.square(var10);
         Vec3d var14 = FreeCam.INSTANCE.method1952(tickDelta);
         Vec3d var15 = this.getRotationVector(FreeCam.INSTANCE.method1950(tickDelta), FreeCam.INSTANCE.method1951(tickDelta));
         Vec3d var16 = var14.add(var15.x * var10, var15.y * var10, var15.z * var10);
         BlockHitResult var17 = this.client.world.raycast(new RaycastContext(var14, var16, ShapeType.OUTLINE, FluidHandling.NONE, this.client.player));
         double var18 = var17.getPos().squaredDistanceTo(var14);
         if (var17.getType() != Type.MISS) {
            var12 = var18;
            var10 = Math.sqrt(var18);
         }

         Box var21 = FreeCam.INSTANCE.method1953().stretch(var15.multiply(var10)).expand(1.0, 1.0, 1.0);
         EntityHitResult var22 = ProjectileUtil.raycast(camera, var14, var16, var21, GameRendererMixin::lambda$onUpdateTargetedEntity$2, var12);
         if (var22 != null && var22.getPos().squaredDistanceTo(var14) < var18) {
            cir.setReturnValue(GameRenderer.ensureTargetInRange(var22, var14, entityInteractionRange));
         }

         cir.setReturnValue(GameRenderer.ensureTargetInRange(var17, var14, blockInteractionRange));
      }
   }

   private final Vec3d getRotationVector(float var1, float var2) {
      float var3 = var1 * (float) (Math.PI / 180.0);
      float var4 = -var2 * (float) (Math.PI / 180.0);
      float var5 = MathHelper.cos(var4);
      float var6 = MathHelper.sin(var4);
      float var7 = MathHelper.cos(var3);
      float var8 = MathHelper.sin(var3);
      return new Vec3d((double)(var6 * var7), (double)(-var8), (double)(var5 * var7));
   }

   private static boolean lambda$onUpdateTargetedEntity$2(Entity var0) {
      return !var0.isSpectator() && var0.canHit();
   }

   private static boolean lambda$onRaycastEntity$1(Entity var0) {
      return !var0.isSpectator() && var0.canHit();
   }

   private static void lambda$onRenderHandRenderItem$0(HeldItemRenderer var0, float var1, MatrixStack var2, Immediate var3, ClientPlayerEntity var4, int var5) {
      var0.renderItem(var1, var2, var3, var4, var5);
   }
}
