package dev.boze.client.utils.render.color;

import java.util.Objects;
import mapped.Class5903;
import net.minecraft.nbt.NbtCompound;

public class StaticColor extends Class5903<StaticColor> {
   public int field430;
   public int field431;
   public int field432;

   public StaticColor(int red, int green, int blue) {
      this.field430 = red;
      this.field431 = green;
      this.field432 = blue;
   }

   public StaticColor(NbtCompound tag) {
      this.method216(tag);
   }

   public float method1384() {
      return (float)this.field430 / 255.0F;
   }

   public float method1385() {
      return (float)this.field431 / 255.0F;
   }

   public float method215() {
      return (float)this.field432 / 255.0F;
   }

   @Override
   public StaticColor method208() {
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         StaticColor var5 = (StaticColor)o;
         return this.field430 == var5.field430 && this.field431 == var5.field431 && this.field432 == var5.field432;
      } else {
         return false;
      }
   }

   public String toString() {
      int var3 = this.field432;
      int var4 = this.field431;
      int var5 = this.field430;
      return "StaticColor{red=" + var5 + ", green=" + var4 + ", blue=" + var3 + "}";
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.field430, this.field431, this.field432});
   }

   @Override
   public NbtCompound toTag() {
      NbtCompound var3 = new NbtCompound();
      var3.putInt("type", 0);
      var3.putInt("red", this.field430);
      var3.putInt("green", this.field431);
      var3.putInt("blue", this.field432);
      return var3;
   }

   public StaticColor method216(NbtCompound tag) {
      this.field430 = tag.getInt("red");
      this.field431 = tag.getInt("green");
      this.field432 = tag.getInt("blue");
      return this;
   }

   public StaticColor method217() {
      return new StaticColor(this.field430, this.field431, this.field432);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object fromTag(NbtCompound nbtCompound) {
      return this.method216(nbtCompound);
   }
}
