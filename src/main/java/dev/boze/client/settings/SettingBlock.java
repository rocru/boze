package dev.boze.client.settings;

import java.util.function.BooleanSupplier;
import net.minecraft.nbt.NbtCompound;

public class SettingBlock extends Setting<Boolean> {
   private boolean field977 = false;
   private final Setting<?>[] field978;

   public SettingBlock(String name, String description, Setting<?>... subSettings) {
      super(name, description);

      for (Setting var9 : subSettings) {
         var9.block = this;
      }

      this.field978 = new Setting[subSettings.length + 1];
      this.field978[0] = this;
      System.arraycopy(subSettings, 0, this.field978, 1, subSettings.length);
   }

   public SettingBlock(String name, String description, BooleanSupplier visibility, Setting<?>... subSettings) {
      super(name, description, visibility);

      for (Setting var11 : subSettings) {
         var11.block = this;
      }

      this.field978 = new Setting[subSettings.length + 1];
      this.field978[0] = this;
      System.arraycopy(subSettings, 0, this.field978, 1, subSettings.length);
   }

   public Setting<?>[] method472() {
      return this.field978;
   }

   public Boolean method419() {
      return this.field977;
   }

   public Boolean method420() {
      return this.field977 = false;
   }

   public Boolean method421(Boolean newVal) {
      return this.field977 = newVal;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      return tag;
   }

   public Boolean method422(NbtCompound tag) {
      return false;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method422(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method421((Boolean)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method420();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method419();
   }
}
