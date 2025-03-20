package dev.boze.client.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.Boze;
import dev.boze.client.enums.ChamsMode;
import dev.boze.client.events.PostRender;
import dev.boze.client.font.FontRenderer;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixininterfaces.IWorldRenderer;
import dev.boze.client.renderer.RenderManager;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.systems.modules.render.*;
import dev.boze.client.utils.RGBAColor;
import mapped.Class3032;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({WorldRenderer.class})
public abstract class WorldRendererMixin implements IWorldRenderer {
   @Shadow
   @Nullable
   private Framebuffer entityOutlinesFramebuffer;
   @Shadow
   @Final
   private static Identifier SNOW;
   @Shadow
   private int ticks;
   @Shadow
   @Final
   private static Identifier RAIN;
   @Shadow
   @Final
   private float[] NORMAL_LINE_DX;
   @Shadow
   @Final
   private float[] NORMAL_LINE_DZ;
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   @Final
   private EntityRenderDispatcher entityRenderDispatcher;
   @Shadow
   @Final
   private BufferBuilderStorage bufferBuilders;
   @Shadow
   @Nullable
   private ClientWorld world;
   @Shadow
   private Frustum frustum;
   @Shadow
   private int regularEntityCount;
   @Shadow
   @Nullable
   private Framebuffer entityFramebuffer;

   @Shadow
   protected abstract void renderEntity(Entity var1, double var2, double var4, double var6, float var8, MatrixStack var9, VertexConsumerProvider var10);

   @Shadow
   protected abstract boolean canDrawEntityOutlines();

   @Shadow
   public abstract boolean isRenderingReady(BlockPos var1);

   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   private void afterRender(
      RenderTickCounter var1, boolean var2, Camera var3, GameRenderer var4, LightmapTextureManager var5, Matrix4f var6, Matrix4f var7, CallbackInfo var8
   ) {
      if (RenderSystem.isOnRenderThread()) {
         Boze.EVENT_BUS.post(PostRender.method1086(var1.getTickDelta(true)));
      }
   }

