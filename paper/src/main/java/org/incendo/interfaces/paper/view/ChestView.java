package org.incendo.interfaces.paper.view;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.utils.PaperUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The view of a chest.
 */
@SuppressWarnings("unused")
public final class ChestView extends PaperView<ChestInterface, ChestPane, Inventory>
        implements TaskableView, SelfUpdatingInterfaceView, ChildView {

    private final @Nullable PlayerView<?> parent;
    private final @NonNull Component title;

    private final Set<Integer> tasks = new HashSet<>();


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
        super(viewer, arguments, backing);

        this.parent = parent;
        this.title = title;

        this.finishConstruction();
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

    @Override
    protected void applyInventory(final boolean firstOpen) {
        // Double check that the player is actually viewing this view.
        if (!this.isOpen(firstOpen)) {
            return;
        }

        Map<Vector2, ItemStackElement<ChestPane>> elements = this.pane.chestElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.rows(); y++) {
                Vector2 position = Vector2.at(x, y);
                int slot = PaperUtils.gridToSlot(position);

                final @Nullable Element currentElement = this.current.get(position);
                final @NonNull ItemStackElement<ChestPane> element = elements.get(position);

                if (element.equals(currentElement)) {
                    continue;
                }

                this.current.put(position, element);
                this.inventory.setItem(slot, element.itemStack());
            }
        }
    }

    @Override
    protected ChestPane createPane() {
        return new ChestPane(this.backing.rows());
    }

    @Override
    public @NonNull PlayerView<?> back() {
        if (this.hasParent()) {
            this.parent.open();
            return this.parent;
        }

        throw new NullPointerException("The view has no parent");
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
    public boolean viewing() {
        return this.inventory.getViewers().contains(this.viewer.player());
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

    @Override
    protected @NonNull Inventory createInventory() {
        return Bukkit.createInventory(
                this,
                this.backing.rows() * 9,
                this.title
        );
    }

    @Override
    public boolean updates() {
        return this.backing().updates();
    }

    @Override
    protected @NonNull ChestPane mergePanes() {
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
