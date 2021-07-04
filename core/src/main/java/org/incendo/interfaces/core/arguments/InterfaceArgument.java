package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Holds arguments passed into an interface.
 */
public interface InterfaceArgument {

    /**
     * Returns an immutable wrapper of {@link InterfaceArgument}.
     *
     * @param interfaceArgument the instance to wrap
     * @return immutable wrapper
     */
    static @NonNull InterfaceArgument immutable(final @NonNull InterfaceArgument interfaceArgument) {
        return new ImmutableDelegatingInterfaceArgument(interfaceArgument);
    }

    /**
     * Returns the value at the given key.
     *
     * @param key the key
     * @param <T> the value's type
     * @return the value
     */
    <T> @Nullable T get(@NonNull String key);

    /**
     * Returns the value at the given key.
     *
     * @param key the key
     * @param <T> the value's type
     * @param def the default object
     * @return the value
     */
    <T> @NonNull T getOrDefault(@NonNull String key, @NonNull T def);

    /**
     * Returns whether the given key is stored in this argument.
     *
     * @param key the key
     * @return whether the key is stored in this argument
     */
    boolean contains(@NonNull String key);


}