   @Inject(
      method = {"renderWorldBorder"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRenderWorldBorder(Camera camera, CallbackInfo ci) {
      if (NoRender.method1985()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"drawBlockOutline"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onDrawBlockOutline(
      MatrixStack var1, VertexConsumer var2, Entity var3, double var4, double var6, double var8, BlockPos var10, BlockState var11, CallbackInfo var12
   ) {
      if (BlockHighlight.INSTANCE.isEnabled() || BlockHighlightDev.INSTANCE.isEnabled()) {
         var12.cancel();
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void onRenderHead(
      RenderTickCounter var1, boolean var2, Camera var3, GameRenderer var4, LightmapTextureManager var5, Matrix4f var6, Matrix4f var7, CallbackInfo var8
   ) {
      if (ChamsShaderRenderer.field2246 == null) {
         ChamsShaderRenderer.method1305();
      }

      ChamsShaderRenderer.method1306();
   }

   @Inject(
      method = {"renderEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderEntity(Entity var1, double var2, double var4, double var6, float var8, MatrixStack var9, VertexConsumerProvider var10, CallbackInfo var11) {
      if (!ChamsShaderRenderer.field2247) {
         if (ChamsShaderRenderer.field2248) {
            if (!Chams.INSTANCE.method1924(var1)) {
               var11.cancel();
            }
         } else {
            if (ChamsShaderRenderer.field2246 == null) {
               ChamsShaderRenderer.method1305();
            }

            if (Chams.INSTANCE.isEnabled()
               && Chams.INSTANCE.method1924(var1)
               && Chams.INSTANCE.field3463.getValue() != ChamsMode.Normal
               && var10 != ChamsShaderRenderer.field2246) {
               Framebuffer var14 = this.entityOutlinesFramebuffer;
               this.entityOutlinesFramebuffer = ChamsShaderRenderer.field2244;
               ChamsShaderRenderer.field2247 = true;
               RGBAColor var15 = Chams.INSTANCE.method1926(var1);
               ChamsShaderRenderer.field2246.setColor(var15.field408, var15.field409, var15.field410, var15.field411);
               GlStateManager._disableDepthTest();
               this.renderEntity(var1, var2, var4, var6, var8, var9, ChamsShaderRenderer.field2246);
               GlStateManager._enableDepthTest();
               ChamsShaderRenderer.field2247 = false;
               this.entityOutlinesFramebuffer = var14;
            }
         }
      }
   }

   @Override
   public void boze$renderEntitiesForChams(float tickDelta, MatrixStack matrices, Camera camera) {
      Vec3d var4 = camera.getPos();
      double var5 = var4.getX();
      double var7 = var4.getY();
      double var9 = var4.getZ();
      Immediate var11 = this.bufferBuilders.getEntityVertexConsumers();

      for (Entity var13 : this.client.world.getEntities()) {
         BlockPos var15;
         if ((this.entityRenderDispatcher.shouldRender(var13, this.frustum, var5, var7, var9) || var13.hasPassengerDeep(this.client.player))
            && (this.world.isOutOfHeightLimit((var15 = var13.getBlockPos()).getY()) || this.isRenderingReady(var15))
            && (
               var13 != camera.getFocusedEntity()
                  || camera.isThirdPerson()
                  || camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity)camera.getFocusedEntity()).isSleeping()
            )
            && (!(var13 instanceof ClientPlayerEntity) || camera.getFocusedEntity() == var13)) {
            this.regularEntityCount++;
            if (var13.age == 0) {
               var13.lastRenderX = var13.getX();
               var13.lastRenderY = var13.getY();
               var13.lastRenderZ = var13.getZ();
            }

            Object var14;
            if (this.canDrawEntityOutlines() && this.client.hasOutline(var13)) {
               OutlineVertexConsumerProvider var16 = this.bufferBuilders.getOutlineVertexConsumers();
               var14 = var16;
               int var17 = var13.getTeamColorValue();
               var16.setColor(Argb.getRed(var17), Argb.getGreen(var17), Argb.getBlue(var17), 255);
            } else {
               var14 = var11;
            }

            this.renderEntity(var13, var5, var7, var9, tickDelta, matrices, (VertexConsumerProvider)var14);
         }
      }
   }

   @Inject(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;draw()V",
         ordinal = 0,
         shift = Shift.BEFORE
      )}
   )
   private void onRender(
      RenderTickCounter var1, boolean var2, Camera var3, GameRenderer var4, LightmapTextureManager var5, Matrix4f var6, Matrix4f var7, CallbackInfo var8
   ) {
      if (ChamsShaderRenderer.field2246 == null) {
         ChamsShaderRenderer.method1305();
      }

      ChamsShaderRenderer.method1307(var1.getTickDelta(true), new MatrixStack(), var3);
   }

   @Inject(
      method = {"onResized"},
      at = {@At("HEAD")}
   )
   private void onResized(int var1, int var2, CallbackInfo var3) {
      Class3032.method1964(var1, var2);
      ChamsShaderRenderer.method1314(var1, var2);
      FontRenderer.method1964(var1, var2);
      HUD.INSTANCE.method1340(var1, var2);
      ClickGUI.field1335.method1964(var1, var2);
      MotionBlur.INSTANCE.method1964(var1, var2);
      RenderManager.field1618.resizeFramebuffers(var1, var2);
   }

   @Inject(
      method = {"hasBlindnessOrDarkness(Lnet/minecraft/client/render/Camera;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hasBlindnessOrDarkness(Camera var1, CallbackInfoReturnable<Boolean> var2) {
      if (NoRender.method1988() || NoRender.method1989()) {
         var2.setReturnValue(null);
      }
   }

   @ModifyVariable(
      method = {"getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I"},
      at = @At("STORE"),
      ordinal = 0
   )
   private static int getLightmapCoordinatesModifySkyLight(int var0) {
      return Math.max(FullBright.field3569, var0);
   }

   @ModifyVariable(
      method = {"getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I"},
      at = @At("STORE"),
      ordinal = 1
   )
   private static int getLightmapCoordinatesModifyBlockLight(int var0) {
      return Math.max(FullBright.field3569, var0);
   }

   @ModifyArg(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"
      ),
      index = 3
   )
   private boolean modifyIsSpectator(boolean var1) {
      return FreeCam.INSTANCE.isEnabled() || var1;
   }
}
