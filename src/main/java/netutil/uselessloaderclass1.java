package netutil;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

final class uselessloaderclass1 extends AbstractQueuedSynchronizer {
    private static final long field1 = 4982264981922014374L;

    uselessloaderclass1(final int state) {
        this.setState(state);
    }

    int a() {
        return this.getState();
    }

    void a(final int state) {
        this.setState(state);
    }

    @Override
    protected int tryAcquireShared(final int n) {
        return (this.getState() == 0) ? 1 : -1;
    }

    @Override
    protected boolean tryReleaseShared(final int n) {
        while (true) {
            final int state = this.getState();
            if (state == 0) {
                return false;
            }
            final int update = state - 1;
            if (this.compareAndSetState(state, update)) {
                return update == 0;
            }
        }
    }
}
