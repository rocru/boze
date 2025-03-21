package netutil;

import mapped.Class5904;

import java.util.concurrent.TimeUnit;

public class Count {
    private Class5904 field4011;
    public static boolean field4012;

    public Count(int var1) {
        super();
        boolean var10000 = field4012;
        boolean var4 = var10000;
        if (!var4) {
            if (var1 < 0) {
                throw new IllegalArgumentException("count < 0");
            }

            this.field4011 = new Class5904(var1);
        }
    }

    public void method2311() throws InterruptedException {
        this.field4011.acquireSharedInterruptibly(1);
    }

    public boolean method2312(long var1, TimeUnit var3) throws InterruptedException {
        return this.field4011.tryAcquireSharedNanos(1, var3.toNanos(var1));
    }

    public void method2313() {
        this.field4011.releaseShared(1);
    }

    public long method2314() {
        return this.field4011.method2010();
    }

    public void method2315(int var1) {
        boolean var4 = field4012;
        if (!var4) {
            if (var1 < 0) {
                throw new IllegalArgumentException("count < 0");
            }

            this.field4011.method1649(var1);
        }
    }

    public String toString() {
        return super.toString() + "[Count = " + this.field4011.method2010() + "]";
    }
}
