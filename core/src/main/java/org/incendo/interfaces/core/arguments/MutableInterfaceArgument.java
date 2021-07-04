package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A mutable variant of {@link InterfaceArgument}.
 */
@SuppressWarnings("unused")
public interface MutableInterfaceArgument extends InterfaceArgument {

    /**
     * Sets a value of the argument.
     *
     * @param key   the key
     * @param value the value
     */
    void set(@NonNull String key, @NonNull Object value);

    /**
     * Returns an immutable opy of this interface argument.
     *
     * @return immutable copy
     */
    default @NonNull InterfaceArgument asImmutable() {
        return InterfaceArgument.immutable(this);
    }

}
