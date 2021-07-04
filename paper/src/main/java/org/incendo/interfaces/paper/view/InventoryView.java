package org.incendo.interfaces.paper.view;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;

/**
 * An InterfaceView containing a Bukkit inventory.
 *
 * @param <T> the pane type
 */
public interface InventoryView<T extends Pane> extends InterfaceView<T, PlayerViewer>,
        InventoryHolder {

    /**
     * Returns the inventory.
     *
     * @return the inventory
     */
    @NonNull Inventory inventory();

}
