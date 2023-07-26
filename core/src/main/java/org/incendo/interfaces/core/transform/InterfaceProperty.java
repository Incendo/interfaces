package org.incendo.interfaces.core.transform;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;

public interface InterfaceProperty<T> {

    /**
     * Returns a new interface property with the given initial value
     *
     * @param value initial value
     * @param <T>   type of the value
     * @return the property
     */
    static <T> @NonNull InterfaceProperty<T> of(final T value) {
        return new InterfacePropertyImpl<>(value);
    }

    /**
     * Returns an interface property that never updates.
     *
     * @return the property
     */
    static @NonNull InterfaceProperty<Object> dummy() {
        return DummyInterfaceProperty.INSTANCE;
    }

    /**
     * Returns the current value of the property
     *
     * @return current value
     */
    T get();

    /**
     * Sets the new value of the property
     *
     * @param value new value
     */
    void set(T value);

    /**
     * Adds a listener that gets invoked whenever the property is updated
     *
     * @param consumer the consumer
     */
    <O> void addListener(O reference, @NonNull TriConsumer<O, T, T> consumer);
}
