package org.incendo.interfaces.paper.click;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.clicks.Clicks;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.view.ChestView;
import org.incendo.interfaces.paper.view.CombinedView;
import org.incendo.interfaces.paper.view.PlayerInventoryView;

import java.util.Objects;

/**
 * The click context of a chest.
 *
 * @param <T> the pane type
 * @param <U> the viewer type
 */
@SuppressWarnings("unused")
public final class InventoryClickContext<T extends Pane, U extends InterfaceView<T, PlayerViewer>> implements
        ClickContext<T, InventoryClickEvent, PlayerViewer> {

    private final @NonNull InventoryClickEvent event;
    private final @NonNull U view;
    private final @NonNull Click<InventoryClickEvent> click;

    /**
     * Constructs {@code ChestClickContext}.
     *
     * @param event    the event
     * @param player   whether or not the player clicked their own inventory
     * @param interact whether or nor the click was triggered by an interact event
     */
    @SuppressWarnings("unchecked")
    public InventoryClickContext(
            final @NonNull InventoryClickEvent event,
            final boolean player,
            final boolean interact
    ) {
        this.event = event;

        final @NonNull Inventory inventory = event.getInventory();
        if (inventory.getType() == InventoryType.CHEST) {
            InventoryHolder holder = inventory.getHolder();

            if (holder instanceof CombinedView) {
                this.view = this.viewFromCombinedClick();
            } else if (!(inventory.getHolder() instanceof ChestView)) {
                throw new IllegalArgumentException(
                        "The InventoryHolder wasn't a ChestView."
                );
            } else {
                if (event.getSlot() != event.getRawSlot()) {
                    this.view = (U) Objects.requireNonNull(PlayerInventoryView.forPlayer((Player) event.getWhoClicked()));
                } else {
                    this.view = (U) inventory.getHolder();
                }
            }
        } else if (player) {
            this.view = (U) Objects.requireNonNull(PlayerInventoryView.forPlayer((Player) event.getWhoClicked()));
        } else {
            throw new IllegalArgumentException("Inventory type " + inventory.getType() + " is not a valid inventory.");
        }

        if (this.event.isLeftClick()) {
            this.click = Clicks.leftClick(
                    this.event,
                    this.event.isShiftClick(),
                    interact
            );
        } else if (this.event.isRightClick()) {
            this.click = Clicks.rightClick(
                    this.event,
                    this.event.isShiftClick(),
                    interact
            );
        } else if (this.event.getClick() == ClickType.MIDDLE) {
            this.click = Clicks.rightClick(
                    this.event,
                    this.event.isShiftClick(),
                    interact
            );
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
    public @NonNull ClickStatus status() {
        if (!this.event.isCancelled()) {
            return ClickStatus.ALLOW;
        } else {
            return ClickStatus.DENY;
        }
    }

    @Override
    public void status(
            @NonNull final ClickStatus status
    ) {
        switch (status) {
            case ALLOW:
                this.event.setCancelled(false);
                break;
            case DENY:
                this.event.setCancelled(true);
                break;
            default:
                break;
        }
    }

    @Override
    public @NonNull U view() {
        return this.view;
    }

    @Override
    public @NonNull PlayerViewer viewer() {
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

    private U viewFromCombinedClick() {
        return (U) this.event.getInventory().getHolder();
    }

}
