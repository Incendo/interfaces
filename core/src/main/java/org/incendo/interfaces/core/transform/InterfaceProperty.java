package org.incendo.interfaces.core.transform;

import java.util.function.BiConsumer;
import org.checkerframework.checker.nullness.qual.NonNull;

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
        return new DummyInterfaceProperty();
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
    void addListener(@NonNull BiConsumer<T, T> consumer);

    class DummyInterfaceProperty implements InterfaceProperty<Object> {

        private final Object object = new Object();

        @Override
        public @NonNull Object get() {
            return this.object;
        }

        @Override
        public void set(final Object value) {
            throw new UnsupportedOperationException("Cannot update a dummy interface property");
        }

        @Override
        public void addListener(
                final @NonNull BiConsumer<Object, Object> consumer
        ) {
        }

    }

}
