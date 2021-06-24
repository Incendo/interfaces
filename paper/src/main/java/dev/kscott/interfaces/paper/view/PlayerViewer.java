package dev.kscott.interfaces.paper.view;

import dev.kscott.interfaces.core.view.InterfaceView;
import dev.kscott.interfaces.core.view.InterfaceViewer;
import dev.kscott.interfaces.paper.pane.ChestPane;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An interface viewer holding a {@link Player}.
 * <p>
 * Supports {@link ChestPane}.
 */
public class PlayerViewer implements InterfaceViewer {

    /**
     * Returns a new {@code PlayerViewer} containging {@code player}.
     *
     * @param player the player
     * @return the viewer
     */
    public static @NonNull PlayerViewer of(final @NonNull Player player) {
        return new PlayerViewer(player);
    }

    /**
     * The player.
     */
    private final @NonNull Player player;

    /**
     * Constructs {@code PlayerViewer}.
     *
     * @param player the player
     */
    private PlayerViewer(final @NonNull Player player) {
        this.player = player;
    }

    /**
     * Opens a pane for the viewer.
     *
     * @param pane the pane
     * @throws UnsupportedOperationException when the pane
     */
    @Override
    public void open(final @NonNull InterfaceView pane) {
        if (pane instanceof ChestView chestView) {
            openChestView(chestView);
            return;
        }

        throw new UnsupportedOperationException("Cannot open view of type " + pane.getClass().getName() + ".");
    }

    /**
     * Opens a chest pane.
     *
     * @param chestPane the chest pane
     */
    private void openChestView(final @NonNull ChestView chestPane) {
        this.player.openInventory(chestPane.inventory());
    }

    /**
     * Returns the player.
     *
     * @return the player
     */
    public @NonNull Player player() {
        return this.player;
    }

}
