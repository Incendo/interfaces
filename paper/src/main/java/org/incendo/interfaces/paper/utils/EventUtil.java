package org.incendo.interfaces.paper.utils;

import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.lang.reflect.Field;

@DefaultQualifier(NonNull.class)
public final class EventUtil {

    private EventUtil() {
    }

    /**
     * Set the private final async field for when an appropriate constructor isn't available.
     *
     * @param event event
     * @param async value
     */
    public static void setAsync(final Event event, final boolean async) {
        try {
            final Field field = Event.class.getDeclaredField("async");
            field.setAccessible(true);
            field.set(event, async);
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

}
