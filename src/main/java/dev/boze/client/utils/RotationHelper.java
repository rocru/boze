package dev.boze.client.utils;

import dev.boze.client.systems.modules.client.GhostRotations;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import mapped.Class3092;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationHelper implements IMinecraft {
   public static final double field1361 = 1.4166666666666667;
   public static final double field1362 = 255.0;
   private float field1363;
   private float field1364;

   public RotationHelper(float yaw, float pitch) {
      this.field1363 = yaw;
      this.field1364 = pitch;
   }

   public RotationHelper(Entity entity) {
      this.field1363 = entity.getYaw();
      this.field1364 = entity.getPitch();
   }

   public float method1384() {
      return this.field1363;
   }

   public float method1385() {
      return this.field1364;
   }

   public RotationHelper method1600() {
      return this.method601(null, null);
   }

   public RotationHelper method600(Function<RotationHelper, Boolean> preference) {
      return this.method601(null, preference);
   }

   public RotationHelper method601(RotationHelper prevRotation, Function<RotationHelper, Boolean> preference) {
      RotationHelper var6 = GhostRotations.INSTANCE.field760;
      RotationHelper var7 = mc.player != null ? new RotationHelper(mc.player) : this;
      if (prevRotation == null) {
         prevRotation = var6 != null ? var6 : var7;
      }

      RotationHelper var8 = this.method606(prevRotation);
      Pair[] var9 = method614(var8);
      List var11 = (List)Arrays.stream(var9).map(RotationHelper::lambda$correctSensitivity$0).collect(Collectors.toList());
      if (preference != null) {
         Optional var12 = var11.stream().filter(preference::apply).findFirst();
         if (var12.isPresent()) {
            return (RotationHelper)var12.get();
         }
      }

      return (RotationHelper)var11.stream().min(this::lambda$correctSensitivity$1).orElse(null);
   }

   public RotationHelper method602(RotationHelper target, Pair<Double, Double> aimSpeed) {
      if ((Double)aimSpeed.getLeft() > (Double)aimSpeed.getRight()) {
         aimSpeed = new Pair((Double)aimSpeed.getRight(), (Double)aimSpeed.getLeft());
      }

      double var6;
      if ((Double)aimSpeed.getLeft() == 1.0 && (Double)aimSpeed.getRight() == 1.0) {
         var6 = 1.0;
      } else {
         double var8;
         if (((Double)aimSpeed.getLeft()).equals(aimSpeed.getRight())) {
            var8 = (Double)aimSpeed.getLeft();
         } else {
            var8 = ThreadLocalRandom.current().nextDouble((Double)aimSpeed.getLeft(), (Double)aimSpeed.getRight());
         }

         var6 = Math.max(0.0, Math.min(1.0, var8 * Class3092.field218 * 0.05));
      }

      return this.method604(target, var6);
   }

   public RotationHelper method603(RotationHelper target, double[] aimSpeed) {
      if (aimSpeed[0] > aimSpeed[1]) {
         aimSpeed = new double[]{aimSpeed[1] * 0.2, aimSpeed[0] * 0.2};
      } else {
         aimSpeed = new double[]{aimSpeed[0] * 0.2, aimSpeed[1] * 0.2};
      }

      double var6;
      if (aimSpeed[0] == 1.0 && aimSpeed[1] == 1.0) {
         var6 = 1.0;
      } else {
         double var8;
         if (aimSpeed[0] == aimSpeed[1]) {
            var8 = aimSpeed[0];
         } else {
            var8 = ThreadLocalRandom.current().nextDouble(aimSpeed[0], aimSpeed[1]);
         }

         var6 = Math.max(0.0, Math.min(1.0, var8 * Class3092.field218 * 0.05));
      }

      return this.method604(target, var6);
   }

   public RotationHelper method604(RotationHelper target, double smoothness) {
      RotationHelper var4 = this.method606(target).method612(smoothness);
      return this.method610(var4);
   }

   public float method605(RotationHelper other) {
      return this.method607(this.method606(other));
   }

   public RotationHelper method606(RotationHelper other) {
      float var2 = other.method1384() - this.method1384();
      float var3 = other.method1385() - this.method1385();
      return new RotationHelper(MathHelper.wrapDegrees(var2), var3);
   }

   private float method607(RotationHelper var1) {
      float var2 = var1.method1384();
      float var3 = var1.method1385();
      return (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
   }

   public Vec3d method1954() {
      double var1 = Math.toRadians((double)this.method1384());
      double var3 = Math.toRadians((double)this.method1385());
      return new Vec3d(-Math.sin(var1) * Math.cos(var3), -Math.sin(var3), Math.cos(var1) * Math.cos(var3));
   }

   public RotationHelper method608(float yaw) {
      return new RotationHelper(yaw, this.method1385());
   }

   public RotationHelper method609(float pitch) {
      return new RotationHelper(this.method1384(), pitch);
   }

   public void method488(Entity entity) {
      entity.setYaw(this.method1384());
      entity.setPitch(this.method1385());
   }

   public RotationHelper method610(RotationHelper other) {
      return new RotationHelper(this.method1384() + other.method1384(), this.method1385() + other.method1385());
   }

   public RotationHelper method611(RotationHelper other) {
      return new RotationHelper(this.method1384() - other.method1384(), this.method1385() - other.method1385());
   }

   public RotationHelper method612(double scalar) {
      return new RotationHelper((float)((double)this.method1384() * scalar), (float)((double)this.method1385() * scalar));
   }

   public static double method2091() {
      double var3 = (Double)mc.options.getMouseSensitivity().getValue() * 0.6 + 0.2;
      double var5 = var3 * var3 * var3;
      double var7 = var5 * 8.0;
      return mc.options.getPerspective().isFirstPerson() && mc.player != null && mc.player.isUsingSpyglass() ? var5 : var7;
   }

   public static RotationHelper method613(RotationHelper prevRotation, Pair<Double, Double> cursorDeltas) {
      double var2 = method2091();
      RotationHelper var4 = new RotationHelper((float)((Double)cursorDeltas.getLeft() * var2 * 0.15), (float)((Double)cursorDeltas.getRight() * var2 * 0.15));
      RotationHelper var5 = prevRotation.method610(var4);
      return var5.method609(Math.max(-90.0F, Math.min(90.0F, var5.field1364)));
   }

   public static Pair<Double, Double>[] method614(RotationHelper deltaRotation) {
      double var4 = method2091() * 0.15;
      double var6 = (double)(-deltaRotation.method1384()) / var4;
      double var8 = (double)(-deltaRotation.method1385()) / var4;
      return new Pair[]{
         new Pair(Math.floor(var6), Math.floor(var8)),
         new Pair(Math.ceil(var6), Math.floor(var8)),
         new Pair(Math.ceil(var6), Math.ceil(var8)),
         new Pair(Math.floor(var6), Math.ceil(var8))
      };
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         RotationHelper var5 = (RotationHelper)o;
         return Float.compare(this.field1363, var5.field1363) == 0 && Float.compare(this.field1364, var5.field1364) == 0;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.field1363, this.field1364});
   }

   private int lambda$correctSensitivity$1(RotationHelper var1, RotationHelper var2) {
      return Double.compare((double)this.method605(var1), (double)this.method605(var2));
   }

   private static RotationHelper lambda$correctSensitivity$0(RotationHelper var0, Pair var1) {
      return method613(var0, var1);
   }
}
