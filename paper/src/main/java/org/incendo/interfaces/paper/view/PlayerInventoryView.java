package org.incendo.interfaces.paper.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.PlayerPane;
import org.incendo.interfaces.paper.type.PlayerInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class PlayerInventoryView implements
        PlayerView<PlayerPane>,
        SelfUpdatingInterfaceView {

    private static final Map<Player, PlayerInventoryView> INVENTORY_VIEW_MAP = new WeakHashMap<>();

    private final @NonNull PlayerViewer viewer;
    private final @NonNull PlayerInterface backing;
    private final @NonNull PlayerInventory inventory;
    private final @NonNull InterfaceArguments arguments;
    private @NonNull PlayerPane pane;

    private boolean viewing = true;

    private final @NonNull Map<Integer, Element> current = new HashMap<>();

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
        // If an inventory exists for the player, we "close" the old one.
        final PlayerInventoryView oldView = INVENTORY_VIEW_MAP.remove(viewer.player());
        if (oldView != null) {
            oldView.close();
        }

        // Store the new view.
        INVENTORY_VIEW_MAP.put(viewer.player(), this);

        this.viewer = viewer;
        this.backing = backing;
        this.arguments = argument;
        this.inventory = viewer.player().getInventory();

        this.pane = this.updatePane(true);
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

    private @NonNull PlayerPane updatePane(final boolean firstApply) {
        @NonNull PlayerPane pane = new PlayerPane();

        for (final var transform : this.backing.transformations()) {
            pane = transform.transform().apply(pane, this);

            // If it's the first time we apply the transform, then
            // we add update listeners to all the dependent properties
            if (firstApply) {
                for (final InterfaceProperty<?> property : transform.properties()) {
                    property.addListener((oldValue, newValue) -> this.update());
                }
            }
        }

        return pane;
    }

    @Override
    public @NonNull PlayerInterface backing() {
        return this.backing;
    }

    @Override
    public @NonNull InterfaceArguments arguments() {
        return this.arguments;
    }

    @NonNull
    @Override
    public PlayerViewer viewer() {
        return this.viewer;
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
        this.inventory.clear();
        // and an event is emitted.
        Bukkit.getPluginManager().callEvent(new ViewCloseEvent(this));
    }

    @Override
    public void update() {
        this.pane = this.updatePane(false);

        final @NonNull List<ItemStackElement<PlayerPane>> elements = this.pane.inventoryElements();

        for (int i = 0; i < elements.size(); i++) {
            final @Nullable Element currentElement = this.current.get(i);
            final @NonNull ItemStackElement<PlayerPane> element = elements.get(i);

            if (element.equals(currentElement)) {
                continue;
            }

            this.current.put(i, element);
            this.inventory.setItem(i, element.itemStack());
        }
    }

    @Override
    public @NotNull PlayerInventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean updates() {
        return this.backing().updates();
    }

    @Override
    public <C extends PlayerView<?>> @NonNull C openChild(
            @NonNull final Interface<?, PlayerViewer> backing,
            @NonNull final InterfaceArguments argument
    ) {
        InterfaceView<?, PlayerViewer> view = backing.open(this, argument);
        view.open();

        @SuppressWarnings("unchecked")
        C typedView = (C) view;
        return typedView;
    }

}
