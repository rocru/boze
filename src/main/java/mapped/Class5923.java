package mapped;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.systems.modules.render.Chams;
import dev.boze.client.systems.modules.render.NoRender;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;
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
    private static final MatrixStack field31;
    private static final Vector4f field32;
    private static final Vector4f field33;
    private static final Vector4f field34;
    private static final Vector4f field35;
    private static double field36;
    private static double field37;
    private static double field38;
    private static BozeDrawColor field39;
    private static BozeDrawColor field40;
    private static ShapeMode field41;

    static {
        field31 = new MatrixStack();
        field32 = new Vector4f();
        field33 = new Vector4f();
        field34 = new Vector4f();
        field35 = new Vector4f();
    }

    public Class5923() {
        super();
    }

    public static void method67(final Renderer3D renderer3D, final float tickDelta, final FakePlayerEntity entity, final BozeDrawColor sideColor, final BozeDrawColor lineColor, final ShapeMode shapeMode, final float handSwingProgress, final float limbDistance, float limbAngle) {
        Class5923.field39 = sideColor;
        Class5923.field40 = lineColor;
        Class5923.field41 = shapeMode;
        Class5923.field36 = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        Class5923.field37 = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        Class5923.field38 = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
        Class5923.field31.push();
        final EntityRenderer renderer = Class5923.mc.getEntityRenderDispatcher().getRenderer((Entity) entity);
        if (renderer instanceof final LivingEntityRenderer livingEntityRenderer) {
            final EntityModel model = livingEntityRenderer.getModel();
            if (renderer instanceof final PlayerEntityRenderer playerEntityRenderer) {
                final PlayerEntityModel playerEntityModel = playerEntityRenderer.getModel();
                playerEntityModel.sneaking = (entity.isInSneakingPose() || NoRender.method1992());
                final BipedEntityModel.ArmPose armPose = PlayerEntityRenderer.getArmPose(entity, Hand.MAIN_HAND);
                BipedEntityModel.ArmPose armPose2 = PlayerEntityRenderer.getArmPose(entity, Hand.OFF_HAND);
                if (armPose.isTwoHanded()) {
                    armPose2 = (entity.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM);
                }
                if (entity.getMainArm() == Arm.RIGHT) {
                    playerEntityModel.rightArmPose = armPose;
                    playerEntityModel.leftArmPose = armPose2;
                } else {
                    playerEntityModel.rightArmPose = armPose2;
                    playerEntityModel.leftArmPose = armPose;
                }
            }
            model.handSwingProgress = handSwingProgress;
            model.riding = entity.hasVehicle();
            model.child = entity.isBaby();
            float lerpAngleDegrees = MathHelper.lerpAngleDegrees(tickDelta, entity.prevBodyYaw, entity.bodyYaw);
            final float lerpAngleDegrees2 = MathHelper.lerpAngleDegrees(tickDelta, entity.prevHeadYaw, entity.headYaw);
            float n = lerpAngleDegrees2 - lerpAngleDegrees;
            if (entity.hasVehicle()) {
                final Entity vehicle = entity.getVehicle();
                if (vehicle instanceof final LivingEntity livingEntity) {
                    float wrapDegrees = MathHelper.wrapDegrees(lerpAngleDegrees2 - MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevBodyYaw, livingEntity.bodyYaw));
                    if (wrapDegrees < -85.0f) {
                        wrapDegrees = -85.0f;
                    }
                    if (wrapDegrees >= 85.0f) {
                        wrapDegrees = 85.0f;
                    }
                    lerpAngleDegrees = lerpAngleDegrees2 - wrapDegrees;
                    if (wrapDegrees * wrapDegrees > 2500.0f) {
                        lerpAngleDegrees += (float) (wrapDegrees * 0.2);
                    }
                    n = lerpAngleDegrees2 - lerpAngleDegrees;
                }
            }
            final float lerp = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());
            final float animationProgress = livingEntityRenderer.getAnimationProgress(entity, tickDelta);
            if (!entity.hasVehicle() && entity.isAlive() && entity.isBaby()) {
                limbAngle *= 3.0f;
            }
            model.animateModel(entity, limbAngle, 1.0f, tickDelta);
            model.setAngles(entity, limbAngle, 1.0f, animationProgress, n, lerp);
            livingEntityRenderer.setupTransforms(entity, Class5923.field31, animationProgress, lerpAngleDegrees, tickDelta, entity.getScale());
            Class5923.field31.scale(-1.0f, -1.0f, 1.0f);
            livingEntityRenderer.scale(entity, Class5923.field31, tickDelta);
            Class5923.field31.translate(0.0, -1.5010000467300415, 0.0);
            if (model instanceof final AnimalModel animalModel) {
                if (animalModel.child) {
                    Class5923.field31.push();
                    if (animalModel.headScaled) {
                        final float n2 = 1.5f / animalModel.invertedChildHeadScale;
                        Class5923.field31.scale(n2, n2, n2);
                    }
                    Class5923.field31.translate(0.0, animalModel.childHeadYOffset / 16.0f, animalModel.childHeadZOffset / 16.0f);
                    if (model instanceof final BipedEntityModel bipedEntityModel3) {
                        method70(renderer3D, bipedEntityModel3.head);
                    } else {
                        animalModel.getHeadParts().forEach(v -> lambda$render$0(renderer3D, v));
                    }
                    Class5923.field31.pop();
                    Class5923.field31.push();
                    final float n3 = 1.0f / animalModel.invertedChildBodyScale;
                    Class5923.field31.scale(n3, n3, n3);
                    Class5923.field31.translate(0.0, animalModel.childBodyYOffset / 16.0f, 0.0);
                    if (model instanceof final BipedEntityModel bipedEntityModel) {
                        method70(renderer3D, bipedEntityModel.body);
                        method70(renderer3D, bipedEntityModel.leftArm);
                        method70(renderer3D, bipedEntityModel.rightArm);
                        method70(renderer3D, bipedEntityModel.leftLeg);
                        method70(renderer3D, bipedEntityModel.rightLeg);
                    } else {
                        animalModel.getBodyParts().forEach(v -> lambda$render$1(renderer3D, v));
                    }
                    Class5923.field31.pop();
                } else if (model instanceof final BipedEntityModel bipedEntityModel2) {
                    method70(renderer3D, bipedEntityModel2.head);
                    method70(renderer3D, bipedEntityModel2.body);
                    method70(renderer3D, bipedEntityModel2.leftArm);
                    method70(renderer3D, bipedEntityModel2.rightArm);
                    method70(renderer3D, bipedEntityModel2.leftLeg);
                    method70(renderer3D, bipedEntityModel2.rightLeg);
                } else {
                    animalModel.getHeadParts().forEach(v -> lambda$render$2(renderer3D, v));
                    animalModel.getBodyParts().forEach(v -> lambda$render$3(renderer3D, v));
                }
            }
        }
        Class5923.field31.pop();
    }

    public static void method68(final Renderer3D renderer3D, final float tickDelta, final Entity entity, final BozeDrawColor sideColor, final BozeDrawColor lineColor, final ShapeMode shapeMode) {
        method69(renderer3D, tickDelta, entity, sideColor, lineColor, shapeMode, null);
    }

    public static void method69(final Renderer3D renderer3D, final float tickDelta, final Entity entity, final BozeDrawColor sideColor, final BozeDrawColor lineColor, final ShapeMode shapeMode, final Vec3d difference) {
        Class5923.field39 = sideColor;
        Class5923.field40 = lineColor;
        Class5923.field41 = shapeMode;
        if (difference != null) {
            Class5923.field36 = entity.getX() + difference.x;
            Class5923.field37 = entity.getY() + difference.y;
            Class5923.field38 = entity.getZ() + difference.z;
        } else {
            Class5923.field36 = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
            Class5923.field37 = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
            Class5923.field38 = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
        }
        Class5923.field31.push();
        final EntityRenderer renderer = Class5923.mc.getEntityRenderDispatcher().getRenderer(entity);
        if (renderer instanceof final LivingEntityRenderer livingEntityRenderer) {
            final LivingEntity livingEntity = (LivingEntity) entity;
            final EntityModel model = livingEntityRenderer.getModel();
            if (renderer instanceof final PlayerEntityRenderer playerEntityRenderer) {
                final PlayerEntityModel playerEntityModel = playerEntityRenderer.getModel();
                playerEntityModel.sneaking = (entity.isInSneakingPose() || NoRender.method1992());
                final BipedEntityModel.ArmPose armPose = PlayerEntityRenderer.getArmPose((AbstractClientPlayerEntity) entity, Hand.MAIN_HAND);
                BipedEntityModel.ArmPose armPose2 = PlayerEntityRenderer.getArmPose((AbstractClientPlayerEntity) entity, Hand.OFF_HAND);
                if (armPose.isTwoHanded()) {
                    armPose2 = (livingEntity.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM);
                }
                if (livingEntity.getMainArm() == Arm.RIGHT) {
                    playerEntityModel.rightArmPose = armPose;
                    playerEntityModel.leftArmPose = armPose2;
                } else {
                    playerEntityModel.rightArmPose = armPose2;
                    playerEntityModel.leftArmPose = armPose;
                }
            }
            model.handSwingProgress = livingEntity.getHandSwingProgress(tickDelta);
            model.riding = livingEntity.hasVehicle();
            model.child = livingEntity.isBaby();
            float lerpAngleDegrees = MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
            final float lerpAngleDegrees2 = MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevHeadYaw, livingEntity.headYaw);
            float n = lerpAngleDegrees2 - lerpAngleDegrees;
            if (livingEntity.hasVehicle()) {
                final Entity vehicle = livingEntity.getVehicle();
                if (vehicle instanceof final LivingEntity livingEntity2) {
                    float wrapDegrees = MathHelper.wrapDegrees(lerpAngleDegrees2 - MathHelper.lerpAngleDegrees(tickDelta, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw));
                    if (wrapDegrees < -85.0f) {
                        wrapDegrees = -85.0f;
                    }
                    if (wrapDegrees >= 85.0f) {
                        wrapDegrees = 85.0f;
                    }
                    lerpAngleDegrees = lerpAngleDegrees2 - wrapDegrees;
                    if (wrapDegrees * wrapDegrees > 2500.0f) {
                        lerpAngleDegrees += (float) (wrapDegrees * 0.2);
                    }
                    n = lerpAngleDegrees2 - lerpAngleDegrees;
                }
            }
            final float lerp = MathHelper.lerp(tickDelta, livingEntity.prevPitch, livingEntity.getPitch());
            final float animationProgress = livingEntityRenderer.getAnimationProgress(livingEntity, tickDelta);
            float speed = 0.0f;
            float pos = 0.0f;
            if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
                speed = livingEntity.limbAnimator.getSpeed(tickDelta);
                pos = livingEntity.limbAnimator.getPos(tickDelta);
                if (livingEntity.isBaby()) {
                    pos *= 3.0f;
                }
                if (speed > 1.0f) {
                    speed = 1.0f;
                }
            }
            model.animateModel(livingEntity, pos, speed, tickDelta);
            model.setAngles(livingEntity, pos, speed, animationProgress, n, lerp);
            livingEntityRenderer.setupTransforms(livingEntity, Class5923.field31, animationProgress, lerpAngleDegrees, tickDelta, livingEntity.getScale());
            Class5923.field31.scale(-1.0f, -1.0f, 1.0f);
            livingEntityRenderer.scale(livingEntity, Class5923.field31, tickDelta);
            Class5923.field31.translate(0.0, -1.5010000467300415, 0.0);
            if (model instanceof final AnimalModel animalModel) {
                if (animalModel.child) {
                    Class5923.field31.push();
                    if (animalModel.headScaled) {
                        final float n2 = 1.5f / animalModel.invertedChildHeadScale;
                        Class5923.field31.scale(n2, n2, n2);
                    }
                    Class5923.field31.translate(0.0, animalModel.childHeadYOffset / 16.0f, animalModel.childHeadZOffset / 16.0f);
                    if (model instanceof final BipedEntityModel bipedEntityModel3) {
                        method70(renderer3D, bipedEntityModel3.head);
                    } else {
                        animalModel.getHeadParts().forEach(v -> lambda$render$4(renderer3D, v));
                    }
                    Class5923.field31.pop();
                    Class5923.field31.push();
                    final float n3 = 1.0f / animalModel.invertedChildBodyScale;
                    Class5923.field31.scale(n3, n3, n3);
                    Class5923.field31.translate(0.0, animalModel.childBodyYOffset / 16.0f, 0.0);
                    if (model instanceof final BipedEntityModel bipedEntityModel) {
                        method70(renderer3D, bipedEntityModel.body);
                        method70(renderer3D, bipedEntityModel.leftArm);
                        method70(renderer3D, bipedEntityModel.rightArm);
                        method70(renderer3D, bipedEntityModel.leftLeg);
                        method70(renderer3D, bipedEntityModel.rightLeg);
                    } else {
                        animalModel.getBodyParts().forEach(v -> lambda$render$5(renderer3D, v));
                    }
                    Class5923.field31.pop();
                } else if (model instanceof final BipedEntityModel bipedEntityModel2) {
                    method70(renderer3D, bipedEntityModel2.head);
                    method70(renderer3D, bipedEntityModel2.body);
                    method70(renderer3D, bipedEntityModel2.leftArm);
                    method70(renderer3D, bipedEntityModel2.rightArm);
                    method70(renderer3D, bipedEntityModel2.leftLeg);
                    method70(renderer3D, bipedEntityModel2.rightLeg);
                } else {
                    animalModel.getHeadParts().forEach(v -> lambda$render$6(renderer3D, v));
                    animalModel.getBodyParts().forEach(v -> lambda$render$7(renderer3D, v));
                }
            } else if (model instanceof final SinglePartEntityModel singlePartEntityModel) {
                method70(renderer3D, singlePartEntityModel.getPart());
            } else if (model instanceof final CompositeEntityModel compositeEntityModel2) {
                compositeEntityModel2.getParts().forEach(v -> lambda$render$8(renderer3D, v));
            } else if (model instanceof final LlamaEntityModel llamaEntityModel) {
                if (llamaEntityModel.child) {
                    Class5923.field31.push();
                    Class5923.field31.scale(0.71428573f, 0.64935064f, 0.7936508f);
                    Class5923.field31.translate(0.0, 1.3125, 0.2199999988079071);
                    method70(renderer3D, llamaEntityModel.head);
                    Class5923.field31.pop();
                    Class5923.field31.push();
                    Class5923.field31.scale(0.625f, 0.45454544f, 0.45454544f);
                    Class5923.field31.translate(0.0, 2.0625, 0.0);
                    method70(renderer3D, llamaEntityModel.body);
                    Class5923.field31.pop();
                    Class5923.field31.push();
                    Class5923.field31.scale(0.45454544f, 0.41322312f, 0.45454544f);
                    Class5923.field31.translate(0.0, 2.0625, 0.0);
                    method70(renderer3D, llamaEntityModel.rightHindLeg);
                    method70(renderer3D, llamaEntityModel.leftHindLeg);
                    method70(renderer3D, llamaEntityModel.rightFrontLeg);
                    method70(renderer3D, llamaEntityModel.leftFrontLeg);
                    method70(renderer3D, llamaEntityModel.rightChest);
                    method70(renderer3D, llamaEntityModel.leftChest);
                    Class5923.field31.pop();
                } else {
                    method70(renderer3D, llamaEntityModel.head);
                    method70(renderer3D, llamaEntityModel.body);
                    method70(renderer3D, llamaEntityModel.rightHindLeg);
                    method70(renderer3D, llamaEntityModel.leftHindLeg);
                    method70(renderer3D, llamaEntityModel.rightFrontLeg);
                    method70(renderer3D, llamaEntityModel.leftFrontLeg);
                    method70(renderer3D, llamaEntityModel.rightChest);
                    method70(renderer3D, llamaEntityModel.leftChest);
                }
            } else if (model instanceof final RabbitEntityModel rabbitEntityModel) {
                if (rabbitEntityModel.child) {
                    Class5923.field31.push();
                    Class5923.field31.scale(0.56666666f, 0.56666666f, 0.56666666f);
                    Class5923.field31.translate(0.0, 1.375, 0.125);
                    method70(renderer3D, rabbitEntityModel.head);
                    method70(renderer3D, rabbitEntityModel.leftEar);
                    method70(renderer3D, rabbitEntityModel.rightEar);
                    method70(renderer3D, rabbitEntityModel.nose);
                    Class5923.field31.pop();
                    Class5923.field31.push();
                    Class5923.field31.scale(0.4f, 0.4f, 0.4f);
                    Class5923.field31.translate(0.0, 2.25, 0.0);
                    method70(renderer3D, rabbitEntityModel.leftHindLeg);
                    method70(renderer3D, rabbitEntityModel.rightHindLeg);
                    method70(renderer3D, rabbitEntityModel.leftHaunch);
                    method70(renderer3D, rabbitEntityModel.rightHaunch);
                    method70(renderer3D, rabbitEntityModel.body);
                    method70(renderer3D, rabbitEntityModel.leftFrontLeg);
                    method70(renderer3D, rabbitEntityModel.rightFrontLeg);
                    method70(renderer3D, rabbitEntityModel.tail);
                    Class5923.field31.pop();
                } else {
                    Class5923.field31.push();
                    Class5923.field31.scale(0.6f, 0.6f, 0.6f);
                    Class5923.field31.translate(0.0, 1.0, 0.0);
                    method70(renderer3D, rabbitEntityModel.leftHindLeg);
                    method70(renderer3D, rabbitEntityModel.rightHindLeg);
                    method70(renderer3D, rabbitEntityModel.leftHaunch);
                    method70(renderer3D, rabbitEntityModel.rightHaunch);
                    method70(renderer3D, rabbitEntityModel.body);
                    method70(renderer3D, rabbitEntityModel.leftFrontLeg);
                    method70(renderer3D, rabbitEntityModel.rightFrontLeg);
                    method70(renderer3D, rabbitEntityModel.head);
                    method70(renderer3D, rabbitEntityModel.rightEar);
                    method70(renderer3D, rabbitEntityModel.leftEar);
                    method70(renderer3D, rabbitEntityModel.tail);
                    method70(renderer3D, rabbitEntityModel.nose);
                    Class5923.field31.pop();
                }
            }
        }
        if (renderer instanceof final EndCrystalEntityRenderer endCrystalEntityRenderer) {
            final EndCrystalEntity endCrystalEntity = (EndCrystalEntity) entity;
            final boolean b = Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ad.getValue();
            Class5923.field31.push();
            float yOffset;
            if (b) {
                final float n4 = MathHelper.sin((endCrystalEntity.endCrystalAge + tickDelta) * 0.2f) / 2.0f + 0.5f;
                yOffset = (n4 * n4 + n4) * 0.4f * Chams.INSTANCE.ah.getValue() - 1.4f;
            } else {
                yOffset = EndCrystalEntityRenderer.getYOffset(endCrystalEntity, tickDelta);
            }
            final float n5 = (endCrystalEntity.endCrystalAge + tickDelta) * 3.0f;
            Class5923.field31.push();
            if (b) {
                Class5923.field31.scale(2.0f * Chams.INSTANCE.ae.getValue(), 2.0f * Chams.INSTANCE.af.getValue(), 2.0f * Chams.INSTANCE.ae.getValue());
            } else {
                Class5923.field31.scale(2.0f, 2.0f, 2.0f);
            }
            Class5923.field31.translate(0.0, -0.5, 0.0);
            if (endCrystalEntity.shouldShowBottom()) {
                method70(renderer3D, endCrystalEntityRenderer.bottom);
            }
            if (b) {
                Class5923.field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n5 * Chams.INSTANCE.ag.getValue()));
            } else {
                Class5923.field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n5));
            }
            Class5923.field31.translate(0.0, 1.5f + yOffset / 2.0f, 0.0);
            Class5923.field31.multiply(new Quaternionf().setAngleAxis(1.0471976f, EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0f, EndCrystalEntityRenderer.SINE_45_DEGREES));
            method70(renderer3D, endCrystalEntityRenderer.frame);
            Class5923.field31.scale(0.875f, 0.875f, 0.875f);
            Class5923.field31.multiply(new Quaternionf().setAngleAxis(1.0471976f, EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0f, EndCrystalEntityRenderer.SINE_45_DEGREES));
            if (b) {
                Class5923.field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n5 * Chams.INSTANCE.ag.getValue()));
            } else {
                Class5923.field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n5));
            }
            method70(renderer3D, endCrystalEntityRenderer.frame);
            Class5923.field31.scale(0.875f, 0.875f, 0.875f);
            Class5923.field31.multiply(new Quaternionf().setAngleAxis(1.0471976f, EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0f, EndCrystalEntityRenderer.SINE_45_DEGREES));
            if (b) {
                Class5923.field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n5 * Chams.INSTANCE.ag.getValue()));
            } else {
                Class5923.field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n5));
            }
            method70(renderer3D, endCrystalEntityRenderer.core);
            Class5923.field31.pop();
            Class5923.field31.pop();
        } else if (renderer instanceof final BoatEntityRenderer boatEntityRenderer) {
            final BoatEntity boatEntity = (BoatEntity) entity;
            Class5923.field31.push();
            Class5923.field31.translate(0.0, 0.375, 0.0);
            Class5923.field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw())));
            final float n6 = boatEntity.getDamageWobbleTicks() - tickDelta;
            float n7 = boatEntity.getDamageWobbleStrength() - tickDelta;
            if (n7 < 0.0f) {
                n7 = 0.0f;
            }
            if (n6 > 0.0f) {
                Class5923.field31.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(n6) * n6 * n7 / 10.0f * boatEntity.getDamageWobbleSide()));
            }
            if (!MathHelper.approximatelyEquals(boatEntity.interpolateBubbleWobble(tickDelta), 0.0f)) {
                Class5923.field31.multiply(new Quaternionf().setAngleAxis(boatEntity.interpolateBubbleWobble(tickDelta), 1.0f, 0.0f, 1.0f));
            }
            final CompositeEntityModel compositeEntityModel = boatEntityRenderer.texturesAndModels.get(boatEntity.getVariant()).getSecond();
            Class5923.field31.scale(-1.0f, -1.0f, 1.0f);
            Class5923.field31.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
            compositeEntityModel.setAngles(boatEntity, tickDelta, 0.0f, -0.1f, 0.0f, 0.0f);
            compositeEntityModel.getParts().forEach(arg_0 -> Class5923.lambda$render$9(renderer3D, (ModelPart) arg_0));
            if (!boatEntity.isSubmergedInWater() && compositeEntityModel instanceof ModelWithWaterPatch) {
                method70(renderer3D, ((ModelWithWaterPatch) compositeEntityModel).getWaterPatch());
            }
            Class5923.field31.pop();
        } else if (renderer instanceof ItemEntityRenderer) {
            final double n8 = (entity.getX() - entity.prevX) * tickDelta;
            final double n9 = (entity.getY() - entity.prevY) * tickDelta;
            final double n10 = (entity.getZ() - entity.prevZ) * tickDelta;
            final Box boundingBox = entity.getBoundingBox();
            renderer3D.method1271(n8 + boundingBox.minX, n9 + boundingBox.minY, n10 + boundingBox.minZ, n8 + boundingBox.maxX, n9 + boundingBox.maxY, n10 + boundingBox.maxZ, sideColor, lineColor, shapeMode, 0);
        }
        Class5923.field31.pop();
    }

    private static void method70(final Renderer3D renderer3D, final ModelPart modelPart) {
        if (!modelPart.visible || (modelPart.cuboids.isEmpty() && modelPart.children.isEmpty())) {
            return;
        }
        Class5923.field31.push();
        modelPart.rotate(Class5923.field31);
        for (ModelPart.Cuboid cuboid : modelPart.cuboids) {
            method71(renderer3D, cuboid, Class5923.field36, Class5923.field37, Class5923.field38);
        }
        for (ModelPart part : modelPart.children.values()) {
            method70(renderer3D, part);
        }
        Class5923.field31.pop();
    }

    private static void method71(final Renderer3D renderer3D, final ModelPart.Cuboid modelPart$Cuboid, final double n, final double n2, final double n3) {
        final Matrix4f positionMatrix = Class5923.field31.peek().getPositionMatrix();
        final ModelPart.Quad[] sides = modelPart$Cuboid.sides;
        for (int length = sides.length, i = 0; i < length; ++i) {
            final ModelPart.Quad modelPart$Quad = sides[i];
            Class5923.field32.set(modelPart$Quad.vertices[0].pos.x / 16.0f, modelPart$Quad.vertices[0].pos.y / 16.0f, modelPart$Quad.vertices[0].pos.z / 16.0f, 1.0f);
            Class5923.field32.mul(positionMatrix);
            Class5923.field33.set(modelPart$Quad.vertices[1].pos.x / 16.0f, modelPart$Quad.vertices[1].pos.y / 16.0f, modelPart$Quad.vertices[1].pos.z / 16.0f, 1.0f);
            Class5923.field33.mul(positionMatrix);
            Class5923.field34.set(modelPart$Quad.vertices[2].pos.x / 16.0f, modelPart$Quad.vertices[2].pos.y / 16.0f, modelPart$Quad.vertices[2].pos.z / 16.0f, 1.0f);
            Class5923.field34.mul(positionMatrix);
            Class5923.field35.set(modelPart$Quad.vertices[3].pos.x / 16.0f, modelPart$Quad.vertices[3].pos.y / 16.0f, modelPart$Quad.vertices[3].pos.z / 16.0f, 1.0f);
            Class5923.field35.mul(positionMatrix);
            if (Class5923.field41.method2115()) {
                renderer3D.method1251(n + Class5923.field32.x, n2 + Class5923.field32.y, n3 + Class5923.field32.z, n + Class5923.field33.x, n2 + Class5923.field33.y, n3 + Class5923.field33.z, n + Class5923.field34.x, n2 + Class5923.field34.y, n3 + Class5923.field34.z, n + Class5923.field35.x, n2 + Class5923.field35.y, n3 + Class5923.field35.z, Class5923.field39);
            }
            if (Class5923.field41.method2114()) {
                renderer3D.method1241(n + Class5923.field32.x, n2 + Class5923.field32.y, n3 + Class5923.field32.z, n + Class5923.field33.x, n2 + Class5923.field33.y, n3 + Class5923.field33.z, Class5923.field40);
                renderer3D.method1241(n + Class5923.field33.x, n2 + Class5923.field33.y, n3 + Class5923.field33.z, n + Class5923.field34.x, n2 + Class5923.field34.y, n3 + Class5923.field34.z, Class5923.field40);
                renderer3D.method1241(n + Class5923.field34.x, n2 + Class5923.field34.y, n3 + Class5923.field34.z, n + Class5923.field35.x, n2 + Class5923.field35.y, n3 + Class5923.field35.z, Class5923.field40);
                renderer3D.method1241(n + Class5923.field32.x, n2 + Class5923.field32.y, n3 + Class5923.field32.z, n + Class5923.field32.x, n2 + Class5923.field32.y, n3 + Class5923.field32.z, Class5923.field40);
            }
        }
    }

    private static void lambda$render$9(final Renderer3D renderer3D, final ModelPart modelPart) {
        method70(renderer3D, modelPart);
    }

    private static void lambda$render$8(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }

    private static void lambda$render$7(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }

    private static void lambda$render$6(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }

    private static void lambda$render$5(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }

    private static void lambda$render$4(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }

    private static void lambda$render$3(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }

    private static void lambda$render$2(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }

    private static void lambda$render$1(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }

    private static void lambda$render$0(final Renderer3D renderer3D, final Object o) {
        method70(renderer3D, (ModelPart) o);
    }
}
