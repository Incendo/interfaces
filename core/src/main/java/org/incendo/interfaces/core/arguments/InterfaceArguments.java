package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Holds arguments passed into an interface.
 */
public interface InterfaceArguments {

    /**
     * Returns an immutable wrapper of {@link InterfaceArguments}.
     *
     * @param interfaceArguments the instance to wrap
     * @return immutable wrapper
     */
    static @NonNull InterfaceArguments immutable(final @NonNull InterfaceArguments interfaceArguments) {
        return new ImmutableDelegatingInterfaceArguments(interfaceArguments);
    }

    /**
     * Returns the value at the given key.
     *
     * @param key the key
     * @param <T> the value's type
     * @return the value
     */
    <T> T get(@NonNull ArgumentKey<T> key);

    /**
     * Returns the value at the given key.
     *
     * @param key the key
     * @param <T> the value's type
     * @param def the default object
     * @return the value
     */
    <T> @NonNull T getOrDefault(@NonNull ArgumentKey<T> key, @NonNull T def);

    /**
     * Returns whether the given key is stored in this argument.
     *
     * @param key the key
     * @return whether the key is stored in this argument
     */
    boolean contains(@NonNull ArgumentKey<?> key);

}
