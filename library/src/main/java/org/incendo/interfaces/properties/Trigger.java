package org.incendo.interfaces.properties;

import java.util.function.Consumer;

public interface Trigger {
    void trigger();

    <T> void addListener(T reference, Consumer<T> listener);
}
