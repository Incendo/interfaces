package dev.kscott.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds arguments passed into an interface.
 */
public interface InterfaceArgument {

    /**
     * Returns an empty {@code InterfaceArgument}.
     *
     * @return an empty {@code InterfaceArgument}
     */
    static @NonNull InterfaceArgument empty() {
        return new HashMapInterfaceArgument();
    }

    static InterfaceArgument.@NonNull Builder builder() {
        return new Builder();
    }

    /**
     * Returns the value at the given key.
     *
     * @param key the key
     * @param <T> the value's type
     * @return the value
     */
    <T> T get(final @NonNull String key);

    /**
     * Sets a value of the argument.
     *
     * @param key   the key
     * @param value the value
     */
    void set(final @NonNull String key, final @NonNull Object value);

    /**
     * A utility class to build an InterfaceArgument.
     */
    class Builder {

        /**
         * The argument map.
         */
        private final @NonNull Map<String, Object> argumentMap;

        /**
         * The builder.
         */
        public Builder() {
            this.argumentMap = new HashMap<>();
        }

        /**
         * Puts a value at the given key.
         *
         * @param key the key
         * @param value the value
         * @return the builder
         */
        public InterfaceArgument.@NonNull Builder with(final @NonNull String key, final @NonNull Object value) {
            this.argumentMap.put(key, value);
            return this;
        }

        public @NonNull HashMapInterfaceArgument asHashMapArgument() {
            return new HashMapInterfaceArgument(this.argumentMap);
        }

    }

}
