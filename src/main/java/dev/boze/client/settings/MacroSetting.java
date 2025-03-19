package dev.boze.client.settings;

import dev.boze.client.Boze;
import dev.boze.client.utils.Macro;
import net.minecraft.nbt.NbtCompound;

import java.util.function.BooleanSupplier;

public class MacroSetting extends Setting<String> {
   private String field974 = "";
   private final String field975 = "";

   public MacroSetting(String name, String description) {
      super(name, description);
   }

   public MacroSetting(String name, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
   }

   public MacroSetting(String name, String description, Setting parent) {
      super(name, description, parent);
   }

   public MacroSetting(String name, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
   }

   public Macro method467() {
      if (this.field974.isEmpty()) {
         return null;
      } else {
         for (Macro var5 : Boze.getMacros().field2140) {
            if (var5.field1048.equals(this.field974)) {
               return var5;
            }
         }

         return null;
      }
   }

   public String method1322() {
      if (this.method467() == null) {
         this.field974 = "";
      }

      return this.field974;
   }

   public String method1562() {
      return this.field974 = this.field975;
   }

   public String method1341(String newVal) {
      this.field974 = newVal;
      if (this.method467() == null) {
         this.field974 = "";
      }

      return this.field974;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putString("Value", this.field974);
      return tag;
   }

   public String method1286(NbtCompound tag) {
      if (tag.contains("Value")) {
         this.field974 = tag.getString("Value");
      }

      return this.field974;
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
