package dev.boze.client.settings;

import dev.boze.client.shaders.ShaderProcessor;
import mapped.Class1147;
import mapped.Class1174;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;

public class ShaderSetting extends Setting<String> {
   public static final ArrayList<ShaderSetting> field962 = new ArrayList();
   public final ShaderProcessor field963;
   private Class1147 field964;
   private Class1174 field965;

   public ShaderSetting(ShaderProcessor shader, String name, Class1147 fill, Class1174 outline, String description) {
      super(name, description);
      this.field963 = shader;
      this.field964 = fill;
      this.field965 = outline;
      field962.add(this);
   }

   public ShaderSetting(ShaderProcessor shader, String name, Class1147 fill, Class1174 outline, String description, Setting parent) {
      super(name, description, parent);
      this.field963 = shader;
      this.field964 = fill;
      this.field965 = outline;
      field962.add(this);
   }

   public boolean method2118() {
      return this.field964 != null;
   }

   public boolean method222() {
      return this.field965 != null;
   }

   public Class1147 method457() {
      return this.field964;
   }

   public Class1174 method458() {
      return this.field965;
   }

   public String method1322() {
      return "";
   }

   public String method1562() {
      this.field964.method1649(this.field964.field48);
      this.field964.method205(this.field964.field414.copy());
      this.field964.method67(this.field964.field415);
      this.field965.method206(this.field965.field53);
      this.field965.method1649(this.field965.field54);
      this.field965.method207(this.field965.field55);
      this.field965.method2325(this.field965.field56);
      this.field965.method205(this.field965.field414.copy());
      this.field965.method67(this.field965.field415);
      return "";
   }

   public String method1341(String newVal) {
      return "";
   }

   public void method459(ShaderSetting newSetting) {
      this.field964 = newSetting.field964;
      this.field965 = newSetting.field965;
      this.field963.setShaderSetting(this);
   }

   public ShaderSetting method460() {
      return new ShaderSetting(this.field963, this.name, this.field964.method2266(), this.field965.method2326(), this.desc);
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      NbtCompound var5 = new NbtCompound();
      if (this.method2118()) {
         var5.put("fill", this.field964.toTag());
      }

      if (this.method222()) {
         var5.put("outline", this.field965.toTag());
      }

      return var5;
   }

   public String method1286(NbtCompound tag) {
      if (this.method2118()) {
         this.field964.method2267(tag.getCompound("fill"));
      }

      if (this.method222()) {
         this.field965.method2327(tag.getCompound("outline"));
      }

      this.field963.setShaderSetting(this);
      return "";
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method1286(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method1341((String)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method1562();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method1322();
   }
}
