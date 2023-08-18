package org.incendo.interfaces.core.transform;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

class InterfacePropertyImpl<T> implements InterfaceProperty<T> {

    private final Collection<Pair<WeakReference<Object>, TriConsumer<Object, T, T>>> updateListeners =
            ConcurrentHashMap.newKeySet();
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

        var iterator = this.updateListeners.iterator();
        while (iterator.hasNext()) {
            // Check if the reference has been garbage collected or not
            final Pair<WeakReference<Object>, TriConsumer<Object, T, T>> pair = iterator.next();
            var object = pair.getFirst().get();
            if (object == null) {
                iterator.remove();
                continue;
            }
            pair.getSecond().accept(object, oldValue, this.value);
        }
    }

    @Override
    public <O> void addListener(final O reference, @NonNull final TriConsumer<O, T, T> consumer) {
        this.updateListeners.removeIf(f -> f.getFirst().get() == null);
        this.updateListeners.add(
                new Pair<>(
                        new WeakReference<>(reference),
                        (TriConsumer<Object, T, T>) consumer
                )
        );
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
