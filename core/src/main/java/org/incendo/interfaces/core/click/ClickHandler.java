package org.incendo.interfaces.core.click;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceViewer;

import java.util.function.Consumer;

/**
 * A function that handles a click event on an interface.
 *
 * @param <T> the context type
 * @param <U> the click cause
 * @param <V> the viewer type
 * @param <W> the context type
 */
public interface ClickHandler<T extends Pane, U, V extends InterfaceViewer, W extends ClickContext<T, U, V>> extends Consumer<W> {

    /**
     * Returns a {@code ClickHandler} that cancels the event and then calls
     * the given click handler.
     *
     * @param clickHandler the handler
     * @param <T> the context type
     * @param <U> the click cause
     * @param <V> the viewer type
     * @param <W> the context type
     * @return the handler
     */
    static @NonNull <T extends Pane, U, V extends InterfaceViewer,
            W extends ClickContext<T, U, V>> ClickHandler<T, U, V, W> canceling(
            final @NonNull ClickHandler<T, U, V, W> clickHandler
    ) {
        return (ctx) -> {
            ctx.status(ClickContext.ClickStatus.DENY);
            clickHandler.accept(ctx);
        };
    }

    /**
     * Returns a {@code ClickHandler} that cancels the event.
     *
     * @param <T> the context type
     * @param <U> the click cause
     * @param <V> the viewer type
     * @param <W> the context type
     * @return the handler
     */
    static @NonNull <T extends Pane, U, V extends InterfaceViewer,
            W extends ClickContext<T, U, V>> ClickHandler<T, U, V, W> cancel() {
        return (ctx) -> ctx.status(ClickContext.ClickStatus.DENY);
    }

    /**
     * Returns a {@code ClickHandler} that does nothing.
     *
     * @param <T> the context type
     * @param <U> the click cause
     * @param <V> the viewer type
     * @param <W> the context type
     * @return the handler
     */
    static @NonNull <T extends Pane, U, V extends InterfaceViewer,
            W extends ClickContext<T, U, V>> ClickHandler<T, U, V, W> dummy() {
        return ctx -> {};
    }

    /**
     * Returns a new {@link ClickHandler} that first executes the current handler, and then
     * executes the other handler.
     *
     * @param other the other handler
     * @return a combination of this and the given click handler
     */
    default @NonNull ClickHandler<T, U, V, W> andThen(final @NonNull ClickHandler<T, U, V, W> other) {
        final ClickHandler<T, U, V, W> current = this;
        return (w) -> {
            current.accept(w);
            other.accept(w);
        };
    }

}
