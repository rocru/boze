package dev.boze.client.settings;

import dev.boze.client.utils.ConfigNBTSerializer;
import net.minecraft.nbt.NbtCompound;

import java.util.function.BooleanSupplier;

public class SettingCategory extends Setting<Boolean> {
   public boolean field927 = true;

   public SettingCategory(String name, String description) {
      super(name, description);
   }

   public SettingCategory(String name, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
   }

   public SettingCategory(String name, String description, Setting parent) {
      super(name, description, parent);
   }

   public SettingCategory(String name, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
   }

   @Override
   public Boolean getValue() {
      return false;
   }

   @Override
   public Boolean resetValue() {
      return false;
   }

   @Override
   public Boolean setValue(Boolean newVal) {
      return false;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putBoolean("Value", true);
      return tag;
   }

   @Override
   public Boolean load(NbtCompound tag) {
      if (tag.contains("Value") && !ConfigNBTSerializer.field3913) {
         this.field927 = tag.getBoolean("Value");
      }

      return false;
   }

   // $VF: synthetic method
   // $VF: bridge method
  // @Override
   //public Object load(NbtCompound nbtCompound) {
  //    return this.method422(nbtCompound);
  // }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
  // public Object setValue(Object object) {
   //   return this.method421((Boolean)object);
  // }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object resetValue() {
  //    return this.method420();
  // }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object getValue() {
   //   return this.method419();
   //}
}
