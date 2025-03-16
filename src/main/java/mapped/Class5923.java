package mapped;

import com.mojang.datafixers.util.Pair;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.systems.modules.render.Chams;
import dev.boze.client.systems.modules.render.NoRender;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Cuboid;
import net.minecraft.client.model.ModelPart.Quad;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.RabbitEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

public class Class5923 implements IMinecraft {
   private static final MatrixStack field31 = new MatrixStack();
   private static final Vector4f field32 = new Vector4f();
   private static final Vector4f field33 = new Vector4f();
   private static final Vector4f field34 = new Vector4f();
   private static final Vector4f field35 = new Vector4f();
   private static double field36;
   private static double field37;
   private static double field38;
   private static BozeDrawColor field39;
   private static BozeDrawColor field40;
   private static ShapeMode field41;

   public static void method67(
      Renderer3D renderer3D,
      float tickDelta,
      FakePlayerEntity entity,
      BozeDrawColor sideColor,
      BozeDrawColor lineColor,
      ShapeMode shapeMode,
      float handSwingProgress,
      float limbDistance,
      float limbAngle
   ) {
      field39 = var86;
      field40 = var87;
      field41 = var88;
      field36 = MathHelper.lerp((double)var84, var85.lastRenderX, var85.getX());
      field37 = MathHelper.lerp((double)var84, var85.lastRenderY, var85.getY());
      field38 = MathHelper.lerp((double)var84, var85.lastRenderZ, var85.getZ());
      field31.push();
      EntityRenderer var12 = mc.getEntityRenderDispatcher().getRenderer(var85);
      if (var12 instanceof LivingEntityRenderer var13) {
         EntityModel var15 = var13.getModel();
         if (var12 instanceof PlayerEntityRenderer var16) {
            PlayerEntityModel var17 = (PlayerEntityModel)var16.getModel();
            var17.sneaking = var85.isInSneakingPose() || NoRender.method1992();
            ArmPose var18 = PlayerEntityRenderer.getArmPose(var85, Hand.MAIN_HAND);
            ArmPose var19 = PlayerEntityRenderer.getArmPose(var85, Hand.OFF_HAND);
            if (var18.isTwoHanded()) {
               var19 = var85.getOffHandStack().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
            }

            if (var85.getMainArm() == Arm.RIGHT) {
               var17.rightArmPose = var18;
               var17.leftArmPose = var19;
            } else {
               var17.rightArmPose = var19;
               var17.leftArmPose = var18;
            }
         }

         var15.handSwingProgress = var89;
         var15.riding = var85.hasVehicle();
         var15.child = var85.isBaby();
         float var24 = MathHelper.lerpAngleDegrees(var84, var85.prevBodyYaw, var85.bodyYaw);
         float var26 = MathHelper.lerpAngleDegrees(var84, var85.prevHeadYaw, var85.headYaw);
         float var27 = var26 - var24;
         if (var85.hasVehicle() && var85.getVehicle() instanceof LivingEntity var20) {
            var24 = MathHelper.lerpAngleDegrees(var84, var20.prevBodyYaw, var20.bodyYaw);
            var27 = var26 - var24;
            float var29 = MathHelper.wrapDegrees(var27);
            if (var29 < -85.0F) {
               var29 = -85.0F;
            }

            if (var29 >= 85.0F) {
               var29 = 85.0F;
            }

            var24 = var26 - var29;
            if (var29 * var29 > 2500.0F) {
               var24 = (float)((double)var24 + (double)var29 * 0.2);
            }

            var27 = var26 - var24;
         }

         float var31 = MathHelper.lerp(var84, var85.prevPitch, var85.getPitch());
         float var30 = var13.getAnimationProgress(var85, var84);
         if (!var85.hasVehicle() && var85.isAlive() && var85.isBaby()) {
            var91 *= 3.0F;
         }

         var15.animateModel(var85, var91, 1.0F, var84);
         var15.setAngles(var85, var91, 1.0F, var30, var27, var31);
         var13.setupTransforms(var85, field31, var30, var24, var84, var85.getScale());
         field31.scale(-1.0F, -1.0F, 1.0F);
         var13.scale(var85, field31, var84);
         field31.translate(0.0, -1.501F, 0.0);
         if (var15 instanceof AnimalModel var32) {
            if (var32.child) {
               field31.push();
               if (var32.headScaled) {
                  float var22 = 1.5F / var32.invertedChildHeadScale;
                  field31.scale(var22, var22, var22);
               }

               field31.translate(0.0, (double)(var32.childHeadYOffset / 16.0F), (double)(var32.childHeadZOffset / 16.0F));
               if (var15 instanceof BipedEntityModel var23) {
                  method70(var83, var23.head);
               } else {
                  var32.getHeadParts().forEach(Class5923::lambda$render$0);
               }

               field31.pop();
               field31.push();
               float var33 = 1.0F / var32.invertedChildBodyScale;
               field31.scale(var33, var33, var33);
               field31.translate(0.0, (double)(var32.childBodyYOffset / 16.0F), 0.0);
               if (var15 instanceof BipedEntityModel var35) {
                  method70(var83, var35.body);
                  method70(var83, var35.leftArm);
                  method70(var83, var35.rightArm);
                  method70(var83, var35.leftLeg);
                  method70(var83, var35.rightLeg);
               } else {
                  var32.getBodyParts().forEach(Class5923::lambda$render$1);
               }

               field31.pop();
            } else if (var15 instanceof BipedEntityModel var34) {
               method70(var83, var34.head);
               method70(var83, var34.body);
               method70(var83, var34.leftArm);
               method70(var83, var34.rightArm);
               method70(var83, var34.leftLeg);
               method70(var83, var34.rightLeg);
            } else {
               var32.getHeadParts().forEach(Class5923::lambda$render$2);
               var32.getBodyParts().forEach(Class5923::lambda$render$3);
            }
         }
      }

      field31.pop();
   }

