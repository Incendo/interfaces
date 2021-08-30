package org.incendo.interfaces.paper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.PluginClassLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.view.BookView;
import org.incendo.interfaces.paper.view.ChestView;
import org.incendo.interfaces.paper.view.CombinedView;
import org.incendo.interfaces.paper.view.PlayerInventoryView;

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

    private void openChestView(final @NonNull ChestView chestView) {
        this.player.openInventory(chestView.getInventory());
    }

    private void openCombinedView(final @NonNull CombinedView combinedView) {
        this.player.openInventory(combinedView.getInventory());
    }

    private void openBookView(final @NonNull BookView bookView) {
        this.player.openBook(bookView.book());
    }

    private void openPlayerView(final @NonNull PlayerInventoryView inventoryView) {
        inventoryView.open();
    }

    @Override
    public void open(final @NonNull InterfaceView<?, ?> view) {
        final Runnable runnable = () -> {
            if (view instanceof ChestView) {
                this.openChestView((ChestView) view);
            } else if (view instanceof CombinedView) {
                this.openCombinedView((CombinedView) view);
            } else if (view instanceof BookView) {
                this.openBookView((BookView) view);
            } else if (view instanceof PlayerInventoryView) {
                this.openPlayerView((PlayerInventoryView) view);
            } else {
                throw new UnsupportedOperationException("Cannot open view of type " + view.getClass().getName() + ".");
            }
        };

        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            try {
                Bukkit.getScheduler().callSyncMethod(((PluginClassLoader) getClass().getClassLoader()).getPlugin(), () -> {
                    runnable.run();

                    return null;
                }).get();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
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
