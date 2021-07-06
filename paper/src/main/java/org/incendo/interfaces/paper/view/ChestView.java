package org.incendo.interfaces.paper.view;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.utils.PaperUtils;

import java.util.List;

/**
 * The view of a chest.
 */
public final class ChestView implements
        PlayerView<ChestPane>,
        ChildView {

    private final @NonNull PlayerViewer viewer;
    private final @NonNull ChestInterface backing;
    private final @Nullable PlayerView<?> parent;
    private final @NonNull Inventory inventory;
    private final @NonNull InterfaceArgument argument;
    private final @NonNull ChestPane pane;
    private final @NonNull Component title;

    /**
     * Constructs {@code ChestView}.
     *
     * @param backing  the backing interface
     * @param viewer   the viewer
     * @param argument the interface argument
     * @param title    the title
     */
    public ChestView(
            final @NonNull ChestInterface backing,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArgument argument,
            final @NonNull Component title
    ) {
        this(null, backing, viewer, argument, title);
    }

    /**
     * Constructs {@code ChestView}.
     *
     * @param parent   the parent view
     * @param backing  the backing interface
     * @param viewer   the viewer
     * @param argument the interface argument
     * @param title    the title
     */
    public ChestView(
            final @Nullable PlayerView<?> parent,
            final @NonNull ChestInterface backing,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArgument argument,
            final @NonNull Component title
    ) {
        this.parent = parent;
        this.viewer = viewer;
        this.backing = backing;
        this.argument = argument;
        this.title = title;

        @NonNull ChestPane pane = new ChestPane(this.backing.rows());

        for (final var transform : this.backing.transformations()) {
            pane = transform.apply(pane, this);
        }

        this.pane = pane;

        this.inventory = this.createInventory();
    }

    @Override
    public boolean hasParent() {
        return this.parent != null;
    }

    @Override
    public @Nullable PlayerView<?> parent() {
        return this.parent;
    }


    @Override
    public @NonNull ChestInterface backing() {
        return this.backing;
    }

    @Override
    public @NonNull PlayerViewer viewer() {
        return this.viewer;
    }

    @Override
    public boolean viewing() {
        return this.inventory.getViewers().contains(this.viewer.player());
    }

    @Override
    public @NonNull InterfaceArgument argument() {
        return this.argument;
    }

    @Override
    public void open() {
        this.viewer.open(this);
    }

    @Override
    public @NonNull ChestPane pane() {
        return this.pane;
    }

    @Override
    public @NonNull Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Creates the Bukkit inventory.
     *
     * @return the inventory
     */
    private @NonNull Inventory createInventory() {
        final @NonNull Inventory inventory = Bukkit.createInventory(
                this,
                this.backing.rows() * 9,
                this.title
        );

        final @NonNull List<List<ItemStackElement>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.rows(); y++) {
                final @NonNull ItemStackElement element = elements.get(x).get(y);

                inventory.setItem(PaperUtils.gridToSlot(x, y), element.itemStack());
            }
        }

        return inventory;
    }

}
