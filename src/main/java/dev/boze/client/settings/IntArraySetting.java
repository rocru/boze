package dev.boze.client.settings;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

import java.util.Locale;
import java.util.function.BooleanSupplier;

public class IntArraySetting extends Setting<int[]> {
   private final int[] field917;
   private final int[] field918;
   private int field919;
   public final int field920;
   public final int field921;
   public final int field922;
   public final boolean field923;

   public IntArraySetting(String name, int[] value, int min, int max, int step, String description) {
      super(name, description);
      this.field917 = value;
      this.field918 = (int[])value.clone();
      this.field920 = min;
      this.field921 = max;
      this.field922 = step;
      this.field923 = false;
   }

   public IntArraySetting(String name, int[] value, int min, int max, int step, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field917 = value;
      this.field918 = (int[])value.clone();
      this.field920 = min;
      this.field921 = max;
      this.field922 = step;
      this.field923 = false;
   }

   public IntArraySetting(String name, int[] value, int min, int max, int step, String description, Setting parent) {
      super(name, description, parent);
      this.field917 = value;
      this.field918 = (int[])value.clone();
      this.field920 = min;
      this.field921 = max;
      this.field922 = step;
      this.field923 = false;
   }

   public IntArraySetting(String name, int[] value, int min, int max, int step, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field917 = value;
      this.field918 = (int[])value.clone();
      this.field920 = min;
      this.field921 = max;
      this.field922 = step;
      this.field923 = false;
   }

   public IntArraySetting(String name, int[] value, String description) {
      super(name, description);
      this.field917 = value;
      this.field918 = (int[])value.clone();
      this.field920 = Integer.MIN_VALUE;
      this.field921 = Integer.MAX_VALUE;
      this.field922 = 1;
      this.field923 = true;
   }

   public IntArraySetting(String name, int[] value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field917 = value;
      this.field918 = (int[])value.clone();
      this.field920 = Integer.MIN_VALUE;
      this.field921 = Integer.MAX_VALUE;
      this.field922 = 1;
      this.field923 = true;
   }

   public IntArraySetting(String name, int[] value, String description, Setting parent) {
      super(name, description, parent);
      this.field917 = value;
      this.field918 = (int[])value.clone();
      this.field920 = Integer.MIN_VALUE;
      this.field921 = Integer.MAX_VALUE;
      this.field922 = 1;
      this.field923 = true;
   }

   public IntArraySetting(String name, int[] value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field917 = value;
      this.field918 = (int[])value.clone();
      this.field920 = Integer.MIN_VALUE;
      this.field921 = Integer.MAX_VALUE;
      this.field922 = 1;
      this.field923 = true;
   }

   public int[] method410() {
      return this.field917;
   }

   public int method2010() {
      return this.field917[this.method1365()];
   }

   public int method1547() {
      return this.field917[this.method1366()];
   }

   public int[] method411() {
      this.field917[0] = this.field918[0];
      this.field917[1] = this.field918[1];
      return this.field917;
   }

   public int[] method412(int[] newVal) {
      this.method413(0, newVal[0]);
      this.method413(1, newVal[1]);
      return this.field917;
   }

   public int method413(int index, int newVal) {
      if (!this.field923) {
         if (newVal > this.field921) {
            newVal = this.field921;
         } else if (newVal < this.field920) {
            newVal = this.field920;
         }
      }

      return this.field917[index] = newVal;
   }

   public int method1365() {
      return this.field917[0] < this.field917[1] ? 0 : 1;
   }

   public int method1366() {
      return this.field917[0] > this.field917[1] ? 0 : 1;
   }

   public int method1367() {
      return this.field919;
   }

   public int method1376() {
      int var1 = this.field919;
      int var2 = Math.min(this.field917[0], this.field917[1]);
      int var3 = Math.max(this.field917[0], this.field917[1]);
      this.field919 = (int)(Math.random() * (double)(var3 - var2) + (double)var2);
      return var1;
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(
               method402("firstvalue", IntegerArgumentType.integer())
                  .then(method402("secondvalue", IntegerArgumentType.integer()).executes(this::lambda$build$0))
            )
      );
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putInt("Value0", this.field917[0]);
      tag.putInt("Value1", this.field917[1]);
      return tag;
   }

   public int[] method418(NbtCompound tag) {
      if (tag.contains("Value0")) {
         this.field917[0] = tag.getInt("Value0");
      }

      if (tag.contains("Value1")) {
         this.field917[1] = tag.getInt("Value1");
      }

      return this.field917;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method418(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method412((int[])object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method411();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method410();
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      this.method413(0, IntegerArgumentType.getInteger(var1, "firstvalue"));
      this.method413(1, IntegerArgumentType.getInteger(var1, "secondvalue"));
      return 1;
   }
}
