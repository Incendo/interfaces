package dev.kscott.interfaces.paper.view;

import dev.kscott.interfaces.core.view.InterfaceView;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An InterfaceView containing a Bukkit inventory.
 */
public interface InventoryView extends InterfaceView, InventoryHolder {

    /**
     * Returns the inventory.
     *
     * @return the inventory
     */
    @NonNull Inventory inventory();

}
