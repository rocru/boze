package dev.boze.client.settings;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

public class ListSetting extends Setting<ArrayList<String>> {
   private ArrayList<String> field953;
   private boolean field954 = true;

   public ListSetting(String name, ArrayList<String> value, String description) {
      super(name, description);
      this.field953 = value;
   }

   public ListSetting(String name, ArrayList<String> value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field953 = value;
   }

   public ListSetting(String name, ArrayList<String> value, String description, Setting parent) {
      super(name, description, parent);
      this.field953 = value;
   }

   public ListSetting(String name, ArrayList<String> value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field953 = value;
   }

   public ArrayList<String> method2120() {
      return this.field953;
   }

   public boolean method2118() {
      return this.field954;
   }

   public void method206(boolean dirty) {
      this.field954 = dirty;
   }

   public ArrayList<String> method405() {
      this.field953.clear();
      this.field954 = true;
      return this.field953;
   }

   public ArrayList<String> method406(ArrayList<String> newVal) {
      this.field954 = true;
      return this.field953 = newVal;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      NbtList var4 = new NbtList();
      this.field953.forEach(ListSetting::lambda$addValueToTag$0);
      tag.put("Files", var4);
      return tag;
   }

   public ArrayList<String> method407(NbtCompound tag) {
      if (tag.contains("Files")) {
         NbtList var5 = tag.getList("Files", 8);
         this.field953.clear();

         for (NbtElement var7 : var5) {
            if (var7 instanceof NbtString) {
               this.field954 = true;
               this.field953.add(var7.asString());
            }
         }
      }

      return this.field953;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method407(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method406((ArrayList<String>)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method405();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method2120();
   }

   private static void lambda$addValueToTag$0(NbtList var0, String var1) {
      if (!var0.contains(NbtString.of(var1))) {
         var0.add(NbtString.of(var1));
      }
   }
}
