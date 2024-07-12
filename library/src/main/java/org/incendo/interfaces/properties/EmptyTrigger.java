package org.incendo.interfaces.properties;

import java.util.function.Consumer;

public class EmptyTrigger implements Trigger {

    public static final EmptyTrigger EMPTY_TRIGGER = new EmptyTrigger();

    @Override
    public void trigger() {
        // no implementation
    }

    @Override
    public <T> void addListener(final T reference, final Consumer<T> listener) {
        // no implementation
    }

}
