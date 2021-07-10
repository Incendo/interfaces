package org.incendo.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An InterfaceArgument backed by a HashMap. Accepts providers as arguments.
 */
@SuppressWarnings("unused")
public final class HashMapInterfaceArguments implements MutableInterfaceArguments {

    private final @NonNull Map<@NonNull String, @NonNull Supplier<?>> argumentMap;

    /**
     * Constructs {@code HashMapInterfaceArgument}.
     */
    public HashMapInterfaceArguments() {
        this.argumentMap = new HashMap<>();
    }

    /**
     * Constructs {@code HashMapInterfaceArgument}.
     * <p>
     * Changes to the provided map will not be reflected in the arguments.
     *
     * @param argumentMap the argument map
     */
    public HashMapInterfaceArguments(final @NonNull Map<ArgumentKey<?>, Supplier<?>> argumentMap) {
        this.argumentMap = new HashMap<>();
        for (final Map.Entry<ArgumentKey<?>, Supplier<?>> entry : argumentMap.entrySet()) {
            this.argumentMap.put(entry.getKey().key(), entry.getValue());
        }
    }

    /**
     * Returns an empty {@code HashMapInterfaceArgument}.
     *
     * @return an empty {@code HashMapInterfaceArgument}
     */
    public static @NonNull HashMapInterfaceArguments empty() {
        return new HashMapInterfaceArguments();
    }

    /**
     * Constructs a new argument builder.
     *
     * @param key   the key
     * @param value the value
     * @param <T>   the type
     * @return the argument
     */
    public static <T> HashMapInterfaceArguments.@NonNull Builder with(
            final @NonNull ArgumentKey<T> key,
            final T value
    ) {
        return new Builder().with(key, value);
    }

    /**
     * Constructs a new argument builder.
     *
     * @param key   the key
     * @param value the value supplier
     * @param <T>   the type
     * @return the argument
     */
    public static <T> HashMapInterfaceArguments.@NonNull Builder with(
            final @NonNull ArgumentKey<T> key,
            final @NonNull Supplier<T> value
    ) {
        return new Builder().with(key, value);
    }

    @Override
    public <T> T get(final @NonNull ArgumentKey<T> key) {
        final @Nullable Supplier<?> supplier = this.argumentMap.get(key.key());

        if (supplier == null) {
            throw new NullPointerException("The value at " + key + " cannot be null.");
        }

        @SuppressWarnings("unchecked") final T object = (T) supplier.get();

        return object;
    }

    @Override
    public <T> @NonNull T getOrDefault(
            final @NonNull ArgumentKey<T> key,
            final @NonNull T def
    ) {
        final @Nullable Supplier<?> supplier = this.argumentMap.get(key.key());

        if (supplier == null) {
            return def;
        }

        @SuppressWarnings("unchecked") final T object = (T) supplier.get();

        return object;
    }

    @Override
    public boolean contains(final @NonNull ArgumentKey<?> key) {
        return this.argumentMap.containsKey(key.key());
    }

    @Override
    public <T> void set(
            @NonNull final ArgumentKey<T> key,
            final T value
    ) {
        this.argumentMap.put(key.key(), () -> value);
    }

    /**
     * Sets the value supplier of the key.
     *
     * @param key      the key
     * @param supplier the value supplier
     * @param <T>      the type of the value
     */
    public <T> void set(
            final @NonNull ArgumentKey<T> key,
            final @NonNull Supplier<@NonNull T> supplier
    ) {
        this.argumentMap.put(key.key(), supplier);
    }

    /**
     * A utility class to build an InterfaceArgument.
     */
    public static class Builder {

        /**
         * The argument map.
         */
        private final @NonNull Map<ArgumentKey<?>, Supplier<?>> argumentMap;

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
         * @param <T>   the type
         * @return the builder
         */
        public <T> HashMapInterfaceArguments.@NonNull Builder with(
                final @NonNull ArgumentKey<T> key,
                final T value
        ) {
            this.argumentMap.put(key, () -> value);
            return this;
        }

        /**
         * Puts a value at the given key.
         *
         * @param key      the key
         * @param supplier the value
         * @param <T>   the type
         * @return the builder
         */
        public <T> HashMapInterfaceArguments.@NonNull Builder with(
                final @NonNull ArgumentKey<T> key,
                final @NonNull Supplier<T> supplier
        ) {
            this.argumentMap.put(key, supplier);
            return this;
        }

        /**
         * Builds the argument.
         *
         * @return the argument
         */
        public @NonNull HashMapInterfaceArguments build() {
            return new HashMapInterfaceArguments(this.argumentMap);
        }

    }

}
