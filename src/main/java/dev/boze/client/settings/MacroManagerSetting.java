package dev.boze.client.settings;

import dev.boze.client.manager.MacroManager;
import dev.boze.client.utils.IMinecraft;
import mapped.Class27;
import net.minecraft.nbt.NbtCompound;

public class MacroManagerSetting extends Setting<MacroManager> implements IMinecraft {
   public MacroManagerSetting(String name, String description) {
      super(name, description);
   }

   public MacroManager method449() {
      return Class27.getMacros();
   }

   public MacroManager method450() {
      return Class27.getMacros();
   }

   public MacroManager method451(MacroManager newVal) {
      return Class27.getMacros();
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      return tag;
   }

   public MacroManager method452(NbtCompound tag) {
      return Class27.getMacros();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method452(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method451((MacroManager)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method450();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method449();
   }
}
