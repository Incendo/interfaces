package dev.kscott.interfaces.paper.view;

import dev.kscott.interfaces.core.arguments.InterfaceArgument;
import dev.kscott.interfaces.paper.pane.ChestPane;
import dev.kscott.interfaces.core.view.InterfaceViewer;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The view of a Bukkit inventory-based interface.
 */
public class ChestView implements PaperInventoryView {

    /**
     * The viewer.
     */
    private final @NonNull PlayerInterfaceViewer viewer;

    /**
     * The inventory.
     */
    private final @NonNull Inventory inventory;

    /**
     * The argument.
     */
    private final @NonNull InterfaceArgument argument;

    /**
     * The chest pane.
     */
    private final @NonNull ChestPane pane;

    /**
     * Constructs {@code InventoryInterfaceView}.
     *
     * @param viewer the viewer
     */
    public ChestView(
            final @NonNull PlayerInterfaceViewer viewer,
            final @NonNull InterfaceArgument argument
    ) {
        this.viewer = viewer;
        this.argument = argument;
    }

    @Override
    public @NonNull InterfaceViewer viewer() {
        return this.viewer;
    }

    @Override
    public boolean viewing() {
        return this.inventory.getViewers().contains(this.viewer.player());
    }

    /**
     * Returns the argument provided to this view.
     *
     * @return the argument
     */
    @Override
    public @NonNull InterfaceArgument argument() {
        return this.argument;
    }

    /**
     * Returns the inventory of this view.
     *
     * @return the inventory
     */
    public @NonNull Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Returns the inventory of this view.
     *
     * @return the inventory
     */
    @Override
    public @NonNull Inventory inventory() {
        return this.inventory;
    }

}
