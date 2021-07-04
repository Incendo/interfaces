package org.incendo.interfaces.paper.element;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.view.InventoryView;

import java.util.function.BiConsumer;

/**
 * A function that handles a click event on an interface.
 *
 * @param <T> the pane type
 */
public interface ClickHandler<T extends Pane> extends BiConsumer<InventoryClickEvent, InventoryView<T>> {

    /**
     * Returns a {@code ClickHandler} that cancels the event.
     *
     * @param <T> pane type
     * @return the handler
     */
    static @NonNull <T extends Pane> ClickHandler<T> cancel() {
        return (event, view) -> event.setCancelled(true);
    }

}
