package dev.boze.client.settings;

import dev.boze.client.Boze;
import dev.boze.client.manager.MacroManager;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.nbt.NbtCompound;

public class MacroManagerSetting extends Setting<MacroManager> implements IMinecraft {
   public MacroManagerSetting(String name, String description) {
      super(name, description);
   }

   @Override
   public MacroManager getValue() {
      return Boze.getMacros();
   }

   @Override
   public MacroManager resetValue() {
      return Boze.getMacros();
   }

   @Override
   public MacroManager setValue(MacroManager newVal) {
      return Boze.getMacros();
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      return tag;
   }

   @Override
   public MacroManager load(NbtCompound tag) {
      return Boze.getMacros();
   }

   // $VF: synthetic method
   // $VF: bridge method
  // @Override
  // public Object load(NbtCompound nbtCompound) {
  //    return this.method452(nbtCompound);
 //  }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
  // public Object setValue(Object object) {
 //     return this.method451((MacroManager)object);
 //  }
//
   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object resetValue() {
   //   return this.method450();
  // }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object getValue() {
  //    return this.method449();
  // }
}
