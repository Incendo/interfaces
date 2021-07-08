package org.incendo.interfaces.core.transform;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.checkerframework.checker.nullness.qual.NonNull;

class InterfacePropertyImpl<T> implements InterfaceProperty<T> {

    private final Collection<BiConsumer<T, T>> updateListeners = new HashSet<>();
    private T value;

    InterfacePropertyImpl(final @NonNull T value) {
        this.value = value;
    }

    @Override
    public @NonNull T get() {
        return this.value;
    }

    @Override
    public void set(@NonNull final T value) {
        T oldValue = this.value;
        this.value = value;

        for (final BiConsumer<T, T> consumer : this.updateListeners) {
            consumer.accept(oldValue, this.value);
        }
    }

    @Override
    public void addListener(
            @NonNull final BiConsumer<T, T> consumer
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
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

}
