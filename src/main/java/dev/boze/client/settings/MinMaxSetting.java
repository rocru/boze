package dev.boze.client.settings;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

public class MinMaxSetting extends Setting<Double> {
   private double field940;
   private final double field941;
   public final double field942;
   public final double field943;
   public final double field944;
   public final boolean field945;

   public MinMaxSetting(String name, double value, double min, double max, double step, String description) {
      super(name, description);
      this.field940 = value;
      this.field941 = value;
      this.field942 = min;
      this.field943 = max;
      this.field944 = step;
      this.field945 = false;
   }

   public MinMaxSetting(String name, double value, double min, double max, double step, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field940 = value;
      this.field941 = value;
      this.field942 = min;
      this.field943 = max;
      this.field944 = step;
      this.field945 = false;
   }

   public MinMaxSetting(String name, double value, double min, double max, double step, String description, Setting parent) {
      super(name, description, parent);
      this.field940 = value;
      this.field941 = value;
      this.field942 = min;
      this.field943 = max;
      this.field944 = step;
      this.field945 = false;
   }

   public MinMaxSetting(String name, double value, double min, double max, double step, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field940 = value;
      this.field941 = value;
      this.field942 = min;
      this.field943 = max;
      this.field944 = step;
      this.field945 = false;
   }

   public MinMaxSetting(String name, double value, String description) {
      super(name, description);
      this.field940 = value;
      this.field941 = value;
      this.field942 = Double.MIN_VALUE;
      this.field943 = Double.MAX_VALUE;
      this.field944 = 1.0E-4F;
      this.field945 = true;
   }

   public MinMaxSetting(String name, double value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field940 = value;
      this.field941 = value;
      this.field942 = Double.MIN_VALUE;
      this.field943 = Double.MAX_VALUE;
      this.field944 = 1.0E-4F;
      this.field945 = true;
   }

   public MinMaxSetting(String name, double value, String description, Setting parent) {
      super(name, description, parent);
      this.field940 = value;
      this.field941 = value;
      this.field942 = Double.MIN_VALUE;
      this.field943 = Double.MAX_VALUE;
      this.field944 = 1.0E-4F;
      this.field945 = true;
   }

   public MinMaxSetting(String name, double value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field940 = value;
      this.field941 = value;
      this.field942 = Double.MIN_VALUE;
      this.field943 = Double.MAX_VALUE;
      this.field944 = 1.0E-4F;
      this.field945 = true;
   }

   public Double getValue() {
      return this.field940;
   }

   public Double resetValue() {
      return this.field940 = this.field941;
   }

   @Override
   public Double setValue(Double newVal) {
      if (!this.field945) {
         if (newVal > this.field943) {
            newVal = this.field943;
         } else if (newVal < this.field942) {
            newVal = this.field942;
         }
      }

      return this.field940 = newVal;
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method402("value", DoubleArgumentType.doubleArg()).executes(this::lambda$build$0)));
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putDouble("Value", this.field940);
      return tag;
   }

   @Override
   public Double load(NbtCompound tag) {
      if (tag.contains("Value")) {
         this.field940 = tag.getDouble("Value");
      }

      return this.field940;
   }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object load(NbtCompound nbtCompound) {
   //   return this.method438(nbtCompound);
   //}

   // $VF: synthetic method
   // $VF: bridge method

   // public Object setValue(Object object) {
   //   return this.setValue((Double)object);
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object resetValue() {
   //   return this.resetValue();
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object getValue() {
   //   return this.getValue();
   //}

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      this.setValue((Double)var1.getArgument("value", Double.class));
      return 1;
   }
}
