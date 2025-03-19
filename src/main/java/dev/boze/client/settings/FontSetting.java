package dev.boze.client.settings;

import dev.boze.client.font.FontManager;
import dev.boze.client.manager.ConfigManager;
import net.minecraft.nbt.NbtCompound;

import java.io.File;
import java.util.function.BooleanSupplier;

public class FontSetting extends Setting<String> {
   private String field2177;
   private final String field2178;

   public FontSetting(String name, String value, String description) {
      super(name, description);
      this.field2177 = value;
      this.field2178 = value;
   }

   public FontSetting(String name, String value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field2177 = value;
      this.field2178 = value;
   }

   public FontSetting(String name, String value, String description, Setting parent) {
      super(name, description, parent);
      this.field2177 = value;
      this.field2178 = value;
   }

   public FontSetting(String name, String value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field2177 = value;
      this.field2178 = value;
   }

   public String method1276() {
      return this.field2177;
   }

   public String method1277() {
      return this.field2177 = this.field2178;
   }

   public String method1278(String newVal) {
      if (newVal.equals("vanilla")) {
         FontManager.method1104();
         this.field2177 = newVal;
         return this.field2177;
      } else {
         File var5 = new File(ConfigManager.fonts, newVal + ".ttf");
         if (var5.exists()) {
            this.field2177 = newVal;
            FontManager.method1103(this.field2177);
         }

         return this.field2177;
      }
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putString("Value", this.field2177);
      return tag;
   }

   public String method1279(NbtCompound tag) {
      if (tag.contains("Value")) {
         this.method1278(tag.getString("Value"));
      }

      return this.field2177;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method1279(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method1278((String)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method1277();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method1276();
   }
}
