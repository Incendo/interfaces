package org.incendo.interfaces.core.transform;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

class InterfacePropertyImpl<T> implements InterfaceProperty<T> {

    private final Collection<BiConsumer<T, T>> updateListeners = new CopyOnWriteArrayList<>();
    private T value;

    InterfacePropertyImpl(final T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void set(final T value) {
        T oldValue = this.value;
        this.value = value;

        for (final BiConsumer<T, T> consumer : this.updateListeners) {
            consumer.accept(oldValue, this.value);
        }
    }

    @Override
    public void addListener(
            final @NonNull BiConsumer<T, T> consumer
    ) {
        this.updateListeners.add(consumer);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InterfacePropertyImpl<?> that = (InterfacePropertyImpl<?>) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

}
