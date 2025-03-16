package dev.boze.client.utils;

import mapped.Class5903;

public class ColorWrapper {
   public final String field3909;
   public final Class5903<?> field3910;
   public float field3911;
   public float field3912;

   public ColorWrapper(Class5903<?> color, float fillOpacity, float outlineOpacity) {
      this("_default", color, fillOpacity, outlineOpacity);
   }

   public ColorWrapper(String name, Class5903<?> color, float fillOpacity, float outlineOpacity) {
      this.field3909 = name;
      this.field3910 = color;
      this.field3911 = fillOpacity;
      this.field3912 = outlineOpacity;
   }

   public ColorWrapper method2135() {
      return new ColorWrapper(this.field3909, this.field3910, this.field3911, this.field3912);
   }

   // $VF: synthetic method
   // $VF: bridge method
   public Object clone() throws CloneNotSupportedException {
      return this.method2135();
   }
}
