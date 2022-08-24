package org.incendo.interfaces.paper.view;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
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
import org.incendo.interfaces.paper.pane.CombinedPane;
import org.incendo.interfaces.paper.type.CombinedInterface;
import org.incendo.interfaces.paper.utils.PaperUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The view of a chest.
 */
@SuppressWarnings("unused")
public final class CombinedView extends PaperView<CombinedInterface, CombinedPane, Inventory>
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
    public CombinedView(
            final @NonNull CombinedInterface backing,
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
    public CombinedView(
            final @Nullable PlayerView<?> parent,
            final @NonNull CombinedInterface backing,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments arguments,
            final @NonNull Component title
    ) {
        super(viewer, arguments, backing);

        this.parent = parent;
        this.title = title;

        this.finishConstruction();
    }

    @Override
    protected void applyInventory(final boolean firstOpen) {
        // Double check that the player is actually viewing this view.
        if (!this.isOpen(firstOpen)) {
            return;
        }

        Map<Vector2, ItemStackElement<CombinedPane>> elements = this.pane.inventoryElements();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.backing.chestRows(); y++) {
                Vector2 position = Vector2.at(x, y);
                int slot = PaperUtils.gridToSlot(position);

                Element currentElement = this.current.get(position);
                ItemStackElement<CombinedPane> element = elements.get(position);

                if (element.equals(currentElement)) {
                    continue;
                }

                this.current.put(position, element);
                this.inventory.setItem(slot, element.itemStack());
            }
        }

        this.reapplyPlayerInventory(firstOpen);
    }

    @Override
    protected CombinedPane createPane() {
        return new CombinedPane(this.backing.totalRows());
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

    private void reapplyPlayerInventory(final boolean firstOpen) {
        // Double check that the player is actually viewing this view.
        if (!this.isOpen(firstOpen)) {
            return;
        }

        Map<Vector2, ItemStackElement<CombinedPane>> elements = this.pane.inventoryElements();
        Inventory playerInventory = this.viewer.player().getOpenInventory().getBottomInventory();

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = this.backing.chestRows(); y < this.backing.totalRows() - 1; y++) {
                Vector2 position = Vector2.at(x, y);

                int playerY = y - this.backing.chestRows() + 1;
                int gridSlot = PaperUtils.gridToSlot(Vector2.at(x, playerY));

                ItemStack currentItem = playerInventory.getItem(gridSlot);
                ItemStackElement<CombinedPane> element = elements.get(position);

                if (element.itemStack().equals(currentItem)) {
                    continue;
                }

                playerInventory.setItem(gridSlot, element.itemStack());
            }
        }

        ItemStackElement<CombinedPane>[] hotbar = this.pane.hotbarElements();

        for (int x = 0; x < hotbar.length; x++) {
            ItemStack currentElement = playerInventory.getItem(x);
            ItemStackElement<CombinedPane> element = hotbar[x];

            if (element.itemStack().equals(currentElement)) {
                continue;
            }

            playerInventory.setItem(x, hotbar[x].itemStack());
        }
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
        this.reapplyPlayerInventory(true);
        this.emitEvent();
    }

    @Override
    public @NonNull CombinedPane pane() {
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
                this.backing.chestRows() * 9,
                this.title
        );
    }

    @Override
    public boolean updates() {
        return this.backing().updates();
    }

    @Override
    protected @NonNull CombinedPane mergePanes() {
        ItemStackElement<CombinedPane> empty = ItemStackElement.empty();
        CombinedPane finalPane = new CombinedPane(this.backing.totalRows());

        List<ContextCompletedPane<CombinedPane>> completedPanes = new ArrayList<>(this.panes);

        completedPanes.sort(Comparator.comparingInt(pane -> pane.context().priority()));

        for (final var completedPane : completedPanes) {
            Map<Vector2, ItemStackElement<CombinedPane>> elements = completedPane.pane().inventoryElements();

            for (Vector2 position : elements.keySet()) {
                ItemStackElement<CombinedPane> value = elements.get(position);

                if (!value.equals(empty)) {
                    finalPane = finalPane.element(value, position.x(), position.y());
                }
            }

            ItemStackElement<CombinedPane>[] hotbar = completedPane.pane().hotbarElements();

            for (int x = 0; x < hotbar.length; x++) {
                ItemStackElement<CombinedPane> value = hotbar[x];

                if (!value.equals(empty)) {
                    finalPane = finalPane.hotbar(value, x);
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
