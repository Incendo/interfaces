package org.incendo.interfaces.core.transform;

import java.util.function.BiConsumer;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class DummyInterfaceProperty implements InterfaceProperty<Object> {

    static final InterfaceProperty<Object> INSTANCE = new DummyInterfaceProperty();

    private final Object object = new Object();

    private DummyInterfaceProperty() {
    }

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
