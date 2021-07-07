package org.incendo.interfaces.paper.view;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The view of a Bukkit inventory-based interface.
 */
public final class ChestView implements InventoryView<ChestPane>, SelfUpdatingInterfaceView {

    private final @NonNull PlayerViewer viewer;
    private final @NonNull ChestInterface parent;
    private final @NonNull Inventory inventory;
    private final @NonNull InterfaceArgument argument;
    private final @NonNull Component title;
    private @NonNull ChestPane pane;

    private final @NonNull Map<Integer, Element> current = new HashMap<>();

    /**
     * Constructs {@code InventoryInterfaceView}.
     *
     * @param viewer   the viewer
     * @param parent   the parent interface
     * @param argument the interface argument
     * @param title    the title
     */
    public ChestView(
            final @NonNull ChestInterface parent,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArgument argument,
            final @NonNull Component title
    ) {
        this.viewer = viewer;
        this.parent = parent;
        this.argument = argument;
        this.title = title;
        this.pane = this.updatePane(true);

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

    private @NonNull ChestPane updatePane(final boolean firstApply) {
        @NonNull ChestPane pane = new ChestPane(this.parent.rows());

        for (final var transform : this.parent.transformations()) {
            pane = transform.transform().apply(pane, this);

            // If it's the first time we apply the transform, then
            // we add update listeners to all the dependent properties
            if (firstApply) {
                transform.property().addListener((oldValue, newValue) -> this.update());
            }
        }

        return pane;
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
                this.title
        );

        final @NonNull List<List<ItemStackElement>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.parent.rows(); y++) {
                final @NonNull ItemStackElement element = elements.get(x).get(y);

                this.current.put(gridToSlot(x, y), element);
                inventory.setItem(gridToSlot(x, y), element.itemStack());
            }
        }

        return inventory;
    }

    @Override
    public void update() {
        this.pane = this.updatePane(false);

        final @NonNull List<List<ItemStackElement>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.parent.rows(); y++) {
                final @Nullable Element currentElement = this.current.get(gridToSlot(x, y));
                final @NonNull ItemStackElement element = elements.get(x).get(y);

                if (element.equals(currentElement)) {
                    continue;
                }

                this.current.put(gridToSlot(x, y), element);
                this.inventory.setItem(gridToSlot(x, y), element.itemStack());
            }
        }
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
