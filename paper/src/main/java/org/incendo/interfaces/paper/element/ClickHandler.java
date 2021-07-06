package org.incendo.interfaces.paper.element;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.view.PlayerView;

import java.util.function.BiConsumer;

/**
 * A function that handles a click event on an interface.
 *
 * @param <T> the pane type
 * @param <U> the view type
 */
public interface ClickHandler<T extends Pane, U extends PlayerView<T>> extends BiConsumer<InventoryClickEvent, U> {

    static @NonNull <T extends Pane, U extends PlayerView<T>> ClickHandler<T, U> of(BiConsumer<InventoryClickEvent, U> consumer) {
        return (ClickHandler<T, U>) consumer;
    }

    /**
     * Returns a {@code ClickHandler} that cancels the event and then calls
     * the given click handler.
     *
     * @param clickHandler the handler
     * @param <T>          the pane type
     * @return the handler
     */
    static @NonNull <T extends Pane, U extends PlayerView<T>> ClickHandler<T, U> canceling(final @NonNull ClickHandler<T, U> clickHandler) {
        return (event, view) -> {
            event.setCancelled(true);

            clickHandler.accept(event, view);
        };
    }

    /**
     * Returns a {@code ClickHandler} that cancels the event.
     *
     * @param <T> the pane type
     * @param <U> the view type
     * @return the handler
     */
    static @NonNull <T extends Pane, U extends PlayerView<T>> ClickHandler<T, U> cancel() {
        return (event, view) -> event.setCancelled(true);
    }

}
