package dev.boze.client.utils;

public interface MCServer {
    default boolean method2115() {
        return this.method2117() || this.method2118();
    }

    default boolean method2116() {
        return this.method2115() || this.method222();
    }

    boolean method2117();

    boolean method2118();

    boolean method222();
}
