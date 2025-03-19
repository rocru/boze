package dev.boze.client.settings;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.systems.modules.client.OldColors;
import net.minecraft.nbt.NbtCompound;

import java.util.function.BooleanSupplier;

public class ColorSetting extends Setting<BozeDrawColor> {
   private boolean sync = false;
   private BozeDrawColor field979;
   public final BozeDrawColor field980;

   public ColorSetting(String name, BozeDrawColor value, String description) {
      super(name, description);
      this.field979 = value;
      this.field980 = value.copy();
   }

   public ColorSetting(String name, BozeDrawColor value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field979 = value;
      this.field980 = value.copy();
   }

   public ColorSetting(String name, BozeDrawColor value, String description, Setting parent) {
      super(name, description, parent);
      this.field979 = value;
      this.field980 = value.copy();
   }

   public ColorSetting(String name, BozeDrawColor value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field979 = value;
      this.field980 = value.copy();
   }

   public BozeDrawColor method1362() {
      if (this.sync) {
         BozeDrawColor var4 = OldColors.INSTANCE.clientGradient.method1362();
         return new BozeDrawColor(
            var4.field408, var4.field409, var4.field410, this.field979.field411, var4.field1842, var4.field1843, var4.field1844, var4.field1845, var4.field1846
         );
      } else {
         return this.field979;
      }
   }

   public BozeDrawColor method1374() {
      return this.field979;
   }

   public BozeDrawColor method1375() {
      return this.field979 = this.field980;
   }

   public BozeDrawColor method474(BozeDrawColor newVal) {
      return this.field979 = newVal;
   }

   public boolean getSync() {
      return this.sync;
   }

   public void setSync(boolean sync) {
      this.sync = sync;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.put("Color", this.field979.toTag());
      tag.putBoolean("Sync", this.sync);
      return tag;
   }

   public BozeDrawColor method966(NbtCompound tag) {
      if (tag.contains("Color")) {
         this.field979.fromTag(tag.getCompound("Color"));
      }

      if (tag.contains("Sync")) {
         this.sync = tag.getBoolean("Sync");
      }

      return this.field979;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method966(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method474((BozeDrawColor)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method1375();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method1362();
   }
}
