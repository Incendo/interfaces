package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

class ArgumentKeyImpl<T> implements ArgumentKey<T> {

    private final String key;
    private final Class<T> type;

    ArgumentKeyImpl(
            final @NonNull String key,
            final @NonNull Class<T> type
    ) {
        this.key = key;
        this.type = type;
    }

    @Override
    public @NonNull String key() {
        return this.key;
    }

    @Override
    public @NonNull Class<T> type() {
        return this.type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArgumentKeyImpl<?> that = (ArgumentKeyImpl<?>) o;
        return this.key.equals(that.key) && this.type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.type);
    }

}
