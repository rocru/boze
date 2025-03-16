package mapped;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.manager.ColorManager;
import dev.boze.client.utils.misc.ISerializable;
import net.minecraft.nbt.NbtCompound;

public class Class1147 extends ColorManager implements ISerializable<Class1147> {
   private int field47;
   public final int field48;

   public Class1147(BozeDrawColor color) {
      super(color);
      this.field48 = 0;
   }

   public Class1147(BozeDrawColor color, int blur) {
      this(color, false, blur);
   }

   public Class1147(BozeDrawColor color, boolean sync, int blur) {
      super(color, sync);
      this.field47 = blur;
      this.field48 = blur;
   }

   public boolean method2115() {
      return this.field47 > 0;
   }

   public int method2010() {
      return this.field47;
   }

   public void method1649(int blur) {
      this.field47 = var1196;
   }

   public Class1147 method2266() {
      return new Class1147(this.method1362().copy(), this.method2114(), this.field47);
   }

   @Override
   public NbtCompound toTag() {
      NbtCompound var3 = new NbtCompound();
      var3.put("color", this.method1374().toTag());
      var3.putBoolean("sync", this.method2114());
      var3.putInt("blur", this.field47);
      return var3;
   }

   public Class1147 method2267(NbtCompound tag) {
      this.method1374().fromTag(var1197.getCompound("color"));
      this.method67(var1197.getBoolean("sync"));
      this.field47 = var1197.getInt("blur");
      return this;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object fromTag(NbtCompound nbtCompound) {
      return this.method2267(nbtCompound);
   }
}
