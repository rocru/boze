package dev.boze.client.settings;

import net.minecraft.nbt.NbtCompound;

import java.util.function.BooleanSupplier;

public class WeirdSettingString extends Setting<String> {
   private String field938;
   private final String field939;

   public WeirdSettingString(String name, String value, String description) {
      super(name, description);
      this.field938 = value;
      this.field939 = value;
   }

   public WeirdSettingString(String name, String value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field938 = value;
      this.field939 = value;
   }

   public WeirdSettingString(String name, String value, String description, Setting parent) {
      super(name, description, parent);
      this.field938 = value;
      this.field939 = value;
   }

   public WeirdSettingString(String name, String value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field938 = value;
      this.field939 = value;
   }

   public String method1322() {
      return this.field938;
   }

   public String method1562() {
      return this.field938 = this.field939;
   }

   public String method1341(String newVal) {
      return this.field938 = newVal;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putString("Value", this.field938);
      return tag;
   }

   public String method1286(NbtCompound tag) {
      if (tag.contains("Value")) {
         this.field938 = tag.getString("Value");
      }

      return this.field938;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method1286(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method1341((String)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method1562();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method1322();
   }
}
