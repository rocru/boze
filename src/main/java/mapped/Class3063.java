package mapped;

public class Class3063 {
   private final String field155;

   public Class3063(String name) {
      this.field155 = name;
   }

   public String method5992() {
      return this.field155;
   }

   public boolean equals(Object input) {
      return !(input instanceof Class3063 var5) ? false : var5.field155.equals(this.field155);
   }
}
