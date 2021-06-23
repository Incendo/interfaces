package dev.kscott.interfaces.core.arguments;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class HashMapInterfaceArgument implements InterfaceArgument {

    /**
     * The argument map.
     */
    private final @NonNull Map<String, Object> argumentMap;

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
        this.argumentMap = Map.copyOf(argumentMap);
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
        final @Nullable Object object = this.argumentMap.get(key);

        if (object == null) {
            throw new NullPointerException("The value at " + key + " cannot be null.");
        }

        return (T) object;
    }

    @Override
    public void set(final @NonNull String key, final @NonNull Object value) {

    }

}
