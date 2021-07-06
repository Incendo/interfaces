package org.incendo.interfaces.paper.type;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.view.InventoryView;

import java.util.function.BiConsumer;

/**
 * A function that handles a close event on an interface.
 *
 * @param <T> the pane type
 */
public interface CloseHandler<T extends Pane> extends BiConsumer<InventoryCloseEvent, InventoryView<T>> {

}
