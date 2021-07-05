package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class ImmutableDelegatingInterfaceArgument implements InterfaceArgument {

    private final @NonNull InterfaceArgument backingArgument;

    ImmutableDelegatingInterfaceArgument(final @NonNull InterfaceArgument interfaceArgument) {
        this.backingArgument = interfaceArgument;
    }

    @Override
    public <T> @Nullable T get(@NonNull final String key) {
        return this.backingArgument.get(key);
    }

    @Override
    public <T> @NonNull T getOrDefault(
            @NonNull final String key,
            @NonNull final T def
    ) {
        return this.backingArgument.getOrDefault(key, def);
    }

    @Override
    public boolean contains(@NonNull final String key) {
        return this.backingArgument.contains(key);
    }

}
