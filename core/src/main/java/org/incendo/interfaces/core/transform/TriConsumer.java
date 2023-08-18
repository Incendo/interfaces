package org.incendo.interfaces.core.transform;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * Accepts three values [t], [u] and [v].
     *
     * @param t the first value
     * @param u the second value
     * @param v the third value
     */
    void accept(T t, U u, V v);
}
