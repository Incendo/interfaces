package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class ImmutableDelegatingInterfaceArguments implements InterfaceArguments {

    private final @NonNull InterfaceArguments backingArgument;

    ImmutableDelegatingInterfaceArguments(final @NonNull InterfaceArguments interfaceArguments) {
        this.backingArgument = interfaceArguments;
    }

    @Override
    public <T> @Nullable T get(final @NonNull ArgumentKey<T> key) {
        return this.backingArgument.get(key);
    }

    @Override
    public <T> @NonNull T getOrDefault(
            final @NonNull ArgumentKey<T> key,
            final @NonNull T def
    ) {
        return this.backingArgument.getOrDefault(key, def);
    }

    @Override
    public boolean contains(final @NonNull ArgumentKey<?> key) {
        return this.backingArgument.contains(key);
    }

}
