package netutil;

import java.util.concurrent.TimeUnit;

public class uselessloaderclass3 {
    public static boolean field3;
    private final uselessloaderclass1 field2;

    public uselessloaderclass3(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        this.field2 = new uselessloaderclass1(n);
    }

    public void method6() throws InterruptedException {
        this.field2.acquireSharedInterruptibly(1);
    }

    public boolean method7(final long duration, final TimeUnit timeUnit) throws InterruptedException {
        return this.field2.tryAcquireSharedNanos(1, timeUnit.toNanos(duration));
    }

    public void method8() {
        this.field2.releaseShared(1);
    }

    public long method9() {
        return this.field2.a();
    }

    public void method10(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        this.field2.a(n);
    }

    @Override
    public String toString() {
        return super.toString() + "[Count = " + this.field2.a() + "]";
    }
}
