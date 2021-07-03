package org.incendo.interfaces.paper.element;

import org.incendo.interfaces.paper.view.InventoryView;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;

/**
 * A function that handles a click event on an interface.
 */
public interface ClickHandler extends BiConsumer<InventoryClickEvent, InventoryView> {

    /**
     * Returns a {@code ClickHandler} that cancels the event.
     *
     * @return the handler
     */
    static @NonNull ClickHandler cancel() {
        return (event, view) -> event.setCancelled(true);
    }

}
