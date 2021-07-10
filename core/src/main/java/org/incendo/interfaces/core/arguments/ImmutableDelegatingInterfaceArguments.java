package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class ImmutableDelegatingInterfaceArguments implements InterfaceArguments {

    private final @NonNull InterfaceArguments backingArgument;

    ImmutableDelegatingInterfaceArguments(final @NonNull InterfaceArguments interfaceArguments) {
        this.backingArgument = interfaceArguments;
    }

    @Override
    public <T> @Nullable T get(@NonNull final ArgumentKey<T> key) {
        return this.backingArgument.get(key);
    }

    @Override
    public <T> @NonNull T getOrDefault(
            @NonNull final ArgumentKey<T> key,
            @NonNull final T def
    ) {
        return this.backingArgument.getOrDefault(key, def);
    }

    @Override
    public boolean contains(@NonNull final ArgumentKey<?> key) {
        return this.backingArgument.contains(key);
    }

}
