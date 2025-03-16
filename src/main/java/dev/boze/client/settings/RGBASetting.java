package dev.boze.client.settings;

import dev.boze.client.systems.modules.client.OldColors;
import dev.boze.client.utils.RGBAColor;
import java.awt.Color;
import java.util.function.BooleanSupplier;
import net.minecraft.nbt.NbtCompound;

public class RGBASetting extends Setting<RGBAColor> {
   private RGBAColor field957;
   public final RGBAColor field958;
   private boolean field959 = false;
   private boolean field960 = false;
   private int field961 = 1;

   public RGBASetting(String name, RGBAColor value, String description) {
      super(name, description);
      this.field957 = value;
      this.field958 = value.copy();
   }

   public RGBASetting(String name, RGBAColor value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field957 = value;
      this.field958 = value.copy();
   }

   public RGBASetting(String name, RGBAColor value, String description, Setting parent) {
      super(name, description, parent);
      this.field957 = value;
      this.field958 = value.copy();
   }

   public RGBASetting(String name, RGBAColor value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field957 = value;
      this.field958 = value.copy();
   }

   public RGBASetting(String name, RGBAColor value, boolean rainbow, String description) {
      super(name, description);
      this.field957 = value;
      this.field958 = value.copy();
   }

   public RGBASetting(String name, RGBAColor value, boolean rainbow, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field957 = value;
      this.field958 = value.copy();
   }

   public RGBASetting(String name, RGBAColor value, boolean rainbow, String description, Setting parent) {
      super(name, description, parent);
      this.field957 = value;
      this.field958 = value.copy();
   }

   public RGBASetting(String name, RGBAColor value, boolean rainbow, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field957 = value;
      this.field958 = value.copy();
   }

   public RGBAColor method1347() {
      if (this.field959) {
         RGBAColor var13 = OldColors.method1342();
         return new RGBAColor(var13.field408, var13.field409, var13.field410, this.field957.field411);
      } else if (this.field960) {
         int var4 = this.field957.method2010();
         float[] var5 = Color.RGBtoHSB(var4 >> 16 & 0xFF, var4 >> 8 & 0xFF, var4 & 0xFF, null);
         double var6 = Math.ceil((double)(System.currentTimeMillis() + 300L) / ((double)(22 - this.field961) * 2.0));
         var6 %= 360.0;
         int var8 = Color.getHSBColor((float)(var6 / 360.0), var5[1], var5[2]).getRGB();
         int var9 = var4 >> 24 & 0xFF;
         int var10 = var8 >> 16 & 0xFF;
         int var11 = var8 >> 8 & 0xFF;
         int var12 = var8 & 0xFF;
         return new RGBAColor(var10, var11, var12, var9);
      } else {
         return this.field957;
      }
   }

   public RGBAColor method1348() {
      return this.field957;
   }

   public RGBAColor method1349() {
      return this.field957 = this.field958.copy();
   }

   public RGBAColor method198(RGBAColor newVal) {
      return this.field957 = newVal;
   }

   public boolean method2118() {
      return this.field960;
   }

   public void method206(boolean rainbow) {
      this.field960 = rainbow;
   }

   public int method2010() {
      return this.field961;
   }

   public void method1649(int speed) {
      this.field961 = speed;
   }

   public boolean method222() {
      return this.field959;
   }

   public void method456(boolean sync) {
      this.field959 = sync;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.put("Color", this.field957.toTag());
      tag.putBoolean("Sync", this.field959);
      tag.putBoolean("Rainbow", this.field960);
      tag.putInt("Speed", this.field961);
      return tag;
   }

   public RGBAColor method201(NbtCompound tag) {
      if (tag.contains("Color")) {
         this.field957.fromTag(tag.getCompound("Color"));
      }

      if (tag.contains("Sync")) {
         this.field959 = tag.getBoolean("Sync");
      }

      if (tag.contains("Rainbow")) {
         this.field960 = tag.getBoolean("Rainbow");
      }

      if (tag.contains("Speed")) {
         this.field961 = tag.getInt("Speed");
      }

      return this.field957;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method201(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method198((RGBAColor)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method1349();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method1347();
   }
}
