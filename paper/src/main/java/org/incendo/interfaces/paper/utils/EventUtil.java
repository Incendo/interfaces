package org.incendo.interfaces.paper.utils;

import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.lang.reflect.Field;

@DefaultQualifier(NonNull.class)
public final class EventUtil {

    private static final Field EVENT_IS_ASYNC_FIELD;

    static {
        Field field;
        try {
            try {
                field = Event.class.getDeclaredField("async");
            } catch (final NoSuchFieldException e0) {
                try {
                    field = Event.class.getDeclaredField("isAsync");
                } catch (final NoSuchFieldException e1) {
                    e1.addSuppressed(e0);
                    throw e1;
                }
            }
            field.setAccessible(true);
        } catch (final Throwable thr) {
            throw new RuntimeException("Failed to find async/isAsync field in Event class", thr);
        }
        EVENT_IS_ASYNC_FIELD = field;
    }

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
            EVENT_IS_ASYNC_FIELD.set(event, async);
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

}
