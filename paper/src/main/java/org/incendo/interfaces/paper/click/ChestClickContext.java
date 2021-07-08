package org.incendo.interfaces.paper.click;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.view.InterfaceViewer;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.view.ChestView;

/**
 * The click context of a chest.
 */
public class ChestClickContext implements ClickContext<ChestPane> {

    private final @NonNull InventoryClickEvent event;
    private final @NonNull ChestView view;
    private final @NonNull PaperClick click;

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
            throw new IllegalArgumentException("ChestClickContext requires an InventoryClickEvent with an InventoryType of " +
                    "CHEST.");
        }

        if (!(inventory.getHolder() instanceof ChestView)) {
            throw new IllegalArgumentException("The InventoryHolder wasn't a ChestView.");
        }

        this.view = (ChestView) inventory.getHolder();

        this.click = new PaperClick(this.event);
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
    public @NonNull PaperClick click() {
        return this.click;
    }

}
