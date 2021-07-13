package org.incendo.interfaces.paper.click;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.clicks.Clicks;
import org.incendo.interfaces.core.view.InterfaceViewer;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.view.ChestView;

/**
 * The click context of a chest.
 */
@SuppressWarnings("unused")
public final class ChestClickContext implements ClickContext<ChestPane, InventoryClickEvent> {

    private final @NonNull InventoryClickEvent event;
    private final @NonNull ChestView view;
    private final @NonNull Click<InventoryClickEvent> click;

    /**
     * Constructs {@code ChestClickContext}.
     *
     * @param event the event, must be of {@link InventoryType#CHEST}
     */
    public ChestClickContext(
            final @NonNull InventoryClickEvent event
    ) {
        this.event = event;

        final @NonNull Inventory inventory = event.getInventory();

        if (inventory.getType() != InventoryType.CHEST) {
            throw new IllegalArgumentException(
                    "ChestClickContext requires an InventoryClickEvent with an InventoryType of CHEST."
            );
        }

        if (!(inventory.getHolder() instanceof ChestView)) {
            throw new IllegalArgumentException(
                    "The InventoryHolder wasn't a ChestView."
            );
        }

        this.view = (ChestView) inventory.getHolder();

        if (this.event.isLeftClick()) {
            this.click = Clicks.leftClick(this.event, this.event.isShiftClick());
        } else if (this.event.isRightClick()) {
            this.click = Clicks.rightClick(this.event, this.event.isShiftClick());
        } else if (this.event.getClick() == ClickType.MIDDLE) {
            this.click = Clicks.rightClick(this.event, this.event.isShiftClick());
        } else {
            /* ??? */
            this.click = Clicks.unknownClick(this.event);
        }
    }

    @Override
    public @NonNull InventoryClickEvent cause() {
        return this.event;
    }

    @Override
    public boolean cancelled() {
        return this.event.isCancelled();
    }

    @Override
    public void cancel(final boolean cancelled) {
        this.event.setCancelled(cancelled);
    }

    @Override
    public @NonNull ChestView view() {
        return this.view;
    }

    @Override
    public @NonNull InterfaceViewer viewer() {
        return PlayerViewer.of((Player) this.event.getWhoClicked());
    }

    @Override
    public @NonNull Click<InventoryClickEvent> click() {
        return this.click;
    }

    /**
     * Returns the slot
     *
     * @return the slot
     */
    public int slot() {
        return this.event.getSlot();
    }

    /**
     * Returns the raw slot
     *
     * @return the raw slot
     */
    public int rawSlot() {
        return this.event.getRawSlot();
    }

}
