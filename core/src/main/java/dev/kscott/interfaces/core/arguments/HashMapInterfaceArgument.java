package dev.kscott.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An InterfaceArgument backed by a HashMap. Accepts providers as arguments.
 */
public class HashMapInterfaceArgument implements InterfaceArgument {

    /**
     * The argument map.
     */
    private final @NonNull Map<String, Supplier<Object>> argumentMap;

    /**
     * Constructs {@code HashMapInterfaceArgument}.
     */
    public HashMapInterfaceArgument() {
        this.argumentMap = new HashMap<>();
    }

    /**
     * Constructs {@code HashMapInterfaceArgument}.
     * <p>
     * Changes to the provided map will not be reflected in the arguments.
     *
     * @param argumentMap the argument map
     */
    public HashMapInterfaceArgument(final @NonNull Map<String, Object> argumentMap) {
        this.argumentMap = new HashMap<>();

        for (final var entry : argumentMap.entrySet()) {
            this.argumentMap.put(entry.getKey(), entry::getValue);
        }
    }

    /**
     * Returns an empty {@code HashMapInterfaceArgument}.
     *
     * @return an empty {@code HashMapInterfaceArgument}
     */
    public static @NonNull HashMapInterfaceArgument empty() {
        return new HashMapInterfaceArgument();
    }

    /**
     * Constructs a new argument builder.
     *
     * @param key   the key
     * @param value the value
     * @return the argument
     */
    public static HashMapInterfaceArgument.@NonNull Builder with(
            final @NonNull String key,
            final @NonNull Object value
    ) {
        return new Builder().with(key, value);
    }

    /**
     * Constructs a new argument builder.
     *
     * @param key   the key
     * @param value the value supplier
     * @return the argument
     */
    public static HashMapInterfaceArgument.@NonNull Builder with(
            final @NonNull String key,
            final @NonNull Supplier<Object> value
    ) {
        return new Builder().with(key, value);
    }

    /**
     * Returns the value of the given key. Will throw an exception if there is no value at key.
     *
     * @param key the key
     * @param <T> the value's type
     * @return the value
     * @throws NullPointerException if the key's value is null
     */
    @Override
    public <T> T get(final @NonNull String key) {
        final @Nullable Supplier<Object> supplier = this.argumentMap.get(key);

        if (supplier == null) {
            throw new NullPointerException("The value at " + key + " cannot be null.");
        }

        return (T) supplier.get();
    }

    /**
     * Sets the value of the key.
     *
     * @param key   the key
     * @param value the value
     */
    @Override
    public void set(final @NonNull String key, final @NonNull Object value) {
        this.argumentMap.put(key, () -> value);
    }

    /**
     * Sets the value supplier of the key.
     *
     * @param key   the key
     * @param supplier the value supplier
     */
    public void set(final @NonNull String key, final @NonNull Supplier<Object> supplier) {
        this.argumentMap.put(key, supplier);
    }

    /**
     * A utility class to build an InterfaceArgument.
     */
    public static class Builder {

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
         * @param key   the key
         * @param value the value
         * @return the builder
         */
        public HashMapInterfaceArgument.@NonNull Builder with(final @NonNull String key, final @NonNull Object value) {
            this.argumentMap.put(key, value);
            return this;
        }

        public @NonNull HashMapInterfaceArgument build() {
            return new HashMapInterfaceArgument(this.argumentMap);
        }

    }

}
