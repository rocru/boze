package dev.boze.client.settings;

import dev.boze.client.manager.MacroManager;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.Boze;
import net.minecraft.nbt.NbtCompound;

public class MacroManagerSetting extends Setting<MacroManager> implements IMinecraft {
   public MacroManagerSetting(String name, String description) {
      super(name, description);
   }

   public MacroManager method449() {
      return Boze.getMacros();
   }

   public MacroManager method450() {
      return Boze.getMacros();
   }

   public MacroManager method451(MacroManager newVal) {
      return Boze.getMacros();
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      return tag;
   }

   public MacroManager method452(NbtCompound tag) {
      return Boze.getMacros();
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
