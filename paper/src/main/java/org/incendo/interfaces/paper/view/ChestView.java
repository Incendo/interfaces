package org.incendo.interfaces.paper.view;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.transform.InterruptUpdateException;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.type.ChildTitledInterface;
import org.incendo.interfaces.paper.utils.PaperUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private final @NonNull InterfaceArguments arguments;
    private final @NonNull Component title;
    private @NonNull ChestPane pane;
    private @NonNull Inventory inventory;

    private final @NonNull Map<Integer, Element> current = new HashMap<>();
    private final @NonNull List<ContextCompletedPane<ChestPane>> panes = new ArrayList<>();
    private final Set<Integer> tasks = new HashSet<>();

    private final Plugin plugin;

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

        try {
            this.pane = this.updatePane(true);
        } catch (final InterruptUpdateException ignored) {
            this.pane = new ChestPane(this.backing.rows());
        }

        this.plugin = JavaPlugin.getProvidingPlugin(this.getClass());

        if (Bukkit.isPrimaryThread()) {
            this.inventory = this.createInventory();
        } else {
            try {
                Bukkit.getScheduler().callSyncMethod(this.plugin, () -> {
                    this.inventory = this.createInventory();

                    return null;
                }).get();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private @NonNull ChestPane updatePane(final boolean firstApply) {
        for (final var transform : this.backing.transformations()) {
            ChestPane newPane = transform.transform().apply(new ChestPane(this.backing.rows()), this);
            // If it's the first time we apply the transform, then
            // we add update listeners to all the dependent properties
            if (firstApply) {
                for (final InterfaceProperty<?> property : transform.properties()) {
                    property.addListener((oldValue, newValue) -> this.updateByProperty(property));
                }
            }

            this.panes.removeIf(completedPane -> completedPane.context().equals(transform));
            this.panes.add(new ContextCompletedPane<>(transform, newPane));
        }

        return this.mergePanes();
    }

    private @NonNull ChestPane updatePaneByProperty(final @NonNull InterfaceProperty<?> interfaceProperty) {
        for (final var transform : this.backing.transformations()) {
            if (!transform.properties().contains(interfaceProperty)) {
                continue;
            }

            ChestPane newPane = transform.transform().apply(new ChestPane(this.backing.rows()), this);

            this.panes.removeIf(completedPane -> completedPane.context().equals(transform));
            this.panes.add(new ContextCompletedPane<>(transform, newPane));
        }

        return this.mergePanes();
    }

    private void updateByProperty(final @NonNull InterfaceProperty<?> interfaceProperty) {
        try {
            this.pane = this.updatePaneByProperty(interfaceProperty);
        } catch (final InterruptUpdateException ignored) {
            return;
        }
        this.reApplySync();
    }

    private boolean isOpen(final boolean firstOpen) {
        if (firstOpen) {
            return true;
        }

        final InventoryView open = this.viewer.player().getOpenInventory();
        final Inventory topInventory = open.getTopInventory();
        final InventoryHolder inventoryHolder = topInventory.getHolder();
        return this.equals(inventoryHolder);
    }

    private void reapplyInventory(final boolean firstOpen) {
        // Double check that the player is actually viewing this view.
        if (!this.isOpen(firstOpen)) {
            return;
        }

        Map<Vector2, ItemStackElement<ChestPane>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.rows(); y++) {
                Vector2 position = Vector2.at(x, y);
                int slot = PaperUtils.gridToSlot(position);

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

    @Override
    public @NonNull <C extends PlayerView<?>> C openChild(
            final @NonNull Interface<?, PlayerViewer> backing,
            final @NonNull InterfaceArguments argument
    ) {
        InterfaceView<?, PlayerViewer> view = backing.open(this, argument);

        @SuppressWarnings("unchecked")
        C typedView = (C) view;
        return typedView;
    }

    @Override
    public <C extends PlayerView<?>> @NonNull C openChild(
            @NonNull final ChildTitledInterface<?, PlayerViewer> backing,
            @NonNull final InterfaceArguments argument,
            @NonNull final Component title
    ) {
        InterfaceView<?, PlayerViewer> view = backing.open(this, argument, title);

        @SuppressWarnings("unchecked")
        C typedView = (C) view;
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
        if (!this.viewer.player().isOnline()) {
            return;
        }

        try {
            this.pane = this.updatePane(false);
        } catch (final InterruptUpdateException ignored) {
            return;
        }
        this.reApplySync();
    }

    private void reApplySync() {
        if (Bukkit.isPrimaryThread()) {
            this.reapplyInventory(false);
        } else {
            try {
                Bukkit.getScheduler().callSyncMethod(this.plugin, () -> {
                    this.reapplyInventory(false);

                    return null;
                }).get();
            } catch (final Exception e) {
                throw new RuntimeException(e);
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
                Vector2 position = Vector2.at(x, y);
                int slot = PaperUtils.gridToSlot(position);
                ItemStackElement<ChestPane> element = elements.get(position);

                this.current.put(slot, element);
                inventory.setItem(slot, element.itemStack());
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

        this.panes.sort(Comparator.comparingInt(pane -> pane.context().priority()));

        for (final var completedPane : this.panes) {
            Map<Vector2, ItemStackElement<ChestPane>> elements = completedPane.pane().chestElements();

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
        BukkitRunnable bukkitRunnable = new BukkitNestedRunnable(this.tasks, runnable);
        bukkitRunnable.runTaskLater(plugin, delay);
        this.tasks.add(bukkitRunnable.getTaskId());
    }

    @Override
    public Collection<Integer> taskIds() {
        return this.tasks;
    }

}
