package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A key used as a reference to a value in a {@link InterfaceArguments} instance
 *
 * @param <T> value type
 */
@SuppressWarnings("unused")
public interface ArgumentKey<T> {

    /**
     * Returns a new argument key using the given key and type
     *
     * @param key  the key
     * @param type the type
     * @param <T>  the generic type
     * @return the argument key
     */
    static <T> @NonNull ArgumentKey<T> of(
            final @NonNull String key,
            final @NonNull Class<T> type
    ) {
        return new ArgumentKeyImpl<>(key, type);
    }

    /**
     * Returns a new object argument key using the given key
     *
     * @param key  the key
     * @return the argument key
     */
    static @NonNull ArgumentKey<Object> of(
            final @NonNull String key
    ) {
        return new ArgumentKeyImpl<>(key, Object.class);
    }

    /**
     * The name of the key
     *
     * @return key name
     */
    @NonNull String key();

    /**
     * The type represented by the key
     *
     * @return key type
     */
    @NonNull Class<T> type();

}
