package org.incendo.interfaces.paper.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.PlayerPane;
import org.incendo.interfaces.paper.type.PlayerInterface;
import org.incendo.interfaces.paper.utils.PaperUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class PlayerInventoryView extends PaperView<PlayerInterface, PlayerPane, PlayerInventory>
        implements SelfUpdatingInterfaceView {

    private static final Map<Player, PlayerInventoryView> INVENTORY_VIEW_MAP = new WeakHashMap<>();

    /**
     * Constructs {@code PlayerInventoryView}.
     *
     * @param backing  the backing interface
     * @param viewer   the viewer
     * @param argument the interface argument
     */
    public PlayerInventoryView(
            final @NonNull PlayerInterface backing,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments argument
    ) {
        super(viewer, argument, backing);

        // If an inventory exists for the player, we "close" the old one.
        final PlayerInventoryView oldView = INVENTORY_VIEW_MAP.remove(viewer.player());

        if (oldView != null) {
            oldView.close();
        }

        this.finishConstruction();

        // Store the new view.
        INVENTORY_VIEW_MAP.put(viewer.player(), this);
    }

    /**
     * Returns all the open player inventory views
     *
     * @return the views in a collection
     */
    public static @NonNull Collection<PlayerInventoryView> getAllAndClear() {
        Collection<PlayerInventoryView> values = new ArrayList<>(INVENTORY_VIEW_MAP.values());
        INVENTORY_VIEW_MAP.clear();

        return values;
    }

    /**
     * Returns the inventory view for the given player
     *
     * @param player the player
     * @return the view
     */
    public static @Nullable PlayerInventoryView forPlayer(final @NonNull Player player) {
        return INVENTORY_VIEW_MAP.get(player);
    }

    /**
     * Removes the inventory view mapping for the given player
     *
     * @param player the player
     */
    public static void removeForPlayer(final @NonNull Player player) {
        INVENTORY_VIEW_MAP.remove(player);
    }

    @Override
    protected void applyInventory(final boolean firstApply) {
        final @NonNull List<ItemStackElement<PlayerPane>> elements = this.pane.inventoryElements();

        boolean changed = false;

        for (int i = 0; i < elements.size(); i++) {
            Vector2 vector = PaperUtils.slotToGrid(i);
            final @Nullable Element currentElement = this.current.get(vector);
            final @NonNull ItemStackElement<PlayerPane> element = elements.get(i);

            if (element.equals(currentElement)) {
                continue;
            }

            this.current.put(vector, element);
            this.inventory.setItem(i, element.itemStack());

            changed = true;
        }

        if (changed && this.viewer.player().getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
            this.viewer.player().updateInventory();
        }
    }

    @Override
    protected PlayerPane createPane() {
        return new PlayerPane();
    }

    @Override
    protected PlayerInventory createInventory() {
        return this.viewer.player().getInventory();
    }

    @Override
    protected PlayerPane mergePanes() {
        ItemStackElement<PlayerPane> empty = ItemStackElement.empty();
        PlayerPane finalPane = new PlayerPane();

        List<ContextCompletedPane<PlayerPane>> completedPanes = new ArrayList<>(this.panes);

        completedPanes.sort(Comparator.comparingInt(pane -> pane.context().priority()));

        for (final var completedPane : completedPanes) {
            List<ItemStackElement<PlayerPane>> elements = completedPane.pane().inventoryElements();

            for (int i = 0; i < elements.size(); i++) {
                ItemStackElement<PlayerPane> element = elements.get(i);

                if (!element.equals(empty)) {
                    finalPane = finalPane.element(i, element);
                }
            }
        }

        return finalPane;
    }

    @Override
    public boolean viewing() {
        return this.viewing;
    }

    @Override
    public PlayerPane pane() {
        return this.pane;
    }

    @Override
    public void open() {
        this.viewing = true;
        this.current.clear();
        this.update();
        this.emitEvent();
    }

    /**
     * Closes the inventory for the viewing player.
     */
    public void close() {
        // The player is no longer viewing the inventory,
        this.viewing = false;
        // so all items are removed
        //todo(josh): shouldn't be needed but im seeing exceptions where this is null.
        if (this.inventory != null) {
            this.inventory.clear();
        }
        // and an event is emitted.
        Bukkit.getPluginManager().callEvent(new ViewCloseEvent(this));
    }

    @Override
    public @NotNull PlayerInventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean updates() {
        return this.backing().updates();
    }

}
