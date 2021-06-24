package dev.kscott.interfaces.paper.view;

import dev.kscott.interfaces.core.arguments.InterfaceArgument;
import dev.kscott.interfaces.core.view.InterfaceViewer;
import dev.kscott.interfaces.paper.element.ItemStackElement;
import dev.kscott.interfaces.paper.pane.ChestPane;
import dev.kscott.interfaces.paper.type.ChestInterface;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * The view of a Bukkit inventory-based interface.
 */
public class ChestView implements InventoryView {

    /**
     * Converts a Bukkit slot index to an x/y position.
     *
     * @param slot the slot
     * @return the x/y position
     */
    public static int[] slotToGrid(int slot) {
        return new int[]{slot % 9, slot / 9};
    }


    /**
     * Converts the x/y position to a Bukkit slot index.
     *
     * @param x the x position
     * @param y the y position
     * @return the slot
     */
    public static int gridToSlot(int x, int y) {
        return y * 9 + x;
    }

    /**
     * The viewer.
     */
    private final @NonNull PlayerViewer viewer;

    /**
     * The parent interface.
     */
    private final @NonNull ChestInterface parent;

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
     * @param viewer          the viewer
     * @param parent the parent interface
     * @param argument        the interface argument
     */
    public ChestView(
            final @NonNull ChestInterface parent,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArgument argument
    ) {
        this.viewer = viewer;
        this.parent = parent;
        this.argument = argument;

        this.pane = new ChestPane(parent.rows());

        for (final var transform : this.parent.transformations()) {
            transform.apply(this.pane, this);
        }

        this.inventory = this.createInventory();
    }

    /**
     * Creates the Bukkit inventory.
     *
     * @return the inventory
     */
    private @NonNull Inventory createInventory() {
        final @NonNull Inventory inventory = Bukkit.createInventory(
                this,
                this.parent.rows(),
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

    @Override
    public void open() {
        this.viewer.open(this);
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
