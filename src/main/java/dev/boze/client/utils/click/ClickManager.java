package dev.boze.client.utils.click;

import dev.boze.client.enums.ClickMethod;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.IntSetting;

public class ClickManager {
   private final EnumSetting<ClickMethod> field3933;
   private final IntArraySetting field3934;
   private final IntSetting field3935;
   private final IntSetting field3936;
   private final FloatSetting field3937;
   private final IClickMethod[] field3938;

   public ClickManager(EnumSetting<ClickMethod> clicking, IntSetting minCps, IntSetting maxCps, FloatSetting vanillaOffset) {
      this.field3933 = clicking;
      this.field3934 = null;
      this.field3935 = minCps;
      this.field3936 = maxCps;
      this.field3937 = vanillaOffset;
      this.field3938 = new IClickMethod[]{new ClickImplementationA(), new ClickImplementationB(vanillaOffset), new ClickImplementationC()};
   }

   public ClickManager(EnumSetting<ClickMethod> clicking, IntArraySetting cps, FloatSetting vanillaOffset) {
      this.field3933 = clicking;
      this.field3934 = cps;
      this.field3935 = null;
      this.field3936 = null;
      this.field3937 = vanillaOffset;
      this.field3938 = new IClickMethod[]{new ClickImplementationA(), new ClickImplementationB(vanillaOffset), new ClickImplementationC()};
   }

   public int method2171() {
      return this.field3938[this.field3933.method461().ordinal()].method578(this.method2173());
   }

   public void method2172() {
      this.field3938[this.field3933.method461().ordinal()].method938(this.method2173());
   }

   private double method2173() {
      if (this.field3934 != null) {
         return (double)this.field3934.method1376();
      } else {
         double var4 = (double)Math.min(this.field3935.method434(), this.field3936.method434());
         double var6 = (double)Math.max(this.field3935.method434(), this.field3936.method434());
         return var4 + (var6 - var4) * Math.random();
      }
   }
}
