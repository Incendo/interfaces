package org.incendo.interfaces.paper.view;

import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * The view of a Bukkit inventory-based interface.
 */
public class ChestView implements InventoryView<ChestPane> {

    private final @NonNull PlayerViewer viewer;
    private final @NonNull ChestInterface parent;
    private final @NonNull Inventory inventory;
    private final @NonNull InterfaceArgument argument;
    private final @NonNull ChestPane pane;

    /**
     * Constructs {@code InventoryInterfaceView}.
     *
     * @param viewer   the viewer
     * @param parent   the parent interface
     * @param argument the interface argument
     */
    public ChestView(
            final @NonNull ChestInterface parent,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArgument argument
    ) {
        this.viewer = viewer;
        this.parent = parent;
        this.argument = argument;

        @NonNull ChestPane pane = new ChestPane(parent.rows());

        for (final var transform : this.parent.transformations()) {
            pane = transform.apply(pane, this);
        }

        this.pane = pane;

        this.inventory = this.createInventory();
    }

    /**
     * Converts a Bukkit slot index to an x/y position.
     *
     * @param slot the slot
     * @return the x/y position
     */
    public static int[] slotToGrid(final int slot) {
        return new int[]{slot % 9, slot / 9};
    }

    /**
     * Converts the x/y position to a Bukkit slot index.
     *
     * @param x the x position
     * @param y the y position
     * @return the slot
     */
    public static int gridToSlot(final int x, final int y) {
        return y * 9 + x;
    }

    /**
     * Creates the Bukkit inventory.
     *
     * @return the inventory
     */
    private @NonNull Inventory createInventory() {
        final @NonNull Inventory inventory = Bukkit.createInventory(
                this,
                this.parent.rows() * 9,
                this.parent.title()
        );

        final @NonNull List<List<ItemStackElement>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.parent.rows(); y++) {
                final @NonNull ItemStackElement element = elements.get(x).get(y);

                inventory.setItem(gridToSlot(x, y), element.itemStack());
            }
        }

        return inventory;
    }

    /**
     * Returns the parent.
     *
     * @return the parent
     */
    @Override
    public @NonNull ChestInterface parent() {
        return this.parent;
    }

    /**
     * Returns the viewer.
     *
     * @return the viewer
     */
    @Override
    public @NonNull PlayerViewer viewer() {
        return this.viewer;
    }

    /**
     * Returns true if {@link #viewer()} is viewing this view, false if not.
     *
     * @return true if {@link #viewer()} is viewing this view, false if not
     */
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
     * Opens this view to the viewer.
     */
    @Override
    public void open() {
        this.viewer.open(this);
    }

    /**
     * Returns this view's pane.
     *
     * @return the view's pane
     */
    @Override
    public @NonNull ChestPane pane() {
        return this.pane;
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