   public static void method68(Renderer3D renderer3D, float tickDelta, Entity entity, BozeDrawColor sideColor, BozeDrawColor lineColor, ShapeMode shapeMode) {
      method69(var92, var93, var94, var95, var96, var97, null);
   }

   public static void method69(
      Renderer3D renderer3D, float tickDelta, Entity entity, BozeDrawColor sideColor, BozeDrawColor lineColor, ShapeMode shapeMode, Vec3d difference
   ) {
      field39 = var101;
      field40 = var102;
      field41 = var103;
      if (var104 != null) {
         field36 = var100.getX() + var104.x;
         field37 = var100.getY() + var104.y;
         field38 = var100.getZ() + var104.z;
      } else {
         field36 = MathHelper.lerp((double)var99, var100.lastRenderX, var100.getX());
         field37 = MathHelper.lerp((double)var99, var100.lastRenderY, var100.getY());
         field38 = MathHelper.lerp((double)var99, var100.lastRenderZ, var100.getZ());
      }

      field31.push();
      EntityRenderer var10 = mc.getEntityRenderDispatcher().getRenderer(var100);
      if (var10 instanceof LivingEntityRenderer var11) {
         LivingEntity var12 = (LivingEntity)var100;
         EntityModel var13 = var11.getModel();
         if (var10 instanceof PlayerEntityRenderer var14) {
            PlayerEntityModel var15 = (PlayerEntityModel)var14.getModel();
            var15.sneaking = var100.isInSneakingPose() || NoRender.method1992();
            ArmPose var16 = PlayerEntityRenderer.getArmPose((AbstractClientPlayerEntity)var100, Hand.MAIN_HAND);
            ArmPose var17 = PlayerEntityRenderer.getArmPose((AbstractClientPlayerEntity)var100, Hand.OFF_HAND);
            if (var16.isTwoHanded()) {
               var17 = var12.getOffHandStack().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
            }

            if (var12.getMainArm() == Arm.RIGHT) {
               var15.rightArmPose = var16;
               var15.leftArmPose = var17;
            } else {
               var15.rightArmPose = var17;
               var15.leftArmPose = var16;
            }
         }

         var13.handSwingProgress = var12.getHandSwingProgress(var99);
         var13.riding = var12.hasVehicle();
         var13.child = var12.isBaby();
         float var31 = MathHelper.lerpAngleDegrees(var99, var12.prevBodyYaw, var12.bodyYaw);
         float var35 = MathHelper.lerpAngleDegrees(var99, var12.prevHeadYaw, var12.headYaw);
         float var39 = var35 - var31;
         if (var12.hasVehicle() && var12.getVehicle() instanceof LivingEntity var18) {
            var31 = MathHelper.lerpAngleDegrees(var99, var18.prevBodyYaw, var18.bodyYaw);
            var39 = var35 - var31;
            float var44 = MathHelper.wrapDegrees(var39);
            if (var44 < -85.0F) {
               var44 = -85.0F;
            }

            if (var44 >= 85.0F) {
               var44 = 85.0F;
            }

            var31 = var35 - var44;
            if (var44 * var44 > 2500.0F) {
               var31 = (float)((double)var31 + (double)var44 * 0.2);
            }

            var39 = var35 - var31;
         }

         float var50 = MathHelper.lerp(var99, var12.prevPitch, var12.getPitch());
         float var45 = var11.getAnimationProgress(var12, var99);
         float var52 = 0.0F;
         float var20 = 0.0F;
         if (!var12.hasVehicle() && var12.isAlive()) {
            var52 = var12.limbAnimator.getSpeed(var99);
            var20 = var12.limbAnimator.getPos(var99);
            if (var12.isBaby()) {
               var20 *= 3.0F;
            }

            if (var52 > 1.0F) {
               var52 = 1.0F;
            }
         }

         var13.animateModel(var12, var20, var52, var99);
         var13.setAngles(var12, var20, var52, var45, var39, var50);
         var11.setupTransforms(var12, field31, var45, var31, var99, var12.getScale());
         field31.scale(-1.0F, -1.0F, 1.0F);
         var11.scale(var12, field31, var99);
         field31.translate(0.0, -1.501F, 0.0);
         if (var13 instanceof AnimalModel var21) {
            if (var21.child) {
               field31.push();
               if (var21.headScaled) {
                  float var22 = 1.5F / var21.invertedChildHeadScale;
                  field31.scale(var22, var22, var22);
               }

               field31.translate(0.0, (double)(var21.childHeadYOffset / 16.0F), (double)(var21.childHeadZOffset / 16.0F));
               if (var13 instanceof BipedEntityModel var23) {
                  method70(var98, var23.head);
               } else {
                  var21.getHeadParts().forEach(Class5923::lambda$render$4);
               }

               field31.pop();
               field31.push();
               float var54 = 1.0F / var21.invertedChildBodyScale;
               field31.scale(var54, var54, var54);
               field31.translate(0.0, (double)(var21.childBodyYOffset / 16.0F), 0.0);
               if (var13 instanceof BipedEntityModel var57) {
                  method70(var98, var57.body);
                  method70(var98, var57.leftArm);
                  method70(var98, var57.rightArm);
                  method70(var98, var57.leftLeg);
                  method70(var98, var57.rightLeg);
               } else {
                  var21.getBodyParts().forEach(Class5923::lambda$render$5);
               }

               field31.pop();
            } else if (var13 instanceof BipedEntityModel var55) {
               method70(var98, var55.head);
               method70(var98, var55.body);
               method70(var98, var55.leftArm);
               method70(var98, var55.rightArm);
               method70(var98, var55.leftLeg);
               method70(var98, var55.rightLeg);
            } else {
               var21.getHeadParts().forEach(Class5923::lambda$render$6);
               var21.getBodyParts().forEach(Class5923::lambda$render$7);
            }
         } else if (var13 instanceof SinglePartEntityModel var56) {
            method70(var98, var56.getPart());
         } else if (var13 instanceof CompositeEntityModel var58) {
            var58.getParts().forEach(Class5923::lambda$render$8);
         } else if (var13 instanceof LlamaEntityModel var24) {
            if (var24.child) {
               field31.push();
               field31.scale(0.71428573F, 0.64935064F, 0.7936508F);
               field31.translate(0.0, 1.3125, 0.22F);
               method70(var98, var24.head);
               field31.pop();
               field31.push();
               field31.scale(0.625F, 0.45454544F, 0.45454544F);
               field31.translate(0.0, 2.0625, 0.0);
               method70(var98, var24.body);
               field31.pop();
               field31.push();
               field31.scale(0.45454544F, 0.41322312F, 0.45454544F);
               field31.translate(0.0, 2.0625, 0.0);
               method70(var98, var24.rightHindLeg);
               method70(var98, var24.leftHindLeg);
               method70(var98, var24.rightFrontLeg);
               method70(var98, var24.leftFrontLeg);
               method70(var98, var24.rightChest);
               method70(var98, var24.leftChest);
               field31.pop();
            } else {
               method70(var98, var24.head);
               method70(var98, var24.body);
               method70(var98, var24.rightHindLeg);
               method70(var98, var24.leftHindLeg);
               method70(var98, var24.rightFrontLeg);
               method70(var98, var24.leftFrontLeg);
               method70(var98, var24.rightChest);
               method70(var98, var24.leftChest);
            }
         } else if (var13 instanceof RabbitEntityModel var25) {
            if (var25.child) {
               field31.push();
               field31.scale(0.56666666F, 0.56666666F, 0.56666666F);
               field31.translate(0.0, 1.375, 0.125);
               method70(var98, var25.head);
               method70(var98, var25.leftEar);
               method70(var98, var25.rightEar);
               method70(var98, var25.nose);
               field31.pop();
               field31.push();
               field31.scale(0.4F, 0.4F, 0.4F);
               field31.translate(0.0, 2.25, 0.0);
               method70(var98, var25.leftHindLeg);
               method70(var98, var25.rightHindLeg);
               method70(var98, var25.leftHaunch);
               method70(var98, var25.rightHaunch);
               method70(var98, var25.body);
               method70(var98, var25.leftFrontLeg);
               method70(var98, var25.rightFrontLeg);
               method70(var98, var25.tail);
               field31.pop();
            } else {
               field31.push();
               field31.scale(0.6F, 0.6F, 0.6F);
               field31.translate(0.0, 1.0, 0.0);
               method70(var98, var25.leftHindLeg);
               method70(var98, var25.rightHindLeg);
               method70(var98, var25.leftHaunch);
               method70(var98, var25.rightHaunch);
               method70(var98, var25.body);
               method70(var98, var25.leftFrontLeg);
               method70(var98, var25.rightFrontLeg);
               method70(var98, var25.head);
               method70(var98, var25.rightEar);
               method70(var98, var25.leftEar);
               method70(var98, var25.tail);
               method70(var98, var25.nose);
               field31.pop();
            }
         }
      }

      if (var10 instanceof EndCrystalEntityRenderer var26) {
         EndCrystalEntity var28 = (EndCrystalEntity)var100;
         boolean var33 = Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ad.method419();
         field31.push();
         float var36;
         if (var33) {
            float var41 = (float)var28.endCrystalAge + var99;
            float var46 = MathHelper.sin(var41 * 0.2F) / 2.0F + 0.5F;
            var46 = (var46 * var46 + var46) * 0.4F * Chams.INSTANCE.ah.method423();
            var36 = var46 - 1.4F;
         } else {
            var36 = EndCrystalEntityRenderer.getYOffset(var28, var99);
         }

         float var42 = ((float)var28.endCrystalAge + var99) * 3.0F;
         field31.push();
         if (var33) {
            field31.scale(2.0F * Chams.INSTANCE.ae.method423(), 2.0F * Chams.INSTANCE.af.method423(), 2.0F * Chams.INSTANCE.ae.method423());
         } else {
            field31.scale(2.0F, 2.0F, 2.0F);
         }

         field31.translate(0.0, -0.5, 0.0);
         if (var28.shouldShowBottom()) {
            method70(var98, var26.bottom);
         }

         if (var33) {
            field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var42 * Chams.INSTANCE.ag.method423()));
         } else {
            field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var42));
         }

         field31.translate(0.0, (double)(1.5F + var36 / 2.0F), 0.0);
         field31.multiply(
            new Quaternionf().setAngleAxis((float) (Math.PI / 3), EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES)
         );
         method70(var98, var26.frame);
         field31.scale(0.875F, 0.875F, 0.875F);
         field31.multiply(
            new Quaternionf().setAngleAxis((float) (Math.PI / 3), EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES)
         );
         if (var33) {
            field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var42 * Chams.INSTANCE.ag.method423()));
         } else {
            field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var42));
         }

         method70(var98, var26.frame);
         field31.scale(0.875F, 0.875F, 0.875F);
         field31.multiply(
            new Quaternionf().setAngleAxis((float) (Math.PI / 3), EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES)
         );
         if (var33) {
            field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var42 * Chams.INSTANCE.ag.method423()));
         } else {
            field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var42));
         }

         method70(var98, var26.core);
         field31.pop();
         field31.pop();
      } else if (var10 instanceof BoatEntityRenderer var27) {
         BoatEntity var29 = (BoatEntity)var100;
         field31.push();
         field31.translate(0.0, 0.375, 0.0);
         field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - MathHelper.lerp(var99, var100.prevYaw, var100.getYaw())));
         float var34 = (float)var29.getDamageWobbleTicks() - var99;
         float var37 = var29.getDamageWobbleStrength() - var99;
         if (var37 < 0.0F) {
            var37 = 0.0F;
         }

         if (var34 > 0.0F) {
            field31.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(var34) * var34 * var37 / 10.0F * (float)var29.getDamageWobbleSide()));
         }

         float var43 = var29.interpolateBubbleWobble(var99);
         if (!MathHelper.approximatelyEquals(var43, 0.0F)) {
            field31.multiply(new Quaternionf().setAngleAxis(var29.interpolateBubbleWobble(var99), 1.0F, 0.0F, 1.0F));
         }

         CompositeEntityModel var48 = (CompositeEntityModel)((Pair)var27.texturesAndModels.get(var29.getVariant())).getSecond();
         field31.scale(-1.0F, -1.0F, 1.0F);
         field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
         var48.setAngles(var29, var99, 0.0F, -0.1F, 0.0F, 0.0F);
         var48.getParts().forEach(Class5923::lambda$render$9);
         if (!var29.isSubmergedInWater() && var48 instanceof ModelWithWaterPatch var51) {
            method70(var98, var51.getWaterPatch());
         }

         field31.pop();
      } else if (var10 instanceof ItemEntityRenderer) {
         double var30 = (var100.getX() - var100.prevX) * (double)var99;
         double var38 = (var100.getY() - var100.prevY) * (double)var99;
         double var49 = (var100.getZ() - var100.prevZ) * (double)var99;
         Box var53 = var100.getBoundingBox();
         var98.method1271(
            var30 + var53.minX, var38 + var53.minY, var49 + var53.minZ, var30 + var53.maxX, var38 + var53.maxY, var49 + var53.maxZ, var101, var102, var103, 0
         );
      }

      field31.pop();
   }

   private static void method70(Renderer3D var0, ModelPart var1) {
      if (var1.visible && (!var1.cuboids.isEmpty() || !var1.children.isEmpty())) {
         field31.push();
         var1.rotate(field31);

         for (Cuboid var6 : var1.cuboids) {
            method71(var0, var6, field36, field37, field38);
         }

         for (ModelPart var8 : var1.children.values()) {
            method70(var0, var8);
         }

         field31.pop();
      }
   }

   private static void method71(Renderer3D var0, Cuboid var1, double var2, double var4, double var6) {
      Matrix4f var11 = field31.peek().getPositionMatrix();

      for (Quad var15 : var1.sides) {
         field32.set(var15.vertices[0].pos.x / 16.0F, var15.vertices[0].pos.y / 16.0F, var15.vertices[0].pos.z / 16.0F, 1.0F);
         field32.mul(var11);
         field33.set(var15.vertices[1].pos.x / 16.0F, var15.vertices[1].pos.y / 16.0F, var15.vertices[1].pos.z / 16.0F, 1.0F);
         field33.mul(var11);
         field34.set(var15.vertices[2].pos.x / 16.0F, var15.vertices[2].pos.y / 16.0F, var15.vertices[2].pos.z / 16.0F, 1.0F);
         field34.mul(var11);
         field35.set(var15.vertices[3].pos.x / 16.0F, var15.vertices[3].pos.y / 16.0F, var15.vertices[3].pos.z / 16.0F, 1.0F);
         field35.mul(var11);
         if (field41.method2115()) {
            var0.method1251(
               var2 + (double)field32.x,
               var4 + (double)field32.y,
               var6 + (double)field32.z,
               var2 + (double)field33.x,
               var4 + (double)field33.y,
               var6 + (double)field33.z,
               var2 + (double)field34.x,
               var4 + (double)field34.y,
               var6 + (double)field34.z,
               var2 + (double)field35.x,
               var4 + (double)field35.y,
               var6 + (double)field35.z,
               field39
            );
         }

         if (field41.method2114()) {
            var0.method1241(
               var2 + (double)field32.x,
               var4 + (double)field32.y,
               var6 + (double)field32.z,
               var2 + (double)field33.x,
               var4 + (double)field33.y,
               var6 + (double)field33.z,
               field40
            );
            var0.method1241(
               var2 + (double)field33.x,
               var4 + (double)field33.y,
               var6 + (double)field33.z,
               var2 + (double)field34.x,
               var4 + (double)field34.y,
               var6 + (double)field34.z,
               field40
            );
            var0.method1241(
               var2 + (double)field34.x,
               var4 + (double)field34.y,
               var6 + (double)field34.z,
               var2 + (double)field35.x,
               var4 + (double)field35.y,
               var6 + (double)field35.z,
               field40
            );
            var0.method1241(
               var2 + (double)field32.x,
               var4 + (double)field32.y,
               var6 + (double)field32.z,
               var2 + (double)field32.x,
               var4 + (double)field32.y,
               var6 + (double)field32.z,
               field40
            );
         }
      }
   }

   private static void lambda$render$9(Renderer3D var0, ModelPart var1) {
      method70(var0, var1);
   }

   private static void lambda$render$8(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }

   private static void lambda$render$7(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }

   private static void lambda$render$6(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }

   private static void lambda$render$5(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }

   private static void lambda$render$4(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }

   private static void lambda$render$3(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }

   private static void lambda$render$2(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }

   private static void lambda$render$1(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }

   private static void lambda$render$0(Renderer3D var0, Object var1) {
      method70(var0, (ModelPart)var1);
   }
}
