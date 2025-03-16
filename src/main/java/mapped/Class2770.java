package mapped;

import net.minecraft.client.gui.DrawContext;

public abstract class Class2770 {
   protected double field68;
   protected double field69;
   protected double field70;
   protected double field71;
   protected final Class2770[] field72;
   protected boolean field73 = false;

   public Class2770(Class2770... children) {
      this.field72 = children;
   }

   public double method5410() {
      return this.field68;
   }

   public double method5411() {
      return this.field69;
   }

   public double method5412() {
      return this.field70;
   }

   public double method5413() {
      return this.field71;
   }

   public boolean method5414() {
      return true;
   }

   public boolean method5415() {
      return this.method5414();
   }

   public boolean method5416() {
      return this.method5414();
   }

   public boolean method5417(int mouseX, int mouseY) {
      if (!this.method5414()) {
         boolean var11 = this.field73;
         this.field73 = false;
         return var11;
      } else {
         boolean var6 = !this.field73;
         this.field73 = true;

         for (Class2770 var10 : this.field72) {
            if (var10.method5417(var2786, var2787)) {
               var6 = true;
            }
         }

         return var6;
      }
   }

   public void method5418(DrawContext context, int mouseX, int mouseY) {
      if (this.method5415()) {
         for (Class2770 var10 : this.field72) {
            var10.method5418(var2788, var2789, var2790);
         }
      }
   }

   public void method5419(DrawContext context, int mouseX, int mouseY) {
      if (this.method5415()) {
         for (Class2770 var10 : this.field72) {
            var10.method5419(var2791, var2792, var2793);
         }
      }
   }

   public void method5420(DrawContext context, int mouseX, int mouseY) {
   }

   public boolean method5421(int mouseX, int mouseY, int button) {
      if (!this.method5416()) {
         return false;
      } else {
         for (Class2770 var10 : this.field72) {
            if (var10.method5421(var2797, var2798, var2799)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean method5422(int mouseX, int mouseY, int button) {
      if (!this.method5416()) {
         return false;
      } else {
         for (Class2770 var10 : this.field72) {
            if (var10.method5422(var2800, var2801, var2802)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean method5423(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
      if (!this.method5416()) {
         return false;
      } else {
         for (Class2770 var14 : this.field72) {
            if (var14.method5423(var2803, var2804, var2805, var2806, var2807)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean method5424(int mouseX, int mouseY, double scrollAmount) {
      if (!this.method5416()) {
         return false;
      } else {
         for (Class2770 var11 : this.field72) {
            if (var11.method5424(var2808, var2809, var2810)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean method5425(int keyCode, int scanCode, int modifiers) {
      if (!this.method5416()) {
         return false;
      } else {
         for (Class2770 var10 : this.field72) {
            if (var10.method5425(var2811, var2812, var2813)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean method5426(int keyCode, int scanCode, int modifiers) {
      if (!this.method5416()) {
         return false;
      } else {
         for (Class2770 var10 : this.field72) {
            if (var10.method5426(var2814, var2815, var2816)) {
               return true;
            }
         }

         return false;
      }
   }
}
