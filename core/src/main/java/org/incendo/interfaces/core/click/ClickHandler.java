package org.incendo.interfaces.core.click;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;

import java.util.function.Consumer;

/**
 * A function that handles a click event on an interface.
 *
 * @param <T> the context type
 * @param <U> the click cause
 * @param <V> the context type
 */
public interface ClickHandler<T extends Pane, U, V extends ClickContext<T, U>> extends Consumer<V> {

    /**
     * Returns a {@code ClickHandler} that cancels the event and then calls
     * the given click handler.
     *
     * @param clickHandler the handler
     * @param <T>          the pane type
     * @param <U>          the click cause type
     * @param <V>          the context type
     * @return the handler
     */
    static @NonNull <T extends Pane, U, V extends ClickContext<T, U>> ClickHandler<T, U, V> canceling(
            final @NonNull ClickHandler<T, U, V> clickHandler
    ) {
        return (ctx) -> {
            ctx.cancel(true);
            clickHandler.accept(ctx);
        };
    }

    /**
     * Returns a {@code ClickHandler} that cancels the event.
     *
     * @param <T> the pane type
     * @param <U> the click cause type
     * @param <V> the context type
     * @return the handler
     */
    static @NonNull <T extends Pane, U, V extends ClickContext<T, U>> ClickHandler<T, U, V> cancel() {
        return (ctx) -> ctx.cancel(true);
    }

}
