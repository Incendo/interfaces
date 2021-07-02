package org.incendo.interfaces.paper.view;

import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An InterfaceView containing a Bukkit inventory.
 */
public interface InventoryView<T extends Pane> extends InterfaceView<T, PlayerViewer, Interface<T, PlayerViewer>>,
        InventoryHolder {

    /**
     * Returns the inventory.
     *
     * @return the inventory
     */
    @NonNull Inventory inventory();

}
