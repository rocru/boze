package dev.boze.client.utils.player;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class RotationHelper implements IMinecraft {
    public static Vec3d field3956;

    public static void updateRotation() {
        Vector3f var3 = new Vector3f(0.0F, 0.0F, 1.0F);
        if (mc.options.getBobView().getValue()) {
            MatrixStack var4 = new MatrixStack();
            applyRotationToMatrix(var4);
            var3.mulPosition(var4.peek().getPositionMatrix().invert());
        }

        field3956 = new Vec3d(var3.x, -var3.y, var3.z)
                .rotateX(-((float) Math.toRadians(mc.gameRenderer.getCamera().getPitch())))
                .rotateY(-((float) Math.toRadians(mc.gameRenderer.getCamera().getYaw())))
                .add(mc.gameRenderer.getCamera().getPos());
    }

    private static void applyRotationToMatrix(MatrixStack var0) {
        if (mc.getCameraEntity() instanceof PlayerEntity var5) {
            float var6 = mc.getRenderTickCounter().getTickDelta(true);
            float var7 = var5.horizontalSpeed - var5.prevHorizontalSpeed;
            float var8 = -(var5.horizontalSpeed + var7 * var6);
            float var9 = MathHelper.lerp(var6, var5.prevStrideDistance, var5.strideDistance);
            var0.translate(
                    -((double) (MathHelper.sin(var8 * (float) Math.PI) * var9) * 0.5), -(-Math.abs(MathHelper.cos(var8 * (float) Math.PI) * var9)), 0.0
            );
            var0.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(var8 * (float) Math.PI) * var9 * 3.0F));
            var0.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(var8 * (float) Math.PI - 0.2F) * var9) * 5.0F));
        }
    }
}
