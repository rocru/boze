package dev.boze.client.settings;

import java.util.function.BooleanSupplier;
import net.minecraft.nbt.NbtCompound;

public class SoundStringSetting extends Setting<String> {
   private String field966;
   private final String field967;

   public SoundStringSetting(String name, String value, String description) {
      super(name, description);
      this.field966 = value;
      this.field967 = value;
   }

   public SoundStringSetting(String name, String value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field966 = value;
      this.field967 = value;
   }

   public SoundStringSetting(String name, String value, String description, Setting parent) {
      super(name, description, parent);
      this.field966 = value;
      this.field967 = value;
   }

   public SoundStringSetting(String name, String value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field966 = value;
      this.field967 = value;
   }

   public String method1322() {
      return this.field966;
   }

   public String method1562() {
      return this.field966 = this.field967;
   }

   public String method1341(String newVal) {
      return this.field966 = newVal;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putString("Value", this.field966);
      return tag;
   }

   public String method1286(NbtCompound tag) {
      if (tag.contains("Value")) {
         this.field966 = tag.getString("Value");
      }

      return this.field966;
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
