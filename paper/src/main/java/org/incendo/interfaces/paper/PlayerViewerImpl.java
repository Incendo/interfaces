package org.incendo.interfaces.paper;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.view.BookView;
import org.incendo.interfaces.paper.view.ChestView;

final class PlayerViewerImpl implements PlayerViewer {

    private final @NonNull Player player;

    /**
     * Constructs {@code PlayerViewer}.
     *
     * @param player the player
     */
    PlayerViewerImpl(final @NonNull Player player) {
        this.player = player;
    }

    /**
     * Opens a chest pane.
     *
     * @param chestView the chest view
     */
    private void openChestView(final @NonNull ChestView chestView) {
        this.player.openInventory(chestView.getInventory());
    }

    /**
     * Opens a book pane.
     *
     * @param bookView the book view
     */
    private void openBookView(final @NonNull BookView bookView) {
        this.player.openBook(bookView.book());
    }

    @Override
    public void open(final @NonNull InterfaceView<?, ?> pane) {
        if (pane instanceof ChestView) {
            this.openChestView((ChestView) pane);
            return;
        }

        if (pane instanceof BookView) {
            this.openBookView((BookView) pane);
            return;
        }

        throw new UnsupportedOperationException("Cannot open view of type " + pane.getClass().getName() + ".");
    }

    @Override
    public void close() {
        this.player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
    }

    @Override
    public @NonNull Player player() {
        return this.player;
    }

}
