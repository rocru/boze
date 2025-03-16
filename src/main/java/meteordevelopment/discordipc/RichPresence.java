package meteordevelopment.discordipc;

import com.google.gson.JsonObject;
import mapped.Class5937;
import mapped.Class5938;

public class RichPresence {
   private String field3997;
   private String field3998;
   private Class5937 field3999;
   private Class5938 field4000;

   public void method2291(String details) {
      this.field3997 = details;
   }

   public void method2292(String state) {
      this.field3998 = state;
   }

   public void method2293(String key, String text) {
      if (this.field3999 == null) {
         this.field3999 = new Class5937();
      }

      this.field3999.field244 = key;
      this.field3999.field245 = text;
   }

   public void method2294(String key, String text) {
      if (this.field3999 == null) {
         this.field3999 = new Class5937();
      }

      this.field3999.field246 = key;
      this.field3999.field247 = text;
   }

   public void method2295(long time) {
      if (this.field4000 == null) {
         this.field4000 = new Class5938();
      }

      this.field4000.field248 = time;
   }

   public void method2296(long time) {
      if (this.field4000 == null) {
         this.field4000 = new Class5938();
      }

      this.field4000.field249 = time;
   }

   public JsonObject method2297() {
      JsonObject var4 = new JsonObject();
      if (this.field3997 != null) {
         var4.addProperty("details", this.field3997);
      }

      if (this.field3998 != null) {
         var4.addProperty("state", this.field3998);
      }

      if (this.field3999 != null) {
         JsonObject var5 = new JsonObject();
         if (this.field3999.field244 != null) {
            var5.addProperty("large_image", this.field3999.field244);
         }

         if (this.field3999.field245 != null) {
            var5.addProperty("large_text", this.field3999.field245);
         }

         if (this.field3999.field246 != null) {
            var5.addProperty("small_image", this.field3999.field246);
         }

         if (this.field3999.field247 != null) {
            var5.addProperty("small_text", this.field3999.field247);
         }

         var4.add("assets", var5);
      }

      if (this.field4000 != null) {
         JsonObject var6 = new JsonObject();
         if (this.field4000.field248 != null) {
            var6.addProperty("start", this.field4000.field248);
         }

         if (this.field4000.field249 != null) {
            var6.addProperty("end", this.field4000.field249);
         }

         var4.add("timestamps", var6);
      }

      return var4;
   }
}
