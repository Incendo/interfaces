package dev.kscott.interfaces.paper.view;

import dev.kscott.interfaces.paper.pane.ChestPane;
import dev.kscott.interfaces.core.pane.Pane;
import dev.kscott.interfaces.core.view.InterfaceViewer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An interface viewer holding a {@link Player}.
 * <p>
 * Supports {@link ChestPane}.
 */
public class PlayerInterfaceViewer implements InterfaceViewer {

    /**
     * The player.
     */
    private final @NonNull Player player;

    public PlayerInterfaceViewer(final @NonNull Player player) {
        this.player = player;
    }

    /**
     * Opens a pane for the viewer.
     *
     * @param pane the pane
     * @throws UnsupportedOperationException when the pane
     */
    @Override
    public void open(final @NonNull Pane pane) {
        if (pane instanceof ChestPane chestPane) {
            openChestPane(chestPane);
            return;
        }

        throw new UnsupportedOperationException("Cannot open pane of type "+pane.getClass().getName()+".");
    }

    /**
     * Opens a chest pane.
     *
     * @param chestPane the chest pane
     */
    private void openChestPane(final @NonNull ChestPane chestPane) {

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
