package org.incendo.interfaces.paper.view;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.utils.PaperUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * The view of a chest.
 */
@SuppressWarnings("unused")
public final class ChestView implements
        PlayerView<ChestPane>,
        TaskableView,
        SelfUpdatingInterfaceView,
        ChildView {

    private final @NonNull PlayerViewer viewer;
    private final @NonNull ChestInterface backing;
    private final @Nullable PlayerView<?> parent;
    private final @NonNull Inventory inventory;
    private final @NonNull InterfaceArguments arguments;
    private final @NonNull Component title;
    private @NonNull ChestPane pane;

    private final @NonNull Map<Integer, Element> current = new HashMap<>();
    private final @NonNull Map<Integer, ChestPane> panes = new HashMap<>();
    private final Collection<Integer> tasks = new HashSet<>();

    /**
     * Constructs {@code ChestView}.
     *
     * @param backing   the backing interface
     * @param viewer    the viewer
     * @param arguments the interface argument
     * @param title     the title
     */
    public ChestView(
            final @NonNull ChestInterface backing,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments arguments,
            final @NonNull Component title
    ) {
        this(null, backing, viewer, arguments, title);
    }

    /**
     * Constructs {@code ChestView}.
     *
     * @param parent    the parent view
     * @param backing   the backing interface
     * @param viewer    the viewer
     * @param arguments the interface argument
     * @param title     the title
     */
    public ChestView(
            final @Nullable PlayerView<?> parent,
            final @NonNull ChestInterface backing,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments arguments,
            final @NonNull Component title
    ) {
        this.parent = parent;
        this.viewer = viewer;
        this.backing = backing;
        this.arguments = arguments;
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
        for (final var transform : this.backing.transformations()) {
            ChestPane currentPane = this.panes.getOrDefault(transform.priority(), new ChestPane(this.backing.rows()));
            ChestPane newPane = transform.transform().apply(currentPane, this);
            // If it's the first time we apply the transform, then
            // we add update listeners to all the dependent properties
            if (firstApply) {
                transform.property().addListener((oldValue, newValue) -> this.updateByProperty(transform.property()));
            }

            this.panes.put(transform.priority(), newPane);
        }

        return this.mergePanes();
    }

    private @NonNull ChestPane updatePaneByProperty(final InterfaceProperty<?> interfaceProperty) {
        for (final var transform : this.backing.transformations()) {
            if (transform.property() != interfaceProperty) {
                continue;
            }

            ChestPane currentPane = this.panes.getOrDefault(transform.priority(), new ChestPane(this.backing.rows()));
            ChestPane newPane = transform.transform().apply(currentPane, this);

            this.panes.put(transform.priority(), newPane);
        }

        return this.mergePanes();
    }

    private void updateByProperty(final InterfaceProperty<?> interfaceProperty) {
        this.pane = this.updatePaneByProperty(interfaceProperty);
        this.reapplyInventory();
    }

    private void reapplyInventory() {
        final Map<Vector2, ItemStackElement<ChestPane>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.rows(); y++) {
                Vector2 position = Vector2.at(x, y);
                int slot = PaperUtils.gridToSlot(x, y);

                final @Nullable Element currentElement = this.current.get(slot);
                final @NonNull ItemStackElement<ChestPane> element = elements.get(position);

                if (element.equals(currentElement)) {
                    continue;
                }

                this.current.put(slot, element);
                this.inventory.setItem(slot, element.itemStack());
            }
        }
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

        @SuppressWarnings("unchecked")
        final T typedView = (T) view;
        return typedView;
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
        this.reapplyInventory();

        final @NonNull Map<Vector2, ItemStackElement<ChestPane>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.rows(); y++) {
                final @Nullable Element currentElement = this.current.get(PaperUtils.gridToSlot(x, y));
                final @NonNull ItemStackElement<ChestPane> element = elements.get(Vector2.at(x, y));

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
        return this.arguments;
    }

    @Override
    public void open() {
        this.viewer.open(this);
        this.emitEvent();
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

        final @NonNull Map<Vector2, ItemStackElement<ChestPane>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.rows(); y++) {
                final @NonNull ItemStackElement<ChestPane> element = elements.get(Vector2.at(x, y));

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

    private @NonNull ChestPane mergePanes() {
        ItemStackElement<ChestPane> empty = ItemStackElement.empty();
        ChestPane finalPane = new ChestPane(this.backing.rows());

        List<Integer> keys = new ArrayList<>(this.panes.keySet());
        Collections.sort(keys);

        for (final int key : keys) {
            Map<Vector2, ItemStackElement<ChestPane>> elements = this.panes.get(key).chestElements();

            for (Vector2 position : elements.keySet()) {
                ItemStackElement<ChestPane> value = elements.get(position);

                if (!value.equals(empty)) {
                    finalPane = finalPane.element(value, position.x(), position.y());
                }
            }
        }

        return finalPane;
    }

    @Override
    public void addTask(final @NonNull Plugin plugin, final @NonNull Runnable runnable, final int delay) {
        BukkitRunnable bukkitRunnable = new NestedRunnable(runnable);
        bukkitRunnable.runTaskLater(plugin, delay);
        this.tasks.add(bukkitRunnable.getTaskId());
    }

    @Override
    public Collection<Integer> taskIds() {
        return this.tasks;
    }

    private final class NestedRunnable extends BukkitRunnable {

        private final Runnable runnable;

        private NestedRunnable(final @NonNull Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            this.runnable.run();
            ChestView.this.tasks.remove(this.getTaskId());
        }
    }

}
