package dev.boze.client.systems.modules.client;

import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.ConfigCategory;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.render.color.ChangingColor;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import java.util.HashMap;
import java.util.Map.Entry;
import mapped.Class5903;
import mapped.Class5916;
import net.minecraft.nbt.NbtCompound;

public class Colors extends Module {
   public static final Colors INSTANCE = new Colors();
   public final HashMap<String, Class5903<?>> field2343 = new HashMap();

   private Colors() {
      super("Colors", "Manage client colors", Category.Client, ConfigCategory.Visuals);
      this.method219(this::lambda$new$0);
   }

   @Override
   public boolean setEnabled(boolean newState) {
      return false;
   }

   public boolean method1335(String color) {
      Class5903 var5 = (Class5903)this.field2343.get(color);
      if (var5 != null) {
         var5.method2142();
         this.field2343.remove(color);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public NbtCompound toTag() {
      NbtCompound var4 = super.toTag();
      NbtCompound var5 = new NbtCompound();

      for (Entry var7 : this.field2343.entrySet()) {
         var5.put((String)var7.getKey(), ((Class5903)var7.getValue()).toTag());
      }

      var4.put("colors", var5);
      return var4;
   }

   @Override
   public Module method235(NbtCompound tag) {
      if (tag.contains("colors")) {
         NbtCompound var5 = tag.getCompound("colors");

         for (String var7 : var5.getKeys()) {
            NbtCompound var8 = var5.getCompound(var7);
            int var9 = var8.getInt("type");
            if (var9 == 0) {
               this.field2343.put(var7, new StaticColor(var8));
            } else if (var9 == 1) {
               this.field2343.put(var7, new ChangingColor(var8));
            } else if (var9 == 2) {
               this.field2343.put(var7, new GradientColor(var7, var8));
            }
         }
      }

      return super.method235(tag);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object fromTag(NbtCompound nbtCompound) {
      return this.method235(nbtCompound);
   }

   private ScaledBaseComponent lambda$new$0() {
      return new Class5916("Manage Colors", this.field2343);
   }
}
