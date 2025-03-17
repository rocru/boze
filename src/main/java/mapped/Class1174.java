package mapped;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.manager.ColorManager;
import dev.boze.client.utils.misc.ISerializable;
import net.minecraft.nbt.NbtCompound;

public class Class1174 extends ColorManager implements ISerializable<Class1174> {
   private boolean field49;
   private int field50;
   private float field51;
   private float field52;
   public final boolean field53;
   public final int field54;
   public final float field55;
   public final float field56;

   public Class1174(BozeDrawColor color, boolean fastRender, int radius, float glowSize, float maxGlow) {
      this(color, false, fastRender, radius, glowSize, maxGlow);
   }

   public Class1174(BozeDrawColor color, boolean sync, boolean fastRender, int radius, float glowSize, float maxGlow) {
      super(color, sync);
      this.field49 = fastRender;
      this.field50 = radius;
      this.field51 = glowSize;
      this.field52 = maxGlow;
      this.field53 = fastRender;
      this.field54 = radius;
      this.field55 = glowSize;
      this.field56 = maxGlow;
   }

   public boolean method2115() {
      return this.field49;
   }

   public void method206(boolean fastRender) {
      this.field49 = var1223;
   }

   public int method2010() {
      return this.field50;
   }

   public void method1649(int radius) {
      this.field50 = var1224;
   }

   public float method1384() {
      return this.field51;
   }

   public void method207(float glowSize) {
      this.field51 = var1225;
   }

   public float method1385() {
      return this.field52;
   }

   public void method2325(float maxGlow) {
      this.field52 = var1226;
   }

   public Class1174 method2326() {
      return new Class1174(this.method1374().copy(), this.method2114(), this.field49, this.field50, this.field51, this.field52);
   }

   @Override
   public NbtCompound toTag() {
      NbtCompound var4 = new NbtCompound();
      var4.put("color", this.method1374().toTag());
      var4.putBoolean("sync", this.method2114());
      var4.putBoolean("fastRender", this.field49);
      var4.putInt("radius", this.field50);
      var4.putFloat("glowSize", this.field51);
      var4.putFloat("maxGlow", this.field52);
      return var4;
   }

   @Override
   public Class1174 fromTag(NbtCompound tag) {
      this.method1374().fromTag(var1227.getCompound("color"));
      this.method67(var1227.getBoolean("sync"));
      this.field49 = var1227.getBoolean("fastRender");
      this.field50 = var1227.getInt("radius");
      this.field51 = var1227.getFloat("glowSize");
      this.field52 = var1227.getFloat("maxGlow");
      return this;
   }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object fromTag(NbtCompound nbtCompound) {
   //   return this.method2327(nbtCompound);
   //}
}
