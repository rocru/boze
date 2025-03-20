package dev.boze.client.settings;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

import java.util.Locale;
import java.util.function.BooleanSupplier;

public class MinMaxDoubleSetting extends Setting<double[]> {
   private final double[] field2198;
   private final double[] field2199;
   private double field2200;
   public final double field2201;
   public final double field2202;
   public final double field2203;
   public final boolean field2204;

   public MinMaxDoubleSetting(String name, double[] value, double min, double max, double step, String description) {
      super(name, description);
      this.field2198 = value;
      this.field2199 = (double[])value.clone();
      this.field2201 = min;
      this.field2202 = max;
      this.field2203 = step;
      this.field2204 = false;
      this.method1296();
   }

   public MinMaxDoubleSetting(String name, double[] value, double min, double max, double step, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field2198 = value;
      this.field2199 = (double[])value.clone();
      this.field2201 = min;
      this.field2202 = max;
      this.field2203 = step;
      this.field2204 = false;
      this.method1296();
   }

   public MinMaxDoubleSetting(String name, double[] value, double min, double max, double step, String description, Setting parent) {
      super(name, description, parent);
      this.field2198 = value;
      this.field2199 = (double[])value.clone();
      this.field2201 = min;
      this.field2202 = max;
      this.field2203 = step;
      this.field2204 = false;
      this.method1296();
   }

   public MinMaxDoubleSetting(String name, double[] value, double min, double max, double step, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field2198 = value;
      this.field2199 = (double[])value.clone();
      this.field2201 = min;
      this.field2202 = max;
      this.field2203 = step;
      this.field2204 = false;
      this.method1296();
   }

   public MinMaxDoubleSetting(String name, double[] value, String description) {
      super(name, description);
      this.field2198 = value;
      this.field2199 = (double[])value.clone();
      this.field2201 = Double.MIN_VALUE;
      this.field2202 = Double.MAX_VALUE;
      this.field2203 = 1.0E-4F;
      this.field2204 = true;
      this.method1296();
   }

   public MinMaxDoubleSetting(String name, double[] value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field2198 = value;
      this.field2199 = (double[])value.clone();
      this.field2201 = Double.MIN_VALUE;
      this.field2202 = Double.MAX_VALUE;
      this.field2203 = 1.0E-4F;
      this.field2204 = true;
      this.method1296();
   }

   public MinMaxDoubleSetting(String name, double[] value, String description, Setting parent) {
      super(name, description, parent);
      this.field2198 = value;
      this.field2199 = (double[])value.clone();
      this.field2201 = Double.MIN_VALUE;
      this.field2202 = Double.MAX_VALUE;
      this.field2203 = 1.0E-4F;
      this.field2204 = true;
      this.method1296();
   }

   public MinMaxDoubleSetting(String name, double[] value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field2198 = value;
      this.field2199 = (double[])value.clone();
      this.field2201 = Double.MIN_VALUE;
      this.field2202 = Double.MAX_VALUE;
      this.field2203 = 1.0E-4F;
      this.field2204 = true;
      this.method1296();
   }

   @Override
   public double[] getValue() {
      return this.field2198;
   }

   @Override
   public double[] resetValue() {
      this.field2198[0] = this.field2199[0];
      this.field2198[1] = this.field2199[1];
      return this.field2198;
   }

   @Override
   public double[] setValue(double[] newVal) {
      this.method1290(0, newVal[0]);
      this.method1290(1, newVal[1]);
      return this.field2198;
   }

   public double method1290(int index, double newVal) {
      if (!this.field2204) {
         if (newVal > this.field2202) {
            newVal = this.field2202;
         } else if (newVal < this.field2201) {
            newVal = this.field2201;
         }
      }

      return this.field2198[index] = newVal;
   }

   public int method1291() {
      return this.field2198[0] < this.field2198[1] ? 0 : 1;
   }

   public int method1292() {
      return this.field2198[0] > this.field2198[1] ? 0 : 1;
   }

   public double method1293() {
      return this.field2198[this.method1291()];
   }

   public double method1294() {
      return this.field2198[this.method1292()];
   }

   public double method1295() {
      return this.field2200;
   }

   public double method1296() {
      double var1 = this.field2200;
      double var3 = Math.min(this.field2198[0], this.field2198[1]);
      double var5 = Math.max(this.field2198[0], this.field2198[1]);
      this.field2200 = Math.random() * (var5 - var3) + var3;
      return var1;
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(
               method402("firstvalue", DoubleArgumentType.doubleArg())
                  .then(method402("secondvalue", DoubleArgumentType.doubleArg()).executes(this::lambda$build$0))
            )
      );
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putDouble("Value0", this.field2198[0]);
      tag.putDouble("Value1", this.field2198[1]);
      return tag;
   }

   @Override
   public double[] load(NbtCompound tag) {
      if (tag.contains("Value0")) {
         this.field2198[0] = tag.getDouble("Value0");
      }

      if (tag.contains("Value1")) {
         this.field2198[1] = tag.getDouble("Value1");
      }

      return this.field2198;
   }

   // $VF: synthetic method
   // $VF: bridge method
  // @Override
  // public Object load(NbtCompound nbtCompound) {
  //    return this.method1297(nbtCompound);
  // }

   // $VF: synthetic method
   // $VF: bridge method
  // @Override
   //public Object setValue(Object object) {
   //   return this.method1289((double[])object);
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object resetValue() {
   //   return this.method1288();
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object getValue() {
   //   return this.method1287();
   //}

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      this.method1290(0, DoubleArgumentType.getDouble(var1, "firstvalue"));
      this.method1290(1, DoubleArgumentType.getDouble(var1, "secondvalue"));
      return 1;
   }
}
