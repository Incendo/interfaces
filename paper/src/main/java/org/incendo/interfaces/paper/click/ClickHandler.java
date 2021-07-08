package org.incendo.interfaces.paper.click;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.view.PlayerView;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A function that handles a click event on an interface.
 *
 * @param <T> the context type
 */
public interface ClickHandler<T extends Pane> extends Consumer<ClickContext<T>> {

    /**
     * Returns a {@code ClickHandler} that cancels the event and then calls
     * the given click handler.
     *
     * @param clickHandler the handler
     * @param <T>          the pane type
     * @return the handler
     */
    static @NonNull <T extends Pane> ClickHandler<T> canceling(final @NonNull ClickHandler<T> clickHandler) {
        return (ctx) -> {
            ctx.cancel(true);
            clickHandler.accept(ctx);
        };
    }

    /**
     * Returns a {@code ClickHandler} that cancels the event.
     *
     * @param <T> the pane type
     * @return the handler
     */
    static @NonNull <T extends Pane> ClickHandler<T> cancel() {
        return (ctx) -> {
            ctx.cancel(true);
        };
    }

}
