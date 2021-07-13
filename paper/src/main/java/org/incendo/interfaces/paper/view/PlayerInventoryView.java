package org.incendo.interfaces.paper.view;

import java.util.Collection;

import java.util.Collections;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.PlayerPane;
import org.incendo.interfaces.paper.type.PlayerInterface;
import org.jetbrains.annotations.NotNull;

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
        INVENTORY_VIEW_MAP.put(viewer.player(), this);

        this.viewer = viewer;
        this.backing = backing;
        this.arguments = argument;
        this.inventory = viewer.player().getInventory();

        this.pane = this.updatePane(true);


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
                transform.property().addListener((oldValue, newValue) -> this.update());
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
        return true;
    }

    @Override
    public PlayerPane pane() {
        return this.pane;
    }

    @Override
    public void open() {
        this.update();
        this.emitEvent();
    }

    @Override
    public void addTask(
            @NonNull final Plugin plugin,
            @NonNull final Runnable runnable, final int delay
    ) {
        throw new UnsupportedOperationException("Cannot add tasks to player inventories.");
    }

    @Override
    public Collection<Integer> taskIds() {
        return Collections.emptyList();
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

}
