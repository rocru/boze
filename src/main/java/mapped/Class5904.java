package mapped;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import netutil.Count;

final class Class5904 extends AbstractQueuedSynchronizer {
   private static final long serialVersionUID = 4982264981922014374L;

   Class5904(int var1) {
      this.setState(var1);
   }

   int method2010() {
      return this.getState();
   }

   void method1649(int var1) {
      this.setState(var1);
   }

   protected int tryAcquireShared(int var1) {
      boolean var4 = Count.field4012;
      int var10000 = this.getState();
      if (!var4) {
         var10000 = var10000 == 0 ? 1 : -1;
      }

      return var10000;
   }

   protected boolean tryReleaseShared(int var1) {
      boolean var4 = Count.field4012;

      int var7;
      while (true) {
         int var5 = this.getState();
         if (var5 == 0) {
            var7 = 0;
            if (!var4) {
               return false;
            }
         } else {
            var7 = var5 - 1;
         }

         int var6 = var7;
         var7 = this.compareAndSetState(var5, var6);
         if (var4) {
            break;
         }

         if (var7 != 0) {
            var7 = var6;
            break;
         }
      }

      if (!var4) {
         var7 = var7 == 0 ? 1 : 0;
      }

      return (boolean)var7;
   }
}
