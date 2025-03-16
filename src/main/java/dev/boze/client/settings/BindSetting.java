package dev.boze.client.settings;

import dev.boze.client.utils.Bind;
import java.util.function.BooleanSupplier;
import net.minecraft.nbt.NbtCompound;

public class BindSetting extends Setting<Bind> {
   private final Bind bind;
   private final Bind defaultBind;

   public BindSetting(String name, Bind value, String description) {
      super(name, description);
      this.bind = value;
      this.defaultBind = value;
   }

   public BindSetting(String name, Bind value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.bind = value;
      this.defaultBind = value;
   }

   public BindSetting(String name, Bind value, String description, Setting parent) {
      super(name, description, parent);
      this.bind = value;
      this.defaultBind = value;
   }

   public BindSetting(String name, Bind value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.bind = value;
      this.defaultBind = value;
   }

   public Bind method476() {
      return this.bind;
   }

   public Bind method179() {
      return this.bind.copy(this.defaultBind);
   }

   public Bind rebind(Bind newVal) {
      return this.bind.copy(newVal);
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.put("Keybind", this.bind.toTag());
      return tag;
   }

   public Bind method180(NbtCompound tag) {
      if (tag.contains("Keybind")) {
         this.bind.method180(tag.getCompound("Keybind"));
      }

      return this.bind;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method180(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.rebind((Bind)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method179();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method476();
   }
}
