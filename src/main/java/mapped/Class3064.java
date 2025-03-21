package mapped;

import java.util.ArrayDeque;
import java.util.Queue;

public class Class3064<T> {
    private final Queue<T> field156 = new ArrayDeque();
    private final Class3065<T> field157;

    public Class3064(Class3065<T> producer) {
        this.field157 = producer;
    }

    public synchronized T method5993() {
        return this.field156.size() > 0 ? this.field156.poll() : this.field157.method5995();
    }

    public synchronized void method5994(T obj) {
        this.field156.offer(obj);
    }
}
