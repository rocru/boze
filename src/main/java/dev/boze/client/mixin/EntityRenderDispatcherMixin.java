package dev.boze.client.mixin;

import dev.boze.client.enums.ChamsMode;
import dev.boze.client.mixininterfaces.IBox;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.legit.Hitboxes;
import dev.boze.client.systems.modules.render.Chams;
import dev.boze.client.systems.modules.render.NoRender;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({EntityRenderDispatcher.class})
public class EntityRenderDispatcherMixin {
   @Inject(
      method = {"shouldRender"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void shouldRender(Entity var1, Frustum var2, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9) {
      if (var1 instanceof LivingEntity && NoRender.method1990() && ((LivingEntity)var1).getHealth() <= 0.0F) {
         var9.setReturnValue(false);
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private <E extends Entity> void render(
      E var1, double var2, double var4, double var6, float var8, float var9, MatrixStack var10, VertexConsumerProvider var11, int var12, CallbackInfo var13
   ) {
      if ((var1 instanceof FakePlayerEntity var16 && var16.field1265 || NoRender.method1991() && var1 != MinecraftClient.getInstance().player)
         && var1.getBoundingBox().contains(MinecraftClient.getInstance().cameraEntity.getPos())) {
         var13.cancel();
      }
   }

   @Inject(
      method = {"renderHitbox"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/WorldRenderer;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V",
         ordinal = 0
      )},
      locals = LocalCapture.CAPTURE_FAILSOFT
   )
   private static void onRenderHitbox(
      MatrixStack var0, VertexConsumer var1, Entity var2, float var3, float var4, float var5, float var6, CallbackInfo var7, Box var8
   ) {
      double var11 = Hitboxes.INSTANCE.method1603(var2);
      if (var11 != 0.0) {
         ((IBox)var8).boze$expand(var11);
      }
   }

   @Inject(
      method = {"renderShadow"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onRenderShadowHead(
      MatrixStack var0, VertexConsumerProvider var1, Entity var2, float var3, float var4, WorldView var5, float var6, CallbackInfo var7
   ) {
      if (!ChamsShaderRenderer.field2248) {
         if (ChamsShaderRenderer.field2247) {
            var7.cancel();
         }

         if (Chams.INSTANCE.field3461.method419()
            && Chams.INSTANCE.isEnabled()
            && Chams.INSTANCE.method1924(var2)
            && Chams.INSTANCE.field3463.method461() != ChamsMode.Shader) {
            var7.cancel();
         }
      }
   }

   @ModifyArg(
      method = {"renderShadow"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderShadowPart(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;DDDFF)V"
      ),
      index = 9
   )
   private static float onRenderShadowHead(float var0) {
      return ChamsShaderRenderer.field2248 ? 0.0F : var0;
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public <E extends Entity> void onRenderPre(
      E entity,
      double x,
      double y,
      double z,
      float yaw,
      float tickDelta,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      if (NoRender.method1999(entity)) {
         ci.cancel();
      }
   }
}
