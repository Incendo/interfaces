package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A mutable variant of {@link InterfaceArguments}.
 */
@SuppressWarnings("unused")
public interface MutableInterfaceArguments extends InterfaceArguments {

    /**
     * Sets a value of the argument.
     *
     * @param key   the key
     * @param value the value
     * @param <T>   the type
     */
    <T> void set(
            @NonNull ArgumentKey<T> key,
            T value
    );

    /**
     * Returns an immutable opy of this interface argument.
     *
     * @return immutable copy
     */
    default @NonNull InterfaceArguments asImmutable() {
        return InterfaceArguments.immutable(this);
    }

}
