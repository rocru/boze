package mapped;

import netutil.Count;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public final class Class5904 extends AbstractQueuedSynchronizer {
    private static final long serialVersionUID = 4982264981922014374L;

    public Class5904(final int state) {
        super();
        this.setState(state);
    }

    public int method2010() {
        return this.getState();
    }

    public void method1649(final int state) {
        this.setState(state);
    }

    @Override
    protected int tryAcquireShared(final int n) {
        final boolean field4012 = Count.field4012;
        final int state = this.getState();
        if (!field4012 && state != 0) {
        }
        return state;
    }

    @Override
    protected boolean tryReleaseShared(final int n) {
        final boolean field4012 = Count.field4012;
        int compareAndSetState;
        boolean b;
        while (true) {
            final int state = this.getState();
            int n3;
            if (state == 0) {
                final int n2 = n3 = 0;
                if (!field4012) {
                    return n2 != 0;
                }
            } else {
                n3 = state - 1;
            }
            final int update = n3;
            b = ((compareAndSetState = (this.compareAndSetState(state, update) ? 1 : 0)) != 0);
            if (field4012) {
                break;
            }
            if (b) {
                final int n4;
                compareAndSetState = (n4 = update);
                break;
            }
        }
        if (!field4012) {
            if (!b) {
                compareAndSetState = 1;
            } else {
                compareAndSetState = 0;
            }
        }
        return compareAndSetState != 0;
    }
}