package org.incendo.interfaces.paper.view;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.utils.PaperUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The view of a chest.
 */
public final class ChestView implements
        PlayerView<ChestPane>,
        SelfUpdatingInterfaceView,
        ChildView {

    private final @NonNull PlayerViewer viewer;
    private final @NonNull ChestInterface backing;
    private final @Nullable PlayerView<?> parent;
    private final @NonNull Inventory inventory;
    private final @NonNull InterfaceArguments argument;
    private final @NonNull Component title;
    private @NonNull ChestPane pane;

    private final @NonNull Map<Integer, Element> current = new HashMap<>();

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
            final @NonNull InterfaceArguments argument,
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
            final @NonNull InterfaceArguments argument,
            final @NonNull Component title
    ) {
        this.parent = parent;
        this.viewer = viewer;
        this.backing = backing;
        this.argument = argument;
        this.title = title;
        this.pane = this.updatePane(true);

        this.inventory = this.createInventory();
    }

    /**
     * Opens a child interface.
     *
     * @param backing the backing interface
     * @param <T>     the type of view
     * @return the view
     */
    public @NonNull <T extends PlayerView<?>> T openChild(final @NonNull Interface<?, PlayerViewer> backing) {
        return this.openChild(backing, HashMapInterfaceArguments.empty());
    }


    private @NonNull ChestPane updatePane(final boolean firstApply) {
        @NonNull ChestPane pane = new ChestPane(this.backing.rows());

        for (final var transform : this.backing.transformations()) {
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
     * Opens a child interface.
     *
     * @param backing  the backing interface
     * @param argument the argument
     * @param <T>      the type of view
     * @return the view
     */
    public @NonNull <T extends PlayerView<?>> T openChild(
            final @NonNull Interface<?, PlayerViewer> backing,
            final @NonNull InterfaceArguments argument
    ) {
        final InterfaceView<?, PlayerViewer> view = backing.open(this, argument);

        view.open();

        return (T) view;
    }

    @Override
    public @NonNull PlayerView<?> back() {
        if (this.hasParent()) {
            this.parent.open();
            return this.parent;
        }

        throw new NullPointerException("The view has no parent");
    }

    @Override
    public void update() {
        this.pane = this.updatePane(false);

        final @NonNull List<List<ItemStackElement<ChestPane>>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.rows(); y++) {
                final @Nullable Element currentElement = this.current.get(PaperUtils.gridToSlot(x, y));
                final @NonNull ItemStackElement<ChestPane> element = elements.get(x).get(y);

                if (element.equals(currentElement)) {
                    continue;
                }

                this.current.put(PaperUtils.gridToSlot(x, y), element);
                this.inventory.setItem(PaperUtils.gridToSlot(x, y), element.itemStack());
            }
        }
    }

    /**
     * Returns the parent.
     *
     * @return the parent
     */
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
    public @NonNull InterfaceArguments arguments() {
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

        final @NonNull List<List<ItemStackElement<ChestPane>>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.rows(); y++) {
                final @NonNull ItemStackElement<ChestPane> element = elements.get(x).get(y);

                this.current.put(PaperUtils.gridToSlot(x, y), element);
                inventory.setItem(PaperUtils.gridToSlot(x, y), element.itemStack());
            }
        }

        return inventory;
    }

    @Override
    public boolean updates() {
        return this.backing().updates();
    }

}
