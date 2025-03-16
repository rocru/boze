package dev.boze.client.utils.click;

import java.util.concurrent.ThreadLocalRandom;

public class ClickImplementationC extends IClickMethod {
   private int field1330 = 0;
   private long field1331 = System.currentTimeMillis();

   @Override
   public int method578(double targetedCPS) {
      if (ThreadLocalRandom.current().nextDouble(1.0) > Math.sqrt((double)this.field1330 + 4.0) / 3.0) {
         this.field1330++;
         return 0;
      } else {
         int var6 = (int)Math.round((double)(System.currentTimeMillis() - this.field1331) / (1000.0 / (targetedCPS + (double)this.field1330)));
         this.field1331 = this.field1331 + (long)((double)var6 * (1000.0 / (targetedCPS + (double)this.field1330)));
         this.field1330 = 0;
         return var6;
      }
   }

   @Override
   public void method938(double targetedCPS) {
      this.field1331 = System.currentTimeMillis() - (long)(1000.0 / targetedCPS);
      this.field1330 = 0;
   }
}
